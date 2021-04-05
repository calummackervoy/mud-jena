package com.mackervoy.calum.mud.io;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Scanner;

public class FileReader implements IReader {
    @Override
    public String readFile(Path path) throws IOException {
        Scanner scanner = new Scanner(path.toFile());
        StringBuilder builder = new StringBuilder();
        while (scanner.hasNextLine()) {
            builder.append(scanner.nextLine());
        }
        scanner.close();

        return builder.toString();
    }
}
