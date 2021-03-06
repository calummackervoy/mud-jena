package com.mackervoy.calum.mud;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;

import org.apache.jena.query.Dataset;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.*;
import org.apache.jena.tdb2.TDB2Factory;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.VCARD4;

import com.mackervoy.calum.mud.vocabularies.*;
import com.mackervoy.calum.mud.behaviour.task.TransitActor;
import com.mackervoy.calum.mud.content.*;

public class MUDApplication extends javax.ws.rs.core.Application{
	
	private static String ROOT_DIRECTORY;
	private static String SITE_URL;
	
	public final static String getRootDirectory() { return ROOT_DIRECTORY; }
	public final static String getSiteUrl() { return SITE_URL; }
	
	protected static void initSettlement() {
		// Make a TDB-backed dataset
	    //TODO: extend the DatasetItem class to wrap the Jena dataset, so that the write can be completed by this class
		//TODO: use a new datasetItem for the world content or continue with the global one ? Open issue
	    //DatasetItem demoData = TDBStore.WORLD.getNewDataset();
		Dataset dataset = TDB2Factory.connectDataset(TDBStore.WORLD.getFileLocation()) ;
		try {
			dataset.begin(ReadWrite.WRITE) ;
			
			if(!dataset.isEmpty()) {
		    	dataset.abort();
		    	return;
		    }
			
			Model model = dataset.getDefaultModel() ;
			
			// add a football stadium (South Babylon FC)
			Resource stadium = ResourceFactory.createResource(TDBStore.WORLD.getNewResourceUri("building", "south_babylon_fc_stadium"));
			model.add(stadium, RDF.type, MUDBuildings.Stadium);
			model.add(stadium, VCARD4.fn, "South Babylon Football Club Stadium");
			Resource image = ResourceFactory.createResource("https://www.arthistoryabroad.com/wp-content/uploads/2013/08/LOWRY-Football-Match.jpg");
			model.add(stadium, MUD.primaryImageContent, image);
			model.add(stadium, MUD.primaryTextContent, "A towering brickwork structure of an industrial appearance");
			
			// there's a match at the stadium
			Resource match = ResourceFactory.createResource(TDBStore.WORLD.getNewResourceUri("event", "demo_football_match"));
			model.add(match, RDF.type, MUDEvents.FootballMatch);
			
			Resource matchBegins = ResourceFactory.createResource(TDBStore.WORLD.getNewResourceUri("instant", "demo_football_match_begins"));
			model.add(matchBegins, RDF.type, Time.Instant);
			DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
			model.add(matchBegins, Time.inXSDDateTimeStamp, LocalDateTime.now().format(formatter));
			
			Resource matchEnds = ResourceFactory.createResource(TDBStore.WORLD.getNewResourceUri("instant", "demo_football_match_ends"));
			model.add(matchEnds, RDF.type, Time.Instant);
			model.add(matchEnds, Time.inXSDDateTimeStamp, LocalDateTime.now().plusHours(2).format(formatter));
			model.add(match, Time.hasBeginning, matchBegins);
			model.add(match, Time.hasEnd, matchEnds);
			
			model.add(stadium, MUDEvents.hasEvent, match);
			
			// add a night club in Babylon
			Resource collective = ResourceFactory.createResource(TDBStore.WORLD.getNewResourceUri("building", "the_collective_night_club"));
			model.add(collective, RDF.type, MUDBuildings.Nightclub);
			model.add(collective, VCARD4.fn, "The Collective Night Club");
			
			Resource babylon = ResourceFactory.createResource(TDBStore.WORLD.getNewResourceUri("settlement", "babylon"));
			model.add(babylon, RDF.type, MUD.Settlement);
			model.add(babylon, VCARD4.fn, "Babylon");
			model.add(babylon, MUD.population, "8000000");
			model.add(babylon, MUD.hasBuilding, stadium);
			model.add(babylon, MUD.hasBuilding, collective);
			model.add(babylon, MUD.primaryTextContent, "The Capital of the Babylonian Empire");
			
			Resource roric = ResourceFactory.createResource(TDBStore.WORLD.getNewResourceUri("settlement", "roric"));
			model.add(roric, RDF.type, MUD.Settlement);
			model.add(roric, VCARD4.fn, "Roric");
			model.add(roric, MUD.population, "1000000");
			model.add(roric, MUD.primaryTextContent, "The Second City of the Babylon region, but a cosmopolis in its own right, and an industrial powerhouse");
			
			model.commit();
		}
		finally {
			dataset.end();
		}
	}
	
	/**
	 * init a TDB server storing world from configuration in Assembler
	 * https://jena.apache.org/documentation/tdb/java_api.html
	 */
	public static void initWorld() {
        MUDApplication.initSettlement();
		
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
	
	public static void registerActors() {
		TransitActor.registerTargetRDFTypes();
	}

	public MUDApplication(@Context ServletContext context) {
		SITE_URL = context.getInitParameter("com.mackervoy.calum.mud.BASE_URL");
		ROOT_DIRECTORY = context.getInitParameter("com.mackervoy.calum.mud.ROOT_DIRECTORY");

		//normalize directory/site parameters - ensure they have a trailing /
		if(!ROOT_DIRECTORY.endsWith("/")) ROOT_DIRECTORY += "/";
		if(!SITE_URL.endsWith("/")) SITE_URL += "/";
		
		MUDApplication.initWorld();
		MUDApplication.registerDescribers();
		MUDApplication.registerActors();
	}
}
