package com.mackervoy.calum.mud.content;

import java.util.Optional;

import javax.ws.rs.NotFoundException;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.query.*;

import com.mackervoy.calum.mud.DatasetItem;
import com.mackervoy.calum.mud.TDBStore;
import com.mackervoy.calum.mud.vocabularies.MUDCharacter;
import com.mackervoy.calum.mud.vocabularies.MUDContent;

public class SPARQLDescriber extends AbstractDescriber {
	
	//TODO: this class will be a generic describer for completing a SPARQL lookup
	public SPARQLDescriber() {
		this.addTargetRDFType(MUDCharacter.Character.toString());
	}
	
	/**
	 * @return A DatasetItem where the graph of content in the TDBStore is stored, or None
	 */
	protected Optional<DatasetItem> getContentDatasetItem() {
		try {
			return Optional.of(TDBStore.getDatasetItem(TDBStore.CONTENT.getFileLocation()));
		}
		catch(NotFoundException e) {
			return Optional.empty();
		}
	}
	
	protected void getContentDescribingObject(DatasetItem contentDataset, Resource r) {
		// Select all triples where the subject describes a character
		// TODO: prioritise those triples which target *this character*
		String queryString = "SELECT ?s WHERE { ?s <" + MUDContent.describes.toString() + "> <" + MUDCharacter.Character.toString() + "> }";
		Query query = QueryFactory.create(queryString);
	  
		try (QueryExecution qexec = QueryExecutionFactory.create(query, contentDataset.getModel())) {
		    ResultSet results = qexec.execSelect();
		    ResultSetFormatter.out(System.out, results, query);
		}
	}
	
	public Optional<Model> describe(Resource agent, Resource r) {
		this.getContentDatasetItem().ifPresent(datasetItem -> this.getContentDescribingObject(datasetItem, r));
		
		return Optional.empty();
	}
}
