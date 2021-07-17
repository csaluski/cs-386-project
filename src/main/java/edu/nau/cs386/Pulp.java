package edu.nau.cs386;

import edu.nau.cs386.manager.PaperManager;
import edu.nau.cs386.manager.UserManager;

public class Pulp {

    private UserManager userManager;
    private PaperManager paperManager;

    public Pulp() {
        this.userManager = new UserManager();
        this.paperManager = new PaperManager();
    }

    public UserManager getUserManager() {
        return userManager;
    }

    public PaperManager getPaperManager() {
        return paperManager;
    }

}
