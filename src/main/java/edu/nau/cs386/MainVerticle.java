package edu.nau.cs386;

import edu.nau.cs386.model.Paper;
import edu.nau.cs386.model.Tag;
import edu.nau.cs386.model.User;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.Cookie;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.FileUpload;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
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
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class MainVerticle extends AbstractVerticle {

    private Pulp pulp = Pulp.getInstance();
    private TemplateEngine engine;

    public User getUserfromCookie(RoutingContext ctx, JsonObject data) {
        Cookie crumb = ctx.getCookie("user");
        String uuidString = crumb.getValue();
        UUID userUUID = UUID.fromString(uuidString);
        User user = pulp.getUserManager().getUser(userUUID);
        data.put("name", user.getName());
        data.put("email", user.getEmail());
        data.put("bio", user.getBio());
        return user;
    }

    public Paper getPaperCookie(RoutingContext ctx, JsonObject data){
        Cookie crumb = ctx.getCookie("paper");
        String uuidString = crumb.getValue();
        UUID paperUUID = UUID.fromString(uuidString);
        Paper paper = pulp.getPaperManager().getPaper(paperUUID);
        data.put("title", paper.getTitle());
        data.put("pdf", paper.getPdf());
        data.put("abstract", paper.getPaperAbstract());
        data.put("doi", paper.getDoi());
        data.put("authors", paper.getAuthors());
        data.put("owners", paper.getOwners());
        data.put("tags", paper.getTags());
        return paper;
    }

    public void logout(RoutingContext ctx, JsonObject data){
        data.remove("name");
        data.remove("email");
        data.remove("bio");
        ctx.removeCookie("user", true);
    }
    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        // create the template engine
        engine = HandlebarsTemplateEngine.create(vertx);
        TemplateHandler templateHandler = TemplateHandler.create(engine);

        // create the http server and router
        HttpServer server = vertx.createHttpServer();
        Router router = Router.router(vertx);


        // configure the router
        router.route().handler(BodyHandler.create().setMergeFormAttributes(true).setUploadsDirectory("build/uploadedPdfs"));
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

            engine.render(data, "templates/indexGet.hbs", res -> {
                if (res.succeeded()) {
                    ctx.response().end(res.result());
                } else {
                    ctx.fail(res.cause());
                }
            });
        });

        router.get("/createUser.hbs").handler(this::getCreateUser);

        router.get("/login").handler(this::getLogin);
        router.get("/logout").handler(this::getLogout);
        router.get("/edit").handler(this::getEdit);
        router.get("/create").handler(this::getCreate);
        router.get("/tag").handler(this::getTag);
        router.get("/removeTag").handler(this::getRemoveTag);
        router.get("/profile").handler(this::getProfile);
        router.get("/browsePaper").handler(this::getBrowsePapers);

        router.route("/view/paper/:paperUuid").handler(this::viewPaperRoute);

        router.post("/create").handler(this::postCreateUser);

        router.post("/login").handler(this::postLogin);
        router.post("/logout").handler(this::postLogout);


        router.get("/uploadPDF").handler(this::getUploadPDF);
        router.get("/editPDF").handler(this::getEditPDF);

        router.post("/uploadPDF").handler(this::PostUploadPDF);

        router.post("/profile").handler(this::postProfile);

        router.post("/edit").handler(this::EditPost);
        router.post("/tag").handler(this::postTag);
        router.post("/removeTag").handler(this::postRemoveTag);
