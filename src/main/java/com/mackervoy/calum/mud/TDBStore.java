/**
 * 
 */
package com.mackervoy.calum.mud;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Optional;

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
	 * takes an unsafe URI String and returns the path part of the URI if valid, or null if not
	 */
	private final static Optional<String> getFilePathFromUri(String uriInput) {
		try {
			URL uri = new URL(uriInput);
			
			if(!uri.getHost().equals(new URL(MUDApplication.getSiteUrl()).getHost())) {
				throw new NotFoundException("The given dataset is not hosted on this site!");
			}
			
			return Optional.of(uri.getPath().substring(1));
		}
		catch(MalformedURLException e) {
			return Optional.empty();
		}
	}
	
	private final static String getFullFilePath(String filePath) {
		String root = MUDApplication.getRootDirectory();
		
		//normalise style
		if(filePath.startsWith("./")) filePath = filePath.substring(2);
		if(root.startsWith("./")) root = root.substring(2);
		
		if(filePath.startsWith(root)) return filePath;
		return root + filePath;
	}
	
	/**
	 * @param uriOrFilePath a URI or filesystem path for the dataset item
	 * @return the DatasetItem found at this resource
	 * @throws NotFoundException if it can't be found
	 * example URI: http://localhost:8080/mud/world/#something
	 * example file paths: ./mud/world/, /mud/world/ or world/
	 */
	public final static DatasetItem getDatasetItem(String uriOrFilePath) {
		//convert a URI to a file system path
		String filePath = getFilePathFromUri(uriOrFilePath).orElse(uriOrFilePath);
		
		if(!TDBStore.inUseLocation(new File(getFullFilePath(filePath)))) {
			throw new NotFoundException("The given dataset is not in use!");
		}
		
		String[] splitString = filePath.split("/");
		String name = splitString[splitString.length - 1];
		
		splitString = Arrays.copyOfRange(splitString, 0, splitString.length - 1);
		String collection = String.join("/", splitString);
		
		return new DatasetItem(collection, name);
	}
}
