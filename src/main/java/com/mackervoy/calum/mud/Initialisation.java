package com.mackervoy.calum.mud;

import com.mackervoy.calum.mud.io.IReader;
import org.apache.jena.rdf.model.Model;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class Initialisation {
    private final IReader reader;

    public Initialisation(IReader reader) {
        this.reader = reader;
    }

    public void init(Model model) throws IOException {
        Path initialisationFile = FileSystems.getDefault().getPath(getInitialisationFile());
        String initialisationFileContents = this.reader.readFile(initialisationFile);

        model.read(new ByteArrayInputStream(initialisationFileContents.getBytes()), null, "TTL");
    }

    private String getInitialisationFile() {
        String settlementsFile = System.getenv("initialisation_file");

        return settlementsFile == null ? "/home/mud/initialisation.ttl" : settlementsFile;
    }
}