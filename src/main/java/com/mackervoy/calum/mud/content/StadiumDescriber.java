package com.mackervoy.calum.mud.content;

import java.util.List;
import java.util.Optional;
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
	public Optional<Model> describe(Resource stadium) {
		//is there a match on?
		//TODO: a custom iterator like "has events now" would be a nice way to simplify this code
		StmtIterator iter = stadium.listProperties(MUDEvents.hasEvent);
		
		while(iter.hasNext()) {
			Resource event = iter.nextStatement().getResource();
			
			Resource beginning = event.getPropertyResourceValue(Time.hasBeginning);
			Resource end = event.getPropertyResourceValue(Time.hasEnd);
			if(beginning == null || end == null) continue;
			
			try {
				LocalDateTime beginTime = Time.instantToLocalDateTime(beginning);
				if(beginTime != null) {
					LocalDateTime endTime = Time.instantToLocalDateTime(end);
					
					if(LocalDateTime.now().isAfter(beginTime) && LocalDateTime.now().isBefore(endTime)) {
						return Optional.of(contentFromVisualDescription("Thousands of people are in and outside the stadium. There is a lot of noise"));
					}
					return Optional.of(contentFromVisualDescription("There is no game on right now"));
				}
			}
			catch(LiteralRequiredException e) {
				return Optional.empty();
			}
		}
		
		//no match on - I have nothing to describe
		return Optional.empty();
	}

}
