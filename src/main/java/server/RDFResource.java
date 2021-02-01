package server;

import extraction.KnowledgeGraphBuilder;
import org.apache.jena.rdf.model.Model;
import sparqlConnect.RDFConnection_spaqrql;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.StringWriter;

/**
 * @author Malte Brockmeier
 */
@Path("/resource/{resource}")
public class RDFResource{

    @GET
    @Produces({MediaType.TEXT_HTML})
    public String getResourceHTML(@PathParam("resource") String resource) {
        Model model = KnowledgeGraphBuilder.getInstance().createKnowledgeGraphForWikiPage(resource, true);
        return HTMLRenderer.renderModel(model, "http://dbpedia.org/resource/" + resource);
    }

    @GET
    @Produces("application/rdf+xml")
    public String getResourceRDFXML(@PathParam("resource") String resource) {
        StringWriter outputWriter = new StringWriter();
        Model model = KnowledgeGraphBuilder.getInstance().createKnowledgeGraphForWikiPage(resource, true);
        model.write(outputWriter);
        return outputWriter.toString();
    }

    /**
     * @author:Yawen Liu
     * @param resource
     * @param sparql
     * @return
     */
    @POST
    @Produces({MediaType.TEXT_HTML})
    public String getSparql(@PathParam("resource") String resource,@FormParam("textarea") String sparql) {
        Model model = KnowledgeGraphBuilder.getInstance().createKnowledgeGraphForWikiPage(resource, true);
        String query = sparql;
        RDFConnection_spaqrql rdfConnection_spaqrql = new RDFConnection_spaqrql(query,model);
        String erg = rdfConnection_spaqrql.connect();
        //System.out.println("here+++++++++"+erg);
        return erg;
    }

}
