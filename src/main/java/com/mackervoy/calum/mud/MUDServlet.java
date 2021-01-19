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
import org.apache.jena.vocabulary.VCARD;

import com.mackervoy.calum.mud.vocabularies.MUD;

public class MUDServlet extends com.sun.jersey.spi.container.servlet.ServletContainer {
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
		Resource agora = ResourceFactory.createResource(local + "athens_agora");
		model.add(agora, RDF.type, MUD.Building);
		model.add(agora, VCARD.FN, "Agora");
		
		Resource ampitheatre = ResourceFactory.createResource(local + "athens_ampitheatre");
		model.add(ampitheatre, RDF.type, MUD.Building);
		model.add(ampitheatre, VCARD.FN, "Ampitheatre");
		
		Resource athens = ResourceFactory.createResource(local + "athens");
		model.add(athens, RDF.type, MUD.Settlement);
		model.add(athens, VCARD.FN, "Athens");
		model.add(athens, MUD.population, "20000");
		model.add(athens, MUD.hasBuilding, agora);
		model.add(athens, MUD.hasBuilding, ampitheatre);
		
		Resource sparta = ResourceFactory.createResource(local + "sparta");
		model.add(sparta, RDF.type, MUD.Settlement);
		model.add(sparta, VCARD.FN, "Sparta");
		model.add(sparta, MUD.population, "5000");
		
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
	
	public void init() throws ServletException {
		System.out.println("PATH is " + this.PATH);
		this.initFiles();
		super.init();
	}
}
