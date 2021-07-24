package edu.nau.cs386.database;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DatabaseDriver extends AbstractVerticle {

    PgConnectOptions connectOptions = new PgConnectOptions()
        .setPort(5432)
        .setHost("postgres")
        .setDatabase("pulp")
        .setUser("postgres")
        .setPassword("mysecretpassword");


    PoolOptions poolOptions = new PoolOptions()
        .setMaxSize(5);

    PgPool pool;

    @Override
    public void start(Promise<Void> startPromise) throws SQLException {
        pool = PgPool.pool(vertx, connectOptions, poolOptions);

        String[] requests = {"create TABLE IF NOT EXISTS users( " +
            "    user_uuid uuid DEFAULT gen_random_uuid (), " +
            "    email VARCHAR(255) UNIQUE NOT NULL, " +
            "    name VARCHAR(50)," +
            "    bio TEXT) ",
            "create TABLE IF NOT EXISTS papers( " +
                "    paper_uuid uuid DEFAULT gen_random_uuid (), " +
                "    title VARCHAR(255), " +
                "    abstract TEXT, " +
                "    file BYTEA, " +
                "    doi VARCHAR(255)) ",
            "create TABLE IF NOT EXISTS authors( " +
                "    author_uuid uuid DEFAULT gen_random_uuid (), " +
                "    name VARCHAR(50)) ",
            "create TABLE IF NOT EXISTS paper_authors( " +
                "    author_uuid uuid, " +
                "    paper_uuid uuid " +
                ") ",
            "create TABLE IF NOT EXISTS paper_owners( " +
                "    user_uuid uuid, " +
                "    paper uuid " +
                ") "};

        List<String> requestsArray = new ArrayList<String>(Arrays.asList(requests));

        System.out.println("Starting to create database, reading file");

        //Future<Buffer> sqlFile = vertx.fileSystem().readFile("/usr/psql/tables.psql");

        requestsArray.forEach(request -> {
            pool.query(request)
                .execute(ar -> {
                    System.out.println("Figuring out of it broke");
                    if (ar.succeeded()) {
//                    RowSet<Row> result = ar.result();
                        System.out.println("Success");
                    } else {
                        System.out.println("Failure: " + ar.cause().getMessage());
                    }
                });
        });
    }


    public Future<RowSet<Row>> insertUser(String name, String email) {
        Future<RowSet<Row>> future;

        future = pool
            .query("INSERT INTO users (name, email) VALUES (?, ?)")
            .execute();

        return future;
    }


    private void createDatabase() {

//        System.out.println(sqlFile.result().toString());

//                System.out.println("Starting to create future to run SQL query");
//                Future<Void> future = Future.future(fut ->
//                    pool.query(sqlFile.result().toString())
//                        .execute()
//                );
//                return future;
    }


}


