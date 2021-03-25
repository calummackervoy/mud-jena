package com.mackervoy.calum.mud.behaviour.task;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Optional;

import javax.ws.rs.BadRequestException;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;

import com.mackervoy.calum.mud.vocabularies.MUDLogic;

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
	public static boolean register(String targetTaskClass, Class<? extends ITaskActor> actor) {
		if(taskActors.put(targetTaskClass, actor) == null) {
			return true;
		}
		return false;
	}
	
	private Constructor<? extends ITaskActor> resolveActorClassConstructor(String targetTaskClass, Class<?>... parameterTypes) throws NoSuchMethodException, SecurityException {
		Class<? extends ITaskActor> actor = taskActors.get(targetTaskClass);
		if(actor == null) return null;
		return actor.getConstructor(parameterTypes);
	}
	
	public Optional<ITaskActor> getActorWithNewTask(String targetTaskClass) {
		try {
			return Optional.of(this.resolveActorClassConstructor(targetTaskClass).newInstance());
		}
		catch (Exception e) {
			System.out.println("Error in TaskActorFactory.getActorWithNewTask!");
			e.printStackTrace();
			return Optional.empty();
		}
	}
	
	public Optional<ITaskActor> getActorWithExistingTask(Resource task) {
		String taskUri = task.getURI();
	    Resource targetTaskClass = task.getPropertyResourceValue(MUDLogic.taskImplements);
	    
	    if(targetTaskClass == null) throw new BadRequestException("The existing Task was found but it must have the property mudLogic:taskImplements");
	    
	    try {
			return Optional.of(this.resolveActorClassConstructor(targetTaskClass.toString(), String.class).newInstance(taskUri));
		}
		catch (Exception e) {
			System.out.println("Error in TaskActorFactory.getActorWithExistingTask!");
			e.printStackTrace();
			return Optional.empty();
		}
	}
}
