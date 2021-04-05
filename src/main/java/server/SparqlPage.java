package server;

import extraction.KnowledgeGraphBuilder;
import org.apache.jena.base.Sys;
import org.apache.jena.rdf.model.Model;
import parser.ModelCacheEntry;
import sparql.RDFConnection_sparql;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * @author Yawen Liu
 */

@Path("/sparql/{resource}")
public class SparqlPage {
    @GET
    @Produces({MediaType.TEXT_HTML})
    public String getSparql(@PathParam("resource") String resource) {
        return SparqlView.displayView(resource);
    }

    @POST
    @Produces({MediaType.TEXT_HTML})
    public String getSparql(@PathParam("resource") String resource,@QueryParam("wikiBaseUrl") String wikiBaseUrl, @FormParam("textarea") String sparql, @QueryParam("refreshModel") boolean refreshModel) {
        Model model = KnowledgeGraphBuilder.getInstance().createKnowledgeGraphForWikiPage(wikiBaseUrl,resource, true, refreshModel).getModel();
        String query = sparql;
        RDFConnection_sparql rdfConnection_spaqrql = new RDFConnection_sparql(query,model);
        String erg = rdfConnection_spaqrql.connect();
        return SparqlHTML.sparqlRender(erg);
    }


    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED})
    public String getSparqlJSON(@PathParam("resource") String resource, @QueryParam("wikiBaseUrl") String wikiBaseUrl, @FormParam("textarea") String sparql, @QueryParam("refreshModel") boolean refreshModel) {
        System.out.println("call the sparql..........");
        ModelCacheEntry modelCacheEntry = KnowledgeGraphBuilder.getInstance().createKnowledgeGraphForWikiPage(wikiBaseUrl, resource, false, false);
        JSONSparql jsonSparql = new JSONSparql();
        System.out.println("sparql input: "+ sparql);
        jsonSparql.init(modelCacheEntry,sparql);
        return jsonSparql.getJSON();
    }
}