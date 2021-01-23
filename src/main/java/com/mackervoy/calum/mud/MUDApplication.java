package com.mackervoy.calum.mud;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

import javax.servlet.ServletException;

import org.apache.jena.rdf.model.*;
import org.apache.jena.sparql.vocabulary.FOAF;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.VCARD4;

import com.mackervoy.calum.mud.vocabularies.MUD;

public class MUDApplication extends javax.ws.rs.core.Application {
	//TODO: configuring permanent file storage (will probably be replaced by a triple data store later)
	public final static String MUD_DIRECTORY = "./mud";
	public final static String SETTLEMENTS_STORAGE = "settlements.ttl";
	public final static String PATH = MUD_DIRECTORY + "/" + SETTLEMENTS_STORAGE;
	
	protected void initSettlement(File file) throws IOException {
		Model model = ModelFactory.createDefaultModel() ;
		model.read(this.PATH) ;
		
		// TODO: configuration needed here, in the web.xml ?
		String local = "http://localhost:8080/#";
		
		//configuration data
		Resource stadium = ResourceFactory.createResource(local + "south_babylon_fc_stadium");
		model.add(stadium, RDF.type, MUD.Building);
		model.add(stadium, VCARD4.fn, "South Babylon Football Club Stadium");
		Resource image = ResourceFactory.createResource("https://www.arthistoryabroad.com/wp-content/uploads/2013/08/LOWRY-Football-Match.jpg");
		model.add(stadium, MUD.primaryImageContent, image);
		model.add(stadium, MUD.primaryTextContent, "There is no game on right now");
		
		Resource collective = ResourceFactory.createResource(local + "the_collective_night_club");
		model.add(collective, RDF.type, MUD.Building);
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
		
		FileWriter fis = new FileWriter(file);
        model.write(fis, "Turtle");
	}
	
	protected void initFiles() {
    	File directory = new File(this.MUD_DIRECTORY);
        if (! directory.exists()){
        	// NOTE: if you require it to make entire directory path including parents use mkdirs() method instead
            directory.mkdir();
        }

        
        File file = new File(this.PATH);
        if (! file.exists()) {
        	try {
	        	file.createNewFile();
	        	this.initSettlement(file);
        	}
        	catch(java.io.IOException e) {
        		System.out.println("Error in creation of init file .mud/settlements.ttl");
        		e.printStackTrace();
        	}
        }
	}
	
	public MUDApplication() {
		this.initFiles();
	}
}
