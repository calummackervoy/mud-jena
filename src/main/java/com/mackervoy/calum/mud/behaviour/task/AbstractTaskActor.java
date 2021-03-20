package com.mackervoy.calum.mud.behaviour.task;

import java.io.ByteArrayOutputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.ws.rs.BadRequestException;

import java.util.List;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.ResIterator;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.reasoner.rulesys.GenericRuleReasoner;
import org.apache.jena.reasoner.rulesys.Rule;
import org.apache.jena.tdb2.TDB2Factory;
import org.apache.jena.vocabulary.RDF;

import com.mackervoy.calum.mud.DatasetItem;
import com.mackervoy.calum.mud.vocabularies.MUD;
import com.mackervoy.calum.mud.vocabularies.MUDBuildings;
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
	
	/**
	 * version of the constructor which creates a new dataset
	 */
	public AbstractTaskActor() {
		this.targetRDFTypes = new HashSet<String>();
		this.model = ModelFactory.createDefaultModel();
		
		this.taskDatasetItem = TDBStore.TASK_ACTIONS.getNewDataset();
		this.task = new Task(this.taskDatasetItem);
	}
	
	/**
	 * version of the constructor which links to an existing (parameterised task)
	 */
	public AbstractTaskActor(String taskUri) {
		this.targetRDFTypes = new HashSet<String>();
		this.model = ModelFactory.createDefaultModel();
		
		this.taskDatasetItem = TDBStore.getDatasetItem(taskUri);
		this.task = new Task(this.taskDatasetItem, taskUri);
	}
	
	/**
	 * searches the parameterised request Model for any resource with the parameterised type
	 * @return first match
	 * @throws NullPointerException on case of no match
	 */
	protected Resource getFirstResourceMatchingType(Model request, Resource type) {
		//taken from https://jena.apache.org/documentation/inference/#RDFSPlusRules
		//TODO: read from local file: https://github.com/Multi-User-Domain/mud-jena/issues/9
		List<Rule> rules = Rule.rulesFromURL("https://calum.inrupt.net/public/rules/rdfsbasicrules.txt");
		
		//TODO: https://github.com/Multi-User-Domain/mud-jena/issues/14
		Model buildings = ModelFactory.createDefaultModel();
		buildings.read(MUDBuildings.getURI(), null, "TTL");
		buildings.read(MUD.getURI(), null, "TTL");
		
		//we are performing transitive reasoning: if A is type B and B is subclass of C, then A is C as well
		GenericRuleReasoner reasoner = new GenericRuleReasoner(rules);
		reasoner.setOWLTranslation(true);               // not needed in RDFS case
		reasoner.setTransitiveClosureCaching(true);
		InfModel inf = ModelFactory.createInfModel(reasoner, buildings, request);
		
		ResIterator matches = inf.listResourcesWithProperty(RDF.type, type);
		try {
			Resource match = matches.next();
			return match;
		}
		//TODO: the error is never thrown because of the workaround to https://github.com/Multi-User-Domain/mud-jena/issues/14
		catch(java.util.NoSuchElementException e) {
			throw new BadRequestException("An object with type " + type + " must be passed with the request");
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
	
	public boolean complete() {
		//if the task has a complete time and that's passed, it's complete
		
		//it's also complete if it doesn't have a complete time
		
		return false;
	}
}
