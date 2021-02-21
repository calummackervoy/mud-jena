/**
 * 
 */
package com.mackervoy.calum.mud.behaviour.task;

import javax.ws.rs.core.Response;

import org.apache.jena.rdf.model.Model;

/**
 * @author Calum Mackervoy
 * ITaskActor describes an interface for classes which implement the necessary functions to provide
 * a Task, which is an action carried out over a time:Interval, upon the completion of which a set
 * of consequences are carried out to datasets
 */
public interface ITaskActor {
	public Response act(Model request);
	public boolean complete(String uri);
}
