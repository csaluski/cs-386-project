package edu.nau.cs386.manager;

import com.thedeanda.lorem.Lorem;
import com.thedeanda.lorem.LoremIpsum;
import edu.nau.cs386.model.User;

import java.util.HashMap;
import java.util.UUID;

public class UserManager {

    private final HashMap<UUID, User> activeUsers = new HashMap<>();
    public UUID testUserUUID;

    public User createUser(String name, String email) {
        User wkgUser = new User(name, email);
        activeUsers.put(wkgUser.getUuid(), wkgUser);
        return wkgUser;
    }

    public User createTestUser() {
        LoremIpsum lorem = LoremIpsum.getInstance();

        User wkgUser = createUser("Test User", "email@nau.edu");
        updateBio(wkgUser.getUuid(), lorem.getWords(10));
        testUserUUID = wkgUser.getUuid();
        return wkgUser;
    }

    public User updateBio(UUID uuid, String newBio) {
        User wkgUser = activeUsers.get(uuid);
        wkgUser.setBio(newBio);

        return wkgUser;
    }

    public User getUser(UUID uuid) {
        return activeUsers.get(uuid);
    }

}