//        DatabaseDriver databaseDriver = new DatabaseDriver();
//
//        pulp.setDatabaseDriver(databaseDriver);

        server.requestHandler(router).listen(config().getInteger("port", 8888),
            http -> {
                if (http.succeeded()) {
                    startPromise.complete();
                    System.out.println("HTTP server started on port " + config().getInteger("port", 8888));
                } else {
                    startPromise.fail(http.cause());
                }
            });
    }

    private void postRemoveTag(RoutingContext ctx) {
        JsonObject data = new JsonObject();
        Paper paper = getPaperCookie(ctx, data);
        String tagName = ctx.request().getFormAttribute("tagName");
        paper.removeTag(paper.getTags(), tagName);
        data.put("tags", paper.getTags());
        ctx.reroute("/view/paper/" + paper.getUuid());
        engine.render(data, "templates/removeTag.hbs", res -> {
            if (res.succeeded()) {
                ctx.response().end(res.result());
            } else {
                ctx.fail(res.cause());
            }
        });
    }

    private void postTag(RoutingContext ctx) {
        JsonObject data = new JsonObject();
        Paper paper = getPaperCookie(ctx, data);
        String tagString = ctx.request().getFormAttribute("tags");
        Tag tag = pulp.getTagManager().createTag( tagString );
        paper.addTag(tag);
        data.put("tags", tag);
        ctx.reroute("/view/paper/" + paper.getUuid());
        engine.render(data, "templates/paperViewGet.hbs", res -> {
            if (res.succeeded()) {
                ctx.response().end(res.result());
            } else {
                ctx.fail(res.cause());
            }
        });
    }

    private void getRemoveTag(RoutingContext ctx) {
        JsonObject data = new JsonObject();
        Paper paper = getPaperCookie(ctx, data);

        engine.render(data, "templates/removeTag.hbs", res -> {
            if (res.succeeded()) {
                ctx.response().end(res.result());
            } else {
                ctx.fail(res.cause());
            }
        });
    }

    private void getTag(RoutingContext ctx) {
        JsonObject data = new JsonObject();
        Paper paper = getPaperCookie(ctx, data);
        User user = getUserfromCookie(ctx, data);

        engine.render(data, "templates/tagGetTag.hbs", res -> {
            if (res.succeeded()) {
                ctx.response().end(res.result());
            } else {
                ctx.fail(res.cause());
            }
        });
    }

    private void getEditPDF(RoutingContext ctx) {
        JsonObject data = new JsonObject();
        engine.render(data, "templates/paperEditPost.hbs", res -> {
            if (res.succeeded()) {
                ctx.response().end(res.result());
            } else {
                ctx.fail(res.cause());
            }
        });

    }

    private void getUploadPDF(RoutingContext ctx) {
        JsonObject data = new JsonObject();
        engine.render(data, "templates/paperCreatePost.hbs", res -> {
            if (res.succeeded()) {
                ctx.response().end(res.result());
            } else {
                ctx.fail(res.cause());
            }
        });
    }


    private void getCreate(RoutingContext ctx) {
        JsonObject data = new JsonObject();

        engine.render(data, "templates/userCreatePost.hbs", res -> {
            if (res.succeeded()) {
                ctx.response().end(res.result());
            } else {
                ctx.fail(res.cause());
            }
        });
    }

    private void getEdit(RoutingContext ctx) {
        JsonObject data = new JsonObject();
        User user = getUserfromCookie(ctx, data);

        engine.render(data, "templates/userProfilEditPost.hbs", res -> {
            if (res.succeeded()) {
                ctx.response().end(res.result());
            } else {
                ctx.fail(res.cause());
            }
        });
    }

    private void getLogout(RoutingContext ctx) {
        JsonObject data = new JsonObject();
        User user = getUserfromCookie(ctx, data);
        data.put("name", user.getName());

        engine.render(data, "templates/userLogoutGet.hbs", res -> {
            if (res.succeeded()) {
                ctx.response().end(res.result());
            } else {
                ctx.fail(res.cause());
            }
        });
    }

    private void getLogin(RoutingContext ctx) {
        JsonObject data = new JsonObject();

        engine.render(data, "templates/userLoginPost.hbs", res -> {
            if (res.succeeded()) {
                ctx.response().end(res.result());
            } else {
                ctx.fail(res.cause());
            }
        });
    }

    private void getCreateUser(RoutingContext ctx) {
        JsonObject data = new JsonObject();
        String name = ctx.request().getParam("name");

        System.out.println(name);

        name = (name != null && !name.trim().isEmpty() ? name : "person");

        data.put("name", name);

        engine.render(data, "templates/userCreatePost.hbs", res -> {
            if (res.succeeded()) {
                ctx.response().end(res.result());
            } else {
                ctx.fail(res.cause());
            }
        });
    }

    private void getProfile(RoutingContext ctx) {
        JsonObject data = new JsonObject();
        User user = getUserfromCookie(ctx, data);

        engine.render(data, "templates/userProfileGet.hbs", res -> {
            if (res.succeeded()) {
                ctx.response().end(res.result());
            } else {
                ctx.fail(res.cause());
            }
        });
    }

    private void postProfile(RoutingContext ctx) {
        JsonObject data = new JsonObject();
        User user = getUserfromCookie(ctx, data);

        engine.render(data, "templates/userProfileGet.hbs", res -> {
            if (res.succeeded()) {
                ctx.response().end(res.result());
            } else {
                ctx.fail(res.cause());
            }
        });
    }

    private void postLogout(RoutingContext ctx) {
        JsonObject data = new JsonObject();
        User user = getUserfromCookie(ctx, data);
        String logStatus = ctx.request().getFormAttribute("log");
        // logStatus = logStatus.toLowerCase(Locale.ROOT);
        if( logStatus.equalsIgnoreCase("Yes") )
        {
            logout(ctx, data);
            ctx.reroute("/");
        }
        else{
            ctx.reroute("/profile");
        }

        engine.render(data, "templates/userLogoutGet.hbs", res -> {
            if (res.succeeded()) {
                ctx.response().end(res.result());
            } else {
                ctx.fail(res.cause());
            }
        });
    }

    private void postLogin(RoutingContext ctx) {
        String email = ctx.request().getFormAttribute("email");
        User user = pulp.getUserManager().getUserByEmail(email);
        Cookie cookie = Cookie.cookie("user", user.getUuid().toString());
        ctx.addCookie(cookie);
        JsonObject data = new JsonObject();
        data.put("email", user.getEmail());
        data.put("name", user.getName());
        data.put("bio", user.getBio());
        if (user != null) {
            System.out.println("Name: " + user.getName() + "email: " + user.getEmail() + "bio: " + user.getBio() + "UUID: " + user.getUuid());
            ctx.reroute("/profile");
        } else {
            ctx.reroute("/login");
        }

        engine.render(data, "templates/userProfileGet.hbs", res -> {
            if (res.succeeded()) {
                ctx.response().end(res.result());
            } else {
                ctx.fail(res.cause());
            }
        });
    }

    private void postCreateUser(RoutingContext ctx) {
        String name = ctx.request().getFormAttribute("name");
        System.out.println(name);

        JsonObject data = new JsonObject();
        data.put("name", name);
        String email = ctx.request().getFormAttribute("email");
        System.out.println(email);

        // JsonObject data = new JsonObject();
        data.put("email", email);
        User user1 = pulp.getUserManager().createUser(name, email);
        System.out.println(pulp.getUserManager().getUser(user1.getUuid()));
        UUID userUuid = user1.getUuid();

        data.put("uuid", userUuid);

        engine.render(data, "templates/userHandlerPost.hbs", res -> {
            if (res.succeeded()) {
                ctx.response().end(res.result());
            } else {
                ctx.fail(res.cause());
            }
        });
    }

    private void viewPaperRoute(RoutingContext ctx) {
        UUID uuid = UUID.fromString(ctx.pathParam("paperUuid"));

        Paper paper = pulp.getPaperManager().getPaper(uuid);

        JsonObject paperJson = new JsonObject();

        StringBuilder paperAuthors = new StringBuilder();
        List<String> authors = paper.getAuthors();
        authors.forEach(paperAuthors::append);

        StringBuilder paperTags = new StringBuilder();
        List<Tag> tags = paper.getTags();
        tags.forEach(paperTags::append);

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
        String tagString = pulp.getTagManager().activeTagsToString(paper.getTags());

        paperJson.put("title", paper.getTitle());
        paperJson.put("paperAbstract", paper.getPaperAbstract());
        paperJson.put("doi", paper.getDoi());
        paperJson.put("authors", paperAuthors.toString());
        paperJson.put("owners", paperOwners.toString());
        paperJson.put("paperFile", pdfBase64);
        paperJson.put("tags", tagString);

        engine.render(paperJson, "templates/paperViewGet.hbs", res -> {
            if (res.succeeded()) {
                ctx.response().end(res.result());
            } else {
                ctx.fail(res.cause());
            }
        });
    }

    private void PostUploadPDF(RoutingContext ctx) {
        System.out.println("Reached the upload post handler");
        String title = ctx.request().getFormAttribute("title");
        String doi = ctx.request().getFormAttribute("doi");
        // TODO: parse this as comma delimited list of authors
        List<String> authors = Collections.singletonList(ctx.request().getFormAttribute("authors"));
        String paperAbstract = ctx.request().getFormAttribute("abstract");
        String uploader = ctx.request().getFormAttribute("email");

        Iterator<FileUpload> uploadIterator = ctx.fileUploads().iterator();

        File pdfFile = new File("/dev/null");

        if (uploadIterator.hasNext()) {
            System.out.println("We got a file bois");
            FileUpload file = uploadIterator.next();

            Buffer uploadedFile = vertx.fileSystem().readFileBlocking(file.uploadedFileName());

            // Uploaded File Name
            String fileName = URLDecoder.decode(file.fileName(), StandardCharsets.UTF_8);
            System.out.println(fileName);
            vertx.fileSystem().writeFileBlocking(fileName, uploadedFile);

            pdfFile = new File(fileName);
        }
        User paperUploader = pulp.getUserManager().getUserByEmail(uploader);
        Paper createdPaper = pulp.getPaperManager().createPaper(title, pdfFile, authors, paperUploader.getUuid());
        createdPaper.setDoi(doi);
        createdPaper.setPaperAbstract(paperAbstract);
        Cookie paperCookie = Cookie.cookie("paper", createdPaper.getUuid().toString());
        ctx.addCookie(paperCookie);
        System.out.println("Reached the reroute");
        System.out.println(createdPaper.getUuid());
        ctx.reroute("/view/paper/" + createdPaper.getUuid());
    }

    private void EditPost(RoutingContext ctx) {
        JsonObject data = new JsonObject();
        User original = getUserfromCookie(ctx, data);
        String name = ctx.request().getFormAttribute("name");
        String email = ctx.request().getFormAttribute("email");
        System.out.println(email);
        data.put("email", email);
        //Getting the bio into update variable
        String bio = ctx.request().getFormAttribute("bio");
        System.out.println(bio);
        data.put("bio", bio);
        original.setName(name);
        original.setEmail(email);
        original.setBio(bio);
        data.put("name",name);
        data.put("email",email);
        data.put("bio", bio);
        engine.render(data, "templates/editUser.hbs", res -> {
            if (res.succeeded()) {
                ctx.response().end(res.result());
            } else {
                ctx.fail(res.cause());
            }
        });
    }

    private void getBrowsePapers(RoutingContext ctx) {
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

        engine.render(data, "templates/paperBrowseGet.hbs", res -> {
            if (res.succeeded()) {
                ctx.response().end(res.result());
            } else {
                ctx.fail(res.cause());
            }
        });
    }
}
