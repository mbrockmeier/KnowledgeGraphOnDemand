package server;

import org.json.JSONObject;
import org.apache.jena.rdf.model.Model;
import parser.ModelCacheEntry;
import sparql.RDFConnection_sparql;

/**
 * @author: Yawen Liu
 */

public class JSONSparql {
    private JSONObject jsonObject;
    private Model model;
    private String sparql;

    public JSONSparql(){
        this.jsonObject = new JSONObject();
    }

    public void init(ModelCacheEntry modelCacheEntry, String sparql){
        this.sparql = sparql;
        this.model = modelCacheEntry.getModel();
        RDFConnection_sparql rdfConnection_spaqrql = new RDFConnection_sparql(sparql, model);
        String erg = rdfConnection_spaqrql.connect();
        this.jsonObject.put("result", erg);
    }
    public String getJSON(){
        return jsonObject.toString();
    }

}
