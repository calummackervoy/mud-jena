package com.mackervoy.calum.mud;

import java.io.ByteArrayOutputStream;
import java.io.StringReader;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

public abstract class AbstractMUDController {
	
	public Model serializeTurtleRequestToModel(String requestBody) {
		return ModelFactory.createDefaultModel().read(new StringReader(requestBody), "", "TURTLE");
	}
	
	public String serializeModelToTurtle(Model m) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
        m.write(baos, "Turtle");
        
        return baos.toString();
	}
}
