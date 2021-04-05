package com.mackervoy.calum.mud;

import com.mackervoy.calum.mud.behaviour.task.TransitActor;
import com.mackervoy.calum.mud.content.*;
import com.mackervoy.calum.mud.io.FileReader;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.tdb2.TDB2Factory;

import java.io.IOException;

public class MUDApplication extends javax.ws.rs.core.Application {
	
	//TODO: configurable by web.xml
	private final static String ROOT_DIRECTORY = "./mud/";
	private final static String SITE_URL = "http://localhost:8080/mud/";
	
	public final static String getRootDirectory() { return ROOT_DIRECTORY; }
	public final static String getSiteUrl() { return SITE_URL; }

	/**
	 * init a TDB server storing world from configuration in Assembler
	 * https://jena.apache.org/documentation/tdb/java_api.html
	 */
	public static void initWorld() {
		Dataset dataset = TDB2Factory.connectDataset(TDBStore.WORLD.getFileLocation());
		try {
			dataset.begin(ReadWrite.WRITE);

			if (!dataset.isEmpty()) {
				dataset.abort();
				return;
			}

			Model model = dataset.getDefaultModel();
			new Initialisation(new FileReader()).init(model);

			model.commit();

			dataset.end();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
		new TransitActor();
	}
	
	public MUDApplication() {
		MUDApplication.initWorld();
		MUDApplication.registerDescribers();
		MUDApplication.registerActors();
	}
}
