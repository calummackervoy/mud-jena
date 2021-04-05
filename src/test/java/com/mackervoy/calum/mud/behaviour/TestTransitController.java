package com.mackervoy.calum.mud.behaviour;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.RDF;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mackervoy.calum.mud.MUDApplication;
import com.mackervoy.calum.mud.Random;
//import com.mackervoy.calum.mud.SettlementController;
import com.mackervoy.calum.mud.vocabularies.MUD;

public class TestTransitController extends JerseyTest {
//	@Override
//    protected Application configure() {
//		MUDApplication.initFiles();
//		MUDApplication.initActions();
//        return new ResourceConfig(TransitController.class);
//    }
	
	@BeforeClass
    public static void beforeAllTestMethods() {
		//register describers
//        MUDApplication.initActions();
    }
	
//	protected String getRandomLocalTaskUrl() {
//		return MUDApplication.TASK_LOCAL + Random.getRandomUUIDString();
//	}
	
	// test creation of new Transit task - no prior destination
//	@Test
//	public void testCreateTransit() {
//		Model request = ModelFactory.createDefaultModel();
//		Resource destination = ResourceFactory.createResource();
//
//		Resource character = ResourceFactory.createResource("http://calum.mackervoy.com/example/#character");
//		request.add(character, RDF.type, MUD.Character);
//
//		//TODO: getting URL resources to work with the Grizzly instance
//		Resource dest = ResourceFactory.createResource("http://calum.mackervoy.com/example/#destination");
//		// NOTE: a Building is of type mud:Locatable. POSTing this will require inference
//		//request.add(dest, RDF.type, MUD.Building);
//		request.add(dest, RDF.type, MUD.Locatable);
//
//		ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        request.write(baos, "Turtle");
//
//		Entity<String> reqEntity = Entity.entity(baos.toString(), "text/turtle");
//		Response response = target("/actions/transit/").request()
//		        .post(reqEntity);
//
//	    assertEquals("Http Response should be 201: ", Status.CREATED.getStatusCode(), response.getStatus());
//	    assertEquals("Http Content-Type should be: ", "text/turtle", response.getHeaderString(HttpHeaders.CONTENT_TYPE));
//
//	    String responseTurtle = response.readEntity(String.class);
//	    System.out.println(responseTurtle);
//	}
	
	// TODO: test location is a subclass of MUD.Locatable
	
	// TODO: test creation of Transit task with prior destination set
	
	// TODO: test POSTs where I am not posting a character, or not posting a Locatable
	
	// test GET transit task complete
	
	// test GET transit task not complete
	
	// test GET transit task does not exist
	
	// test POST transit invalid format/data
	
	// test no destination included
}
