package com.mackervoy.calum.mud.behaviour.task;

import java.lang.reflect.InvocationTargetException;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Optional;

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
	
	public Optional<ITaskActor> getActor(String rdfType) {
		Class<? extends ITaskActor> actor = taskActors.get(rdfType);
		if(actor == null) return Optional.empty();
		try {
			return Optional.of(actor.getDeclaredConstructor().newInstance());
		} catch (IllegalArgumentException | InvocationTargetException | NoSuchMethodException
				| SecurityException | IllegalAccessException | InstantiationException e) {
			System.out.println("Error in TaskActorFactory.getActor!");
			e.printStackTrace();
			return Optional.empty();
		}
	}
}
