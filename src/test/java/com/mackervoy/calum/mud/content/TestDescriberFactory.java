package com.mackervoy.calum.mud.content;

import org.junit.Test;
import org.junit.BeforeClass;
import org.junit.Assert;

import com.mackervoy.calum.mud.MUDApplication;
import com.mackervoy.calum.mud.vocabularies.MUDBuildings;

public class TestDescriberFactory {
	DescriberFactory factory = new DescriberFactory();
	
	@BeforeClass
    public static void beforeAllTestMethods() {
        MUDApplication.registerDescribers();
    }
	
	@Test
	public void testGetRDFTypeOfStadiumDescriber() {
		IContentDescriber result = factory.getDescriber(MUDBuildings.Stadium.toString());
		Assert.assertNotNull(result);
	}
	
	@Test
	public void testGETRDFTypeDoesntExist() {
		IContentDescriber result = factory.getDescriber("idonotexist");
		Assert.assertNull(result);
	}
}
