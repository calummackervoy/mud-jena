/**
 * 
 */
package com.mackervoy.calum.mud.content;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;

import com.mackervoy.calum.mud.MUDApplication;

/**
 * @author Calum Mackervoy
 * Provides endpoints for client-side applications to request content 
 * (e.g. "please describe this object")
 */
@Path("/content/")
public class ContentController {
	@GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getContent(@QueryParam("uri") String uri) {
		// bounds checking - could provide a catch-all describer this way
		if(uri == null) uri = "";
		
		// check if I have a describer for the URI
		DescriberFactory factory = new DescriberFactory();
		IContentDescriber describer = factory.getDescriber(uri);
		Model m = ModelFactory.createDefaultModel();
    	m.read(uri);
		
		// if I don't, try the RDF type
		if(describer == null) {
			Resource r = m.getResource(uri);
	    	String type = r.getPropertyResourceValue(RDF.type).toString();
	    	System.out.println("Retrieved type: " + type);
	    	describer = factory.getDescriber(type);
		}
		
		// found a Describer, return 200 with content
		if(describer != null) {
			return describer.describe(m, uri);
		}
		
		// could not find a describer - return 204
    	return null;
    }
}
