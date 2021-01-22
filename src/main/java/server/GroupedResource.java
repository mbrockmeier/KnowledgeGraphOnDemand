package server;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

public class GroupedResource {
    private Model model;
    private Resource resource;
    private HashMap<String, List<RDFNode>> groupedProperties;

    private GroupedResource(Resource resource, Model model) {
        this.resource = resource;
        this.model = model;
        this.groupedProperties = new HashMap<String, List<RDFNode>>();

        List<Statement> statements = this.resource.listProperties().toList();

        for (Statement statement : statements) {
            List<RDFNode> valueList = this.groupedProperties.get(model.shortForm(statement.getPredicate().toString()));

            if (valueList == null) {
                valueList = new ArrayList<RDFNode>();
            }

            valueList.add(statement.getObject());

            this.groupedProperties.put(model.shortForm(statement.getPredicate().toString()), valueList);
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
}