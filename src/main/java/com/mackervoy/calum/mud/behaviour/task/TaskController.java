package com.mackervoy.calum.mud.behaviour.task;

import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.net.URI;
import java.util.Optional;

import javax.ws.rs.POST;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;

import com.mackervoy.calum.mud.AbstractMUDController;

/**
 * @author Calum Mackervoy
 * Provides endpoint for client-side applications to interface with Task Actions (actions which involve waiting)
 */

@Path("/act/task/")
public class TaskController extends AbstractMUDController {
	@POST
    public Response post(@QueryParam("taskUri") String taskUri, String requestBody) {
		// bounds checking - should not provide catch-all providers in this case
		if(taskUri == null) taskUri = "";
		
		// check if I have a provider for the URI
		TaskActorFactory factory = new TaskActorFactory();
		Optional<ITaskActor> result = factory.getActorWithNewTask(taskUri);
		
		// found a provider, return 200 with content
		if(result.isPresent()) {
			ITaskActor actor = result.get();
			Model request = ModelFactory.createDefaultModel();
			request.read(new StringReader(requestBody), "", "TURTLE");
			return Response.created(URI.create(actor.act(request))).build();
		}
		
		// could not find a provider - return 204
    	return null;
    }
	
	@GET
	public Response get(@QueryParam("taskUri") String taskUri) {
		//get the task referenced
		Model model = ModelFactory.createDefaultModel();
		model.read(taskUri);
		Resource task = model.getResource(taskUri);
		
		//get the provider for this type of task
		TaskActorFactory factory = new TaskActorFactory();
		Optional<ITaskActor> result = factory.getActorWithExistingTask(task);
		
		if(result.isPresent()) {
			ITaskActor actor = result.get();
			
			if(actor.isComplete()) {
				//TODO: if the task is completed, mark it as completed
				System.out.println("The Task is complete!");
				
				//TODO: effectuate the world-side changes
			}
		
			//return the task, complete or not
			return Response.ok(this.serializeModelToTurtle(model)).build();
		}
		
		// could not find a provider - return 204
    	return null;
	}

}