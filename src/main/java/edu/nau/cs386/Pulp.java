package edu.nau.cs386;

import edu.nau.cs386.database.DatabaseDriver;
import edu.nau.cs386.manager.PaperManager;
import edu.nau.cs386.manager.UserManager;
import edu.nau.cs386.manager.TagManager;
import edu.nau.cs386.model.Paper;
import edu.nau.cs386.model.User;
import io.vertx.core.Vertx;
import edu.nau.cs386.model.Tag;

public class Pulp {
    private final UserManager userManager;
    private final PaperManager paperManager;
    private DatabaseDriverJDBC databaseDriver;
    private final TagManager tagManager;
    private DatabaseDriver databaseDriver;

    private Pulp() {
        this.userManager = UserManager.getInstance();
        this.paperManager = PaperManager.getInstance();
        this.databaseDriver = new DatabaseDriverJDBC();

        userManager.setDatabaseDriver(databaseDriver);
        paperManager.setDatabaseDriver(databaseDriver);

        this.tagManager = TagManager.getInstance();

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
