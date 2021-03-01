/**
 * 
 */
package com.mackervoy.calum.mud.content;

import org.apache.jena.rdf.model.*;
import java.util.Optional;

/**
 * @author Calum Mackervoy
 * The IContentDescriber defines an interface which all Content Describers should implement
 * It allows the implementing class to define rich content for a parameterised object
 */
public interface IContentDescriber {
	public Optional<Model> describe(Model dataModel, String uri);
}
