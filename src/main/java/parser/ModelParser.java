package parser;

import extraction.KnowledgeGraphConfiguration;
import org.apache.jena.rdf.model.*;

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
        String baseDir = KnowledgeGraphConfiguration.getExtractionFrameworkBaseDir();
        String language = KnowledgeGraphConfiguration.getLanguage();
        String currentDate = new SimpleDateFormat("yyyyMMdd").format(new Date());

        String folderPath = "/" + language + "wiki/" + currentDate + "/";

        for (String file : files) {
            String fileName = language + "wiki-" + currentDate + file;
            String filePath = baseDir + folderPath + fileName;

            model.read(filePath, "N-TRIPLES");
        }

        model.clearNsPrefixMap();
        model.setNsPrefixes(NamespacePrefixLoader.getNsPrefixes());

        //model = renameRDF();
        return model;
    }
}
