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
    private HashMap<String, String> namespaces;

    public ModelParser(){
        model = ModelFactory.createDefaultModel();
        this.namespaces = new HashMap<String, String>() {{
            put("dbp", "http://dbpedia.org/property/");
            put("dbo", "http://dbpedia.org/ontology/");
            put("dbr", "http://dbpedia.org/resource/");
        }};
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

    public Model readRDF(String[] files){
        String baseDir = KnowledgeGraphConfiguration.getExtractionFrameworkBaseDir();
        String language = KnowledgeGraphConfiguration.getLanguage();
        String currentDate = new SimpleDateFormat("yyyyMMdd").format(new Date());

        String folderPath = "/" + language + "wiki/" + currentDate + "/";

        for (String file : files) {
            String fileName = language + "wiki-" + currentDate + file;
            String filePath = baseDir + folderPath + fileName;

            model.read(filePath, "N-TRIPLES");
        }

        model.setNsPrefixes(namespaces);

        return model;
    }

    /**
     * @author Yawen Liu
     * print the model
     */
    public void printRDF() {
        StmtIterator iter = model.listStatements();
        while (iter.hasNext())
        {
            Statement stmt = iter.nextStatement();
            String subject = stmt.getSubject().toString();
            String predicate = stmt.getPredicate().toString();
            RDFNode object = stmt.getObject();

            System.out.print("subject " + subject+"\t");
            System.out.print(" predicate " + predicate+"\t");
            if (object instanceof Resource)
            {
                System.out.print(" object " + object);
            }
            else {
                System.out.print("object \"" + object.toString() + "\"");
            }
            System.out.println(" .");
        }
    }
}
