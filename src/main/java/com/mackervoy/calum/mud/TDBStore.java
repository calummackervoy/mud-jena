/**
 * 
 */
package com.mackervoy.calum.mud;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Optional;

import org.apache.commons.io.FilenameUtils;
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
			
			if(!TDBStore.isLocalURI(uriInput)) {
				throw new NotFoundException("The given dataset is not hosted on this site!");
			}
			
			return Optional.of(uri.getPath().substring(1));
		}
		catch(MalformedURLException e) {
			return Optional.empty();
		}
	}
	
	/**
	 * @return a version of the parameterised file path which is relative from the applications' configured root directory
	 *  and has no end separator
	 */
	private final static String getFullFilePath(String filePath) {
		
		// normalise style - remove prefixes
		String result = FilenameUtils.getPathNoEndSeparator(filePath) + "/" + FilenameUtils.getName(filePath);
		String root =  FilenameUtils.getPath(MUDApplication.getRootDirectory());
		
		if(result.startsWith(root)) return result;
		return root + result;
	}
	
	/**
	 * @param uriOrFilePath a URI or filesystem path for the dataset item
	 * @return the DatasetItem found at this resource
	 * @throws NotFoundException if it can't be found
	 * example URI: http://localhost:8080/mud/world/#something
	 * example file paths: ./mud/world/, /mud/world/ or world/
	 */
	public final static DatasetItem getDatasetItem(String uriOrFilePath) {
		
		// convert a URI to a file system path
		String filePath = getFilePathFromUri(uriOrFilePath).orElse(uriOrFilePath);
		
		if(!TDBStore.inUseLocation(new File(getFullFilePath(filePath)))) {
			throw new NotFoundException("The given dataset " + filePath + " is not in use!");
		}
		
		return new DatasetItem(FilenameUtils.getPath(filePath), FilenameUtils.getName(filePath));
	}
	
	/**
	 * @return true if the URI represents a local resource (if the host matches the site URL), false if not
	 * @throws MalformedURLException 
	 */
	public static boolean isLocalURI(URL uri) throws MalformedURLException {
		return uri.getHost().equals(new URL(MUDApplication.getSiteUrl()).getHost());
	}
	
	public static boolean isLocalURI(String uri) throws MalformedURLException {
		return TDBStore.isLocalURI(new URL(uri));
	}
}
