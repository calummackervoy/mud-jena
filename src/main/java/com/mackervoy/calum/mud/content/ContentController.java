/**
 *
 */
package com.mackervoy.calum.mud.content;

import com.mackervoy.calum.mud.vocabularies.MUD;
import com.mackervoy.calum.mud.vocabularies.MUDBuildings;
import com.mackervoy.calum.mud.vocabularies.MUDEvents;

import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.util.Optional;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.rdf.model.ResIterator;
import org.apache.jena.vocabulary.RDF;

/**
 * @author Calum Mackervoy
 * Provides endpoints for client-side applications to request content
 * (e.g. "please describe this object")
 */
@Path("/content/")
public class ContentController {
	//NOTE: the ContentContoller POST must receives objects with RDF type set, or it will ignore them
	@POST
	public Response post(String requestBody) {
		final Model request = ModelFactory.createDefaultModel();
		request.read(new StringReader(requestBody), "", "TURTLE");
	
		//build the result model by iterating over each object in the scene and annotating a description
		//TODO: for now we are just describing everything, later we will want to be able to optimise what is described
		Model result = ModelFactory.createDefaultModel();
		ResIterator resources = request.listResourcesWithProperty(RDF.type);
		while(resources.hasNext()) {
			Resource res = resources.next();
			Model m = ModelFactory.createDefaultModel();
			m.read(res.getURI());
			final Resource r = m.getResource(res.getURI());
			
			System.out.println(r);
			this.getDescriber(r)
				.flatMap(describer -> describer.describe(r))
				.ifPresent(content -> result.add(content));
		}
		
		String responseData = result.isEmpty() ? null : modelToTurtle(result);
		return Response.ok(responseData).build();
	}
	
  private Optional<IContentDescriber> getDescriber(Resource r) {
		// check if I have a describer for the URI
		DescriberFactory factory = new DescriberFactory();
		IContentDescriber describer = factory.getDescriber(r.getURI());
		System.out.println("found " + describer + " from " + r.getURI());
		
		// if I don't, try the RDF type
		if (describer == null) {
			try {
				String type = r.getPropertyResourceValue(RDF.type).toString();
				describer = factory.getDescriber(type);
			}
			catch(NullPointerException e) {
				describer = null;
			}
		}

		return Optional.ofNullable(describer);
  }

  private String modelToTurtle(Model m) {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    m.write(baos, "Turtle");
    return baos.toString();
  }
}
