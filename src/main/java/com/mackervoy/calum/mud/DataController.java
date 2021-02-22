/**
 * 
 */
package com.mackervoy.calum.mud;

import java.io.ByteArrayOutputStream;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.PathSegment;

import org.apache.jena.query.Dataset;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.tdb2.TDB2Factory;

/**
 * @author Calum Mackervoy
 * A generic endpoint for GETting datasets and resources
 */
@Path("/{any: .*}")
public class DataController {
	@GET
    @Produces("text/turtle")
    public String get(@PathParam("any") List<PathSegment> segments) {
		//TODO: check if the dataset exists and return 404 if not (rather than creating an empty one on each 'not found' request
		//	in TDB1 you would use inUseLocation(..) but this is not provided by the TDB2Factory (needs research)
		//  best to use incorporate this into the responsibilities of TDBStore
		
		String datasetSubPath = "";
		
		for(int i = 0; i < segments.size(); i++) {
			datasetSubPath += "/" + segments.get(i).toString();
		}
		
		String datasetPath = MUDApplication.getRootDirectory() + datasetSubPath;
    	Dataset dataset = TDB2Factory.connectDataset(datasetPath);
    	
    	dataset.begin(ReadWrite.READ) ;
	    Model m = dataset.getDefaultModel() ;
	        		
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        m.write(baos, "Turtle");
        dataset.end();
    	
        return baos.toString();
    }
}
