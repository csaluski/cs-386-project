package edu.nau.cs386;

import edu.nau.cs386.database.DatabaseDriverJDBC;
import edu.nau.cs386.manager.PaperManager;
import edu.nau.cs386.manager.UserManager;
import edu.nau.cs386.model.Paper;
import edu.nau.cs386.model.User;
import io.vertx.core.Vertx;

import java.util.concurrent.TimeUnit;

public class Pulp {
    private static final Pulp INSTANCE = new Pulp();
    private final UserManager userManager;
    private final PaperManager paperManager;
    private DatabaseDriverJDBC databaseDriver;

    public Pulp( ) {
        this.userManager = UserManager.getInstance();
        this.paperManager = PaperManager.getInstance();
        this.databaseDriver = new DatabaseDriverJDBC();

        userManager.setDatabaseDriver(databaseDriver);
        paperManager.setDatabaseDriver(databaseDriver);

        try {
            TimeUnit.SECONDS.sleep(15);
        } catch (InterruptedException e) {
            ;
        }


        //User testUser = userManager.createTestUser();
        //Paper testPaper = paperManager.createTestPaper(testUser.getUuid());
//        vertx.deployVerticle(new DatabaseDriver(), ar -> {
//            if (ar.succeeded()) {
//                System.out.println("We're trying to run the data creation shit");
//
//            }
//            else {
//                System.out.println("Oh no");
//                throw new RuntimeException();
//            }
//        });
    }

    public static Pulp getInstance() {return INSTANCE;}
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
