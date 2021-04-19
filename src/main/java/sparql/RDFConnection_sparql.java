package sparql;

import org.apache.jena.base.Sys;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.rdfconnection.RDFConnection;
import org.apache.jena.rdfconnection.RDFConnectionFactory;
import org.apache.jena.system.Txn;
import server.SparqlResource;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
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

    public ArrayList<ArrayList<String>> connect(){
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
        ArrayList<ArrayList<String>> results = new ArrayList<>();
        switch (type){
            case ASK:
                Boolean rs_ask = this.getAskResult(conn,query);
                //System.out.println(rs_ask);
                erg = String.valueOf(rs_ask);
                break;
            case SELECT:
                ResultSet rs_select = this.getSelectResult(conn,query);
                results = this.formatR(rs_select);
                //results = this.formatErg(rs_select);
                //erg = this.formatResult(rs_select);
                break;
            case DESCRIBE:
                Model rs_describe = this.getDescribeResult(conn,query);
                StmtIterator stmtIterator = rs_describe.listStatements();
                StringBuffer sb = new StringBuffer();
                while (stmtIterator.hasNext()){
                    Statement stmt = stmtIterator.nextStatement();
                    String subject = stmt.getSubject().toString();
                    String predicate = stmt.getPredicate().toString();
                    RDFNode object = stmt.getObject();
                    String object_final = "";
                    if (object instanceof Resource) {
                        object_final = object+"";
                    } else {
                        object_final = object.toString();
                    }
                    sb.append(subject+" "+predicate+" "+object_final+"\n");
                }
                erg = sb.toString();
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
        return results;
    }
    private ArrayList<ArrayList<String>> formatR(ResultSet rs){
        ArrayList<ArrayList<String>> results = new ArrayList<>();
        //int count = 0;
        ArrayList<String> first_row = new ArrayList<>();
        int count = 0;
        while (rs.hasNext()){
            QuerySolution querySolution = rs.nextSolution();
            Iterator<String> iter = querySolution.varNames();
            ArrayList<String> eachLine = new ArrayList<>();
            while (iter.hasNext()) {
                String name = iter.next();
                if (count == 0) {
                    first_row.add(name);
                }
                //System.out.println("other name: "+name);
                RDFNode rdfNode = querySolution.get(name);
                //System.out.println("name: "+ name);
                String line = "";
                if (rdfNode.isLiteral()) {
                    Literal literal = (Literal) rdfNode;
                    line = literal + "";
                } else if (rdfNode.isURIResource()) {
                    Resource res = (Resource) rdfNode;
                    line = res.getURI();
                } else {
                    line = rdfNode.toString();
                }
                eachLine.add(line);
            }
            count++;
            results.add(eachLine);
        }
        results.add(0,first_row);
        return results;
    }

 /**   private HashMap<String, ArrayList<String>> formatErg(ResultSet rs){
        //System.out.println("in the formatErg method");
        HashMap<String,ArrayList<String>> erg = new HashMap<>();
        Collection<String> keys = erg.keySet();
        while (rs.hasNext()){

          //check every line of the results
          QuerySolution querySolution = rs.nextSolution();
          Iterator<String> iter = querySolution.varNames();

          //If there is no key in the HashMap, that means the first line
         if (keys.isEmpty()){
              System.out.println("Now the key set of the hashMap is empty!!!!!!!!!!");
              while (iter.hasNext()){
                  String name = iter.next();
                  RDFNode rdfNode = querySolution.get(name);
                  //System.out.println("name: "+ name);
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
                  ArrayList<String> arr = new ArrayList<>();
                  arr.add(line);
                  erg.put(name,arr);
              }
          }else{
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
                  ArrayList<String> arr = erg.get(name);
                  arr.add(line);
                  erg.put(name,arr);
              }
          }

        }
        return erg;
    }
    **/

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
