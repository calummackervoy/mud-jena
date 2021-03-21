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
public class MUDLogic {
	public static final String uri ="https://raw.githubusercontent.com/Multi-User-Domain/vocab/main/mudlogic.ttl#";
	
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
    
    public final static Resource Action = resource( "Action" );
    public final static Resource Task = resource( "Task" );
    public final static Resource Transit = resource( "Transit" );
    public final static Property isComplete = property( "isComplete" );
    public final static Property endState = property( "endState" );
    public final static Property inserts = property( "inserts" );
    public final static Property deletes = property( "deletes" );
}
