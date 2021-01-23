package com.mackervoy.calum.mud;

import java.io.ByteArrayOutputStream;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import org.apache.jena.rdf.model.*;

@Path("/settlements/")
public class SettlementController {
    @GET
    @Produces("text/turtle")
    //@Produces(MediaType.TEXT_PLAIN)
    public String getSettlements() {
    	Model m = ModelFactory.createDefaultModel();
        
    	m.read(MUDServlet.PATH);
    		
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        m.write(baos, "Turtle");
    	
        return baos.toString();
    }
}