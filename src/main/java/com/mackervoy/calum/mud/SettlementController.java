package com.mackervoy.calum.mud;

import java.io.ByteArrayOutputStream;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.jena.query.Dataset;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.*;
import org.apache.jena.tdb2.TDB2Factory;

@Path("/settlements/")
public class SettlementController {
    @GET
    @Produces("text/turtle")
    public String getSettlements() {
    	Dataset dataset = TDB2Factory.connectDataset(TDBStore.WORLD.getFileLocation()) ;
    	dataset.begin(ReadWrite.READ) ;
	    Model m = dataset.getDefaultModel() ;
	        		
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        m.write(baos, "Turtle");
        dataset.end();
    	
        return baos.toString();
    }
}