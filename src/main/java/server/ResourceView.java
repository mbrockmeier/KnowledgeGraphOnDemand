package server;

import j2html.tags.ContainerTag;
import j2html.tags.Tag;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;

import java.util.Map;

import static j2html.TagCreator.*;

public class ResourceView {
    private Resource resource;
    private Model model;

    public ResourceView(Resource resource, Model model) {
        this.resource = resource;
        this.model = model;
    }

    public Tag render() {
        GroupedResource groupedResource = GroupedResource.create(resource, model);

        return table(attrs("#properties"),
                thead(
                  tr(
                          th("Property"),
                          th("Value")
                  )
                ),
                tbody(
                        each(groupedResource.getGroupedProperties().entrySet(), groupedProperty ->
                                tr(attrs(".property"),
                                        td(
                                                a(model.shortForm(groupedProperty.getKey())).withHref(groupedProperty.getKey())
                                        ),
                                        td(ul(
                                                groupedProperty.getValue().stream().map(resourceValue ->
                                                        li(getRDFNodeValue(resourceValue))
                                                ).toArray(ContainerTag[]::new)
                                        ))
                                )
                        ),
                        each(groupedResource.getIncomingArcs().entrySet(), incomingArc ->
                                tr(attrs(".property"),
                                        td(
                                                span("is "),
                                                a(model.shortForm(incomingArc.getKey())).withHref(incomingArc.getKey()),
                                                span(" of")
                                        ),
                                        td(ul(
                                                incomingArc.getValue().stream().map(resourceValue ->
                                                        li(getRDFNodeValue(resourceValue))
                                                ).toArray(ContainerTag[]::new)
                                        ))
                                )
                        )
                )
        ).attr("border", "1");
    }

    public Tag getRDFNodeValue(RDFNode rdfNode) {
        if (rdfNode.isURIResource()) {
            return a(model.shortForm(rdfNode.asResource().getURI())).withHref(rdfNode.asResource().getURI().replace("http://dbpedia.org/", "http://localhost:8080/kgod/"));
        } else if (rdfNode.isLiteral()) {
            return div(
                    span(rdfNode.asLiteral().getString()),
                    small(dataType((rdfNode)))
            );
        } else {
            return span(rdfNode.toString());
        }
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
