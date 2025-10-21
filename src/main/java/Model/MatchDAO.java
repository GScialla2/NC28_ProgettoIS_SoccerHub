package Model;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MatchDAO {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/soccerhub";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "password";

    /**
     * Retrieves all matches from the database
     * @return List of all matches
     */
    public static ArrayList<Match> doRetriveAll() {
        ArrayList<Match> matches = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM matches")) {

            while (rs.next()) {
                Match match = new Match();
                match.setId(rs.getInt("id"));
                match.setOpponent(rs.getString("opponent"));
                match.setMatchDate(rs.getDate("match_date"));
                match.setMatchTime(rs.getString("match_time"));
                match.setType(rs.getString("type"));
                match.setStadium(rs.getString("stadium"));

                matches.add(match);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return matches;
    }

    /**
     * Retrieves a match by its ID
     * @param id The match ID
     * @return The match object or null if not found
     */
    public static Match doRetriveById(int id) {
        Match match = null;

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM matches WHERE id = ?")) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    match = new Match();
                    match.setId(rs.getInt("id"));
                    match.setOpponent(rs.getString("opponent"));
                    match.setMatchDate(rs.getDate("match_date"));
                    match.setMatchTime(rs.getString("match_time"));
                    match.setType(rs.getString("type"));
                    match.setStadium(rs.getString("stadium"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return match;
    }

    /**
     * Retrieves matches by type
     * @param type The match type
     * @return List of matches of the specified type
     */
    public static ArrayList<Match> doRetriveByType(String type) {
        ArrayList<Match> matches = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM matches WHERE type = ?")) {

            stmt.setString(1, type);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Match match = new Match();
                    match.setId(rs.getInt("id"));
                    match.setOpponent(rs.getString("opponent"));
                    match.setMatchDate(rs.getDate("match_date"));
                    match.setMatchTime(rs.getString("match_time"));
                    match.setType(rs.getString("type"));
                    match.setStadium(rs.getString("stadium"));

                    matches.add(match);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return matches;
    }

    /**
     * Retrieves matches by category
     * @param category The match category
     * @return List of matches of the specified category
     */
    public static ArrayList<Match> doRetriveByCategoria(String category) {
        ArrayList<Match> matches = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM matches WHERE category = ?")) {

            stmt.setString(1, category);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Match match = new Match();
                    match.setId(rs.getInt("id"));
                    match.setOpponent(rs.getString("opponent"));
                    match.setMatchDate(rs.getDate("match_date"));
                    match.setMatchTime(rs.getString("match_time"));
                    match.setType(rs.getString("type"));
                    match.setStadium(rs.getString("stadium"));

                    matches.add(match);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return matches;
    }

    /**
     * Retrieves matches by type and category
     * @param category The match category
     * @param type The match type
     * @return List of matches of the specified category and type
     */
    public static ArrayList<Match> doRetriveByCategoriaTipo(String category, String type) {
        ArrayList<Match> matches = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM matches WHERE category = ? AND type = ?")) {

            stmt.setString(1, category);
            stmt.setString(2, type);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Match match = new Match();
                    match.setId(rs.getInt("id"));
                    match.setOpponent(rs.getString("opponent"));
                    match.setMatchDate(rs.getDate("match_date"));
                    match.setMatchTime(rs.getString("match_time"));
                    match.setType(rs.getString("type"));
                    match.setStadium(rs.getString("stadium"));

                    matches.add(match);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return matches;
    }

    /**
     * Inserts a new match into the database
     * @param match The match to insert
     * @return true if successful, false otherwise
     */
    public static boolean doSave(Match match) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO matches (opponent, match_date, match_time, type, stadium) VALUES (?, ?, ?, ?, ?)",
                     Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, match.getOpponent());
            stmt.setDate(2, new java.sql.Date(match.getMatchDate().getTime()));
            stmt.setString(3, match.getMatchTime());
            stmt.setString(4, match.getType());
            stmt.setString(5, match.getStadium());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        match.setId(generatedKeys.getInt(1));
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Updates an existing match in the database
     * @param match The match to update
     * @return true if successful, false otherwise
     */
    public static boolean doUpdate(Match match) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(
                     "UPDATE matches SET opponent = ?, match_date = ?, match_time = ?, type = ?, stadium = ? WHERE id = ?")) {

            stmt.setString(1, match.getOpponent());
            stmt.setDate(2, new java.sql.Date(match.getMatchDate().getTime()));
            stmt.setString(3, match.getMatchTime());
            stmt.setString(4, match.getType());
            stmt.setString(5, match.getStadium());
            stmt.setInt(6, match.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Deletes a match from the database
     * @param id The ID of the match to delete
     * @return true if successful, false otherwise
     */
    public static boolean doDelete(int id) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM matches WHERE id = ?")) {

            stmt.setInt(1, id);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}