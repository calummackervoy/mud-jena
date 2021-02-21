/**
 * 
 */
package com.mackervoy.calum.mud.content;

import java.util.HashSet;
import java.util.Set;

import org.apache.jena.rdf.model.*;

import com.mackervoy.calum.mud.vocabularies.MUD;

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

	protected Model sensesFromVisualDescription(String visualDescription) {
		Model m = ModelFactory.createDefaultModel();
    Resource senses = ResourceFactory.createResource("http://localhost:8080/mud/content/#senses"); // not sure what this ought to be
    m.add(senses, MUD.sight, visualDescription);
    return m;
	}
}
