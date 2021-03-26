package com.mackervoy.calum.mud;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

import junit.framework.Assert;

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
		assertEquals(expected.split("/")[index].length(), index > 0 ? (root.length() - 3) : root.length());
		
		// with just subPath
		assertEquals(new DatasetItem(subPath, name).getFileLocation(), expected);
		
		// with subPath/
		assertEquals(new DatasetItem(subPath + "/", name).getFileLocation(), expected);
		
		// with root/subPath/
		assertEquals(new DatasetItem(root + subPath, name).getFileLocation(), expected);
		
		// with ./root/subPath
		if(!root.startsWith("./")) assertEquals(new DatasetItem("./" + root + subPath, name).getFileLocation(), expected);
		else assertEquals(new DatasetItem(root.substring(2) + subPath, name).getFileLocation(), expected);
	}
}
