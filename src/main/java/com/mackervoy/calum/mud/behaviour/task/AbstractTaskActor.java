package com.mackervoy.calum.mud.behaviour.task;

import javax.ws.rs.BadRequestException;

import java.util.List;

import com.mackervoy.calum.mud.DatasetItem;
import com.mackervoy.calum.mud.TDBStore;
import com.mackervoy.calum.mud.behaviour.Task;
import com.mackervoy.calum.mud.vocabularies.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.reasoner.rulesys.GenericRuleReasoner;
import org.apache.jena.reasoner.rulesys.Rule;
import org.apache.jena.vocabulary.RDF;

import java.net.MalformedURLException;
import java.time.Instant;
import java.util.Optional;

/**
 * @author Calum Mackervoy
 * The superclass from which all Task Actors should inherit from
 * A Task Actor is an actor which is responsible for creating and completing a Task
 */
public abstract class AbstractTaskActor implements ITaskActor {
	
	//TODO: useful to store this information ?
	//private final static String[] targetRdfTypes = {};
	
	protected Model model;
	protected Resource task;
	protected DatasetItem taskDatasetItem; // the graph containing the task
	//protected String insertsDatasetLocation;
	//protected String deletesDatasetLocation;
	
	/**
	 * Register with the TaskActorFactory this class with its target RDF types
	 * NOTE: should be implemented also in child classes. It's not possible in Java to make a method abstract and static,
	 * since static methods cannot be overridden
	 */
	public static void registerTargetRDFTypes() { }
	
	protected static void registerTargetRDFType(String targetRDFType, Class<? extends AbstractTaskActor> taskActor) {
		TaskActorFactory.register(targetRDFType, taskActor);
	}
	
	/**
	 * version of the constructor which creates a new dataset
	 */
	public AbstractTaskActor(Resource taskImplements) {
		this.taskDatasetItem = Task.getNewTaskDataset();
		this.model = this.taskDatasetItem.getModel();
		
		//create and initialise a new task dataset
		String taskUri = this.taskDatasetItem.getNewResourceUri("task");
		this.task = ResourceFactory.createResource(taskUri);
		this.model.add(Task.getTaskProperties(this.taskDatasetItem, this.task, taskImplements));
	}
	
	/**
	 * version of the constructor which links to an existing (parameterised task)
	 */
	public AbstractTaskActor(String taskUri) {
		this.taskDatasetItem = TDBStore.getDatasetItem(taskUri);
		this.model = this.taskDatasetItem.getModel();
		
		this.task = this.model.getResource(taskUri);
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
	
	/**
	 * @return the time.Instant when this task will end, or null if it doesn't have one set 
	 * Default uses the Time ontology, so it's useful to override this if you want to use something else
	 */
	public Optional<Instant> getTaskEndTime() {
		Resource end = this.task.getPropertyResourceValue(Time.hasEnd);
		if(end == null) return Optional.empty();
		
		return Optional.of(Time.resourceToInstant(end));
	}
	
	/**
	 * @return true if the task is complete, false if not
	 */
	public boolean isComplete() {
		//if the task has a complete time and that's passed, it's complete
		//it's also complete if it doesn't have a complete time
		return this.getTaskEndTime().map(end -> Instant.now().isAfter(end)).orElse(true);
	}
	
	protected void effectCompletedTaskEndState() {
		// iterate over each patch
		ResIterator patches = this.model.listResourcesWithProperty(RDF.type, SolidTerms.Patch);
		
		while(patches.hasNext()) {
			Resource patch = patches.next();
			Resource subject = patch.getPropertyResourceValue(SolidTerms.patches);
			
			try {
				// will only update local resources
				if(subject == null || !TDBStore.isLocalURI(subject.getURI())) continue;
				
				// get the subject model
				DatasetItem subjectDatasetItem = TDBStore.getDatasetItem(subject.getURI());
				Model subjectModel = subjectDatasetItem.getModel();
				
				// write each insert triple to the subject model
				StmtIterator inserts = patch.listProperties(MUDLogic.inserts);
				
				while(inserts.hasNext()) {
					Statement statement = inserts.next();
					subjectModel.add(subject, statement.getPredicate(), statement.getObject());
				}
				
				subjectDatasetItem.writeModel(subjectModel);
			}
			catch(MalformedURLException e) {
				continue;
			}
		}
	}
	
	/**
	 * marks the task as completed in the database and effectuates the task endState
	 * @return the completed Task graph
	 */
	public Model complete() {
		if(this.isComplete()) {
			// mark as complete
			this.model.add(this.task, MUDLogic.isComplete, "true");
			
			this.effectCompletedTaskEndState();
			this.save();
		}
		return this.model;
	}
	
	protected void save() {
		this.taskDatasetItem.writeModel(this.model);
	}
}
