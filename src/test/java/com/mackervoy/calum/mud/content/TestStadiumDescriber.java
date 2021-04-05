package com.mackervoy.calum.mud.content;

import org.junit.Test;
import org.junit.BeforeClass;
import org.junit.Before;
import org.junit.Assert;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
	String local = "http://localhost:9998/mud/settlements/#";
	
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
	
	protected void createMatch(LocalDateTime beginning) {
		Resource match = ResourceFactory.createResource(local + "demo_football_match");
		model.add(match, RDF.type, MUDEvents.FootballMatch);
		
		model.add(stadium, MUDEvents.hasEvent, match);
		
		Resource matchBegins = ResourceFactory.createResource(local + "demo_football_match_begins");
		model.add(matchBegins, RDF.type, Time.Instant);
		DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
		model.add(matchBegins, Time.inXSDDateTimeStamp, beginning.format(formatter));
		
		Resource matchEnds = ResourceFactory.createResource(local + "demo_football_match_ends");
		model.add(matchEnds, RDF.type, Time.Instant);
		model.add(matchEnds, Time.inXSDDateTimeStamp, beginning.plusHours(2).format(formatter));
		model.add(match, Time.hasBeginning, matchBegins);
		model.add(match, Time.hasEnd, matchEnds);
	}
	
//	@Test
//	public void testGetStadiumContentGameIsOn() {
//		//Create RDF resource where the game is on now
//		createMatch(LocalDateTime.now());
//
//		//assert that the text content is as expected
//		String result = stadiumDescriber.describe(model, stadium.toString());
//
//		Assert.assertEquals("Thousands of people are in and outside the stadium. There is a lot of noise", result);
//	}
//
//	@Test
//	public void testGetStadiumContentGameIsOff() {
//		//Create RDF resource where the game isn't on yet
//		createMatch(LocalDateTime.now().plusDays(1));
//
//		//assert that the text content is as expected
//		String result = stadiumDescriber.describe(model, stadium.toString());
//
//		Assert.assertEquals("There is no game on right now", result);
//	}
}
