package edu.nau.cs386;

import edu.nau.cs386.model.Paper;
import edu.nau.cs386.model.User;
import io.vertx.config.ConfigRetriever;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Context;
import io.vertx.core.Promise;
import io.vertx.core.http.Cookie;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.common.template.TemplateEngine;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.handler.TemplateHandler;
import io.vertx.ext.web.templ.handlebars.HandlebarsTemplateEngine;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.UUID;


public class MainVerticle extends AbstractVerticle {


    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        Pulp pulp = Pulp.getInstance();
        // create the template engine
        TemplateEngine engine = HandlebarsTemplateEngine.create(vertx);
        TemplateHandler templateHandler = TemplateHandler.create(engine);

        // create the http server and router
        HttpServer server = vertx.createHttpServer();
        Router router = Router.router(vertx);

        // configure the router
        router.route().handler(BodyHandler.create());
        router.route("/static/*").handler(StaticHandler.create("static"));
        router.route("/").handler(ctx -> {
            List<Paper> papers = pulp.getPaperManager().getAllPapers();
            JsonObject data = new JsonObject();
            JsonObject wkgObject;
            JsonArray papersArray = new JsonArray();


            for (Paper paper : papers) {
                wkgObject = new JsonObject();
                StringBuilder authorsString = new StringBuilder();

                wkgObject.put("uuid", paper.getUuid());
                wkgObject.put("title", paper.getTitle());
                for (String author : paper.getAuthors()) {
                    authorsString.append(author);
                    authorsString.append(' ');
                }
                wkgObject.put("authors", authorsString.toString());

                papersArray.add(wkgObject);
            }

            data.put("papers", papersArray);

            engine.render(data, "templates/index.hbs", res -> {
                if (res.succeeded()) {
                    ctx.response().end(res.result());
                } else {
                    ctx.fail(res.cause());
                }
            });
        });

        router.get("/createUser.hbs").handler(ctx -> {
            JsonObject data = new JsonObject();
            String name = ctx.request().getParam("name");

            System.out.println(name);

            name = (name != null && !name.trim().isEmpty() ? name : "person");

            data.put("name", name);

            engine.render(data, "templates/createUser.hbs", res -> {
                if (res.succeeded()) {
                    ctx.response().end(res.result());
                } else {
                    ctx.fail(res.cause());
                }
            });
        });
        router.get("/login").handler(ctx -> {
            JsonObject data = new JsonObject();

            engine.render(data, "templates/login.hbs", res -> {
                if (res.succeeded()) {
                    ctx.response().end(res.result());
                } else {
                    ctx.fail(res.cause());
                }
            });
        });
        router.get("/create").handler(ctx -> {
            JsonObject data = new JsonObject();

            engine.render(data, "templates/createUser.hbs", res -> {
                if (res.succeeded()) {
                    ctx.response().end(res.result());
                } else {
                    ctx.fail(res.cause());
                }
            });
        });
        router.get("/profile").handler(ctx -> {
            Cookie crumb = ctx.getCookie("user");
            String uuidString = crumb.getValue();
            UUID userUUID = UUID.fromString(uuidString);
            User user = pulp.getUserManager().getUser(userUUID);
            JsonObject data = new JsonObject();
            data.put("name", user.getName());
            data.put("email", user.getEmail());
            data.put("bio", user.getBio());

            engine.render(data, "templates/profileGet.hbs", res -> {
                if (res.succeeded()) {
                    ctx.response().end(res.result());
                } else {
                    ctx.fail(res.cause());
                }
            });
        });

        router.route("/view/paper/:paperUuid").handler(ctx -> {

            UUID uuid = UUID.fromString(ctx.pathParam("paperUuid"));

            Paper paper = pulp.getPaperManager().getPaper(uuid);

            JsonObject paperJson = new JsonObject();

            StringBuilder paperAuthors = new StringBuilder();
            List<String> authors = paper.getAuthors();
            authors.forEach(paperAuthors::append);

            StringBuilder paperOwners = new StringBuilder();
            List<UUID> owners = paper.getOwners();
            owners.forEach(ownerUuid ->
                paperOwners.append(pulp.getUserManager().getUser(ownerUuid).getName()));

            byte[] fileBytes = new byte[0];
            try {
                fileBytes = FileUtils.readFileToByteArray(paper.getPdf());
            } catch (IOException e) {
                e.printStackTrace();
            }

            String pdfBase64 = Base64.getEncoder().encodeToString(fileBytes);

            paperJson.put("title", paper.getTitle());
            paperJson.put("paperAbstract", paper.getPaperAbstract());
            paperJson.put("doi", paper.getDoi());
            paperJson.put("authors", paperAuthors.toString());
            paperJson.put("owners", paperOwners.toString());
            paperJson.put("paperFile", pdfBase64);

            engine.render(paperJson, "templates/paperViewGet.hbs", res -> {
                if (res.succeeded()) {
                    ctx.response().end(res.result());
                } else {
                    ctx.fail(res.cause());
                }
            });
        });

