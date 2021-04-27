package com.mackervoy.calum.mud.behaviour.task;

import java.io.StringReader;
import java.util.Optional;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

/**
 * @author Calum Mackervoy
 * Provides endpoint for client-side applications to interface with Task Actions (actions which involve waiting)
 */

@Path("/act/task/")
public class TaskController {
	@POST
    public Response post(@QueryParam("taskUri") String taskUri, String requestBody) {
		// bounds checking - should not provide catch-all providers in this case
		if(taskUri == null) taskUri = "";
		
		// check if I have a provider for the URI
		TaskActorFactory factory = new TaskActorFactory();
		Optional<ITaskActor> result = factory.getActor(taskUri);
		
		// found a provider, return 200 with content
		if(result.isPresent()) {
			ITaskActor actor = result.get();
			Model request = ModelFactory.createDefaultModel();
			request.read(new StringReader(requestBody), "", "TURTLE");
			return actor.act(request);
		}
		
		// could not find a provider - return 204
    	return null;
    }
}
