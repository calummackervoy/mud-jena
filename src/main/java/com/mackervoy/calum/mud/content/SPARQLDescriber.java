package com.mackervoy.calum.mud.content;

import java.util.Optional;

import javax.ws.rs.NotFoundException;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.query.*;

import com.mackervoy.calum.mud.DatasetItem;
import com.mackervoy.calum.mud.TDBStore;
import com.mackervoy.calum.mud.vocabularies.MUDCharacter;

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
	
	public Optional<Model> describe(Resource agent, Resource r) {
		this.getContentDatasetItem().ifPresent(datasetItem -> System.out.println(datasetItem.getModel()));
		
		return Optional.empty();
	}
}
