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
	
	public AbstractFileStorageWrapper(String path) {
		this.path = path;
	}
	
	public String getUri() {
		return MUDApplication.getSiteUrl() + this.path;
	}
	
	public String getFileLocation() {
		return MUDApplication.getRootDirectory() + this.path;
	}
	
	/**
	 * @param shortHand a short string to help human readers understand the type of the resource
	 * @param name identifier for the resource
	 * @return a full URI with the dataset path + the shorthand type + a random ID
	 */
	public String getNewResourceUri(String shortHand, String name) {
		return this.getUri() + "#" + shortHand + "-" + name;
	}
	
	public String getNewResourceUri(String shortHand) {
		return this.getNewResourceUri(shortHand, Random.getRandomUUIDString());
	}
}
