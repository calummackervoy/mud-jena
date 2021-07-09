package com.mackervoy.calum.mud.behaviour;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;

import java.util.Set;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import com.mackervoy.calum.mud.AbstractMUDController;
import com.mackervoy.calum.mud.MUDApplication;
import com.mackervoy.calum.mud.behaviour.task.TaskActorFactory;
import com.mackervoy.calum.mud.vocabularies.MUDLogic;

/**
 * @author Calum Mackervoy
 * Provides Action Discovery (https://multi-user-domain.github.io/docs/05-action-server.html)
 */

@Path("/mud/act/discover/")
public class ActionController extends AbstractMUDController {
	private String getActAtURL(Resource action) {
		String base = MUDApplication.getSiteUrl() + "mud/act/";
		return action.getPropertyResourceValue(RDF.type) == MUDLogic.Task ? base + "task/" : base;
	}
	
	// pass a URI in the query string for an object
	// expect to get back the configured endpoints for actions and tasks on this object
	@POST
	public Response discoverActions(String requestBody) {
		// TODO: analyse the objects in the request (the scene), and return those Actions which have matching shapes for them
		//  https://github.com/Multi-User-Domain/mud-jena/issues/44
		// final Model request = this.serializeTurtleRequestToModel(requestBody);
		
		Model result = ModelFactory.createDefaultModel();
		
		Set<String> keys = TaskActorFactory.keySet();
		
		for(String key : keys) {
			Model m = ModelFactory.createDefaultModel().read(key, "TURTLE");
			Resource r = m.getResource(key);
			result.add(r, RDF.type, r.getPropertyResourceValue(RDF.type));
			result.add(r, MUDLogic.actAt, this.getActAtURL(r));
		}
		
		String responseData = result.isEmpty() ? null : serializeModelToTurtle(result);
		return Response.ok(responseData).build();
  }

}
