package server;

import extraction.KnowledgeGraphBuilder;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
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
        return HTMLRenderer.renderModel(model, ResourceTransformer.getResourceName(resource));
    }

    @GET
    @Produces("application/rdf+xml")
    public String getResourceRDFXML(@PathParam("resource") String resource, @QueryParam("wikiBaseUrl") String wikiBaseUrl, @QueryParam("refreshModel") boolean refreshModel) {
        Model model = KnowledgeGraphBuilder.getInstance().createKnowledgeGraphForWikiPage(wikiBaseUrl, resource, true, refreshModel).getModel();
        return ResourceTransformer.getResourceXML(model, ResourceTransformer.getResourceName(resource));
    }

    @GET
    @Produces("application/n-triples")
    public String getResourceNTriples(@PathParam("resource") String resource, @QueryParam("wikiBaseUrl") String wikiBaseUrl, @QueryParam("refreshModel") boolean refreshModel) {
        Model model = KnowledgeGraphBuilder.getInstance().createKnowledgeGraphForWikiPage(wikiBaseUrl, resource, true, refreshModel).getModel();
        return ResourceTransformer.getResourceNTriples(model, ResourceTransformer.getResourceName(resource));
    }

    @GET
    @Produces("text/turtle")
    public String getResourceTurtle(@PathParam("resource") String resource, @QueryParam("wikiBaseUrl") String wikiBaseUrl, @QueryParam("refreshModel") boolean refreshModel) {
        Model model = KnowledgeGraphBuilder.getInstance().createKnowledgeGraphForWikiPage(wikiBaseUrl, resource, true, refreshModel).getModel();
        return ResourceTransformer.getResourceTurtle(model, ResourceTransformer.getResourceName(resource));
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getResourceJSON(@PathParam("resource") String resource, @QueryParam("wikiBaseUrl") String wikiBaseUrl, @QueryParam("refreshModel") boolean refreshModel) {
        ModelCacheEntry modelCacheEntry = KnowledgeGraphBuilder.getInstance().createKnowledgeGraphForWikiPage(wikiBaseUrl, resource, true, refreshModel);
        JSONResource jsonResource = new JSONResource();
        jsonResource.createFromModel(modelCacheEntry, ResourceTransformer.getResourceName(resource));
        return jsonResource.getJSON();
    }
}
