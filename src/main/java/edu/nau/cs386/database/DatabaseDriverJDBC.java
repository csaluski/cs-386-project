package edu.nau.cs386.database;

import edu.nau.cs386.model.User;
import edu.nau.cs386.Pulp;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.templates.SqlTemplate;
import edu.nau.cs386.manager.UserManager;
import java.sql.*;
import java.util.UUID;

public class DatabaseDriverJDBC {
    private final String url = "jdbc:postgresql://postgres:5432/postgres";
    private final String user = "postgres";
    private final String password = "postgres";


    private Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to the PostgreSQL server successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return conn;
    }

    public ResultSet getUsers() {
        ResultSet rs = null;


        String SQL = "SELECT * FROM users";

        try (Connection conn = connect()) {
            Statement stmt = conn.createStatement();
            rs = stmt.executeQuery(SQL);
            // display actor information

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return rs;
    }

    public User insertUser(User wkgUser) throws SQLException {
        String SQL = "INSERT INTO users (email, name) VALUES (?,?)";

        String UserSQL = "SELECT * FROM users WHERE user_uuid = ?";
        User newUser = null;


        UUID uuid = null;

        try (Connection conn = connect()) {
            PreparedStatement preparedStatement = conn.prepareStatement(SQL, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, wkgUser.getEmail());
            preparedStatement.setString(2, wkgUser.getName());


            int affectedRows = preparedStatement.executeUpdate();
            // check the affected rows
            if (affectedRows > 0) {
                // get the ID back
                try (ResultSet rs = preparedStatement.getGeneratedKeys()) {
                    if (rs.next()) {
                        uuid = UUID.fromString(rs.getString("user_uuid"));
                        System.out.println(uuid);
                    }
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }

            preparedStatement = conn.prepareStatement(UserSQL);
            preparedStatement.setObject(1, uuid);
            // check the affected rows
            System.out.println("Checking if query good");
            if (affectedRows > 0) {
                // get the ID back

                try (ResultSet userRow = preparedStatement.executeQuery()) {
                    System.out.println("making user");
                    userRow.next();

                    uuid = UUID.fromString(userRow.getString("user_uuid"));
                    String name = userRow.getString("name");
                    String email = userRow.getString("email");
                    String bio = userRow.getString("bio");

                    newUser = new User(uuid, name, email, bio);
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return newUser;
    }

//    public ResultSet getUser() {
//        ResultSet rs = null;
//        getUsers();
//
//        String SQL = "SELECT * FROM users";
//
//        try (Connection conn = connect()) {
//            Statement stmt = conn.createStatement();
//            rs = stmt.executeQuery(SQL);
//            // display actor information
//
//        } catch (SQLException ex) {
//            System.out.println(ex.getMessage());
//        }
//
//        return rs;
//    }


    public User updateUser(UUID userUUID, String name, String email, String bio) {
        String SQL = "UPDATE USER "
            + "SET email = ? "
            + "WHERE userUUID = ?"
            + "WHERE name =?"
            + "WHERE bio =?";

        int affectedrows = 0;

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setString(1, userUUID.toString());
            pstmt.setString(2, email);
            pstmt.setString(3, name);
            pstmt.setString(2, email);
            pstmt.setString(4, bio);
            affectedrows = pstmt.executeUpdate();

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        Pulp pulp = new Pulp();
        return pulp.getUserManager().getUser( userUUID );
    }
//    public User updateUser(User user, String name, String email, String bio) throws RuntimeException {
//        User updatedUser = new User(user.getUuid(), name, email, bio);
//
//        Future<RowSet<User>> results = SqlTemplate.forQuery(
//            pool.getConnection().result(),
//            "UPDATE users SET (name, email, bio) = (#{name}, #{email}, #{bio}) WHERE user_uuid == #{user_uuid} RETURNING *")
//            .mapFrom(PARAMETERS_USER_MAPPER)
//            .mapTo(ROW_USER_MAPPER)
//            .execute(updatedUser);
//
//        return processRowFuture(results).iterator().next();
//    }
}
