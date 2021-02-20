/**
 * 
 */
package com.mackervoy.calum.mud;

import java.io.ByteArrayOutputStream;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.apache.jena.query.Dataset;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.tdb2.TDB2Factory;

/**
 * @author Calum Mackervoy
 * A generic endpoint for GETting datasets and resources
 */
@Path("/")
public class DataController {
	@GET
    @Produces("text/turtle")
	@Path("/{dataset}/")
    public String get(@PathParam("dataset") String datasetSubPath) {
    	Dataset dataset = TDB2Factory.connectDataset(MUDApplication.MUD_DIRECTORY + "/" + datasetSubPath) ;
    	dataset.begin(ReadWrite.READ) ;
	    Model m = dataset.getDefaultModel() ;
	        		
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        m.write(baos, "Turtle");
        dataset.end();
    	
        return baos.toString();
    }
}
