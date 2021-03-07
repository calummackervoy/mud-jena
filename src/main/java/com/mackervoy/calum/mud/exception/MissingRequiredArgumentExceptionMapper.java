package com.mackervoy.calum.mud.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class MissingRequiredArgumentExceptionMapper implements ExceptionMapper<MissingRequiredArgumentException> {
	
	@Override
    public Response toResponse(MissingRequiredArgumentException e) {
           
        return Response
                .status(Response.Status.BAD_REQUEST.getStatusCode())
                .type(MediaType.TEXT_PLAIN)
                .entity(e.getMessage())
                .build();
    }
}
