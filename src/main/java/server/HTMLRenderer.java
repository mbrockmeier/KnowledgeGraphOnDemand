package server;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;

import static j2html.TagCreator.*;

public class HTMLRenderer {
    public static String renderModel(Model model, String resource) {
        Resource resourceToRender = model.getResource(resource);
        return document(html(
                head(
                        title("KnowledgeGraphOnDemand")
                ),
                body(
                        main(
                            new ResourceView(resourceToRender).render()
                        )
                )
        ));
    }
}
