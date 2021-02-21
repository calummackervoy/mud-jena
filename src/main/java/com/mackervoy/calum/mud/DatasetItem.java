/**
 * 
 */
package com.mackervoy.calum.mud;

/**
 * @author Calum Mackervoy
 * String accessors for the URI, File system location and for generating new resource Ids of a Dataset stored in the MUD TDB
 */
public class DatasetItem extends AbstractFileStorageWrapper {
	private String name;
	
	public DatasetItem(String collectionPath) {
		this.path = collectionPath;
		this.name = Random.getRandomUUIDString();
	}
	
	@Override
	public String getUri() {
		return super.getUri() + this.name + "/";
	}
	
	@Override
	public String getFileLocation() {
		return super.getFileLocation() + this.name + "/";
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
