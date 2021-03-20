/**
 * 
 */
package com.mackervoy.calum.mud;

import java.io.File;
import java.util.Arrays;

import javax.ws.rs.NotFoundException;

/**
 * @author Calum Mackervoy
 * A central class which manages access to directories and sub-directories of the dataset store
 */
public class TDBStore {
	public final static DatasetCollection WORLD = new DatasetCollection("world/");
	public final static DatasetCollection ACTIONS = new DatasetCollection("actions/");
	public final static DatasetCollection TASK_ACTIONS = new DatasetCollection("actions/tasks/");
	
	public final static boolean inUseLocation(File f) {
		return f.exists();
		//return f.exists() && !f.isDirectory();
	}
	
	public final static boolean inUseLocation(String fileLocation) {
		File f = new File(fileLocation);
		return TDBStore.inUseLocation(f);
	}
	
	/**
	 * @param uriOrFilePath a URI or filesystem path for the dataset item
	 * @return the DatasetItem found at this resource
	 * @throws NotFoundException if it can't be found
	 */
	public final static DatasetItem getDatasetItem(String uriOrFilePath) {
		//TODO: convert a URI to a file system path
		
		if(!TDBStore.inUseLocation(new File(uriOrFilePath))) {
			throw new NotFoundException();
		}
		
		String[] splitString = uriOrFilePath.split("/");
		String name = splitString[splitString.length];
		String collection = String.join("/", Arrays.copyOfRange(splitString, 0, splitString.length));
		
		System.out.println("built collection String " + collection);
		System.out.println("built dataset name " + name);
		
		return new DatasetItem(collection, name);
	}
}
