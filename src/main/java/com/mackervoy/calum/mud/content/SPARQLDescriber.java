package com.mackervoy.calum.mud.content;

import java.util.Optional;

import javax.ws.rs.NotFoundException;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
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
	
	protected Optional<Model> getContentDescribingObject(DatasetItem contentDataset, Resource r) {
		// Select all triples where the subject describes a character
		// TODO: prioritise those triples which target *this character*
		String queryString = "SELECT ?s WHERE { ?s <" + MUDContent.describes.toString() + "> <" + MUDCharacter.Character.toString() + "> }";
		Query query = QueryFactory.create(queryString);
		
		Model result = ModelFactory.createDefaultModel();
		Model contentGraph = contentDataset.getModel();
	  
		try (QueryExecution qexec = QueryExecutionFactory.create(query, contentGraph)) {
		    ResultSet results = qexec.execSelect();
		    
		    while(results.hasNext()) {
		    	Resource subject = results.nextSolution().getResource("s");
		    	
		    	// read the full subject into the result graph
		    	// TODO: this could probably be more efficient
		    	result.add(ModelFactory.createDefaultModel().read(subject.getURI()));
		    }
		}
		
		return result.isEmpty() ? Optional.empty() : Optional.of(result);
	}
	
	public Optional<Model> describe(Resource agent, Resource r) {
		return this.getContentDatasetItem()
				.flatMap(datasetItem -> this.getContentDescribingObject(datasetItem, r));
	}
}
