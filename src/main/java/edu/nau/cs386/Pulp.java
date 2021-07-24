package edu.nau.cs386;

import edu.nau.cs386.database.DatabaseDriver;
import edu.nau.cs386.manager.PaperManager;
import edu.nau.cs386.manager.UserManager;
import edu.nau.cs386.model.Paper;
import edu.nau.cs386.model.User;

public class Pulp {
    private static final Pulp INSTANCE = new Pulp();


    private final UserManager userManager;
    private final PaperManager paperManager;
    private DatabaseDriver databaseDriver;

    private Pulp() {
        this.userManager = UserManager.getInstance();
        this.paperManager = PaperManager.getInstance();

        User testUser = userManager.createTestUser();
        Paper testPaper = paperManager.createTestPaper(testUser.getUuid());
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

    public DatabaseDriver getDatabaseDriver() {
        return databaseDriver;
    }

    public void setDatabaseDriver(DatabaseDriver databaseDriver) {
        this.databaseDriver = databaseDriver;

        userManager.setDatabaseDriver(databaseDriver);
        paperManager.setDatabaseDriver(databaseDriver);
    }


}
