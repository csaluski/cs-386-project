package edu.nau.cs386.model;

import java.util.Objects;
import java.util.UUID;

public class Author {
    private UUID uuid;
    private final String name;

    public Author(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
    }

    public Author(String name) {
        this.name = name;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Author author = (Author) o;
        return Objects.equals(name, author.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, name);
    }
}
