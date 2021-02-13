/**
 * 
 */
package com.mackervoy.calum.mud.behaviour;

import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.apache.jena.graph.*;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.*;
import org.apache.jena.tdb2.TDB2Factory;
import org.apache.jena.vocabulary.RDF;

import com.mackervoy.calum.mud.MUDApplication;
import com.mackervoy.calum.mud.Random;
import com.mackervoy.calum.mud.vocabularies.MUD;
import com.mackervoy.calum.mud.vocabularies.MUDEvents;
import com.mackervoy.calum.mud.vocabularies.MUDLogic;
import com.mackervoy.calum.mud.vocabularies.Time;
import com.mackervoy.calum.mud.vocabularies.SolidTerms;

/**
 * @author Calum Mackervoy
 * The TransitController provides behaviour to travel between two mud:Locatable objects
 */

@Path("/actions/transit/")
public class TransitController extends AbstractTaskController {
	//TODO: endpoint for user to finish the task
	
	//TODO: documentation
	protected Resource getCharacterPatch(Model out, Resource character, Resource destination) {
		//create a solid:Patch to represent the transit location change
		Resource endState = ResourceFactory.createResource(this.getRandomLocalUrl());
		out.add(endState, RDF.type, SolidTerms.Patch);
		
		out.add(endState, SolidTerms.patches, character);
		
		//TODO: build deleted Character
		//delete that the character has an old location
		System.out.println("TODO here.. character url is " + character.toString());
		
		//represent the inserted Character
		Resource characterInsert = ResourceFactory.createResource(this.getRandomLocalUrl());
		out.add(characterInsert, MUD.locatedAt, destination);
		out.add(endState, MUDLogic.inserts, characterInsert);
		
		//SolidTerms.inserts points to our Graph as a Node
		//this makes the output in N3 format (allows us reference a Graph in a triple)
		/*Triple t = Triple.create(endState.asNode(), SolidTerms.inserts.asNode(), 
				NodeFactory.createGraphNode(inserts.getGraph()));*/
		//out.add(out.asResource(t));
		
		/*out.add(endState, SolidTerms.inserts, 
				out.asRDFNode(NodeFactory.createGraphNode(inserts.getGraph())));*/
		
		return endState;
	}
	
	//TODO: documentation
	protected Model addIntervalProperties(Model out, Resource transit) {
		//create the task beginning and end instants
		Resource taskBegins = ResourceFactory.createResource(this.getRandomLocalUrl());
		out.add(taskBegins, RDF.type, Time.Instant);
		DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
		out.add(taskBegins, Time.inXSDDateTimeStamp, LocalDateTime.now().format(formatter));
		
		Resource taskEnds = ResourceFactory.createResource(this.getRandomLocalUrl());
		out.add(taskEnds, RDF.type, Time.Instant);
		out.add(taskEnds, Time.inXSDDateTimeStamp, LocalDateTime.now().plusHours(2).format(formatter));

		//add them to the transit resource
		out.add(transit, Time.hasBeginning, taskBegins);
		out.add(transit, Time.hasEnd, taskEnds);
		
		return out;
	}
	
	protected Resource getFirstDestinationFromModel(Model request) {
		ResIterator destinations = request.listResourcesWithProperty(RDF.type, MUD.Locatable);
		Resource destination = destinations.next();
		if(destination == null) {
			System.out.println("Error! No destination given in request");
			throw new NullPointerException("A mud:Locatable object must be passed as a destination");
		}
		return destination;
	}
	
	protected Model getModelForCharacterPatches(Model out, Model request, Resource transit, Resource destination) {
		ResIterator characters = request.listResourcesWithProperty(RDF.type, MUD.Character);
		
		// append a Task for each Character in the list
		while(characters.hasNext()) {
			out.add(transit, MUDLogic.endState, this.getCharacterPatch(out, characters.next(), destination));
			System.out.println("appended a Patch");
		}
		return out;
	}
	
	@POST
    @Produces("text/turtle")
    public String getTransit(String requestBody) {
		//read the request data
		Model request = ModelFactory.createDefaultModel();
		request.read(new StringReader(requestBody), "", "TURTLE");
		
		//get the location from the request
		Resource destination = this.getFirstDestinationFromModel(request);
		
		// the transit task is a time:Interval, so it should have a start & end
		Model model = ModelFactory.createDefaultModel();
		Resource transit = ResourceFactory.createResource(this.getRandomLocalUrl());
		model.add(transit, RDF.type, MUDLogic.Transit);
		model = this.addIntervalProperties(model, transit);
		
		//append endState changes for the Task, for each character
		model = this.getModelForCharacterPatches(model, request, transit, destination);
		
		System.out.println("done.");
		
		// commit to TDB
		Model out = null;
		Dataset dataset = TDB2Factory.connectDataset(MUDApplication.ACTION_DATASET);
		System.out.println("connected to dataset");
		/*try {
			dataset.begin(ReadWrite.WRITE);
			out = dataset.getDefaultModel();
			
			//needs to be committed first because of some write operations completed when adding
			//the graph node (getModelForCharacterPatches), throwing a Transaction related exception
			//transit is added just before so that there's something to commit
			out.commit();
			//out.add(model);
			out.commit();
		}
		finally {
			dataset.end();
		}*/

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if(model != null) model.write(baos, "n3");
    	
        return baos.toString();
    }
}
