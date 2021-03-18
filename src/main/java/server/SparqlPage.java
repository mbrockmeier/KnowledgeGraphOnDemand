package server;

import extraction.KnowledgeGraphBuilder;
import org.apache.jena.rdf.model.Model;
import sparql.RDFConnection_sparql;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/sparql/{resource}")
public class SparqlPage {
    @GET
    @Produces({MediaType.TEXT_HTML})
    public String getSparql(@PathParam("resource") String resource) {
        return SparqlView.displayView(resource);
    }

    @POST
    @Produces({MediaType.TEXT_HTML})
    public String getSparql(@PathParam("resource") String resource,@QueryParam("wikiBaseUrl") String wikiBaseUrl,@FormParam("textarea") String sparql, @QueryParam("refreshModel") boolean refreshModel) {
        Model model = KnowledgeGraphBuilder.getInstance().createKnowledgeGraphForWikiPage(wikiBaseUrl,resource, true, refreshModel).getModel();
        String query = sparql;
        RDFConnection_sparql rdfConnection_spaqrql = new RDFConnection_sparql(query,model);
        String erg = rdfConnection_spaqrql.connect();
        return SparqlHTML.sparqlRender(erg);
    }
}