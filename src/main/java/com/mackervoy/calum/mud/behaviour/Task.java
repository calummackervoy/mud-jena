/**
 * 
 */
package com.mackervoy.calum.mud.behaviour;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.vocabulary.RDF;

import com.mackervoy.calum.mud.DatasetItem;
import com.mackervoy.calum.mud.ResourceItem;
import com.mackervoy.calum.mud.vocabularies.MUDLogic;
import com.mackervoy.calum.mud.vocabularies.Time;

/**
 * @author Calum Mackervoy
 * A wrapper class which manages the building of a MUDLogic:Task (with Patch to model endState) - a kind of action which takes time to complete
 * involving waiting and modelled with a sub-type of time:Interval in the ontology)
 */
public class Task extends ResourceItem {
	
	protected Resource task;
	protected String taskUri;
	
	public Task(DatasetItem parentDatasetItem) {
		super(parentDatasetItem);
		
		this.taskUri = this.parent.getNewResourceUri("task");
		this.task = ResourceFactory.createResource(this.taskUri);
		this.model.add(this.task, RDF.type, MUDLogic.Task);
		this.addTaskProperties();
	}
	
	public Resource getResource() {
		return this.task;
	}
	
	/**
	 * Hook for adding properties to the Task model. Default implementation adds properties for time:Interval
	 * @param the model which represents the task, already with RDF type and parameterised task Resource
	 * Note that consequences are appended to the Task later
	 */
	protected void addTaskProperties() {
		//create the task beginning and end instants
		Resource taskBegins = ResourceFactory.createResource(this.parent.getNewResourceUri("instant"));
		this.model.add(taskBegins, RDF.type, Time.Instant);
		DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
		this.model.add(taskBegins, Time.inXSDDateTimeStamp, LocalDateTime.now().format(formatter));
		
		Resource taskEnds = ResourceFactory.createResource(this.parent.getNewResourceUri("instant"));
		this.model.add(taskEnds, RDF.type, Time.Instant);
		this.model.add(taskEnds, Time.inXSDDateTimeStamp, LocalDateTime.now().plusHours(2).format(formatter));

		//add them to the transit resource
		this.model.add(this.task, Time.hasBeginning, taskBegins);
		this.model.add(this.task, Time.hasEnd, taskEnds);
	}
	
	public void addPatch(Patch patch) {
		this.model.add(patch.getModel());
		this.model.add(this.task, MUDLogic.endState, patch.getPatch());
	}
}
