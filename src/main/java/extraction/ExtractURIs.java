package extraction;

import org.apache.jena.graph.Node;
import org.apache.jena.graph.Triple;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.sparql.algebra.Algebra;
import org.apache.jena.sparql.algebra.Op;
import org.apache.jena.sparql.algebra.OpVisitorBase;
import org.apache.jena.sparql.algebra.OpWalker;
import org.apache.jena.sparql.algebra.op.OpBGP;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Sven Hertling, University of Mannheim
 */
public class ExtractURIs {
    
    public static Set<String> extract(String query){
        return extract(QueryFactory.create(query));
    }
    
    public static Set<String> extract(Query query){
        Op op = Algebra.compile(query) ;
        op = Algebra.optimize(op) ;
        
        SubjectObjectUriCollector visitor = new SubjectObjectUriCollector();
        OpWalker.walk(op, visitor);
        
        return visitor.getUris();
    }
}

/**
 * @author Sven Hertling, University of Mannheim
 */
class SubjectObjectUriCollector extends OpVisitorBase {
    private final Set<String> uris;

    public SubjectObjectUriCollector(){
        this.uris = new HashSet<>();
    }

    @Override
    public void visit(final OpBGP opBGP) {
        for (Triple triple : opBGP.getPattern()) {
            checkNode(triple.getSubject());
            checkNode(triple.getObject());
        }
    }

    private void checkNode(Node node){
        if(node.isURI()){
            uris.add(node.getURI());
        }
    }

    public Set<String> getUris() {
        return uris;
    }
}