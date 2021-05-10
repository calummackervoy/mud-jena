package com.mackervoy.calum.mud;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class TestAbstractFileStorageWrapper {
	@Test
	public void testFileStorageWrapperCreationFilePath() {
		//testing instantiating different paths with the file storage wrapper normalised correctly
		//they should all become {root}/{subPath}/
		
		String root = MUDApplication.getRootDirectory();
		String subPath = "subPath";
		String name = "name";
		String expected = root + subPath + "/" + name + "/";
		
		// test assumptions
		int index = root.startsWith("./") ? 1 : 0;
		assertEquals(index > 0 ? (root.length() - 3) : root.length(), expected.split("/")[index].length());
		
		// with just subPath
		assertEquals(expected, new DatasetItem(subPath, name).getFileLocation());
		
		// with subPath/
		assertEquals(expected, new DatasetItem(subPath + "/", name).getFileLocation());
		
		// with root/subPath/
		assertEquals(expected, new DatasetItem(root + subPath, name).getFileLocation());
		
		// with ./root/subPath
		if(!root.startsWith("./")) assertEquals(expected, new DatasetItem("./" + root + subPath, name).getFileLocation());
		else assertEquals(expected, new DatasetItem(root.substring(2) + subPath, name).getFileLocation());
	}
}
