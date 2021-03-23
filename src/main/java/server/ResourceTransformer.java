package server;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFFormat;
import parser.NamespacePrefixLoader;

import java.io.StringWriter;

public class ResourceTransformer {
    private static Model getReducedModel(Model model, String resource) {
        Resource resourceToRender = model.getResource("http://dbpedia.org/resource/" + resource);
        Model reducedModel = ModelFactory.createDefaultModel();
        reducedModel.add(resourceToRender.listProperties());
        reducedModel.clearNsPrefixMap();
        reducedModel.setNsPrefixes(NamespacePrefixLoader.getNsPrefixes());
        return reducedModel;
    }

    public static String getResourceXML(Model model, String resource) {
        StringWriter outputWriter = new StringWriter();
        Model reducedModel = getReducedModel(model, resource);
        RDFDataMgr.write(outputWriter, reducedModel, RDFFormat.RDFXML);

        return outputWriter.toString();
    }

    public static String getResourceNTriples(Model model, String resource) {
        StringWriter outputWriter = new StringWriter();
        Model reducedModel = getReducedModel(model, resource);
        RDFDataMgr.write(outputWriter, reducedModel, RDFFormat.NTRIPLES);

        return outputWriter.toString();
    }

    public static String getResourceTurtle(Model model, String resource) {
        StringWriter outputWriter = new StringWriter();
        Model reducedModel = getReducedModel(model, resource);
        RDFDataMgr.write(outputWriter, reducedModel, RDFFormat.TURTLE);

        return outputWriter.toString();
    }
}
