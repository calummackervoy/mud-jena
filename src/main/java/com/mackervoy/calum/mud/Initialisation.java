package com.mackervoy.calum.mud;

import com.mackervoy.calum.mud.io.IReader;
import com.mackervoy.calum.mud.vocabularies.MUD;
import com.mackervoy.calum.mud.vocabularies.MUDBuildings;
import com.mackervoy.calum.mud.vocabularies.MUDEvents;
import com.mackervoy.calum.mud.vocabularies.Time;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.tdb2.TDB2Factory;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.VCARD4;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Initialisation {
    private final IReader reader;

    public Initialisation(IReader reader) {
        this.reader = reader;
    }

    public void init(Model model) throws IOException {
        Path initialisationFile = FileSystems.getDefault().getPath("/home/mud/settlements.ttl");
        String initialisationFileContents = this.reader.readFile(initialisationFile);

        model.read(new ByteArrayInputStream(initialisationFileContents.getBytes()), null, "TTL");
    }

    protected static void initSettlement() {
        // Make a TDB-backed dataset
        //TODO: extend the DatasetItem class to wrap the Jena dataset, so that the write can be completed by this class
        //TODO: use a new datasetItem for the world content or continue with the global one ? Open issue
        //DatasetItem demoData = TDBStore.WORLD.getNewDataset();
        Dataset dataset = TDB2Factory.connectDataset(TDBStore.WORLD.getFileLocation());
        try {
            dataset.begin(ReadWrite.WRITE);

            if (!dataset.isEmpty()) {
                dataset.abort();
                return;
            }

            buildDefaultModel(dataset).commit();
        } finally {
            dataset.end();
        }
    }

    private static Model buildDefaultModel(Dataset dataset) {
        Model model = dataset.getDefaultModel();

        // add a football stadium (South Babylon FC)
        Resource stadium = ResourceFactory.createResource(TDBStore.WORLD.getNewResourceUri("building", "south_babylon_fc_stadium"));
        model.add(stadium, RDF.type, MUDBuildings.Stadium);
        model.add(stadium, VCARD4.fn, "South Babylon Football Club Stadium");
        Resource image = ResourceFactory.createResource("https://www.arthistoryabroad.com/wp-content/uploads/2013/08/LOWRY-Football-Match.jpg");
        model.add(stadium, MUD.primaryImageContent, image);
        model.add(stadium, MUD.primaryTextContent, "A towering brickwork structure of an industrial appearance");

        // there's a match at the stadium
        Resource match = ResourceFactory.createResource(TDBStore.WORLD.getNewResourceUri("event", "demo_football_match"));
        model.add(match, RDF.type, MUDEvents.FootballMatch);

        Resource matchBegins = ResourceFactory.createResource(TDBStore.WORLD.getNewResourceUri("instant", "demo_football_match_begins"));
        model.add(matchBegins, RDF.type, Time.Instant);
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        model.add(matchBegins, Time.inXSDDateTimeStamp, LocalDateTime.now().format(formatter));

        Resource matchEnds = ResourceFactory.createResource(TDBStore.WORLD.getNewResourceUri("instant", "demo_football_match_ends"));
        model.add(matchEnds, RDF.type, Time.Instant);
        model.add(matchEnds, Time.inXSDDateTimeStamp, LocalDateTime.now().plusHours(2).format(formatter));
        model.add(match, Time.hasBeginning, matchBegins);
        model.add(match, Time.hasEnd, matchEnds);

        model.add(stadium, MUDEvents.hasEvent, match);

        // add a night club in Babylon
        Resource collective = ResourceFactory.createResource(TDBStore.WORLD.getNewResourceUri("building", "the_collective_night_club"));
        model.add(collective, RDF.type, MUDBuildings.Nightclub);
        model.add(collective, VCARD4.fn, "The Collective Night Club");

        Resource babylon = ResourceFactory.createResource(TDBStore.WORLD.getNewResourceUri("settlement", "babylon"));
        model.add(babylon, RDF.type, MUD.Settlement);
        model.add(babylon, VCARD4.fn, "Babylon");
        model.add(babylon, MUD.population, "8000000");
        model.add(babylon, MUD.hasBuilding, stadium);
        model.add(babylon, MUD.hasBuilding, collective);
        model.add(babylon, MUD.primaryTextContent, "The Capital of the Babylonian Empire");

        Resource roric = ResourceFactory.createResource(TDBStore.WORLD.getNewResourceUri("settlement", "roric"));
        model.add(roric, RDF.type, MUD.Settlement);
        model.add(roric, VCARD4.fn, "Roric");
        model.add(roric, MUD.population, "1000000");
        model.add(roric, MUD.primaryTextContent, "The Second City of the Babylon region, but a cosmopolis in its own right, and an industrial powerhouse");

        return model;
    }
}