        router.post("/create").handler(ctx -> {
            String name = ctx.request().getFormAttribute("name");
            System.out.println(name);

            JsonObject data = new JsonObject();
            data.put("name", name);
            String email = ctx.request().getFormAttribute("email");
            System.out.println(email);

           // JsonObject data = new JsonObject();
            data.put("email", email);
            User user1 = pulp.getUserManager().createUser(name,email);
            System.out.println(pulp.getUserManager().getUser(user1.getUuid()));
            UUID userUuid = user1.getUuid();

            data.put("uuid", userUuid);

            engine.render(data, "templates/postHandlerUser.hbs", res -> {
                if (res.succeeded()) {
                    ctx.response().end(res.result());
                } else {
                    ctx.fail(res.cause());
                }
            });


        });

        router.post("/login").handler(ctx -> {

              String email = ctx.request().getFormAttribute("email");
//            String name = ctx.request().getFormAttribute("name");
//            System.out.println(name);
//
//            JsonObject data = new JsonObject();
//            String email = ctx.request().getFormAttribute("email");
//            System.out.println(email);
//
            User user = pulp.getUserManager().getUserByEmail(email);
            Cookie cookie = Cookie.cookie("user", user.getUuid().toString());
            ctx.addCookie(cookie);
            JsonObject data = new JsonObject();
            data.put("email", user.getEmail());
            data.put("name", user.getName());
            data.put("bio", user.getBio());
            if ( user != null )
            {
                System.out.println("Name: " + user.getName() + "email: " + user.getEmail() + "bio: " + user.getBio() + "UUID: " + user.getUuid());
                router.post("/profile");
            }
            else{
                router.post("/login");
            }

            engine.render(data, "templates/profileGet.hbs", res -> {
                if (res.succeeded()) {
                    ctx.response().end(res.result());
                } else {
                    ctx.fail(res.cause());
                }
            });
        });

        router.get("/uploadPDF").handler(ctx -> {
            JsonObject data = new JsonObject();
            engine.render(data, "templates/paperCreate", res -> {
                if (res.succeeded()) {
                    ctx.response().end(res.result());
                } else {
                    ctx.fail(res.cause());
                }
            });
        });


        router.post("/uploadPDF").handler(ctx -> {
            System.out.println("Reached the upload post handler");
            String title = ctx.request().getFormAttribute("title");
            String doi = ctx.request().getFormAttribute("doi");
            // TODO: parse this as comma delimited list of authors
            List<String> authors = Collections.singletonList(ctx.request().getFormAttribute("authors"));
            String paperAbstract = ctx.request().getFormAttribute("abstract");
            String uploader = ctx.request().getFormAttribute("email");
            File pdfFile = new File(ctx.request().getFormAttribute("paper"));

            User paperUploader = pulp.getUserManager().getUserByEmail(uploader);

            Paper createdPaper = pulp.getPaperManager().createPaper(title, pdfFile, authors, paperUploader.getUuid());
            createdPaper.setDoi(doi);
            createdPaper.setPaperAbstract(paperAbstract);

            System.out.println("Reached the reroute");
            System.out.println(createdPaper.getUuid());
            ctx.reroute("/view/paper/" + createdPaper.getUuid());
        });

        router.post("/profile").handler(ctx -> {
            Cookie crumb = ctx.getCookie("user");
            String uuidString = crumb.getValue();
            UUID userUUID = UUID.fromString(uuidString);

            String name = ctx.request().getFormAttribute("name");
            System.out.println(name);

            JsonObject data = new JsonObject();
            data.put("name", name);
            String email = ctx.request().getFormAttribute("email");
            System.out.println(email);

            // JsonObject data = new JsonObject();
            data.put("email", email);
            String bio = "";
            data.put("bio", bio);
            User user1 = new User(name, email, bio);
//            if ( pulp.userManager.loginChecker(user1) )
//            {
//                user1 = pulp.userManager.getUser(pulp.userManager.getID(user1));
//                System.out.println("Name: " + user1.getName() + "email: " + user1.getEmail() + "bio: " + user1.getBio() + "UUID: " + user1.getUuid());
//                router.post("/profile");
//            } else{
//                router.post("/login");
//                //return page not found
//            }

            engine.render(data, "templates/profileGet.hbs", res -> {
                if (res.succeeded()) {
                    ctx.response().end(res.result());
                } else {
                    ctx.fail(res.cause());
                }
            });
        });

        router.post("/edit").handler(ctx -> {
            Cookie crumb = ctx.getCookie("user");
            String uuidString = crumb.getValue();
            UUID userUUID = UUID.fromString(uuidString);
            //saving the email so we can get the user
            String name = ctx.request().getFormAttribute("name");
            System.out.println(name);
            //creating the json object to temporarily store html variable newName
            JsonObject data = new JsonObject();
            data.put("name", name);
            //Getting the email into update variable
            String email = ctx.request().getFormAttribute("email");
            System.out.println(email);
            data.put("email", email);
            //Getting the bio into update variable
            String bio = ctx.request().getFormAttribute("bio");
            System.out.println(bio);
            data.put("bio", bio);
            User original = pulp.getUserManager().getUser(userUUID);
            original.setName(name);
            original.setEmail(email);
            original.setBio(bio);
            engine.render(data, "templates/editUser.hbs", res -> {
                if (res.succeeded()) {
                    ctx.response().end(res.result());
                } else {
                    ctx.fail(res.cause());
                }
            });
        });

        server.requestHandler(router).listen(config().getInteger("port", 8888),
            http -> {
                if (http.succeeded()) {
                    startPromise.complete();
                    System.out.println("HTTP server started on port "+ config().getInteger("port", 8888));
                } else {
                    startPromise.fail(http.cause());
                }
            });
    }
}
