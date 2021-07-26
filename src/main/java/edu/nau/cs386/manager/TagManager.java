package edu.nau.cs386.manager;

import edu.nau.cs386.database.DatabaseDriverJDBC;
import edu.nau.cs386.model.Tag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class TagManager {

    private static final TagManager INSTANCE = new TagManager();
    private final HashMap<UUID, Tag> activeTags = new HashMap<>();
    private Tag testTag;
    private DatabaseDriverJDBC databaseDriver;

    private TagManager() {
        this.databaseDriver = DatabaseDriverJDBC.getInstance();
    }

    public static TagManager getInstance() {
        return INSTANCE;
    }

    public DatabaseDriverJDBC getDatabaseDriver() {
        return databaseDriver;
    }

    public void setDatabaseDriver(DatabaseDriverJDBC driver) {
        this.databaseDriver = driver;
    }

    public Tag createTag(String name) {
        UUID uuid = UUID.randomUUID();
        Tag wrgTag = new Tag(uuid, name);
        this.activeTags.put(uuid, wrgTag);
        return wrgTag;
    }

    public Tag createTestTag() {
        Tag wkgTag = createTag("Pulp");
        return wkgTag;
    }

    public Tag getTag(UUID uuid) {
        return activeTags.get(uuid);
    }

    public List<Tag> getActiveTags() {
        return new ArrayList<>(this.activeTags.values());
    }

    public String activeTagsToString(List<Tag> tags) {
        String tagString = "";
        for (int i = 0; i < tags.size(); i++) {
            tagString += tags.get(i).getTag() + " ";
        }
        return tagString;
    }
}
