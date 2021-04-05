package com.mackervoy.calum.mud;

import com.mackervoy.calum.mud.io.IReader;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestInitalisation {
    @Test(expected = IOException.class)
    public void testThrowsExceptionIfReaderThrows() throws IOException {
        IReader readerMock = mock(IReader.class);
        when(readerMock.readFile(any())).thenThrow(new IOException());

        Initialisation initialisation = new Initialisation(readerMock);

        Model model = ModelFactory.createDefaultModel();
        initialisation.init(model);
    }

    @Test
    public void testGeneratesModelIfReaderReturnsValidTurtle() throws IOException {
        StringBuilder settlementInTurtle = new StringBuilder();
        File initialisationFile = new File("/home/matttennison/development/multi-user-domain/mud-jena/initialisation.ttl");
        Scanner scanner = new Scanner(initialisationFile);
        while(scanner.hasNextLine()) {
            String data = scanner.nextLine();
            settlementInTurtle.append(data);
        }
        IReader readerMock = mock(IReader.class);
        when(readerMock.readFile(any())).thenReturn(settlementInTurtle.toString());

        Initialisation initialisation = new Initialisation(readerMock);
        Model model = ModelFactory.createDefaultModel();
        initialisation.init(model);

        int propertiesOnCollectiveNightClub = model.getResource("http://localhost:8080/mud/settlements/#the_collective_night_club").listProperties().toList().size();
        assertEquals(2, propertiesOnCollectiveNightClub);
    }
}
