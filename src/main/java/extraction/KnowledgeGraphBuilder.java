package extraction;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.w3c.dom.Document;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class KnowledgeGraphBuilder {
    public static void runExtractionFramework() {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/C", "java -jar extraction.jar extraction.infoboxes.properties");
            processBuilder.directory(new File(KnowledgeGraphConfiguration.getExtractionFrameworkDir()));

            processBuilder.inheritIO();
            Process extractionProcess = processBuilder.start();

            extractionProcess.waitFor(15, TimeUnit.SECONDS);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void retrieveAndStoreWikipageXmlSource(String wikiPage) {
        WikiPageExtractor wikiPageExtractor = new WikiPageExtractor();

        String source = wikiPageExtractor.retrieveWikiPageByTitle(wikiPage);
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

    public static void decompressExtractedData() {
        int buffersize = 64;
        String baseDir = KnowledgeGraphConfiguration.getExtractionFrameworkBaseDir();
        String language = KnowledgeGraphConfiguration.getLanguage();
        String currentDate = new SimpleDateFormat("yyyyMMdd").format(new Date());

        String folderPath = "/" + language + "wiki/" + currentDate + "/";
        String fileName = language + "wiki-" + currentDate + "-infobox-properties.ttl";
        String filePath = baseDir + folderPath + fileName;

        try {
            InputStream inputStream = Files.newInputStream(Paths.get(filePath + ".bz2"));
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
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
