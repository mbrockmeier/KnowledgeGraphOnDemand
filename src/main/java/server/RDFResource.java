package server;

import extraction.KnowledgeGraphBuilder;
import jdk.nashorn.api.scripting.JSObject;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.jena.rdf.model.*;
import org.json.JSONObject;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.StringWriter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;
import java.util.*;
import org.json.JSONObject;
import org.apache.jena.rdf.model.*;

/**
 * @author Malte Brockmeier
 */
@Path("/resource/{resource}")
public class RDFResource {

    public String getHtmlText() {
        BufferedReader reader;
        String htmlText = new String("");
        try {
            // File directory = new File("./");
            // System.out.println(directory.getAbsolutePath());
            reader = new BufferedReader(new FileReader(
                    ".\\src\\main\\resources\\index.html"));
            String line = reader.readLine();
            while (line != null) {
                System.out.println(line);
                htmlText += line;
                // read next line
                line = reader.readLine();
            }
            reader.close();
            // System.out.println(html_text);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return htmlText;
    }

    public List<JSONObject> rdfToJSON(Model model) {
        List<JSONObject> rdfJSON = new ArrayList<JSONObject>();

        StmtIterator iter = model.listStatements();
        while (iter.hasNext())
        {
            Statement stmt = iter.nextStatement();

            String subject = stmt.getSubject().toString();
            String predicate = stmt.getPredicate().toString();
            RDFNode object = stmt.getObject();

            JSONObject iterObj = new JSONObject();
            iterObj.put("subject", subject);
            iterObj.put("predicate", predicate);

            if (object instanceof Resource)
            {
                iterObj.put("object", object);
            }
            else {
                iterObj.put("object", object.toString());
            }
            rdfJSON.add(iterObj);
        }
        return rdfJSON;
    }

    public String getTable(List<JSONObject> rdfJSON) {
        String tableHeadString = "<thead><tr><th class='center'>Subject</th><th class='center'>Property</th><th class='center'>Value</th></tr></thead>";

        String tableBodyString = "<tbody>";
        for (JSONObject obj : rdfJSON) {
            String tableRow = "<tr><td>" + obj.get("subject") + "</td><td>" +obj.get("predicate") + "</td><td>" + obj.get("object") + "</td></tr>";
            tableBodyString += tableRow;
        }
        tableBodyString += "</tbody>";

        String tableData = tableHeadString + tableBodyString;
        return tableData;
    }

    @GET
    @Produces({MediaType.TEXT_HTML})
    public String getResourceHTML(@PathParam("resource") String resource) {
        StringWriter outputWriter = new StringWriter();
        Model model = KnowledgeGraphBuilder.getInstance().createKnowledgeGraphForWikiPage(resource, false);
        model.write(outputWriter, "N-TRIPLES");
        System.out.print("\n\n" + outputWriter);
        String htmlText = this.getHtmlText();
        List<JSONObject> rdfJSON = this.rdfToJSON(model);
        String tableData = this.getTable(rdfJSON);
        System.out.println(resource);
        System.out.println(tableData);
        htmlText = htmlText.replace("{{tableName}}", resource);
        htmlText = htmlText.replace("{{tableData}}", tableData);
        // return "<pre>" + StringEscapeUtils.escapeHtml4(outputWriter.toString()) + "</pre>";
        return htmlText;
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
