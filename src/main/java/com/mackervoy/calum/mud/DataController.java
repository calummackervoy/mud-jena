/**
 * 
 */
package com.mackervoy.calum.mud;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.Response;

import org.apache.jena.query.Dataset;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.tdb2.TDB2Factory;

/**
 * @author Calum Mackervoy
 * A generic endpoint for GETting datasets and resources
 */
@Path("/{any: .*}")
public class DataController extends AbstractMUDController {
	@GET
    @Produces("text/turtle")
    public Response get(@PathParam("any") List<PathSegment> segments) {
		//parse inpu
		String datasetSubPath = "";
		
		for(int i = 0; i < segments.size(); i++) {
			if(datasetSubPath.length() > 0) datasetSubPath += "/";
			datasetSubPath += segments.get(i).toString();
		}
		
		String datasetPath = MUDApplication.getRootDirectory() + datasetSubPath;
		
		//if the dataset is not in use, return 404
		if(!TDBStore.inUseLocation(new File(datasetPath))) {
			throw new NotFoundException();
		}
		
    	Dataset dataset = TDB2Factory.connectDataset(datasetPath);
    	
    	// serialize response
    	dataset.begin(ReadWrite.READ) ;
	    Model m = dataset.getDefaultModel() ;
	    String response = this.serializeModelToTurtle(m);
        dataset.end();
    	
        return Response.ok(response).build();
    }
}
