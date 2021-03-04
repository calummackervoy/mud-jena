/**
 * 
 */
package com.mackervoy.calum.mud;

import java.io.File;

/**
 * @author Calum Mackervoy
 * A collection (directory) of datasets. Manages the URI and file-store bindings to each particular
 */
public class DatasetCollection extends AbstractFileStorageWrapper {
	
	/**
	 * @param subPath: the directory path of the collection *relative* the the root directory, starting and ending with a "/"
	 * e.g. /world/mydatacollection/
	 */
	public DatasetCollection(String subPath) {
		super(subPath);
		
		// initialise directories storing the collection
		File dir = new File(this.getFileLocation());
		
		// if the directory path doesn't exist, create it
		if (! dir.exists()) {
			dir.mkdirs();
		}
	}
	
	/**
	 * @return a file location for a new dataset in this collection
	 */
	public DatasetItem getNewDataset() {
		return new DatasetItem(this.path);
	}
}
