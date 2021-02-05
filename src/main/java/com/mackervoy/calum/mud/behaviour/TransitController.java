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
	protected Model appendCharacterPatch(Model out, Resource character, Resource destination) {
		//create a solid:Patch to represent the transit location change
		Resource endState = ResourceFactory.createResource(this.getRandomLocalUrl());
		out.add(endState, RDF.type, SolidTerms.Patch);
		
		out.add(endState, SolidTerms.patches, character);
		
		//TODO: build Graph which should be deleted from the requested Character
		//delete that the character has an old location
		System.out.println("TODO here.. character url is " + character.toString());
		
		//build Graph which should be inserted from the requested destination
		Model inserts = ModelFactory.createDefaultModel();
		
		//insert that the character has a new location
		inserts.add(character, MUD.locatedAt, destination);
		
		//SolidTerms.inserts points to our Graph as a Node
		//this makes the output in N3 format (allows us reference a Graph in a triple)
		System.out.println("adding inserts graph");
		out.add(endState, SolidTerms.inserts, 
				out.asRDFNode(NodeFactory.createGraphNode(inserts.getGraph())));
		System.out.println("done adding inserts");
		
		return out;
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
	
	@POST
    @Produces("text/turtle")
    public String getTransit(String requestBody) {
		Model model = null;
		
		System.out.println(requestBody);
		Model request = ModelFactory.createDefaultModel();
		request.read(new StringReader(requestBody), "", "TURTLE");
		System.out.println("read requestBody successfully");
		
		Dataset dataset = TDB2Factory.connectDataset(MUDApplication.ACTION_DATASET);
		System.out.println("connected to dataset");
		try {
			dataset.begin(ReadWrite.WRITE);
			model = dataset.getDefaultModel();
			
			Resource transit = ResourceFactory.createResource(this.getRandomLocalUrl());
			
			//get the location from the request
			//if it includes mutliple, just get the first
			ResIterator destinations = request.listResourcesWithProperty(RDF.type, MUD.Locatable);
			Resource destination = destinations.next();
			if(destination == null) {
				System.out.println("Error! No destination given in request");
				throw new NullPointerException("A mud:Locatable object must be passed as a destination");
			}
				
			//get the character from the request and set this to the patches property
			ResIterator characters = request.listResourcesWithProperty(RDF.type, MUD.Character);
			
			// append a Task for each Character in the list
			while(characters.hasNext()) {
				model = this.appendCharacterPatch(model, characters.next(), destination);
			}
			
			// the transit task is a time:Interval, so it should have a start & end
			model = this.addIntervalProperties(model, transit);
			
			System.out.println("done.");
			System.out.println(model.toString());
			
			// write the task to TDB
			model.commit();
		}
		finally {
			dataset.end();
		}

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if(model != null) model.write(baos, "Turtle");
    	
        return baos.toString();
    }
}
