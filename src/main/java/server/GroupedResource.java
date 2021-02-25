package server;

import org.apache.jena.rdf.model.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

import static j2html.TagCreator.*;

public class GroupedResource {
    private Model model;
    private Resource resource;
    private HashMap<String, List<RDFNode>> groupedProperties;

    private GroupedResource(Resource resource, Model model) {
        this.resource = resource;
        this.model = model;
        this.groupedProperties = new HashMap<>();

        List<Statement> statements = this.resource.listProperties().toList();

        for (Statement statement : statements) {
            List<RDFNode> valueList = this.groupedProperties.get(statement.getPredicate().toString());

            if (valueList == null) {
                valueList = new ArrayList<>();
            }

            valueList.add(statement.getObject());

            this.groupedProperties.put(statement.getPredicate().toString(), valueList);
        }
    }

    public static GroupedResource create(Resource resource, Model model) {
        return new GroupedResource(resource, model);
    }

    public TreeMap<String, List<RDFNode>> getGroupedProperties() {
        TreeMap<String, List<RDFNode>> sorted = new TreeMap<>();
        sorted.putAll(this.groupedProperties);
        return sorted;
    }

    /**
     * @Author: Yawen Liu
     * @return
     */
    public String getSubject(){
        return this.resource.getLocalName();
    }

    /**
     * @Author: Sunita Pateer
     * @return
     */
    public void printProperties() {
        /*
        for (String property: this.groupedProperties.keySet()){
            String value = this.groupedProperties.get(property).toString();
            System.out.println(property + " <<>> " + value);
        }
        */
        JSONArray rdfJSONArray = this.rdfToJSONArray();
        Iterator<Object> propertyIterator = rdfJSONArray.iterator();

        while(propertyIterator.hasNext())
        {
            JSONObject jsonObj = (JSONObject) propertyIterator.next();

            String subject = jsonObj.getString("subject");

            JSONObject predicate = jsonObj.getJSONObject("predicate");
            String property = "[value:] " + predicate.getString("value");
            property += "[short-form:] " + predicate.getString("shortForm");
            property += "[href:] " + predicate.getString("href");

            JSONArray objectValues = jsonObj.getJSONArray("object");
            Iterator<Object> objectValuesIterator = objectValues.iterator();

            String value = "";
            while(objectValuesIterator.hasNext())
            {
                JSONObject object = (JSONObject) objectValuesIterator.next();
                value += "(";
                value += "[value:] " + object.getString("value");
                value += "[short-form:] " + object.getString("shortForm");
                value += "[href:] " + object.getString("href");
                value += ")";
            }

            System.out.println("subject :: " + subject + " predicate :: " + property + " object :: " + value);
        }
    }

    /**
     * @Author: Sunita Pateer
     * @return
     */
    public JSONArray rdfToJSONArray() {
        JSONArray rdfJSONArray = new JSONArray();

        String subject = this.getSubject();
        TreeMap<String, List<RDFNode>> groupedProperties = this.getGroupedProperties();

        for(Map.Entry<String, List<RDFNode>> groupedProperty : groupedProperties.entrySet())
        {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("subject", subject);

            JSONObject predicate = new JSONObject();

            String property = groupedProperty.getKey();
            predicate.put("value", property);
            predicate.put("shortForm", model.shortForm(property));
            predicate.put("href", property);
            jsonObj.put("predicate", predicate);

            JSONArray objectValues = new JSONArray();

            for(RDFNode rdfNode : groupedProperty.getValue())
            {
                JSONObject object = new JSONObject();

                if (rdfNode.isURIResource()) {
                    object.put("value", rdfNode.asResource().getURI());
                    object.put("shortForm", model.shortForm(rdfNode.asResource().getURI()));
                    object.put("href", rdfNode.asResource().getURI().replace("http://dbpedia.org/", "http://localhost:8080/kgod/"));
                } else if (rdfNode.isLiteral()) {
                    object.put("value", rdfNode.asLiteral().getString() + this.dataType(rdfNode));
                    object.put("shortForm", "");
                    object.put("href", "");
                } else {
                    object.put("value", rdfNode.toString());
                    object.put("shortForm", "");
                    object.put("href", "");
                }

                objectValues.put(object);
            }

            jsonObj.put("object", objectValues);

            rdfJSONArray.put(jsonObj);
        }
        return rdfJSONArray;
    }

    /**
     * @Author: Yawen Liu
     * @param rdfNode the object
     * @return datatype as String
     */
    private String dataType(RDFNode rdfNode){
        String language = rdfNode.asLiteral().getLanguage();
        String uri = rdfNode.asLiteral().getDatatypeURI();

        String dataType = "";

        if (!language.equals("")) {
            dataType += " (" + language + ")";
        }
        if (!uri.equals("")) {
            dataType += " (" + model.shortForm(uri) + ")";
        }
        return dataType;
    }
}
