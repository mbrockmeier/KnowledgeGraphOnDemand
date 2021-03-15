package server;

import extraction.KnowledgeGraphBuilder;
import org.apache.jena.rdf.model.Model;
import parser.ModelCacheEntry;

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
    public String getResourceHTML(@PathParam("resource") String resource, @QueryParam("wikiBaseUrl") String wikiBaseUrl, @QueryParam("refreshModel") boolean refreshModel) {
        Model model = KnowledgeGraphBuilder.getInstance().createKnowledgeGraphForWikiPage(wikiBaseUrl, resource, true, refreshModel).getModel();
        return HTMLRenderer.renderModel(model, "http://dbpedia.org/resource/" + resource);
    }

    @GET
    @Produces("application/rdf+xml")
    public String getResourceRDFXML(@PathParam("resource") String resource, @QueryParam("wikiBaseUrl") String wikiBaseUrl, @QueryParam("refreshModel") boolean refreshModel) {
        StringWriter outputWriter = new StringWriter();
        Model model = KnowledgeGraphBuilder.getInstance().createKnowledgeGraphForWikiPage(wikiBaseUrl, resource, true, refreshModel).getModel();
        model.write(outputWriter);
        return outputWriter.toString();
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getResourceJSON(@PathParam("resource") String resource, @QueryParam("wikiBaseUrl") String wikiBaseUrl, @QueryParam("refreshModel") boolean refreshModel) {
        ModelCacheEntry modelCacheEntry = KnowledgeGraphBuilder.getInstance().createKnowledgeGraphForWikiPage(wikiBaseUrl, resource, true, refreshModel);
        JSONResource jsonResource = new JSONResource();
        jsonResource.createFromModel(modelCacheEntry, "http://dbpedia.org/resource/" + resource);
        return jsonResource.getJSON();
    }
}
