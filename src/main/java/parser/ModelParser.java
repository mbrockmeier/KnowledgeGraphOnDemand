package parser;

import extraction.KnowledgeGraphConfiguration;
import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.RDFS;
import org.tinylog.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;


/**
 * @author Yawen Liu
 */
public class ModelParser {

    private Model model;

    public ModelParser(){
        model = ModelFactory.createDefaultModel();
    }

    public ModelParser(Model model) {
        this();
        this.model = model;
    }

    public Model getModel() {
        return this.model;
    }

    /**
     * @author Yawen Liu
     * read the TTL file to a model
     * @return the RDF model
     */

    public Model readRDF(String[] files) {
        model = ModelFactory.createDefaultModel();
        String baseDir = KnowledgeGraphConfiguration.getExtractionFrameworkBaseDir();
        String language = KnowledgeGraphConfiguration.getLanguage();
        String currentDate = new SimpleDateFormat("yyyyMMdd").format(new Date());

        String folderPath = "/" + language + "wiki/" + currentDate + "/";

        for (String file : files) {
            String fileName = language + "wiki-" + currentDate + file;
            String filePath = baseDir + folderPath + fileName;

            File sourceFile = new File(filePath);
            if (sourceFile.exists() && !sourceFile.isDirectory()) {
                try {
                    InputStream inputStream = new FileInputStream(sourceFile);

                    model.read(inputStream, null, "N-TRIPLES");

                    inputStream.close();
                } catch (Exception exception) {
                    Logger.info(exception);
                }
            }
        }

        model.clearNsPrefixMap();
        model.setNsPrefixes(NamespacePrefixLoader.getNsPrefixes());

        //model = renameRDF();
        return model;
    }

    /**
     * @author Sunita Pateer
     * @param wikiPage
     * @param extractedAbstract
     */
    public void addAbstract(String wikiPage, String extractedAbstract) {
        String resourceName = model.expandPrefix("dbr:" + wikiPage);
        Property abstractProperty = model.createProperty(model.expandPrefix("dbo:abstract"));

        model.addLiteral(model.getResource(resourceName), RDFS.comment, model.createLiteral((extractedAbstract), KnowledgeGraphConfiguration.getLanguage()));
        model.addLiteral(model.getResource(resourceName), abstractProperty, model.createLiteral((extractedAbstract), KnowledgeGraphConfiguration.getLanguage()));
    }
}
