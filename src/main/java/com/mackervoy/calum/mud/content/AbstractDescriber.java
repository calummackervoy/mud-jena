/**
 * 
 */
package com.mackervoy.calum.mud.content;

import java.util.HashSet;
import java.util.Set;

import org.apache.jena.rdf.model.*;

import com.mackervoy.calum.mud.vocabularies.MUDContent;

/**
 * @author Calum Mackervoy
 * The AbstractDescriber is the superclass which all Describers should inherit from
 */
public abstract class AbstractDescriber implements IContentDescriber {
	// the RDF types which are described by the child class
	private Set<String> targetRDFTypes;
	
	public Set<String> getTargetRDFTypes() {
		return this.targetRDFTypes;
	}
	
	protected boolean addTargetRDFType(String rdfType) {
		if(!this.targetRDFTypes.add(rdfType)) return false;
		
		//register with the Factory that the class provides this RDF type
		DescriberFactory.register(rdfType, this.getClass());
		return true;
	}
	
	public AbstractDescriber() {
		this.targetRDFTypes = new HashSet<String>();
	}
}
