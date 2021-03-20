package com.mackervoy.calum.mud.behaviour.task;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Optional;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;

/**
 * @author Calum Mackervoy
 * Resolves at runtime a TaskActor class for a requested RDF class
 */
public class TaskActorFactory {
	private static Dictionary<String, Class<? extends ITaskActor>> taskActors = 
			new Hashtable<String, Class<? extends ITaskActor>>();
	
	/*
	 * Registers a provider to an RDF type with String
	 * @returns true if the object could be registered, false if another class has already registered
	 */
	public static boolean register(String rdfType, Class<? extends ITaskActor> actor) {
		if(taskActors.put(rdfType, actor) == null) {
			return true;
		}
		return false;
	}
	
	private Constructor<? extends ITaskActor> resolveActorClassConstructor(String rdfType, Class<?>... parameterTypes) throws NoSuchMethodException, SecurityException {
		Class<? extends ITaskActor> actor = taskActors.get(rdfType);
		if(actor == null) return null;
		return actor.getConstructor(parameterTypes);
	}
	
	public Optional<ITaskActor> getActorWithNewTask(String rdfType) {
		try {
			return Optional.of(this.resolveActorClassConstructor(rdfType).newInstance());
		}
		catch (Exception e) {
			System.out.println("Error in TaskActorFactory.getActorWithNewTask!");
			e.printStackTrace();
			return Optional.empty();
		}
	}
	
	public Optional<ITaskActor> getActorWithExistingTask(Resource task) {
		String taskUri = task.getURI();
	    String rdfType = task.getPropertyResourceValue(RDF.type).toString();
	    
	    try {
			return Optional.of(this.resolveActorClassConstructor(rdfType, String.class).newInstance(taskUri));
		}
		catch (Exception e) {
			System.out.println("Error in TaskActorFactory.getActorWithExistingTask!");
			e.printStackTrace();
			return Optional.empty();
		}
	}
}
