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

    public Tag getRDFNodeValue(RDFNode rdfNode) {
        if (rdfNode.isURIResource()) {
            return a(rdfNode.asResource().getURI()).withHref(rdfNode.asResource().getURI().replace("http://dbpedia.org/resource", "http://localhost:8080/kgod/resource"));
        } else if (rdfNode.isLiteral()) {
            //System.out.println(rdfNode.asLiteral().getString()+" DataType: "+dataType(rdfNode)+" #Original: "+rdfNode.toString());
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
        String erg = "";
        if(!language.equals("")){
            erg = "("+language+")";
        }else{
            String object = rdfNode.toString();
            int index2 = object.indexOf("#");
            String back = object.substring(index2 + 1, object.toString().length());
            erg = "(xsd:"+back+")";
        }
        return erg;
    }
}
