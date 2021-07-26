package edu.nau.cs386.manager;

import com.thedeanda.lorem.LoremIpsum;
import edu.nau.cs386.database.DatabaseDriverJDBC;
import edu.nau.cs386.model.User;
import io.vertx.core.json.JsonObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class UserManager {

    private static final UserManager INSTANCE = new UserManager();
    private final HashMap<UUID, User> activeUsers = new HashMap<>();
    public UUID testUserUUID;
    private DatabaseDriverJDBC databaseDriver;

    private UserManager() {

    }


    public static UserManager getInstance() {
        return INSTANCE;
    }

    public DatabaseDriverJDBC getDatabaseDriver() {
        return databaseDriver;
    }

    public void setDatabaseDriver(DatabaseDriverJDBC databaseDriver) {
        this.databaseDriver = databaseDriver;
    }


    public User createUser(String name, String email) {
        User wkgUser = new User(name, email);
        System.out.println("Trying to use database driver");

        try {
            wkgUser = databaseDriver.insertUser(wkgUser);
            if (wkgUser == null) {
                return null;
            }
            activeUsers.put(wkgUser.getUuid(), wkgUser);
            System.out.println("Worked!");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println("Rip!");

        }
        return wkgUser;
    }
    public User editUser(UUID userUuid, String name, String email, String bio, JsonObject data)
    {
        User oldUser = getUser(userUuid);
        User newUser = databaseDriver.updateUser(userUuid, name, email, bio);
        newUser.setName(name);
        newUser.setEmail(email);
        newUser.setBio(bio);
        data.remove("name");
        data.remove("email");
        data.remove("bio");
        data.put("name", name);
        data.put("email", email);
        data.put("bio", bio);
        return newUser;
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
        wkgUser = databaseDriver.updateUser(uuid, wkgUser.getName(), wkgUser.getEmail(), newBio);
        wkgUser.setBio(newBio);
        return wkgUser;
    }


    public User getUserByEmail(String email) {
        ArrayList<User> users = new ArrayList<>(activeUsers.values());
        User foundUser = null;
        for (User user : users) {
            if (user.getEmail().equals(email)) {
                foundUser = user;
            }
        }
        return foundUser;
    }

    public User getUser(UUID uuid) {
        return activeUsers.get(uuid);
    }

}
