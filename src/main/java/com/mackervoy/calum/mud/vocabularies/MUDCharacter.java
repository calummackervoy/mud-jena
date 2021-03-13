package com.mackervoy.calum.mud.vocabularies;

import org.apache.jena.rdf.model.* ;

public class MUDCharacter {
	public static final String uri ="https://raw.githubusercontent.com/Multi-User-Domain/vocab/main/mudchar.ttl#";
	
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
    
    public final static Resource Party = resource( "Party" );
    public final static Resource Character = resource( "Character" );
    public final static Property hasTask = property( "hasTask" );
    public final static Property mainParty = property( "mainParty" );
}
