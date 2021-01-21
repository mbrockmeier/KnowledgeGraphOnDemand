package parser;

import extraction.KnowledgeGraphConfiguration;
import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.VCARD;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


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
        model = renameRDF();
        return model;
    }

    /**
     * @author Yawen Liu
     * print the model
     */
    public Model renameRDF() {
        StmtIterator iter = model.listStatements();
        Model result = ModelFactory.createDefaultModel();
        while (iter.hasNext())
        {
            Statement stmt = iter.nextStatement();
            String predicate = stmt.getPredicate().toString();
            if(predicate.startsWith("http://dbpedia.org/ontology/")){
                predicate = predicate.replace("http://dbpedia.org/ontology/","dbo: ");
                Property property = result.createProperty(predicate);
                result.add(stmt.getSubject(),property,stmt.getObject());
            }else if (predicate.startsWith("http://dbpedia.org/resource/")){
                predicate = predicate.replace("http://dbpedia.org/resource/","dbr: ");
                Property property = result.createProperty(predicate);
                result.add(stmt.getSubject(),property,stmt.getObject());
            }else if (predicate.startsWith("http://dbpedia.org/property/")) {
                predicate = predicate.replace("http://dbpedia.org/property/", "dbp: ");
                Property property = result.createProperty(predicate);
                result.add(stmt.getSubject(), property, stmt.getObject());
            }else if(predicate.startsWith("http://www.w3.org/2000/01/rdf-schema#")){
                predicate = predicate.replace("http://www.w3.org/2000/01/rdf-schema#","rdfs: ");
                Property property = result.createProperty(predicate);
                result.add(stmt.getSubject(),property,stmt.getObject());
            }else{
                result.add(stmt.getSubject(),stmt.getPredicate(),stmt.getObject());
            }

        }
        return result;
    }


}
