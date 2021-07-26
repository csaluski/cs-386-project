package edu.nau.cs386;

import edu.nau.cs386.database.DatabaseDriverJDBC;
import edu.nau.cs386.manager.PaperManager;
import edu.nau.cs386.manager.TagManager;
import edu.nau.cs386.manager.UserManager;
import edu.nau.cs386.model.Paper;
import edu.nau.cs386.model.Tag;
import edu.nau.cs386.model.User;

import java.util.concurrent.TimeUnit;

public class Pulp {
    private final UserManager userManager;
    private final PaperManager paperManager;
    private DatabaseDriverJDBC databaseDriver;
    private final TagManager tagManager;

    private final static Pulp INSTANCE = new Pulp();

    private Pulp() {
        this.userManager = UserManager.getInstance();
        this.paperManager = PaperManager.getInstance();
        this.databaseDriver = DatabaseDriverJDBC.getInstance();
        this.tagManager = TagManager.getInstance();

        userManager.setDatabaseDriver(databaseDriver);
        paperManager.setDatabaseDriver(databaseDriver);


        // This is so that the database has time to start
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
        }
        User testUser = userManager.createTestUser();
        Paper testPaper = paperManager.createTestPaper(testUser.getUuid());
        Tag testTag = tagManager.createTestTag();
    }

    public static Pulp getInstance() {
        return INSTANCE;
    }

    public UserManager getUserManager() {
        return userManager;
    }

    public PaperManager getPaperManager() {
        return paperManager;
    }

    public TagManager getTagManager() {
        return tagManager;
    }

    public User getTestUser() {
        return userManager.getUser(userManager.testUserUUID);
    }

    public Paper getTestPaper() {
        return paperManager.getPaper(paperManager.testPaperUUID);
    }

    public DatabaseDriverJDBC getDatabaseDriver() {
        return databaseDriver;
    }

    public void setDatabaseDriver(DatabaseDriverJDBC databaseDriver) {
        this.databaseDriver = databaseDriver;

        userManager.setDatabaseDriver(databaseDriver);
        paperManager.setDatabaseDriver(databaseDriver);
    }


}
