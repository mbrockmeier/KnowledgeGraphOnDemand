package server;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import parser.DBpediaOntology;

import static j2html.TagCreator.*;

/**
 * @author Malte Brockmeier, Yawen Liu
 */

public class HTMLRenderer {
    public static String renderModel(Model model, String resource) {
        Resource resourceToRender = model.getResource(resource);
        GroupedResource groupedResource = GroupedResource.create(resourceToRender, model);
        OntModel ontModel = DBpediaOntology.getInstance().getOntology();
        return document(html(
                style("h1 {display : inline;}" +
                        "textarea{height: 200px; width: 600px;}"),
                head(
                        h1("About: "),
                        h1(a(groupedResource.getSubject(resourceToRender.getURI())).withHref(resourceToRender.getURI().replace("http://dbpedia.org/", "http://localhost:8080/kgod/"))),
                        h4()
                ),
                body(
                        main(
                            new ResourceView(resourceToRender, model).render(),
                                form().withMethod("post").with(
                                        textarea().withType("text").withName("textarea"),
                                        button("Sparql").withType("submit").withId("button1"))

                        )
                )
        ));
    }
}
