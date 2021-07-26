package edu.nau.cs386.manager;

import com.thedeanda.lorem.LoremIpsum;
import edu.nau.cs386.database.DatabaseDriver;
import edu.nau.cs386.model.Paper;
import edu.nau.cs386.model.Tag;
import edu.nau.cs386.model.User;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class TagManager {

    private static final TagManager INSTANCE = new TagManager();
    private List<Tag> activeTags = new ArrayList<>();
    private Tag testTag;
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
        this.activeTags.add( wrgTag );
        return wrgTag;
    }
    public Tag createTestTag()
    {
        LoremIpsum lorem = LoremIpsum.getInstance();
        Tag wkgTag = createTag("Pulp");
        return wkgTag;
    }
    public Tag getTag(String name)
    {
        Tag foundTag = null;
        int loopCounter = 0;
        while (loopCounter < activeTags.size())
        {
            if( activeTags.get(loopCounter).getName().equals(name))
            {
                foundTag = activeTags.get(loopCounter);
            }
        }
        if(foundTag == null)
        {
            foundTag = new Tag("Pulp");
        }
        return foundTag;
    }
    public List<Tag> getActiveTags()
    {
        if (this.activeTags.isEmpty())
        {
            return List.of(new Tag("Pulp"));
        }
        return this.activeTags;
    }
    public String activeTagsToString( List<Tag> tags )
    {
        String tagString = "";
        for( int i = 0; i < tags.size(); i++)
        {
            tagString += tags.get(i).getName() + " ";
        }
        return tagString;
    }
}
