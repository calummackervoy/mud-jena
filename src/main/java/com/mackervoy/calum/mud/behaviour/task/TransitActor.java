package com.mackervoy.calum.mud.behaviour.task;

import javax.ws.rs.core.Response;
import java.net.URI;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ResIterator;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.vocabulary.RDF;

import com.mackervoy.calum.mud.behaviour.Patch;
import com.mackervoy.calum.mud.vocabularies.MUD;
import com.mackervoy.calum.mud.vocabularies.MUDCharacter;
import com.mackervoy.calum.mud.vocabularies.MUDLogic;

/**
 * @author Calum Mackervoy
 * Provides a Transit Task - the movement of A to B
 */
public class TransitActor extends AbstractTaskActor {
	
	public TransitActor() {
		this.addTargetRDFType(MUDLogic.Transit.toString());
	}
	
	public TransitActor(String taskUri) {
		super(taskUri);
		this.addTargetRDFType(MUDLogic.Transit.toString());
	}
	
	private void getCharacterPatches(Model request, Resource destination) {
		ResIterator characters = request.listResourcesWithProperty(RDF.type, MUDCharacter.Character);
		
		// append a Task for each Character in the list
		while(characters.hasNext()) {
			Resource character = characters.next();
			Patch patch = new Patch(this.taskDatasetItem, character);
			Resource insert = ResourceFactory.createResource(this.taskDatasetItem.getNewResourceUri("characterInsert"));
			
			this.model.add(insert, RDF.type, character.getPropertyResourceValue(RDF.type));
			this.model.add(insert, MUD.locatedAt, destination);
			
			patch.addInsert(insert);
			this.task.addPatch(patch);
		}
	}

	@Override
	public String act(Model request) {
		//get the location from the request
		Resource destination = this.getFirstResourceMatchingType(request, MUD.Locatable);
		
		//append endState changes for the Task, for each character
		this.getCharacterPatches(request, destination);
		
		this.model.add(this.task.getResource(), RDF.type, MUDLogic.Transit);
		
		this.commitToDB();
		return this.taskDatasetItem.getUri();
	}
	
}
