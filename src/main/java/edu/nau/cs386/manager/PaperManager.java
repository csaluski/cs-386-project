package edu.nau.cs386.manager;

import edu.nau.cs386.model.Paper;

import java.io.File;
import java.util.List;
import java.util.UUID;

public class PaperManager {

    public Paper createPaper() {
        return new Paper("title", new File("/null"), List.of("author1", "author2"));
    }

    public Paper getPaper(UUID uuid) {
        return new Paper("title", new File("/null"), List.of("author1", "author2"));
    }

}
