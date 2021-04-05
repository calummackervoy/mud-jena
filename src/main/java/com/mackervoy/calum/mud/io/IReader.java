package com.mackervoy.calum.mud.io;

import java.io.IOException;
import java.nio.file.Path;

public interface IReader {
    String readFile(Path path) throws IOException;
}
