/**
 * 
 */
package com.mackervoy.calum.mud;

import java.io.File;

/**
 * @author Calum Mackervoy
 * A central class which manages access to directories and sub-directories of the dataset store
 */
public class TDBStore {
	public final static DatasetCollection WORLD = new DatasetCollection("world/");
	public final static DatasetCollection ACTIONS = new DatasetCollection("actions/");
	public final static DatasetCollection TASK_ACTIONS = new DatasetCollection("actions/tasks/");
}
