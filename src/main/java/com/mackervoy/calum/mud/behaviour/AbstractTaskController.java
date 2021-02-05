package com.mackervoy.calum.mud.behaviour;

import org.apache.jena.rdf.model.Model;

import com.mackervoy.calum.mud.MUDApplication;
import com.mackervoy.calum.mud.Random;

/**
 * @author Calum Mackervoy
 * Provides a superclass to Task Controllers
 * Implements a getIsComplete function which checks whether a time:Interval has been completed
 * Override this function to provide for additional requirements
 */
public abstract class AbstractTaskController implements ITaskActor {
	
	/**
	 * @param task: an RDF model representing an object of type mudlogic:Task
	 * @return true if task is complete. The model will have mudlogic:taskComplete written to value true as well
	 */
	public boolean getIsComplete(Model task) {
		return true;
	}
	
	/**
	 * @return the MUDApplication.LOCAL_TASK + random uuid
	 */
	protected String getRandomLocalUrl() {
		return MUDApplication.TASK_LOCAL + Random.getRandomUUIDString();
	}
	
	/**
	 * Gets the uri task if it exists or creates it if it is null
	 * @param uri: the URI of the existing task to fetch, or null if the aim
	 * @return a Model containing the Task or null if it could not be found
	 */
	/*protected Model getTask(String uri, String rdfType) {
		
	}
	
	protected abstract void createModel();*/
}
