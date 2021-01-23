package server;

import org.apache.jena.ontology.OntModel;
import parser.DBpediaOntology;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.StringWriter;

/**
 * @author Malte Brockmeier
 */
@Path("/ontology/{ontology}")
public class RDFOntology {
    @GET
    @Produces({MediaType.TEXT_HTML})
    public String getOntologyHTML(@PathParam("ontology") String ontology) {
        OntModel model = DBpediaOntology.getInstance().getOntology();
        return HTMLRenderer.renderModel(model, "http://dbpedia.org/ontology/" + ontology);
    }

    @GET
    @Produces("application/rdf+xml")
    public String getOntologyRDFXML(@PathParam("ontology") String ontology) {
        StringWriter outputWriter = new StringWriter();
        OntModel model = DBpediaOntology.getInstance().getOntology();
        model.write(outputWriter);
        return outputWriter.toString();
    }
}
