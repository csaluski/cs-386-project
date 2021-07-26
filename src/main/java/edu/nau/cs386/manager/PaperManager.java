package edu.nau.cs386.manager;

import com.thedeanda.lorem.LoremIpsum;
import edu.nau.cs386.database.DatabaseDriverJDBC;
import edu.nau.cs386.model.Author;
import edu.nau.cs386.model.Paper;
import edu.nau.cs386.model.Tag;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class PaperManager {

    private static final PaperManager INSTANCE = new PaperManager();
    private final HashMap<UUID, Paper> activePapers = new HashMap<>();
    private DatabaseDriverJDBC databaseDriver;
    public UUID testPaperUUID;

    private PaperManager() {

    }

    public static PaperManager getInstance() {
        return INSTANCE;
    }


    public Paper createPaper(String title, File file, List<Author> authors, String doi, UUID owner) {
        Paper wkgPaper = new Paper(title, file, authors, owner);
        Paper newPaper = null;
        try {
            newPaper = databaseDriver.insertPaper(wkgPaper);
            for (Author author : authors) {
                try {
                    addAuthor(newPaper.getUuid(), author);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        } catch (SQLException e) {

        }
        activePapers.put(wkgPaper.getUuid(), wkgPaper);
        return wkgPaper;
    }

    public Paper createTestPaper(UUID owner) {
        LoremIpsum lorem = LoremIpsum.getInstance();

        Paper wkgPaper = createPaper("testPaper", new File("/null"), List.of("testAuthor1", "testAuthor2").stream().map(Author::new).collect(Collectors.toList()), "https://doi.org/", owner);
        editPaperAbstract(wkgPaper.getUuid(), lorem.getWords(100, 150));
        testPaperUUID = wkgPaper.getUuid();

        return wkgPaper;
    }

    public List<Paper> getAllPapers() {
        return new ArrayList<>(activePapers.values());
    }


    public Paper getPaper(UUID uuid) {
        // TODO: Make use database
        Paper wkgPaper;
        wkgPaper = activePapers.get(uuid);
        return wkgPaper;
    }


    public Paper editPaperTitle(UUID uuid, String newTitle) {
        Paper wkgPaper = activePapers.get(uuid);
        wkgPaper.setTitle(newTitle);
        return wkgPaper;
    }

    public Paper editPaperAbstract(UUID uuid, String newPaperAbstract) {
        Paper wkgPaper = activePapers.get(uuid);
        wkgPaper.setPaperAbstract(newPaperAbstract);
        return wkgPaper;
    }

    public Paper editDoi(UUID uuid, String newDoi) {
        Paper wkgPaper = activePapers.get(uuid);
        wkgPaper.setDoi(newDoi);
        return wkgPaper;
    }

    public Paper addAuthor(UUID paperUuid, Author author) throws SQLException {
        Paper wkgPaper;
        try {
            wkgPaper = activePapers.get(paperUuid);
            if (wkgPaper == null) {
                throw new UncachedException("Paper not found in cache");
            }
        } catch (UncachedException e) {
            wkgPaper = DatabaseDriverJDBC.getInstance().getPaper(paperUuid);
            activePapers.put(wkgPaper.getUuid(), wkgPaper);
        }
        List<Author> authors = wkgPaper.getAuthors();
        if (!authors.contains(author)) {
            Author newAuthor = AuthorManager.getInstance().createAuthor(author);
            wkgPaper = DatabaseDriverJDBC.getInstance().associateAuthorWithPaper(wkgPaper.getUuid(), newAuthor.getUuid());
        }
        return wkgPaper;
    }

    public Paper addTag(UUID paperUuid, String tag) throws SQLException {
        Paper wkgPaper;
        try {
            wkgPaper = activePapers.get(paperUuid);
            if (wkgPaper == null) {
                throw new UncachedException("Paper not found in cache");
            }
        } catch (UncachedException e) {
            wkgPaper = DatabaseDriverJDBC.getInstance().getPaper(paperUuid);
            activePapers.put(wkgPaper.getUuid(), wkgPaper);
        }
        List<Tag> currentTags = wkgPaper.getTags();
        if (!currentTags.contains(tag)) {
            Tag newTag = TagManager.getInstance().createTag(tag);
            wkgPaper = DatabaseDriverJDBC.getInstance().associateTagWithPaper(wkgPaper.getUuid(), newTag.getUuid());
            activePapers.put(wkgPaper.getUuid(), wkgPaper);
        }
        return wkgPaper;
    }

    public void setDatabaseDriver(DatabaseDriverJDBC databaseDriver) {
        this.databaseDriver = databaseDriver;
    }
}
