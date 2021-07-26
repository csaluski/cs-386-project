package edu.nau.cs386.model;

import java.util.Objects;
import java.util.UUID;

public class User {

    private UUID uuid;
    private String name;
    private String email;
    private String bio;

    public User(UUID uuid, String name, String email, String bio) {
        this.name = name;
        this.email = email;
        this.bio = bio;
        this.uuid = uuid;
    }

    public User(String name, String email, String bio) {
        this.name = name;
        this.email = email;
        this.bio = bio;
        this.uuid = UUID.randomUUID();
    }

    public User(String name, String email, String bio, UUID uuid) {
        this.name = name;
        this.email = email;
        this.bio = bio;
        this.uuid = uuid;
    }

    public User(String name, String email) {
        this(name, email, "");
    }

    public String getBio() {
        return (bio != null) ? bio : "";
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(uuid, user.uuid);
    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }

}
