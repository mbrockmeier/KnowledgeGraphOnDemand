package server;
import static j2html.TagCreator.*;

public class SparqlView {
    public static String displayView(String resource) {
        return document(html(
                style(),
                head(),
                body(main(form().withMethod("post").with(
                        textarea().withType("text").withName("textarea"),
                        button("Sparql").withType("submit").withId("button1"))))
        ));
    }
}
