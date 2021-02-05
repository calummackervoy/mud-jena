/**
 * 
 */
package com.mackervoy.calum.mud.vocabularies;

import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;

/**
 * @author Calum Mackervoy
 * Class for accessing Resources and Properties of the MUDLogic ontology as constants
 */
public class SolidTerms {
	public static final String uri ="http://www.w3.org/ns/solid/terms#";
	
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
    
    public final static Resource Patch = resource( "Patch" );
    public final static Property patches = property( "patches" );
    public final static Property deletes = property( "deletes" );
    public final static Property inserts = property( "inserts" );
    public final static Property where = property( "where" );

}
