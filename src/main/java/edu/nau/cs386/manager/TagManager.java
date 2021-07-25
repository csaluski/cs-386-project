package edu.nau.cs386.manager;

import edu.nau.cs386.database.DatabaseDriver;
import edu.nau.cs386.model.Paper;
import edu.nau.cs386.model.Tag;
import edu.nau.cs386.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class TagManager {

    private static final TagManager INSTANCE = new TagManager();
    private final HashMap<UUID, Tag> activeTags = new HashMap<>();
    private DatabaseDriver databaseDriver;

    private TagManager()
    {

    }
    public static TagManager getInstance()
    {
        return INSTANCE;
    }
    public DatabaseDriver getDatabaseDriver() {
        return databaseDriver;
    }
    public void setDatabaseDriver( DatabaseDriver driver )
    {
        this.databaseDriver = driver;
    }
    public Tag createTag( String name )
    {
        Tag wrgTag = new Tag( name );
        //activeTags.put( wrgTag.getUuid(), wrgTag );
        return wrgTag;
    }
    public Tag getTag(UUID uuid)
    {
        return activeTags.get(uuid);
    }
    public ArrayList<Tag> getActiveTags()
    {
        ArrayList<Tag> tags = new ArrayList<>(activeTags.values());
        return tags;
    }
}
