package server;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;

import static j2html.TagCreator.*;

public class HTMLRenderer {
    public static String renderModel(Model model, String resource) {
        Resource resourceToRender = model.getResource(resource);
        GroupedResource groupedResource = GroupedResource.create(resourceToRender);
        return document(html(
                style("h1 {display : inline;}"),
                head(
                        title("KnowledgeGraphOnDemand"),
                        h1("About: "),
                        h1(groupedResource.getSubject())
                ),
                body(
                        main(
                            new ResourceView(resourceToRender).render()
                        )
                )
        ));
    }
}
