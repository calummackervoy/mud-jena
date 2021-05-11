package com.mackervoy.calum.mud.vocabularies;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.jena.rdf.model.* ;

/*
 * * Class for accessing Resources and Properties of the MUD Content ontology as constants
 */

public class MUDContent {
	public static final String uri ="https://raw.githubusercontent.com/Multi-User-Domain/vocab/main/mudcontent.ttl#";

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

    public final static Resource Content = resource( "Content" );
    public final static Resource Sense = resource("Sense");
    public final static Resource Sight = resource("Sight");
    public final static Property usesSense = property("usesSense");
    public final static Property sees = property("sees");
    public final static Property describes = property("describes");
    public final static Property hasText = property("hasText");
    public final static Property hasImage = property("hasImage");
    
    //Map of Sense classes to their property values
    public static final Map<Resource, Property> SENSE_TO_PROPERTY;
    static {
        Map<Resource, Property> internalMap = new HashMap<Resource,Property>();
        internalMap.put(Sight, sees);
        SENSE_TO_PROPERTY = Collections.unmodifiableMap(internalMap);
    }
}
