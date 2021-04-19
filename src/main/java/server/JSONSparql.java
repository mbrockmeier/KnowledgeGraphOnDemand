package server;

import org.json.JSONArray;
import org.json.JSONObject;
import org.apache.jena.rdf.model.Model;
import parser.ModelCacheEntry;
import sparql.RDFConnection_sparql;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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

    public void init(Model model, String sparql) {
        this.sparql = sparql;
        this.model = model;
        RDFConnection_sparql rdfConnection_spaqrql = new RDFConnection_sparql(sparql, model);
        ArrayList<ArrayList<String>> erg = rdfConnection_spaqrql.connect();
        //this.jsonObject.put("result", erg);
        JSONArray columns = new JSONArray();

        for(ArrayList<String> eachLine:erg){
            columns.put(eachLine);
        }
        this.jsonObject.put("columns",columns);
    }

    public void init(ModelCacheEntry modelCacheEntry, String sparql) {
        this.sparql = sparql;
        this.model = modelCacheEntry.getModel();
        RDFConnection_sparql rdfConnection_spaqrql = new RDFConnection_sparql(sparql, model);
        ArrayList<ArrayList<String>> erg = rdfConnection_spaqrql.connect();
        JSONArray columns = new JSONArray();

        for(ArrayList<String> eachLine:erg){
            columns.put(eachLine);
        }
        this.jsonObject.put("columns",columns);
    }

    public JSONObject getJSON() {
        return jsonObject;
    }

}
