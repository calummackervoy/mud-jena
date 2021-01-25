package com.mackervoy.calum.mud.content;

import org.junit.Test;
import org.junit.BeforeClass;
import org.junit.Before;
import org.junit.Assert;

import java.sql.Timestamp;

import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.VCARD4;

import com.mackervoy.calum.mud.MUDApplication;
import com.mackervoy.calum.mud.vocabularies.MUD;
import com.mackervoy.calum.mud.vocabularies.MUDBuildings;
import com.mackervoy.calum.mud.vocabularies.MUDEvents;
import com.mackervoy.calum.mud.vocabularies.Time;

public class TestStadiumDescriber {
	StadiumDescriber stadiumDescriber = new StadiumDescriber();
	Model model;
	Resource stadium;
	String local = "http://localhost:8080/mud/settlements/#";
	
	@BeforeClass
    public static void beforeAllTestMethods() {
		//register describers
        MUDApplication.registerDescribers();
    }
	
	@Before
	public void beforeEachTest() {
		//create Stadium
        model = ModelFactory.createDefaultModel();
        stadium = ResourceFactory.createResource(local + "south_babylon_fc_stadium");
		model.add(stadium, RDF.type, MUDBuildings.Stadium);
		model.add(stadium, VCARD4.fn, "South Babylon Football Club Stadium");
		Resource image = ResourceFactory.createResource("https://www.arthistoryabroad.com/wp-content/uploads/2013/08/LOWRY-Football-Match.jpg");
		model.add(stadium, MUD.primaryImageContent, image);
		model.add(stadium, MUD.primaryTextContent, "A towering brickwork structure of an industrial appearance");
	}
	
	protected void createMatch(Timestamp beginning) {
		Resource match = ResourceFactory.createResource(local + "demo_football_match");
		model.add(match, RDF.type, MUDEvents.FootballMatch);
		
		model.add(stadium, MUDEvents.hasEvent, match);
		
		Resource matchBegins = ResourceFactory.createResource(local + "demo_football_match_begins");
		model.add(matchBegins, RDF.type, Time.Instant);
		model.add(matchBegins, Time.inXSDDateTimeStamp, beginning.toString());
		
		Resource matchEnds = ResourceFactory.createResource(local + "demo_football_match_ends");
		model.add(matchEnds, RDF.type, Time.Instant);
		model.add(matchEnds, Time.inXSDDateTimeStamp, new Timestamp(beginning.getNanos() + 7200000).toString());
		model.add(match, Time.hasBeginning, matchBegins);
		model.add(match, Time.hasEnd, matchEnds);
	}
	
	@Test
	public void testGetStadiumContentGameIsOn() {
		//Create RDF resource where the game is on now
		createMatch(new Timestamp(System.currentTimeMillis()));
		
		//assert that the text content is as expected
		String result = stadiumDescriber.describe(model);
		
		Assert.assertEquals("Thousands of people are in and outside the stadium. There is a lot of noise", result);
	}
	
	@Test
	public void testGetStadiumContentGameIsOff() {
		//Create RDF resource where the game isn't on yet
		createMatch(new Timestamp(System.currentTimeMillis() + 10000000));
		
		//assert that the text content is as expected
		String result = stadiumDescriber.describe(model);
		
		Assert.assertEquals("There is no game on", result);
	}
}
