/**
 * 
 */
package com.mackervoy.calum.mud.behaviour;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.vocabulary.RDF;

import com.mackervoy.calum.mud.DatasetItem;
import com.mackervoy.calum.mud.TDBStore;
import com.mackervoy.calum.mud.vocabularies.MUDLogic;
import com.mackervoy.calum.mud.vocabularies.Time;

/**
 * @author Calum Mackervoy
 * A wrapper class which manages the building of a MUDLogic:Task (with Patch to model endState) - a kind of action which takes time to complete
 * involving waiting and modelled with a sub-type of time:Interval in the ontology)
 */
public class Task {
	
	public static DatasetItem getNewTaskDataset() {
		return TDBStore.TASK_ACTIONS.getNewDataset();
	}
	
	/**
	 * Hook for adding properties to the Task model. Default implementation adds properties for time:Interval
	 * @param the model which represents the task, already with RDF type and parameterised task Resource
	 * Note that consequences are appended to the Task later
	 */
	public static Model getTaskProperties(DatasetItem taskDataset, Resource task, Resource taskImplements) {
		Model model = ModelFactory.createDefaultModel();
		
		model.add(task, RDF.type, MUDLogic.Task);
		model.add(task, MUDLogic.taskImplements, taskImplements);
		
		//create the task beginning and end instants
		Resource taskBegins = ResourceFactory.createResource(taskDataset.getNewResourceUri("instant"));
		model.add(taskBegins, RDF.type, Time.Instant);
		DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
		model.add(taskBegins, Time.inXSDDateTimeStamp, LocalDateTime.now().format(formatter));
		
		Resource taskEnds = ResourceFactory.createResource(taskDataset.getNewResourceUri("instant"));
		model.add(taskEnds, RDF.type, Time.Instant);
		//TODO: parameterise time to complete
		model.add(taskEnds, Time.inXSDDateTimeStamp, LocalDateTime.now().plusMinutes(1).format(formatter));

		//add them to the transit resource
		model.add(task, Time.hasBeginning, taskBegins);
		model.add(task, Time.hasEnd, taskEnds);
		
		return model;
	}
}
