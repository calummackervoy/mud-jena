/**
 * 
 */
package com.mackervoy.calum.mud.behaviour;

import java.util.ArrayList;
import java.util.List;

import org.apache.jena.query.Dataset;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;

import com.mackervoy.calum.mud.DatasetItem;
import com.mackervoy.calum.mud.ResourceItem;
import com.mackervoy.calum.mud.vocabularies.MUD;
import com.mackervoy.calum.mud.vocabularies.MUDLogic;
import com.mackervoy.calum.mud.vocabularies.SolidTerms;

/**
 * @author Calum Mackervoy
 * A wrapper class which manages the building of a solid:Patch (with inserts and deletes)
 * in MUD typically models the changes to be made to a Resource during a consequence or side-effect
 */
public class Patch extends ResourceItem {
	private List<Resource> inserts;
	//TODO: use Dataset to model inserts/deletes
	//private Dataset inserts;
	//private Dataset deletes;
	private Resource subject;
	private Resource endState;
	
	protected void initPatchResource() {
		this.endState = ResourceFactory.createResource(this.parent.getNewResourceUri("endState"));
		this.model.add(this.endState, RDF.type, SolidTerms.Patch);
		this.model.add(this.endState, SolidTerms.patches, this.subject);
	}
	
	public Patch(DatasetItem parentDatasetItem, Resource subject) {
		super(parentDatasetItem);
		this.subject = subject;	
		this.inserts = new ArrayList<Resource>();
		this.initPatchResource();
	}
	
	public Resource getPatch() {
		return this.endState;
	}
	
	public void addInsert(Resource insert) {
		this.model.add(this.endState, MUDLogic.inserts, insert);
		this.inserts.add(insert);
		
		//SolidTerms.inserts points to our Graph as a Node
		//this makes the output in N3 format (allows us reference a Graph in a triple)
		/*Triple t = Triple.create(endState.asNode(), SolidTerms.inserts.asNode(), 
				NodeFactory.createGraphNode(inserts.getGraph()));*/
		//out.add(out.asResource(t));
		
		/*out.add(endState, SolidTerms.inserts, 
				out.asRDFNode(NodeFactory.createGraphNode(inserts.getGraph())));*/
	}
}
