package com.mackervoy.calum.mud;

import org.apache.jena.query.Dataset;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.tdb2.TDB2Factory;

/**
 * @author Calum Mackervoy
 * String accessors for the URI, File system location and for generating new resource Ids of a Dataset stored in the MUD TDB
 */
public class DatasetItem extends AbstractFileStorageWrapper {
	private String name;
	
	/**
	 * A constructor for a new Dataset in a collection, generates a random name for it
	 */
	public DatasetItem(String collectionPath) {
		super(collectionPath);
		this.name = Random.getRandomUUIDString();
	}
	
	/**
	 * A constructor for an existing Dataset in a collection, reads the model data from the dataset's URI
	 */
	public DatasetItem(String collectionPath, String name) {
		super(collectionPath);
		this.name = name;
	}
	
	@Override
	public String getUri() {
		return super.getUri() + this.name + "/";
	}
	
	@Override
	public String getFileLocation() {
		return super.getFileLocation() + this.name + "/";
	}
	
	//TODO: wrapper for get named model
	
	public Model getModel() {
		Dataset dataset = TDB2Factory.connectDataset(this.getFileLocation());
		Model out = ModelFactory.createDefaultModel();
		try {
			dataset.begin(ReadWrite.READ);
			out.add(dataset.getDefaultModel());
		}
		finally {
			dataset.end();
		}
		
		return out;
	}
	
	public void writeModel(Model model) {
		Dataset dataset = TDB2Factory.connectDataset(this.getFileLocation());
		try {
			dataset.begin(ReadWrite.WRITE);
			Model out = dataset.getDefaultModel();
			out.add(model);
			out.commit();
		}
		finally {
			dataset.end();
		}
	}
}
