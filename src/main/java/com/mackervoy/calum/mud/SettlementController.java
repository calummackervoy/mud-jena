package com.mackervoy.calum.mud;

import java.io.ByteArrayOutputStream;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.*;

@Path("/settlements")
public class SettlementController {
    @GET
    //@Produces("text/turtle")
    @Produces(MediaType.TEXT_PLAIN)
    public String sayHtmlHello() {
    	Model m = ModelFactory.createDefaultModel();

        String NS = "https://example.com/test/";

        Resource r = m.createResource(NS + "r");
        Property p = m.createProperty(NS + "p");

        r.addProperty(p, "hello world!", XSDDatatype.XSDstring);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        m.write(baos, "Turtle");
    	
        return baos.toString();
    }
}
