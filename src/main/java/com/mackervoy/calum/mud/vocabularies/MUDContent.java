package com.mackervoy.calum.mud.vocabularies;

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
    public final static Property sight = property("sight");
}
