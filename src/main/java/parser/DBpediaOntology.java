package parser;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.ModelFactory;

/**
 * @author Malte Brockmeier
 */
public class DBpediaOntology {
    private static DBpediaOntology instance = null;
    private OntModel ontModel;

    private DBpediaOntology() {
        this.ontModel = ModelFactory.createOntologyModel();
        this.ontModel = (OntModel) this.ontModel.read("dbpedia_ontology.owl");
        this.ontModel.clearNsPrefixMap();
        this.ontModel.setNsPrefixes(NamespacePrefixLoader.getNsPrefixes());
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
