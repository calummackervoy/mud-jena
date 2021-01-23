package com.mackervoy.calum.mud;

import static org.junit.Assert.assertEquals;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

import com.mackervoy.calum.mud.*;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.MediaType;

public class SettlementTest extends JerseyTest {
	@Override
    protected Application configure() {
		MUDApplication.initFiles();
        return new ResourceConfig(SettlementController.class);
    }
	
	@Test
	public void testGetSettlements() {
	    Response response = target("/settlements/").request()
	        .get();
	    
	    assertEquals("Http Response should be 200: ", Status.OK.getStatusCode(), response.getStatus());
	    assertEquals("Http Content-Type should be: ", "text/turtle", response.getHeaderString(HttpHeaders.CONTENT_TYPE));
	}
}
