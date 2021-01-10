package server;

import extraction.KnowledgeGraphBuilder;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.jena.rdf.model.Model;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.StringWriter;

/**
 * @author Malte Brockmeier
 */
@Path("/resource/{resource}")
public class RDFResource {

    @GET
    @Produces({MediaType.TEXT_HTML})
    public String getResourceHTML(@PathParam("resource") String resource) {
        StringWriter outputWriter = new StringWriter();
        Model model = KnowledgeGraphBuilder.getInstance().createKnowledgeGraphForWikiPage(resource, true);
        model.write(outputWriter, "N-TRIPLES");
        return "<pre>" + StringEscapeUtils.escapeHtml4(outputWriter.toString()) + "</pre>";
    }

    @GET
    @Produces("application/rdf+xml")
    public String getResourceRDFXML(@PathParam("resource") String resource) {
        StringWriter outputWriter = new StringWriter();
        Model model = KnowledgeGraphBuilder.getInstance().createKnowledgeGraphForWikiPage(resource, true);
        model.write(outputWriter);
        return outputWriter.toString();
    }
}
