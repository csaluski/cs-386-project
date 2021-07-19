package edu.nau.cs386;

import edu.nau.cs386.manager.PaperManager;
import edu.nau.cs386.manager.UserManager;
import edu.nau.cs386.model.Paper;
import edu.nau.cs386.model.User;

public class Pulp {

    public UserManager userManager;
    public PaperManager paperManager;

    public Pulp() {
        this.userManager = new UserManager();
        this.paperManager = new PaperManager();

        User testUser = userManager.createTestUser();
        Paper testPaper = paperManager.createTestPaper(testUser.getUuid());
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

}
