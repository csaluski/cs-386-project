package edu.nau.cs386.model;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Tag {
    public String name;
    public List<UUID> child;
    public List<UUID> parent;
    public Tag( String name )
    {
     this.name = name;
     this.child = new ArrayList<>();
     this.parent = new ArrayList<>();
    }
    public String getName()
    {
        return this.name;
    }
    public void setName( String name )
    {
        this.name = name;
    }
    public List<UUID> getChildTag()
    {
        return child;
    }
    public List<UUID> getParentTag()
    {
        return parent;
    }
    public void setChildTag(List<UUID> newChild)
    {
        this.child = newChild;
    }
    public void setParentTag(List<UUID> newParent)
    {
        this.parent = newParent;
    }

}
