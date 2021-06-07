/**
 * 
 */
package com.mackervoy.calum.mud;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.Response;

/**
 * @author Calum Mackervoy
 * A generic endpoint for GETting datasets and resources
 */
@Path("/mud/{any: .*}")
public class DataController extends AbstractMUDController {
	@GET
    @Produces("text/turtle")
    public Response get(@PathParam("any") List<PathSegment> segments) {
		// parses input into a file directory path
		String datasetSubPath = "";
		
		for(int i = 0; i < segments.size(); i++) {
			if(datasetSubPath.length() > 0) datasetSubPath += "/";
			datasetSubPath += segments.get(i).toString();
		}

		// serializes the dataset into a response, or else raises a 404
	    String response = this.serializeModelToTurtle(TDBStore.getDatasetItem(datasetSubPath).getModel());
        return Response.ok(response).build();
    }
}
