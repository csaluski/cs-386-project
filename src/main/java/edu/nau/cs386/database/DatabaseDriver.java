package edu.nau.cs386.database;

import edu.nau.cs386.model.User;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.templates.RowMapper;
import io.vertx.sqlclient.templates.SqlTemplate;
import io.vertx.sqlclient.templates.TupleMapper;

import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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


        System.out.println("Starting to create database, reading file");
    }

    private RowSet<Row> processRowFuture(Future<RowSet<Row>> future) throws RuntimeException {
        if (future.succeeded()) {
            return future.result();
        } else {
            throw new RuntimeException(future.cause());
        }
    }


    private final RowMapper<User> ROW_USER_MAPPER = row -> {
        UUID uuid = row.getUUID("uuid");
        String name = row.getString("name");
        String email = row.getString("email");
        String bio = row.getString("bio");

        return new User(uuid, name, email, bio);
    };

    private final TupleMapper<User> PARAMETERS_USER_MAPPER = TupleMapper.mapper(user -> {
        Map<String, Object> parameters = new HashMap<>();

        parameters.put("user_uuid", user.getUuid());
        parameters.put("name", user.getName());
        parameters.put("email", user.getEmail());
        parameters.put("bio", user.getBio());

        return parameters;
    });


    public User insertUser(String name, String email) throws RuntimeException {

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", name);
        parameters.put("email", email);

        Future<RowSet<User>> results = SqlTemplate.forQuery(
            pool.getConnection().result(),
            "INSERT INTO users (name, email) VALUES ($name, $email) RETURNING *")
            .mapTo(ROW_USER_MAPPER)
            .execute(parameters);


        if (results.succeeded()) {
            return results.result().iterator().next();
        } else {
            throw new RuntimeException(results.cause());
        }
    }

    public User getUserByEmail(String email) throws RuntimeException {

        Map<String, Object> parameters = Collections.singletonMap("email", email);

        Future<RowSet<User>> results = SqlTemplate.forQuery(
            pool.getConnection().result(),
            "SELECT * FROM users WHERE email==$email")
            .mapTo(ROW_USER_MAPPER)
            .execute(parameters);


        if (results.succeeded()) {
            return results.result().iterator().next();
        } else {
            throw new RuntimeException(results.cause());
        }
    }

    public User updateUser(User user, String name, String email, String bio) throws RuntimeException {
        User updatedUser = new User(user.getUuid(), name, email, bio);

        Future<RowSet<User>> results = SqlTemplate.forQuery(
            pool.getConnection().result(),
            "UPDATE users SET (name, email, bio) = (#{name}, #{email}, #{bio}) WHERE user_uuid == #{user_uuid} RETURNING *")
            .mapFrom(PARAMETERS_USER_MAPPER)
            .mapTo(ROW_USER_MAPPER)
            .execute(updatedUser);

        if (results.succeeded()) {
            return results.result().iterator().next();
        } else {
            throw new RuntimeException(results.cause());
        }
    }

}


