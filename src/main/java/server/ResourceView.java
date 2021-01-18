package server;

import j2html.tags.ContainerTag;
import j2html.tags.Tag;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;

import static j2html.TagCreator.*;

public class ResourceView {
    private Resource resource;

    public ResourceView(Resource resource) {
        this.resource = resource;
    }

    public Tag render() {
        GroupedResource groupedResource = GroupedResource.create(resource);

        return table(attrs("#properties"),
                thead(
                  tr(
                          th("Property"),
                          th("Value")
                  )
                ),
                tbody(
                        groupedResource.getGroupedProperties().entrySet().stream().map(groupedProperty ->
                                tr(attrs(".property"),
                                        td(groupedProperty.getKey()),
                                        td(ul(
                                                groupedProperty.getValue().stream().map(resourceValue ->
                                                    li(getRDFNodeValue(resourceValue))
                                                ).toArray(ContainerTag[]::new)
                                        ))
                        )).toArray(ContainerTag[]::new)
                )
        ).attr("border", "1");
    }

    private Tag getRDFNodeValue(RDFNode rdfNode) {
        if (rdfNode.isURIResource()) {
            return a(rdfNode.asResource().getURI()).withHref(rdfNode.asResource().getURI().replace("http://dbpedia.org/resource", "http://localhost:8080/kgod/resource"));
        } else if (rdfNode.isLiteral()) {
            return div(
                    span(rdfNode.asLiteral().getString()),
                    small(rdfNode.asLiteral().getDatatypeURI())
            );
        } else {
            return span(rdfNode.toString());
        }
    }
}
