package com.mackervoy.calum.mud.content;

import org.junit.Test;
import org.junit.BeforeClass;
import org.junit.Assert;

import com.mackervoy.calum.mud.MUDApplication;

public class TestDescriberFactory {
	DescriberFactory factory = new DescriberFactory();
	
	@BeforeClass
    public static void beforeAllTestMethods() {
        MUDApplication.registerDescribers();
    }
	
	@Test
	public void testGetRDFTypeOfStadiumDescriber() {
		Class result = factory.getDescriber("helloworld");
		Assert.assertNotNull(result);
	}
	
	@Test
	public void testGETRDFTypeDoesntExist() {
		Class result = factory.getDescriber("idonotexist");
		Assert.assertNull(result);
	}
}
