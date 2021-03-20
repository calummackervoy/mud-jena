package com.mackervoy.calum.mud;

/**
 * @author Calum Mackervoy
 * String accessors for the URI, File system location and for generating new resource Ids of a Dataset stored in the MUD TDB
 */
public class DatasetItem extends AbstractFileStorageWrapper {
	private String name;
	
	public DatasetItem(String collectionPath) {
		super(collectionPath);
		this.name = Random.getRandomUUIDString();
	}
	
	public DatasetItem(String collectionPath, String name) {
		super(collectionPath);
		this.name = name;
	}
	
	@Override
	public String getUri() {
		return super.getUri() + this.name + "/";
	}
	
	@Override
	public String getFileLocation() {
		return super.getFileLocation() + this.name + "/";
	}
}
