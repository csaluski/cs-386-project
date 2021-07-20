package edu.nau.cs386;

import edu.nau.cs386.model.Paper;
import edu.nau.cs386.model.User;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Context;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Route;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.common.template.TemplateEngine;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.handler.TemplateHandler;
import io.vertx.ext.web.templ.handlebars.HandlebarsTemplateEngine;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.SqlClient;
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
        Pulp pulp = new Pulp();
        // create the template engine
        TemplateEngine engine = HandlebarsTemplateEngine.create(vertx);
        TemplateHandler templateHandler = TemplateHandler.create(engine);

        // create the http server and router
        HttpServer server = vertx.createHttpServer();
        Router router = Router.router(vertx);

        // configure the router
        router.route("/static/*").handler(StaticHandler.create("static"));
        router.route("/").handler(templateHandler);

        router.get("/view/paper/:paperUuid").handler(ctx -> {

            UUID uuid = UUID.fromString(ctx.pathParam("paperUuid"));

            Paper paper = pulp.paperManager.getPaper(uuid);

            JsonObject paperJson = new JsonObject();

            StringBuilder paperAuthors = new StringBuilder();
            List<String> authors = paper.getAuthors();
            authors.forEach(paperAuthors::append);

            StringBuilder paperOwners = new StringBuilder();
            List<UUID> owners = paper.getOwners();
            owners.forEach(ownerUuid ->
                paperOwners.append(pulp.userManager.getUser(ownerUuid).getName()));


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

        router.get("/upload").handler(ctx -> {
            JsonObject data = new JsonObject();

            engine.render(data, "templates/paperCreate.hbs", res -> {
                if (res.succeeded()) {
                    ctx.response().end(res.result());
                } else {
                    ctx.fail(res.cause());
                }
            });
        });

        router.post("/upload").handler(ctx -> {
            String title = ctx.request().getFormAttribute("title");
            String doi = ctx.request().getFormAttribute("doi");
            // TODO: parse this as comma delimited list of authors
            List<String> authors = Collections.singletonList(ctx.request().getFormAttribute("authors"));
            String paperAbstract = ctx.request().getFormAttribute("abstract");
            String uploader = ctx.request().getFormAttribute("email");
            File pdfFile = new File(ctx.request().getFormAttribute("paper"));

            User paperUploader = pulp.userManager.getUserByEmail(uploader);

            Paper createdPaper = pulp.paperManager.createPaper(title, pdfFile, authors, paperUploader.getUuid());
            createdPaper.setDoi(doi);
            createdPaper.setPaperAbstract(paperAbstract);

            ctx.reroute("/view/paper/" + createdPaper.getUuid());
        });

        router.get("/editPaper/:paperUuid").handler(ctx -> {
            UUID uuid = UUID.fromString(ctx.pathParam("paperUuid"));


        });


        PgConnectOptions connectOptions = new PgConnectOptions()
            .setPort(5432)
            .setHost("postgres")
            .setDatabase("dvdrental")
            .setUser("postgres")
            .setPassword("mysecretpassword");

        // Pool options
        PoolOptions poolOptions = new PoolOptions()
            .setMaxSize(5);

        // Create the client pool
        SqlClient client = PgPool.client(vertx, connectOptions, poolOptions);

        // A simple query
        client
            .query("SELECT * FROM public.customer")
            .execute(ar -> {
                if (ar.succeeded()) {
                    RowSet<Row> result = ar.result();
                    System.out.println("Got " + result.size() + " rows ");
                } else {
                    System.out.println("Failure: " + ar.cause().getMessage());
                }

                // Now close the pool
                client.close();
            });


        // start the http server
        server.requestHandler(router).listen(config().getInteger("port", 8888),
            http -> {
                if (http.succeeded()) {
                    startPromise.complete();
                    System.out.println("HTTP server started on port 8888");
                } else {
                    startPromise.fail(http.cause());
                }
            });
    }
}
