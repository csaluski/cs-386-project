package edu.nau.cs386.manager;

import com.thedeanda.lorem.LoremIpsum;
import edu.nau.cs386.model.Paper;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class PaperManager {

    private final HashMap<UUID, Paper> activePapers = new HashMap<>();
    public UUID testPaperUUID;


    public Paper createPaper(String title, File file, List<String> authors, UUID owner) {
        Paper wkgPaper = new Paper(title, file, authors, owner);
        activePapers.put(wkgPaper.getUuid(), wkgPaper);
        return wkgPaper;
    }

    public Paper createTestPaper(UUID owner) {
        LoremIpsum lorem = LoremIpsum.getInstance();

        Paper wkgPaper = createPaper("testPaper", new File("/null"), List.of("testAuthor1", "testAuthor2"), owner);
        editPaperAbstract(wkgPaper.getUuid(), lorem.getWords(100,150));
        testPaperUUID = wkgPaper.getUuid();

        return wkgPaper;
    }

    public Paper getPaper(UUID uuid) {
        Paper wkgPaper;
        wkgPaper = activePapers.get(uuid);
        return wkgPaper;
    }

    public List<Paper> getAllPapers() {
        return new ArrayList<>(activePapers.values());
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

    public Paper addAuthor(UUID uuid, String author) {
        Paper wkgPaper = activePapers.get(uuid);
        wkgPaper.addAuthor(author);
        return wkgPaper;
    }

    public Paper addOwner(UUID paperUuid, UUID newOwnerUuid) {
        Paper wkgPaper = activePapers.get(paperUuid);
        wkgPaper.addOwner(newOwnerUuid);
        return wkgPaper;
    }

}