/**
 * 
 */
package com.mackervoy.calum.mud.vocabularies;

import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;

/**
 * @author Calum Mackervoy
 * Class for accessing Resources and Properties of the MUDBuildings ontology as constants
 */
public class MUDBuildings {
	public static final String uri ="https://calum.inrupt.net/public/voc/mudbuildings.ttl#";
	
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
    
    public final static Resource Stadium = resource( "Stadium" );
    public final static Resource Nightclub = resource( "Nightclub" );
}
