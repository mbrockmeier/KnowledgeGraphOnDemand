package server;

import java.util.ArrayList;
import java.util.List;

import static j2html.TagCreator.*;

/**
 * @author Yawen Liu
 */

public class SparqlHTML {
    public static String sparqlRender(String sparqlResult){
        List<List<String>> results = new ArrayList<>();
        String[] lines = sparqlResult.split("\n");
        for (String s:lines){
            List<String> line_each = new ArrayList<>();
            String[] element = s.split(" ");
            for(String ele:element){
                line_each.add(ele);
            }
            results.add(line_each);
        }
        return document(html(table(attrs("#sparql"),
                tbody(
                        each(results, i -> tr(
                                each(i, j -> td(
                                    j
                                ))
                        ))
                )
                ).attr("border", "1")));
    }
}
