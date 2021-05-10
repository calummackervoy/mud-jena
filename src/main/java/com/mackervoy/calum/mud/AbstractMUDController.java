package com.mackervoy.calum.mud;

import java.io.ByteArrayOutputStream;

import org.apache.jena.rdf.model.Model;

public abstract class AbstractMUDController {
	
	public String serializeModelToTurtle(Model m) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
        m.write(baos, "Turtle");
        
        return baos.toString();
	}
}
