package server;

import extraction.KnowledgeGraphConfiguration;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/settings")
public class SettingResource {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateSetting(String json) {
        JSONObject jsonObject = new JSONObject(json);
        KnowledgeGraphConfiguration.upteProperties(jsonObject);

        return Response.ok().build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getSettings() {
        return KnowledgeGraphConfiguration.getProperties().toString();
    }

}