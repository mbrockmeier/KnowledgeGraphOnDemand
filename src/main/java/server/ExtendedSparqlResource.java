package server;

import extraction.ExtractURIs;
import extraction.KnowledgeGraphBuilder;
import org.apache.jena.rdf.model.Model;
import org.json.JSONObject;
import parser.ModelCacheEntry;
import sparql.RDFConnection_sparql;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * @author Malte Brockmeier
 */

@Path("/sparql")
public class ExtendedSparqlResource {

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED})
    public String getSparqlJSON(@QueryParam("wikiBaseUrl") String wikiBaseUrl, @FormParam("query") String query, @QueryParam("refreshModel") boolean refreshModel) {
        long startTime = System.nanoTime();
        //extract the URIs from the sparql request
        Set<String> uris = ExtractURIs.extract(query);
        Set<String> resources = new HashSet<>();

        for (String uri : uris) {
            if (uri.matches("http://dbpedia.org/resource/.*")) {
                resources.add(uri.replace("http://dbpedia.org/resource/", ""));
            }
        }

        Model model = KnowledgeGraphBuilder.getInstance().createKnowledgeGraphForWikiPages(resources, true);

        System.out.println("required resources: " + resources);

        JSONSparql jsonSparql = new JSONSparql();
        System.out.println("sparql input: "+ query);
        jsonSparql.init(model, query);

        long elapsedTime = System.nanoTime() - startTime;
        double duration = (double) elapsedTime / 1_000_000_000;

        JSONObject jsonResult = jsonSparql.getJSON();
        jsonResult.put("duration", duration);

        return jsonResult.toString();
    }
}