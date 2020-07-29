package API;
import dal.MediaMetaData;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("music")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MetaData {
    @Path("getMetaData")
    @GET
    public MediaMetaData getMetaData(){
        return new MediaMetaData();
    }
}
