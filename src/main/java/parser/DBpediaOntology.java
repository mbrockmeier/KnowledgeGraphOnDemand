package parser;

import extraction.KnowledgeGraphConfiguration;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.ModelFactory;
import org.tinylog.Logger;

import java.io.FileInputStream;
import java.io.InputStream;

/**
 * @author Malte Brockmeier
 */
public class DBpediaOntology {
    private static DBpediaOntology instance = null;
    private OntModel ontModel;

    private DBpediaOntology() {
        this.ontModel = ModelFactory.createOntologyModel();

        String ontologyPath = KnowledgeGraphConfiguration.getExtractionFrameworkDir().replace("/dump", "") + "/ontology.owl";
        Logger.info("Loading ontology from '" + ontologyPath + "'");

        try {
            InputStream inputStream = new FileInputStream(ontologyPath);

            this.ontModel = (OntModel) this.ontModel.read(inputStream, null);
            inputStream.close();

            this.ontModel.clearNsPrefixMap();
            this.ontModel.setNsPrefixes(NamespacePrefixLoader.getNsPrefixes());
        } catch (Exception e) {
            Logger.info(e);
        }
    }

    public static DBpediaOntology getInstance() {
        if (instance == null) {
            instance = new DBpediaOntology();
        }

        return instance;
    }

    public void printOntology() {

    }

    public OntModel getOntology() {
        return this.ontModel;
    }
}
