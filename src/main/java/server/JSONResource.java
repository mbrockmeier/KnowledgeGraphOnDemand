package server;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.json.JSONArray;
import org.json.JSONObject;
import parser.ModelCacheEntry;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class JSONResource {
    private JSONObject jsonRepresentation;
    private Model model;
    private double extractionTime = 0;
    private Date cachedAt = null;

    public JSONResource() {
        this.jsonRepresentation = new JSONObject();
    }

    public void createFromModel(ModelCacheEntry modelCacheEntry, String resource) {
        this.extractionTime = modelCacheEntry.getExtractionDuration();
        this.cachedAt = modelCacheEntry.getDate();
        createFromModel(modelCacheEntry.getModel(), resource);
    }

    public void createFromModel(Model model, String resource) {
        this.model = model;
        Resource resourceToRender = model.getResource(resource);
        GroupedResource groupedResource = GroupedResource.create(resourceToRender, model);

        // Package properties

        JSONArray groupedProperties = new JSONArray();
        for (Map.Entry<String, List<RDFNode>> groupedPropertyEntry : groupedResource.getGroupedProperties().entrySet()) {
            JSONObject groupedProperty = new JSONObject();
            JSONArray objects = new JSONArray();
            groupedProperty.put("predicate", groupedPropertyEntry.getKey());
            groupedProperty.put("prefixedPredicate", model.shortForm(groupedPropertyEntry.getKey()));

            for (RDFNode objectEntry : groupedPropertyEntry.getValue()) {
                JSONObject object = new JSONObject();

                String type = getType(objectEntry);
                object.put("type", type);

                switch (type) {
                    case "uri":
                        object.put("uri", objectEntry.asResource().getURI());
                        object.put("prefixedUri", model.shortForm(objectEntry.asResource().getURI()));
                        break;
                    case "literal":
                        object.put("value", objectEntry.asLiteral().getString());
                        object.put("language", objectEntry.asLiteral().getLanguage());
                        object.put("datatypeUri", objectEntry.asLiteral().getDatatypeURI());
                        object.put("datatype", model.shortForm(objectEntry.asLiteral().getDatatypeURI()));
                        break;
                    case "else":
                        break;
                }

                objects.put(object);
            }

            groupedProperty.put("objects", objects);
            groupedProperties.put(groupedProperty);
        }

        // Package incoming arcs
        JSONArray incomingArcs = new JSONArray();

        for (Map.Entry<String, List<RDFNode>> groupedPropertyEntry : groupedResource.getIncomingArcs().entrySet()) {
            JSONObject incomingArc = new JSONObject();
            JSONArray objects = new JSONArray();
            incomingArc.put("predicate", groupedPropertyEntry.getKey());
            incomingArc.put("prefixedPredicate", model.shortForm(groupedPropertyEntry.getKey()));

            for (RDFNode objectEntry : groupedPropertyEntry.getValue()) {
                JSONObject object = new JSONObject();

                String type = getType(objectEntry);
                object.put("type", type);

                switch (type) {
                    case "uri":
                        object.put("uri", objectEntry.asResource().getURI());
                        object.put("prefixedUri", model.shortForm(objectEntry.asResource().getURI()));
                        break;
                    case "literal":
                        object.put("language", objectEntry.asLiteral().getLanguage());
                        object.put("datatypeUri", objectEntry.asLiteral().getDatatypeURI());
                        object.put("datatype", model.shortForm(objectEntry.asLiteral().getDatatypeURI()));
                        break;
                    case "else":
                        break;
                }

                objects.put(object);
            }

            incomingArc.put("objects", objects);
            incomingArcs.put(incomingArc);
        }

        // Package JSON
        jsonRepresentation.put("subject", groupedResource.getSubject());
        jsonRepresentation.put("groupedProperties", groupedProperties);
        jsonRepresentation.put("incomingArcs", incomingArcs);

        if (this.extractionTime != 0) {
            jsonRepresentation.put("extractionTime", this.extractionTime);
        }
        if (this.cachedAt != null) {
            jsonRepresentation.put("cachedAt", this.cachedAt);
        }
    }

    public String getJSON() {
        return jsonRepresentation.toString();
    }

    public String getType(RDFNode rdfNode) {
        if (rdfNode.isURIResource()) {
            return "uri";
        }
        else if (rdfNode.isLiteral()) {
            return "literal";
        } else {
            return "else";
        }
    }

    public void setExtract(String extract) {
        jsonRepresentation.put("extract", extract);
    }

    public void setRDFXML(String rdf_xml_content) {
        jsonRepresentation.put("rdf+xml", rdf_xml_content);
    }
}
