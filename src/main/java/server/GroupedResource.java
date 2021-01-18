package server;

import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GroupedResource {
    private Resource resource;
    private HashMap<String, List<RDFNode>> groupedProperties;

    private GroupedResource(Resource resource) {
        this.resource = resource;
        this.groupedProperties = new HashMap<String, List<RDFNode>>();

        List<Statement> statements = this.resource.listProperties().toList();

        for (Statement statement : statements) {
            List<RDFNode> valueList = this.groupedProperties.get(statement.getPredicate().toString());

            if (valueList == null) {
                valueList = new ArrayList<RDFNode>();
            }

            valueList.add(statement.getObject());

            this.groupedProperties.put(statement.getPredicate().toString(), valueList);
        }
    }

    public static GroupedResource create(Resource resource) {
        return new GroupedResource(resource);
    }

    public HashMap<String, List<RDFNode>> getGroupedProperties() {
        return this.groupedProperties;
    }
}
