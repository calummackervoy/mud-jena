package com.mackervoy.calum.mud;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.servlet.ServletException;

import org.apache.jena.assembler.JA;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.*;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.sparql.vocabulary.FOAF;
import org.apache.jena.system.Txn;
import org.apache.jena.tdb2.TDB2Factory;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.VCARD4;

import com.mackervoy.calum.mud.vocabularies.*;
import com.mackervoy.calum.mud.content.*;

public class MUDApplication extends javax.ws.rs.core.Application {
	//TODO: configuring permanent file storage (will probably be replaced by a triple data store later)
	public final static String MUD_DIRECTORY = "./mud";
	public final static String SETTLEMENTS_STORAGE = "settlements.ttl";
	public final static String PATH = MUD_DIRECTORY + "/" + SETTLEMENTS_STORAGE;
	public final static String WORLD_DATASET = MUD_DIRECTORY + "/World";
	
	// TODO: configuration needed here, in the web.xml ?
	public static String local = "http://localhost:8080/mud/settlements/#";
	
	protected static void initSettlement() {
		// Make a TDB-backed dataset
		Dataset dataset = TDB2Factory.createDataset(MUDApplication.WORLD_DATASET) ;
		dataset.begin(ReadWrite.WRITE) ;
	    Model model = dataset.getDefaultModel() ;
		
		// add a football stadium (South Babylon FC)
		Resource stadium = ResourceFactory.createResource(local + "south_babylon_fc_stadium");
		model.add(stadium, RDF.type, MUDBuildings.Stadium);
		model.add(stadium, VCARD4.fn, "South Babylon Football Club Stadium");
		Resource image = ResourceFactory.createResource("https://www.arthistoryabroad.com/wp-content/uploads/2013/08/LOWRY-Football-Match.jpg");
		model.add(stadium, MUD.primaryImageContent, image);
		model.add(stadium, MUD.primaryTextContent, "A towering brickwork structure of an industrial appearance");
		
		// there's a match at the stadium
		Resource match = ResourceFactory.createResource(local + "demo_football_match");
		model.add(match, RDF.type, MUDEvents.FootballMatch);
		
		Resource matchBegins = ResourceFactory.createResource(local + "demo_football_match_begins");
		model.add(matchBegins, RDF.type, Time.Instant);
		DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
		model.add(matchBegins, Time.inXSDDateTimeStamp, LocalDateTime.now().format(formatter));
		
		Resource matchEnds = ResourceFactory.createResource(local + "demo_football_match_ends");
		model.add(matchEnds, RDF.type, Time.Instant);
		model.add(matchEnds, Time.inXSDDateTimeStamp, LocalDateTime.now().plusHours(2).format(formatter));
		model.add(match, Time.hasBeginning, matchBegins);
		model.add(match, Time.hasEnd, matchEnds);
		
		model.add(stadium, MUDEvents.hasEvent, match);
		
		// add a night club in Babylon
		Resource collective = ResourceFactory.createResource(local + "the_collective_night_club");
		model.add(collective, RDF.type, MUDBuildings.Nightclub);
		model.add(collective, VCARD4.fn, "The Collective Night Club");
		
		Resource babylon = ResourceFactory.createResource(local + "babylon");
		model.add(babylon, RDF.type, MUD.Settlement);
		model.add(babylon, VCARD4.fn, "Babylon");
		model.add(babylon, MUD.population, "8000000");
		model.add(babylon, MUD.hasBuilding, stadium);
		model.add(babylon, MUD.hasBuilding, collective);
		model.add(babylon, MUD.primaryTextContent, "The Capital of the Babylonian Empire");
		
		Resource roric = ResourceFactory.createResource(local + "roric");
		model.add(roric, RDF.type, MUD.Settlement);
		model.add(roric, VCARD4.fn, "Roric");
		model.add(roric, MUD.population, "1000000");
		model.add(roric, MUD.primaryTextContent, "The Second City of the Babylon region, but a cosmopolis in its own right, and an industrial powerhouse");
		
		model.commit();
	    dataset.end();
	}
	
	public static void initFiles() {
    	File directory = new File(MUDApplication.MUD_DIRECTORY);
        if (! directory.exists()){
        	// NOTE: if you require it to make entire directory path including parents use mkdirs() method instead
            directory.mkdir();
        }

        MUDApplication.initSettlement();
	}
	
	/**
	 * init a TDB server storing world from configuration in Assembler
	 * https://jena.apache.org/documentation/tdb/java_api.html
	 */
	public static void initWorld() {
		MUDApplication.initFiles();
		
		// TODO: we want to use Assembler method
		//  commented out because was having 404 on the Jena vocab
		//  on both http://jena.hpl.hp.com/2005/11/Assembler
		//  and http://jena.apache.org/2016/tdb
		/*String assemblerFile = "mud/templates/assembler.ttl" ;
		Dataset dataset = TDB2Factory.assembleDataset(assemblerFile) ;*/
	}
	
	/*
	 * Configures Describer classes with the DescriberFactory
	 */
	public static void registerDescribers() {
		new StadiumDescriber();
	}
	
	public MUDApplication() {
		MUDApplication.initWorld();
		MUDApplication.registerDescribers();
	}
}
