package com.mackervoy.calum.mud.content;

import java.util.List;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.apache.jena.rdf.model.*;

import com.mackervoy.calum.mud.vocabularies.MUDBuildings;
import com.mackervoy.calum.mud.vocabularies.MUDEvents;
import com.mackervoy.calum.mud.vocabularies.Time;

public class StadiumDescriber extends AbstractDescriber {
	
	public StadiumDescriber() {
		this.addTargetRDFType(MUDBuildings.Stadium.toString());
	}

	@Override
	public String describe(Model dataModel, String uri) {
		//is there a match on?
		Resource stadium = dataModel.getResource(uri);
		StmtIterator iter = stadium.listProperties(MUDEvents.hasEvent);
		
		while(iter.hasNext()) {
			Resource event = iter.nextStatement().getResource();
			
			Resource beginning = event.getPropertyResourceValue(Time.hasBeginning);
			Resource end = event.getPropertyResourceValue(Time.hasEnd);
			if(beginning == null || end == null) continue;
			
			try {
				LocalDateTime beginTime = LocalDateTime.parse(
						beginning.getProperty(Time.inXSDDateTimeStamp).getString());
				if(beginTime != null) {
					LocalDateTime endTime = LocalDateTime.parse(
							end.getProperty(Time.inXSDDateTimeStamp).getString());
					
					if(LocalDateTime.now().isAfter(beginTime) && LocalDateTime.now().isBefore(endTime)) {
						return "Thousands of people are in and outside the stadium. There is a lot of noise";
					}
					return "There is no game on right now";
				}
			}
			catch(LiteralRequiredException e) {
				return null;
			}
		}
		
		//no match on - I have nothing to describe
		return null;
	}

}
