package sparql;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdfconnection.RDFConnection;
import org.apache.jena.rdfconnection.RDFConnectionFactory;
import org.apache.jena.system.Txn;

import java.io.ByteArrayOutputStream;
import java.util.Iterator;


/**
 * @author Yawen Liu
 */
public class RDFConnection_sparql {
    private String sparql;
    private Model model;
    public RDFConnection_sparql(String sparql, Model model){
        this.sparql = sparql;
        this.model = model;
    }

    public String connect(){
        Query query = QueryFactory.create(this.sparql);
        Dataset dataset = DatasetFactory.createTxnMem();
        RDFConnection conn = RDFConnectionFactory.connect(dataset);
        Txn.executeWrite(conn, () ->{
            System.out.println("Load a model");
            conn.load(this.model);
            System.out.println("In write transaction");

        });
        System.out.println("After write transaction");

        QueryType type = query.queryType();
        String erg = "";

        switch (type){
            case ASK:
                Boolean rs_ask = this.getAskResult(conn,query);
                //System.out.println(rs_ask);
                erg = String.valueOf(rs_ask);
                break;
            case SELECT:
                ResultSet rs_select = this.getSelectResult(conn,query);
                erg = this.formatResult(rs_select);
                break;
            case DESCRIBE:
                Model rs_describe = this.getDescribeResult(conn,query);
                System.out.println(rs_describe);
                break;
            case CONSTRUCT:
                Model rs_construct = this.getConstructResult(conn,query);
                System.out.println(rs_construct);
                break;
            case UNKNOWN:
                System.out.println("this is unknown");
                break;
            default:
                break;
        }


        /**ResultSet rs = conn.query(query).execSelect();
         ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
         ResultSetFormatter.outputAsJSON(outputStream, rs);
         String json = new String(outputStream.toByteArray());**/
        return erg;
    }

    private String formatResult(ResultSet rs){
        StringBuffer sb = new StringBuffer();
        while (rs.hasNext()){
            QuerySolution querySolution = rs.nextSolution();
            Iterator<String> iter = querySolution.varNames();
            while (iter.hasNext()){
                String name = iter.next();
                RDFNode rdfNode = querySolution.get(name);
                String line = "";
                if (rdfNode.isLiteral()){
                    Literal literal = (Literal)rdfNode;
                    line = literal+"";
                }else if (rdfNode.isURIResource()){
                    Resource res = (Resource) rdfNode;
                    line = res.getURI();
                }else {
                    line = rdfNode.toString();
                }
                sb.append(line+" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    private Boolean getAskResult(RDFConnection conn, Query query){
        Boolean rs = conn.query(query).execAsk();
        return rs;
    }

    private  ResultSet getSelectResult(RDFConnection conn, Query query){
        ResultSet rs = conn.query(query).execSelect();
        return rs;
    }

    private Model getDescribeResult(RDFConnection conn, Query query){
        Model rs = conn.query(query).execDescribe();
        return rs;
    }

    private Model getConstructResult(RDFConnection conn, Query query){
        Model rs = conn.query(query).execConstruct();
        return rs;
    }
}
