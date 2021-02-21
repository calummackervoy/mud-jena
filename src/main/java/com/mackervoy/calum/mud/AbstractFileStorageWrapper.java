/**
 * 
 */
package com.mackervoy.calum.mud;

/**
 * @author Calum Mackervoy
 * An abstract class for any class which manages the returning of File system/URI locations for some path
 */
public abstract class AbstractFileStorageWrapper {
	protected String path;
	
	public String getUri() {
		return MUDApplication.getSiteUrl() + this.path;
	}
	
	public String getFileLocation() {
		return MUDApplication.getRootDirectory() + this.path;
	}
}
