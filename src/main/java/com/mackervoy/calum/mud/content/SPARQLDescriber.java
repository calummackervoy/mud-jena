package com.mackervoy.calum.mud.content;

import java.util.Optional;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;

import com.mackervoy.calum.mud.vocabularies.MUDCharacter;

public class SPARQLDescriber extends AbstractDescriber {
	
	//TODO: this class will be a generic describer for completing a SPARQL lookup
	public SPARQLDescriber() {
		this.addTargetRDFType(MUDCharacter.Character.toString());
	}
	
	public Optional<Model> describe(Resource agent, Resource r) {
		return Optional.empty();
	}
}
