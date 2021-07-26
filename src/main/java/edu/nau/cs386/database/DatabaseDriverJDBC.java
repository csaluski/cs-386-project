package edu.nau.cs386.database;

import edu.nau.cs386.model.User;

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


}
