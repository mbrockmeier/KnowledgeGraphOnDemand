package server;

import extraction.KnowledgeGraphBuilder;
import org.apache.jena.rdf.model.Model;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.StringWriter;

/**
 * @author Malte Brockmeier
 */
@Path("/resource/{resource}")
public class RDFResource {

    @GET
    @Produces({MediaType.TEXT_HTML})
    public String getResourceHTML(@PathParam("resource") String resource, @QueryParam("wikiBaseUrl") String wikiBaseUrl) {
        Model model = KnowledgeGraphBuilder.getInstance().createKnowledgeGraphForWikiPage(wikiBaseUrl, resource, true);
        return HTMLRenderer.renderModel(model, "http://dbpedia.org/resource/" + resource);
    }

    @GET
    @Produces("application/rdf+xml")
    public String getResourceRDFXML(@PathParam("resource") String resource, @QueryParam("wikiBaseUrl") String wikiBaseUrl) {
        StringWriter outputWriter = new StringWriter();
        Model model = KnowledgeGraphBuilder.getInstance().createKnowledgeGraphForWikiPage(wikiBaseUrl, resource, true);
        model.write(outputWriter);
        return outputWriter.toString();
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getResourceJSON(@PathParam("resource") String resource, @QueryParam("wikiBaseUrl") String wikiBaseUrl) {
        Model model = KnowledgeGraphBuilder.getInstance().createKnowledgeGraphForWikiPage(wikiBaseUrl, resource, true);
        JSONResource jsonResource = new JSONResource();
        jsonResource.createFromModel(model, "http://dbpedia.org/resource/" + resource);
        String extract = KnowledgeGraphBuilder.getInstance().wikipageExtract;
        jsonResource.setExtract(extract);
        return jsonResource.getJSON();
    }
}
