package com.mackervoy.calum.mud;

import java.io.File;

/**
 * @author Calum Mackervoy
 * A central class which manages the names of directories and datasets
 */
public class DatasetFileStorageManager {
	//TODO: configurable by web.xml
	private final static String ROOT_DIRECTORY = "./mud";
	private final static String WEB_HOST = "http://localhost:8080/mud/";
	
	private final static String WORLD = "world";
	private final static String ACTIONS = "actions";
	private final static String TASK_ACTIONS = ACTIONS + "/tasks";
	
	public static void init() {
		// initialise all folders being used in the application
		//TODO: something more extensible than this.. an EnumMap ?
		String[] directories = new String[] { ROOT_DIRECTORY, getWorldFileLocation(), getActionsFileLocation(), getTasksFileLocation() };
		
		for(String directory : directories) {
			File dir = new File(directory);
			
			if (! dir.exists()) dir.mkdirs();
		}
	}
	
	public final static String getUrl(String path) {
		return WEB_HOST + path + "/";
	}
	
	public final static String getUrlId(String path, String id) {
		return getUrl(path) + "#" + id;
	}
	
	public final static String getFileLocation(String path) {
		return ROOT_DIRECTORY + path;
	}
	
	public static String getWorldUrl() { return getUrl(WORLD); }
	public static String getActionsUrl() { return getUrl(ACTIONS); }
	public static String getTasksUrl() { return getUrl(TASK_ACTIONS); }
	
	public static String getWorldUrlId(String id) { return getUrlId(WORLD, id); }
	public static String getActionsUrlId(String id) { return getUrlId(ACTIONS, id); }
	public static String getTasksUrlId(String id) { return getUrlId(TASK_ACTIONS, id); }
	
	public static String getWorldFileLocation() { return getFileLocation(WORLD); }
	public static String getActionsFileLocation() { return getFileLocation(ACTIONS); }
	public static String getTasksFileLocation() { return getFileLocation(TASK_ACTIONS); }
}
