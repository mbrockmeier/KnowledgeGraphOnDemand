package server;

import extraction.KnowledgeGraphConfiguration;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.vocabulary.RDFS;

import java.util.*;

public class GroupedResource {
    private Model model;
    private Resource resource;
    private HashMap<String, List<RDFNode>> groupedProperties;
    private HashMap<String, List<RDFNode>> incomingArcs;

    private GroupedResource(Resource resource, Model model) {
        this.resource = resource;
        this.model = model;
        this.groupedProperties = new HashMap<>();
        this.incomingArcs = new HashMap<>();

        List<Statement> statements = this.resource.listProperties().toList();

        for (Statement statement : statements) {
            List<RDFNode> valueList = this.groupedProperties.get(statement.getPredicate().toString());

            if (valueList == null) {
                valueList = new ArrayList<>();
            }

            valueList.add(statement.getObject());

            this.groupedProperties.put(statement.getPredicate().toString(), valueList);
        }

        List<Statement> incomingStatements = this.model.listStatements(null, null, resource).toList();

        for (Statement incomingStatement : incomingStatements) {
            List<RDFNode> valueList = this.incomingArcs.get(incomingStatement.getPredicate().toString());

            if (valueList == null) {
                valueList = new ArrayList<>();
            }

            valueList.add(incomingStatement.getSubject());

            this.incomingArcs.put(incomingStatement.getPredicate().toString(), valueList);
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

    public TreeMap<String, List<RDFNode>> getIncomingArcs() {
        TreeMap<String, List<RDFNode>> sorted = new TreeMap<>();
        sorted.putAll(this.incomingArcs);
        return sorted;
    }

    /**
     * @author Yawen Liu
     * @return the subject of the resource
     */
    public String getSubject() {
        String language = KnowledgeGraphConfiguration.getLanguage();
        try {
            Statement labelStatement = this.resource.listProperties(RDFS.label).toList()
                    .stream()
                    .filter(statement -> statement.getObject().asLiteral().getLanguage().equals(language))
                    .findFirst()
                    .get();

            return labelStatement.getObject().asLiteral().getString();
        } catch (NoSuchElementException noSuchElementException) {
            return this.resource.getURI();
        }
    }

    /**
     * @author Sunita Pateer
     * @return the abstract of the resource
     */
    public String getAbstract() {
        String language = KnowledgeGraphConfiguration.getLanguage();
        try {
            Statement labelStatement = this.resource.listProperties(RDFS.comment).toList()
                    .stream()
                    .filter(statement -> statement.getObject().asLiteral().getLanguage().equals(language))
                    .findFirst()
                    .get();

            return labelStatement.getObject().asLiteral().getString();
        } catch(NoSuchElementException noSuchElementException) {
            return null;
        }
    }
}
