package com.mackervoy.calum.mud;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/settlements")
public class SettlementController {
    @GET
    //@Produces("text/turtle")
    @Produces(MediaType.TEXT_HTML)
    public String sayHtmlHello() {
        return "Hello World!";
    }
}
