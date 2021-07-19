package edu.nau.cs386;

import edu.nau.cs386.model.User;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
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
        router.route().handler(BodyHandler.create());
        router.route("/static/*").handler(StaticHandler.create("static"));
        router.route("/").handler(templateHandler);
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
            JsonObject data = new JsonObject();

            engine.render(data, "templates/profileGet.hbs", res -> {
                if (res.succeeded()) {
                    ctx.response().end(res.result());
                } else {
                    ctx.fail(res.cause());
                }
            });
        });
        router.get("/viewPDF").handler(ctx -> {
            JsonObject data = new JsonObject();

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

            engine.render(data, "templates/profileGet.hbs", res -> {
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
            User user1 = pulp.userManager.createUser(name,email);
            System.out.println(pulp.userManager.getUser(user1.getUuid()));
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
            User user = pulp.userManager.getUserByEmail(email);
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
        router.post("/profile").handler(ctx -> {
            String name = ctx.request().getFormAttribute("name");
            System.out.println(name);

            JsonObject data = new JsonObject();
            data.put("name", name);
            String email = ctx.request().getFormAttribute("email");
            System.out.println(email);

            // JsonObject data = new JsonObject();
            data.put("email", email);
            String bio = null;
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
        server.requestHandler(router)
            .listen(8888, http -> {
                if (http.succeeded()) {
                    startPromise.complete();
                    System.out.println("HTTP server started on port 8888");
                } else {
                    startPromise.fail(http.cause());
                }
            });
    }
}
