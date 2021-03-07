package com.mackervoy.calum.mud.behaviour.task;

import java.io.ByteArrayOutputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
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
		//taken from https://jena.apache.org/documentation/inference/#RDFSPlusRules
		//TODO: read from file: https://github.com/Multi-User-Domain/mud-jena/issues/9
		String basicRDFRules = "[rdfs2:  (?x ?p ?y), (?p rdfs:domain ?c) -> (?x rdf:type ?c)]\n" + 
				"[rdfs3:  (?x ?p ?y), (?p rdfs:range ?c) -> (?y rdf:type ?c)]\n" + 
				"[rdfs6:  (?a ?p ?b), (?p rdfs:subPropertyOf ?q) -> (?a ?q ?b)]\n" + 
				"[rdfs9:  (?x rdfs:subClassOf ?y), (?a rdf:type ?x) -> (?a rdf:type ?y)]";
		//List<Rule> rules = Rule.parseRules(basicRDFRules);
		List<Rule> rules = Rule.rulesFromURL("https://calum.inrupt.net/public/rules/rdfsbasicrules.txt");
		
		/*OntModel buildings = ModelFactory.createOntologyModel(OntModelSpec.);
		buildings.read("https://raw.githubusercontent.com/Multi-User-Domain/vocab/main/mudbuildings.ttl");
		buildings.read("https://raw.githubusercontent.com/Multi-User-Domain/vocab/main/mud.ttl");*/
		//OntModel mud = ModelFactory.createOntologyModel("https://raw.githubusercontent.com/Multi-User-Domain/vocab/main/mud.ttl");
		//buildings.add(mud);
		//schema.read("https://raw.githubusercontent.com/Multi-User-Domain/vocab/main/mudbuildings.ttl");
		
		GenericRuleReasoner reasoner = new GenericRuleReasoner(rules);
		reasoner.setOWLTranslation(true);               // not needed in RDFS case
		reasoner.setTransitiveClosureCaching(true);
		//InfModel inf = ModelFactory.createInfModel(reasoner, buildings, request);
		InfModel inf = ModelFactory.createInfModel(reasoner, request);
		
		ResIterator matches = inf.listResourcesWithProperty(RDF.type, type);
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
