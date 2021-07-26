package edu.nau.cs386.database;

import edu.nau.cs386.manager.AuthorManager;
import edu.nau.cs386.manager.TagManager;
import edu.nau.cs386.model.Author;
import edu.nau.cs386.model.Paper;
import edu.nau.cs386.model.Tag;
import edu.nau.cs386.model.User;

import java.io.File;
import java.sql.*;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class DatabaseDriverJDBC {
    private final String url = "jdbc:postgresql://postgres:5432/postgres";
    private final String user = "postgres";
    private final String password = "postgres";

    private final static DatabaseDriverJDBC INSTANCE = new DatabaseDriverJDBC();

    private DatabaseDriverJDBC() {

    }

    public static DatabaseDriverJDBC getInstance() {
        return INSTANCE;
    }

    private Author parseAuthorRow(ResultSet resultSet) throws SQLException {
        System.out.println("parsing user");

        UUID uuid = UUID.fromString(resultSet.getString("author_uuid"));
        String name = resultSet.getString("name");

        return new Author(uuid, name);
    }

    private User parseUserRow(ResultSet resultSet) throws SQLException {
        System.out.println("parsing user");

        UUID uuid = UUID.fromString(resultSet.getString("user_uuid"));
        String name = resultSet.getString("name");
        String email = resultSet.getString("email");
        String bio = resultSet.getString("bio");

        return new User(uuid, name, email, bio);
    }

    private Paper parsePaperRow(ResultSet resultSet) throws SQLException {
        System.out.println("parsing paper");

        UUID paperUuid = UUID.fromString(resultSet.getString("paper_uuid"));
        String title = resultSet.getString("title");
        String paperAbstract = resultSet.getString("abstract");
        String doi = resultSet.getString("doi");
        File file = new File(resultSet.getString("file"));
        UUID ownerUuid = UUID.fromString(resultSet.getString("owner_uuid"));
        Array authorsArray = resultSet.getArray("authors_uuids");
        Array tagsArray = resultSet.getArray("tags_uuids");

        List<Author> authors = (List.of((String[]) authorsArray.getArray()))
            .stream()
            .map(UUID::fromString)
            .map(authorUuid -> AuthorManager.getInstance().getAuthor(authorUuid))
            .collect(Collectors.toList());

        List<Tag> tags = (List.of((String[]) tagsArray.getArray()))
            .stream()
            .map(UUID::fromString)
            .map(tagUuid -> TagManager.getInstance().getTag(tagUuid))
            .collect(Collectors.toList());

        return new Paper(paperUuid, title, file, paperAbstract, doi, authors, ownerUuid, tags);
    }

    private Tag parseTagRow(ResultSet resultSet) throws SQLException {
        System.out.println("parsing tag");

        UUID tagUuid = UUID.fromString(resultSet.getString("tag_uuid"));
        String name = resultSet.getString("tag");

        return new Tag(tagUuid, name);
    }

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

    public Author insertAuthor(Author wkgAuthor) {
        String SQL = "INSERT INTO authors (name) VALUES (?)";

        try (Connection conn = connect()) {
            PreparedStatement preparedStatement = conn.prepareStatement(SQL, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, wkgAuthor.getName());

            int affectedRows = preparedStatement.executeUpdate();
            // check the affected rows
            if (affectedRows > 0) {
                // get the ID back
                try (ResultSet rs = preparedStatement.getGeneratedKeys()) {
                    if (rs.next()) {
                        String name = rs.getString("name");

                        System.out.println(name);
                        wkgAuthor = parseAuthorRow(rs);
                    }
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return wkgAuthor;
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
                        String name = rs.getString("name");

                        System.out.println(uuid + name);
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
                    userRow.next();
                    newUser = parseUserRow(userRow);

                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return newUser;
    }


    public Paper insertPaper(Paper wkgPaper) throws SQLException {
        String paperSql = "INSERT INTO papers (title, abstract, file, doi, owner_uuid, authors_uuids) VALUES (?, ?, ?, ?, ?, ?)";
        String userSql = "UPDATE users SET papers_uuid = ";

        try (Connection conn = connect()) {
            PreparedStatement preparedStatement = conn.prepareStatement(paperSql, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, wkgPaper.getTitle());
            preparedStatement.setString(2, wkgPaper.getPaperAbstract());
            preparedStatement.setString(3, wkgPaper.getPdf().toString());
            preparedStatement.setString(4, wkgPaper.getDoi());
            preparedStatement.setObject(5, wkgPaper.getOwner());
            preparedStatement.setObject(6, wkgPaper.getAuthors().toArray());

            int affectedRows = preparedStatement.executeUpdate();
            // check the affected rows
            if (affectedRows > 0) {
                // get the ID back
                try (ResultSet rs = preparedStatement.getGeneratedKeys()) {
                    if (rs.next()) {
                        String title = rs.getString("title");
                        String file = rs.getString("file");
                        String authors = rs.getArray("authors_uuids").toString();

                        System.out.println(title + file + authors);
                        wkgPaper = parsePaperRow(rs);


                    }


                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return wkgPaper;
    }


    public Paper associateTagWithPaper(UUID paperUuid, UUID tagUuid) throws SQLException {

        String sql = "INSERT INTO paper_tags (paper_uuid, tag_uuid) VALUES (?,?)";
        Paper wkgPaper = null;

        try (Connection conn = connect()) {
            PreparedStatement preparedStatement = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setObject(1, paperUuid);
            preparedStatement.setObject(2, tagUuid);


            int affectedRows = preparedStatement.executeUpdate();
            // check the affected rows
            if (affectedRows > 0) {
                // get the ID back
                try (ResultSet rs = preparedStatement.getGeneratedKeys()) {
                    if (rs.next()) {
                        paperUuid = UUID.fromString(rs.getString("paper_uuid"));
                        tagUuid = UUID.fromString(rs.getString("tag_uuid"));
                        System.out.println(paperUuid.toString() + ' ' + tagUuid);

                        wkgPaper = getPaper(paperUuid);
                    }
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }

            }


        }
        return wkgPaper;

    }

    public Paper getPaper(UUID paperUuid) throws SQLException {

        String paperSQL = "SELECT * FROM papers WHERE paper_uuid = ?";
        Paper wkgPaper = null;

        try (Connection conn = connect()) {
            PreparedStatement preparedStatement = conn.prepareStatement(paperSQL, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setObject(1, paperUuid);

            int affectedRows = preparedStatement.executeUpdate();
            // check the affected rows
            if (affectedRows > 0) {
                // get the ID back
                try (ResultSet rs = preparedStatement.getGeneratedKeys()) {
                    if (rs.next()) {
                        wkgPaper = parsePaperRow(rs);
                        paperUuid = UUID.fromString(rs.getString("paper_uuid"));

                        System.out.println(paperUuid);
                    }
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }

            }

            return wkgPaper;
        }

    }


    public Paper associateAuthorWithPaper(UUID paperUuid, UUID authorUuid) throws SQLException {

        String sql = "INSERT INTO paper_authors (paper_uuid, author_uuid) VALUES (?,?)";
        Paper wkgPaper = null;

        try (Connection conn = connect()) {
            PreparedStatement preparedStatement = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setObject(1, paperUuid);
            preparedStatement.setObject(2, authorUuid);

            int affectedRows = preparedStatement.executeUpdate();
            // check the affected rows
            if (affectedRows > 0) {
                // get the ID back
                try (ResultSet rs = preparedStatement.getGeneratedKeys()) {
                    if (rs.next()) {
                        paperUuid = UUID.fromString(rs.getString("paper_uuid"));
                        authorUuid = UUID.fromString(rs.getString("author_uuid"));
                        System.out.println(paperUuid.toString() + ' ' + authorUuid);

                        wkgPaper = getPaper(paperUuid);
                    }
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }
        return wkgPaper;
    }

    public void addAssociation(String sql, UUID uuid1, UUID uuid2) throws SQLException {
        try (Connection conn = connect()) {
            PreparedStatement preparedStatement = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setObject(1, uuid1);
            preparedStatement.setObject(2, uuid2);

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows > 0) {

            }
        }
    }

}
