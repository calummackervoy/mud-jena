/**
 * 
 */
package com.mackervoy.calum.mud.vocabularies;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;

/**
 * @author Calum Mackervoy
 * Class for accessing Resources and Properties of the Time ontology as constants
 * Contains some methods for interfacing with the java.time classes
 */
public class Time {
	public static final String uri ="http://www.w3.org/2006/time#";
	
	/** returns the URI for this schema
     * @return the URI for this schema
     */
    public static String getURI() {
          return uri;
    }
    
    protected static final Resource resource( String local )
    { return ResourceFactory.createResource( uri + local ); }

    protected static final Property property( String local )
    { return ResourceFactory.createProperty( uri, local ); }
    
    public final static Resource Instant = resource( "Instant" );
    public final static Resource Interval = resource( "Interval" );
    
    public final static Property hasBeginning = property( "hasBeginning" );
    public final static Property hasEnd = property( "hasEnd" );
    public final static Property inXSDDate = property( "inXSDDate" );
    public final static Property inXSDDateTimeStamp = property( "inXSDDateTimeStamp" );
    
    //TODO: bounds checking on this property
    public final static LocalDateTime resourceToLocalDateTime(Resource instant) {
    	return LocalDateTime.parse(instant.getProperty(Time.inXSDDateTimeStamp).getString());
    }
    
    public final static java.time.Instant resourceToInstant(Resource instant) {
    	try {
    		return java.time.Instant.parse(instant.getProperty(Time.inXSDDateTimeStamp).getString());
    	}
    	catch(java.time.format.DateTimeParseException e) {
    		return Time.resourceToLocalDateTime(instant).toInstant(ZoneOffset.UTC);
    	}
    }
}
