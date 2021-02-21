/**
 * 
 */
package com.mackervoy.calum.mud;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

/**
 * @author Calum Mackervoy
 * A wrapper class for a ResourceItem with reference to a parent DatasetItem
 */
public abstract class ResourceItem {
	protected DatasetItem parent;
	protected Model model;
	
	public ResourceItem(DatasetItem parentDatasetItem) {
		this.parent = parentDatasetItem;
		this.model = ModelFactory.createDefaultModel();
	}
	
	/**
	 * @return Model copy of internal Model
	 */
	public Model getModel() {
		Model out = ModelFactory.createDefaultModel();
		out.add(this.model);
		return out;
	}
}
