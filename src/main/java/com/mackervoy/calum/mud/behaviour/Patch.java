/**
 * 
 */
package com.mackervoy.calum.mud.behaviour;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.vocabulary.RDF;

import com.mackervoy.calum.mud.vocabularies.SolidTerms;

/**
 * @author Calum Mackervoy
 * A wrapper class which manages the building of a solid:Patch (with inserts and deletes)
 * in MUD typically models the changes to be made to a Resource during a consequence or side-effect
 */
public class Patch {
	//TODO: use Dataset to model inserts/deletes
	//private Dataset inserts;
	//private Dataset deletes;
	
	public static Model getNewPatch(String patchUri, Resource subject) {
		Model model = ModelFactory.createDefaultModel();

		Resource patch = ResourceFactory.createResource(patchUri);
		model.add(patch, RDF.type, SolidTerms.Patch);
		model.add(patch, SolidTerms.patches, subject);
		
		return model;
	}
	
	/*public static Model getInsert(Resource patch, Resource insert) {
		
		//SolidTerms.inserts points to our Graph as a Node
		//this makes the output in N3 format (allows us reference a Graph in a triple)
		//Triple t = Triple.create(endState.asNode(), SolidTerms.inserts.asNode(), 
		//		NodeFactory.createGraphNode(inserts.getGraph()));
		//out.add(out.asResource(t));
		
		//out.add(endState, SolidTerms.inserts, 
		//		out.asRDFNode(NodeFactory.createGraphNode(inserts.getGraph())));
	}*/
}
