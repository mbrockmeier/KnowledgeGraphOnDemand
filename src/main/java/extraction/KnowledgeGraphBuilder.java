package extraction;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.jena.rdf.model.Model;
import org.tinylog.Logger;
import org.w3c.dom.Document;
import parser.ModelCache;
import parser.ModelCacheEntry;
import parser.ModelParser;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * @author Malte Brockmeier
 */
public class KnowledgeGraphBuilder {
    private String[] resultFiles = {"-infobox-properties.ttl", "-page-ids.ttl", "-labels.ttl", "-mappingbased-objects-uncleaned.ttl", "-page-links.ttl"};
    private ModelParser modelParser;
    private ModelCache modelCache;
    public String wikipageExtract;

    private static KnowledgeGraphBuilder instance = null;

    public static KnowledgeGraphBuilder getInstance() {
        if (instance == null) {
            instance = new KnowledgeGraphBuilder();
        }
        return instance;
    }

    //Singleton
    private KnowledgeGraphBuilder() {
        modelParser = new ModelParser();
        modelCache = new ModelCache();
    }

    /**
     * @author Malte Brockmeier
     * @param wikiBaseUrl the url of the wikiPage, is not specified wikipedia will be used as a fallback
     * @param wikiPage the wikiPage to extract
     * @param includeBacklinks whether to include backlinks in the extraction process
     * @param refreshModel whether the model should be retrieved from the cache if in the cache or rebuilt using the latest available wikipage
     * @return
     */
    public ModelCacheEntry createKnowledgeGraphForWikiPage(String wikiBaseUrl, String wikiPage, boolean includeBacklinks, boolean refreshModel) {
        if (modelCache.containsModel(wikiPage) && !refreshModel) {
            return modelCache.retrieveModelFromCache(wikiPage);
        } else {
            long startTime = System.nanoTime();
            if (wikiBaseUrl != null) {
                retrieveAndStoreWikipageXmlSource(wikiBaseUrl, wikiBaseUrl, wikiPage, includeBacklinks);
            } else {
                retrieveAndStoreWikipageXmlSource(wikiPage, includeBacklinks);
            }
            runExtractionFramework();
            decompressExtractedData();
            modelParser.readRDF(resultFiles);

            long elapsedTime = System.nanoTime() - startTime;
            double extractionDuration = (double) elapsedTime / 1_000_000_000;

            return modelCache.storeModelInCache(wikiPage, modelParser.getModel(), extractionDuration);
        }
    }

    /**
     * @author Yawen Liu
     */
    private void runExtractionFramework() {
        Logger.info("Running extraction-framework on XML source dumps...");
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/C", "java -jar extraction.jar extraction.kgod.properties");
            processBuilder.directory(new File(KnowledgeGraphConfiguration.getExtractionFrameworkDir()));

            processBuilder.inheritIO();
            Process extractionProcess = processBuilder.start();

            extractionProcess.waitFor(25, TimeUnit.SECONDS);

            Logger.info("extraction-framework run sucessfully.");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * @author Malte Brockmeier
     * @param wikiPage the wikipedia page to retrieve
     * @param includeBacklinks whether backlinks to the specified wikipedia page should also be retrieved
     */
    private void retrieveAndStoreWikipageXmlSource(String wikiPage, boolean includeBacklinks) {
        retrieveAndStoreWikipageXmlSource("https://en.wikipedia.org/", "https://en.wikipedia.org/w/", wikiPage, includeBacklinks);
    }

    /**
     * @author Malte Brockmeier
     * @param
     * @param wikiPage the wikipedia page to retrieve
     * @param includeBacklinks whether backlinks to the specified wikipedia page should also be retrieved
     */
    private void retrieveAndStoreWikipageXmlSource(String wikiBaseUrl, String wikiApiBaseUrl, String wikiPage, boolean includeBacklinks) {
        WikipediaExtractor wikipediaExtractor = new WikipediaExtractor(wikiBaseUrl, wikiApiBaseUrl);

        List<String> pagesToRetrieve;

        if (includeBacklinks) {
            pagesToRetrieve = wikipediaExtractor.getBackLinks(wikiPage);
        } else {
            pagesToRetrieve = new ArrayList<>();
        }

        pagesToRetrieve.add(wikiPage);

        if (KnowledgeGraphConfiguration.getRetrieveExtract()) {
            retrieveWikipageExtract(wikipediaExtractor, wikiPage);
        }

        String source = wikipediaExtractor.retrieveWikiPagesByTitle(pagesToRetrieve);
        Document document = StringToXml.toXmlDocument(source);

        String baseDir = KnowledgeGraphConfiguration.getExtractionFrameworkBaseDir();
        String language = KnowledgeGraphConfiguration.getLanguage();
        String currentDate = new SimpleDateFormat("yyyyMMdd").format(new Date());

        String folderPath = "/" + language + "wiki/" + currentDate + "/";
        String fileName = language + "wiki-" + currentDate + "-dump.xml";

        String filePath = baseDir + folderPath + fileName;

        File file = new File(filePath);

        file.getParentFile().mkdirs();

        StringToXml.saveXML(document, file);
    }

    /**
     * @author Malte Brockmeier
     */
    private void decompressExtractedData() {
        int buffersize = 64;
        String baseDir = KnowledgeGraphConfiguration.getExtractionFrameworkBaseDir();
        String language = KnowledgeGraphConfiguration.getLanguage();
        String currentDate = new SimpleDateFormat("yyyyMMdd").format(new Date());

        String folderPath = "/" + language + "wiki/" + currentDate + "/";

        for (String resultFile : resultFiles) {
            String fileName = language + "wiki-" + currentDate + resultFile;
            String filePath = baseDir + folderPath + fileName;
            String archiveFile = filePath + ".bz2";

            Logger.info("Decompressing '" + archiveFile + "'...");

            try {
                InputStream inputStream = Files.newInputStream(Paths.get(archiveFile));
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                OutputStream outputStream = Files.newOutputStream(Paths.get(filePath));
                BZip2CompressorInputStream bzIn = new BZip2CompressorInputStream(bufferedInputStream);
                final byte[] buffer = new byte[buffersize];
                int n = 0;
                while (-1 != (n = bzIn.read(buffer))) {
                    outputStream.write(buffer, 0, n);
                }
                outputStream.close();
                bzIn.close();

                Logger.info("Decompressed '" + archiveFile + "'.");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    /**
     * @author Sunita Pateer
     * @param wikiPage the title of the wikipedia for which the extract should be retrieved
     */
    private void retrieveWikipageExtract(WikipediaExtractor wikipediaExtractor, String wikiPage) {
        // retrieve extract from wikipedia API endpoint
        System.out.println("[" + wikiPage + "] | BEFORE : " + this.wikipageExtract);
        this.wikipageExtract = wikipediaExtractor.getExtract(wikiPage);
        System.out.println("[" + wikiPage + "] | AFTER : " + this.wikipageExtract);
    }
}
