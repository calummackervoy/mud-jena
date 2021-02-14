/**
 *
 */
package com.mackervoy.calum.mud.content;

import com.mackervoy.calum.mud.MUDApplication;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;

/**
 * @author Calum Mackervoy
 * Provides endpoints for client-side applications to request content
 * (e.g. "please describe this object")
 */
@Path("/content/")
public class ContentController {
  @GET
  @Produces(MediaType.TEXT_PLAIN)
  public String getContent(@QueryParam("uri") String uri) {
		final String safeUri = uri == null ? "" : uri;
		final Model m = ModelFactory.createDefaultModel();
    m.read(safeUri);

		return getDescriber(safeUri, m)
			.map(describer -> describer.describe(m, safeUri))
			.orElse(null);
  }
	
	private Optional<IContentDescriber> getDescriber(String uri, Model m) {
		// check if I have a describer for the URI
    DescriberFactory factory = new DescriberFactory();
    IContentDescriber describer = factory.getDescriber(uri);
		
		// if I don't, try the RDF type
		if (describer == null) {
      Resource r = m.getResource(uri);
      String type = r.getPropertyResourceValue(RDF.type).toString();
      describer = factory.getDescriber(type);
    }

		return Optional.ofNullable(describer);
	}
}
