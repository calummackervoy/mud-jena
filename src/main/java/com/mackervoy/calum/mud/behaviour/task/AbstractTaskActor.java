package com.mackervoy.calum.mud.behaviour.task;

import java.util.HashSet;
import java.util.Set;

import org.apache.jena.query.Dataset;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.ResIterator;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.tdb2.TDB2Factory;
import org.apache.jena.vocabulary.RDF;

import com.mackervoy.calum.mud.DatasetItem;
import com.mackervoy.calum.mud.exception.MissingRequiredArgumentException;
import com.mackervoy.calum.mud.TDBStore;
import com.mackervoy.calum.mud.behaviour.Task;

/**
 * @author Calum Mackervoy
 * The superclass from which all Task Actors should inherit from
 * A Task Actor is an actor which is responsible for creating and completing a Task
 */
public abstract class AbstractTaskActor implements ITaskActor {
	// the RDF types which are provided by the child class
	private Set<String> targetRDFTypes;
	
	protected Model model;
	protected Task task;
	protected DatasetItem taskDatasetItem;
	protected String insertsDatasetLocation;
	protected String deletesDatasetLocation;
	
	public Set<String> getTargetRDFTypes() {
		return this.targetRDFTypes;
	}
	
	protected boolean addTargetRDFType(String rdfType) {
		if(!this.targetRDFTypes.add(rdfType)) return false;
		
		//register with the Factory that the class provides this RDF type
		TaskActorFactory.register(rdfType, this.getClass());
		return true;
	}
	
	public AbstractTaskActor() {
		this.targetRDFTypes = new HashSet<String>();
		this.taskDatasetItem = TDBStore.TASK_ACTIONS.getNewDataset();
		this.model = ModelFactory.createDefaultModel();
		this.task = new Task(this.taskDatasetItem);
	}
	
	/**
	 * searches the parameterised request Model for any resource with the parameterised type
	 * @return first match
	 * @throws NullPointerException on case of no match
	 */
	protected Resource getFirstResourceMatchingType(Model request, Resource type) {
		ResIterator matches = request.listResourcesWithProperty(RDF.type, type);
		try {
			Resource match = matches.next();
			return match;
		}
		catch(java.util.NoSuchElementException e) {
			throw new MissingRequiredArgumentException("An object with type " + type + " must be passed with the request");
		}
	}
	
	protected void commitToDB() {
		this.model.add(this.task.getModel());
		
		Dataset dataset = TDB2Factory.connectDataset(this.taskDatasetItem.getFileLocation());
		try {
			dataset.begin(ReadWrite.WRITE);
			Model out = dataset.getDefaultModel();
			out.add(this.model);
			out.commit();
		}
		finally {
			dataset.end();
		}
	}
}
