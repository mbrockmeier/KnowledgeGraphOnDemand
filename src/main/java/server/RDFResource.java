package server;

import extraction.KnowledgeGraphBuilder;
import org.apache.jena.rdf.model.Model;

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

import org.json.JSONArray;
import org.json.JSONObject;
import org.apache.jena.rdf.model.*;

/**
 * @author Malte Brockmeier
 */
@Path("/resource/{resource}")
public class RDFResource {

    /**
     * @Author: Sunita Pateer
     * @return
     */
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

    /**
     * @Author: Sunita Pateer
     * @return
     */
    public String getTable(GroupedResource groupedResource) {
        JSONArray rdfJSONArray = groupedResource.rdfToJSONArray();
        Iterator<Object> propertyIterator = rdfJSONArray.iterator();

        String tableBodyString = "<tbody>";

        while(propertyIterator.hasNext())
        {
            JSONObject jsonObj = (JSONObject) propertyIterator.next();

            String subject = jsonObj.getString("subject");

            JSONObject predicate = jsonObj.getJSONObject("predicate");

            String property = "";
            if (!predicate.getString("href").equals("")) {
                property += "<a href='" + predicate.getString("href") + "'>";
                property += predicate.getString("shortForm") + "</a>";
            }
            else {
                property += predicate.getString("value");
            }

            JSONArray objectValues = jsonObj.getJSONArray("object");
            Iterator<Object> objectValuesIterator = objectValues.iterator();

            String value = "<ul>";
            while(objectValuesIterator.hasNext())
            {
                JSONObject object = (JSONObject) objectValuesIterator.next();
                value += "<li>";
                if (!object.getString("href").equals("")) {
                    value += "<a href='" + object.getString("href") + "'>";
                    value += object.getString("shortForm") + "</a>";
                }
                else {
                    value += object.getString("value");
                }
                value += "</li>";
            }
            value += "</ul>";

            System.out.println("subject :: " + subject + " predicate :: " + property + " object :: " + value);

            String tableRow = "<tr><td>" + subject + "</td><td>" + property + "</td><td>" + value + "</td></tr>";
            tableBodyString += tableRow;
        }

        tableBodyString += "</tbody>";

        return tableBodyString;
    }

    @GET
    @Produces({MediaType.TEXT_HTML})
    public String getResourceHTML(@PathParam("resource") String resource) {
        Model model = KnowledgeGraphBuilder.getInstance().createKnowledgeGraphForWikiPage(resource, true);
        Resource resourceToRender = model.getResource("http://dbpedia.org/resource/" + resource);
        GroupedResource groupedResource = GroupedResource.create(resourceToRender, model);
        groupedResource.printProperties();
        String htmlText = this.getHtmlText();
        String tableData = this.getTable(groupedResource);
        System.out.println(htmlText);
        System.out.println(tableData);
        htmlText = htmlText.replace("{{tableName}}", resource);
        htmlText = htmlText.replace("{{tableData}}", tableData);
        return htmlText;
        // return HTMLRenderer.renderModel(model, "http://dbpedia.org/resource/" + resource);
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
