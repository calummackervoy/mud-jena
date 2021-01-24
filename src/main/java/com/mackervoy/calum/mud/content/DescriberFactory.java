/**
 * 
 */
package com.mackervoy.calum.mud.content;

import java.util.Dictionary;
import java.util.Hashtable;

/**
 * @author Calum Mackervoy
 * Resolves at runtime a Describer class for a requested RDF class
 */
public class DescriberFactory {
	private static Dictionary<String, Class<? extends IContentDescriber>> contentDescribers = 
			new Hashtable<String, Class<? extends IContentDescriber>>();
	
	/*
	 * Registers an IContentDescriber to an RDF type with String
	 * @returns true if the object could be registered, false if another class has already registered
	 */
	public static boolean register(String rdfType, Class<? extends IContentDescriber> describer) {
		if(contentDescribers.put(rdfType, describer) == null) {
			return true;
		}
		return false;
	}
	
	public Class<? extends IContentDescriber> getDescriber(String rdfType) {
		return contentDescribers.get(rdfType);
	}
}
