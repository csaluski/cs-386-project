package edu.nau.cs386.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Tag {
    public String tag;
    public List<UUID> child;
    public List<UUID> parent;
    public UUID uuid;

    public Tag(String tag) {
        this.tag = tag;
        this.child = new ArrayList<>();
        this.parent = new ArrayList<>();
    }

    public Tag(UUID tagUuid, String tag) {
        this.uuid = tagUuid;
        this.tag = tag;
    }

    public String getTag() {
        return this.tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public List<UUID> getChildTag() {
        return child;
    }

    public List<UUID> getParentTag() {
        return parent;
    }

    public void setChildTag(List<UUID> newChild) {
        this.child = newChild;
    }

    public void setParentTag(List<UUID> newParent) {
        this.parent = newParent;
    }

    public UUID getUuid() {
        return this.uuid;
    }

}
