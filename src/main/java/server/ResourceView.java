package server;

import j2html.attributes.Attr;
import j2html.tags.ContainerTag;
import j2html.tags.Tag;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;

import java.util.List;

import static j2html.TagCreator.*;

public class ResourceView {
    private Resource resource;

    public ResourceView(Resource resource) {
        this.resource = resource;
    }

    public Tag render() {
        List<Statement> statements = resource.listProperties().toList();

        return table(attrs("#properties"),
                thead(
                  tr(
                          th("Property"),
                          th("Value")
                  )
                ),
                tbody(
                        statements.stream().map(statement ->
                                tr(attrs(".property"),
                                        td(statement.getPredicate().getNameSpace() + ":" + statement.getPredicate().getLocalName()),
                                        td(
                                            getStatementValue(statement)
                                        ))
                        ).toArray(ContainerTag[]::new)
                )
        ).attr("border", "1");
    }

    private Tag getStatementValue(Statement statement) {
        if (statement.getObject().isURIResource()) {
            return a(statement.getObject().asResource().getURI()).withHref(statement.getObject().asResource().getURI().replace("http://dbpedia.org/resource", "http://localhost:8080/kgod/resource"));
        } else if (statement.getObject().isLiteral()) {
            return div(
                    span(statement.getObject().asLiteral().getString()),
                    small(statement.getObject().asLiteral().getDatatypeURI())
            );
        } else {
            return span(statement.getObject().toString());
        }
    }
}
