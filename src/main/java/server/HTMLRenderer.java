package server;
import org.apache.jena.rdf.model.*;

import static j2html.TagCreator.*;

public class HTMLRenderer {
    public static String renderModel(Model model, String resource) {
        Resource resourceToRender = model.getResource(resource);
        GroupedResource groupedResource = GroupedResource.create(resourceToRender, model);
        System.out.println("the link is: "+ resourceToRender.getURI());
        return document(html(
                style("h1 {display : inline;}" +
                        "textarea{height: 200px; width: 600px;}"),
                head(
                        h1("About: "),
                        h1(a(groupedResource.getSubject()).withHref(resourceToRender.getURI().replace("http://dbpedia.org/", "http://localhost:8080/kgod/")))
                ),
                body(
                        main(
                                new ResourceView(resourceToRender, model).render(),
                                form().with(a("Sparql").withHref(resourceToRender.getURI().replace("http://dbpedia.org/resource", "http://localhost:8080/kgod/sparql")))
                        )
                )
        ));
    }
}
