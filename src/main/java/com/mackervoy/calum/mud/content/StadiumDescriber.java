package com.mackervoy.calum.mud.content;

import java.util.List;
import java.util.ArrayList;

import org.apache.jena.rdf.model.*;

import com.mackervoy.calum.mud.vocabularies.MUDBuildings;

public class StadiumDescriber extends AbstractDescriber {
	
	public StadiumDescriber() {
		this.addTargetRDFType(MUDBuildings.Stadium.toString());
	}

	@Override
	public String describe(Model obj) {
		return null;
	}

}
