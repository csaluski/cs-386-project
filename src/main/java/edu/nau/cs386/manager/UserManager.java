package edu.nau.cs386.manager;

import edu.nau.cs386.model.User;

import java.util.UUID;

public class UserManager {

    public User createUser() {
        return new User("name", "email@nau.edu", "test bio");
    }

    public User getUser(UUID uuid) {
        return new User("name", "email@nau.edu", "test bio");
    }

}
