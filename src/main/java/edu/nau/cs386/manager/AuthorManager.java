package edu.nau.cs386.manager;

import edu.nau.cs386.database.DatabaseDriverJDBC;
import edu.nau.cs386.model.Author;

import java.util.HashMap;
import java.util.UUID;

public class AuthorManager {

    private static final AuthorManager INSTANCE = new AuthorManager();
    private final HashMap<UUID, Author> activeAuthors = new HashMap<>();
    private DatabaseDriverJDBC databaseDriver;
    public UUID testPaperUUID;

    private AuthorManager() {

    }

    public static AuthorManager getInstance() {
        return INSTANCE;
    }

    public Author createAuthor(Author author) {
        Author wkgAuthor;
        try {
            wkgAuthor = activeAuthors.get(author.getUuid());
            if (wkgAuthor == null) {
                throw new UncachedException("Paper not found in cache");
            }
        } catch (UncachedException e) {
            wkgAuthor = DatabaseDriverJDBC.getInstance().insertAuthor(author);
            e.printStackTrace();
        }
        return wkgAuthor;
    }

    public Author getAuthor(UUID authorUuid) {
        return activeAuthors.get(authorUuid);
    }
}
