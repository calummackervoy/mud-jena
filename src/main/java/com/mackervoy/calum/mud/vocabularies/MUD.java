package com.mackervoy.calum.mud.vocabularies;

import org.apache.jena.rdf.model.* ;

/*
 * * Class for accessing Resources and Properties of the MUD ontology as constants
 */

public class MUD {
	public static final String uri ="https://raw.githubusercontent.com/Multi-User-Domain/vocab/main/mud.ttl#";
	
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
    
    public final static Resource Organization = resource( "Organization" );
    public final static Resource Locatable = resource( "Locatable" );
    public final static Resource Settlement = resource( "Settlement" );
    public final static Resource Building = resource( "Building" );
    public final static Resource Party = resource( "Party" );
    public final static Resource Character = resource( "Character" );
    public final static Property locatedAt = property( "locatedAt" );
    public final static Property hasBuilding = property( "hasBuilding" );
    public final static Property population = property( "population" );
    public final static Property mainParty = property( "mainParty" );
    public final static Property ownedBy = property( "ownedBy" );
    public final static Property CharacterList = property( "CharacterList" );
    public final static Property primaryTextContent = property("primaryTextContent");
    public final static Property primaryImageContent = property("primaryImageContent");
}
