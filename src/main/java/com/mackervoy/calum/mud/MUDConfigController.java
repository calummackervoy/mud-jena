package com.mackervoy.calum.mud;

import java.io.IOException;
import java.nio.file.Files;
import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

/**
 * Serves the static file webapp/WEB-INF/mudserver.ttl at the MUD Discovery endpoint. 
 * See specification: https://multi-user-domain.github.io/docs/02-server-discovery.html
 */
@Path("/.well-known/mud-configuration/")
public class MUDConfigController {
	
    @Context
    private ServletContext context;
	
	@GET
    @Produces("text/turtle")
	public Response get() {
		String configPath = context.getRealPath("/WEB-INF/mudserver.ttl");
		
		try {
			return Response.ok(Files.readString(java.nio.file.Path.of(configPath))).build();
		} catch (IOException e) {
			return Response.serverError().entity("Error reading the server's configuration").build();
		}
	}
}
