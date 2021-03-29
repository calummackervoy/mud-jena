package com.mackervoy.calum.mud.content;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.vocabulary.RDF;

import com.mackervoy.calum.mud.vocabularies.MUDContent;

/**
 * @author Calum Mackervoy
 * A wrapper class which manages the building of a mudcontent:Content
 */
public class Content {
	
	//TODO: when we support more kinds of content other than text use the builder pattern
	public static Model getContentFromText(Resource agent, Resource targetObject, Property senseProperty, String textContent) {
		Model model = ModelFactory.createDefaultModel();
		Resource content = ResourceFactory.createResource();
		
		model.add(content, RDF.type, MUDContent.Content);
		model.add(content, MUDContent.hasText, textContent);
		model.add(content, MUDContent.describes, targetObject);
		model.add(agent, senseProperty, content);
		
		return model;
	}
}
