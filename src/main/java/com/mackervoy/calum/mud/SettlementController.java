package com.mackervoy.calum.mud;

import java.io.ByteArrayOutputStream;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.jena.rdf.model.*;

@Path("/settlements")
public class SettlementController {
    @GET
    //@Produces("text/turtle")
    @Produces(MediaType.TEXT_PLAIN)
    public String sayHtmlHello() {
    	Model m = ModelFactory.createDefaultModel();
        
        m.read("https://calum.inrupt.net/public/collections/settlements.ttl");
    	
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        m.write(baos, "Turtle");
    	
        return baos.toString();
    }
}
