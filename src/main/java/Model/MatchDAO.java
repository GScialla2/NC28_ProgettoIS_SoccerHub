package Model;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MatchDAO {
    // Holds the last SQL error message encountered during DAO operations (best-effort)
    private static volatile String LAST_ERROR_MESSAGE = null;

    public static String getLastError() {
        return LAST_ERROR_MESSAGE;
    }

    private static void clearLastError() {
        LAST_ERROR_MESSAGE = null;
    }

    private static void setLastError(SQLException e) {
        if (e != null) {
            LAST_ERROR_MESSAGE = "SQLState=" + e.getSQLState() + ", ErrorCode=" + e.getErrorCode() + ", Message=" + e.getMessage();
        }
    }
    /**
     * Retrieves all matches from the database
     * @return List of all matches
     */
    public static ArrayList<Match> doRetriveAll() {
        ArrayList<Match> matches = new ArrayList<>();

        try (Connection conn = ConnectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM matches")) {

            while (rs.next()) {
                Match match = new Match();
                match.setId(rs.getInt("id"));
                match.setTournamentId(rs.getInt("tournament_id"));
                int createdBy = rs.getInt("created_by");
                match.setCreatedBy(rs.wasNull() ? null : createdBy);
                match.setHomeTeam(rs.getString("home_team"));
                match.setAwayTeam(rs.getString("away_team"));
                Timestamp ts = rs.getTimestamp("match_date");
                if (ts != null) match.setMatchDate(new Date(ts.getTime()));
                match.setLocation(rs.getString("location"));
                match.setCategory(rs.getString("category"));
                match.setType(rs.getString("type"));
                match.setStatus(rs.getString("status"));
                match.setHomeScore(rs.getInt("home_score"));
                match.setAwayScore(rs.getInt("away_score"));

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

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM matches WHERE id = ?")) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    match = new Match();
                    match.setId(rs.getInt("id"));
                    match.setTournamentId(rs.getInt("tournament_id"));
                    int createdBy = rs.getInt("created_by");
                    match.setCreatedBy(rs.wasNull() ? null : createdBy);
                    match.setHomeTeam(rs.getString("home_team"));
                    match.setAwayTeam(rs.getString("away_team"));
                    Timestamp ts = rs.getTimestamp("match_date");
                    if (ts != null) match.setMatchDate(new Date(ts.getTime()));
                    match.setLocation(rs.getString("location"));
                    match.setCategory(rs.getString("category"));
                    match.setType(rs.getString("type"));
                    match.setStatus(rs.getString("status"));
                    match.setHomeScore(rs.getInt("home_score"));
                    match.setAwayScore(rs.getInt("away_score"));
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

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM matches WHERE type = ?")) {

            stmt.setString(1, type);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Match match = new Match();
                    match.setId(rs.getInt("id"));
                    match.setTournamentId(rs.getInt("tournament_id"));
                    int createdBy = rs.getInt("created_by");
                    match.setCreatedBy(rs.wasNull() ? null : createdBy);
                    match.setHomeTeam(rs.getString("home_team"));
                    match.setAwayTeam(rs.getString("away_team"));
                    Timestamp ts = rs.getTimestamp("match_date");
                    if (ts != null) match.setMatchDate(new Date(ts.getTime()));
                    match.setLocation(rs.getString("location"));
                    match.setCategory(rs.getString("category"));
                    match.setType(rs.getString("type"));
                    match.setStatus(rs.getString("status"));
                    match.setHomeScore(rs.getInt("home_score"));
                    match.setAwayScore(rs.getInt("away_score"));

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

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM matches WHERE category = ?")) {

            stmt.setString(1, category);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Match match = new Match();
                    match.setId(rs.getInt("id"));
                    match.setTournamentId(rs.getInt("tournament_id"));
                    int createdBy = rs.getInt("created_by");
                    match.setCreatedBy(rs.wasNull() ? null : createdBy);
                    match.setHomeTeam(rs.getString("home_team"));
                    match.setAwayTeam(rs.getString("away_team"));
                    Timestamp ts = rs.getTimestamp("match_date");
                    if (ts != null) match.setMatchDate(new Date(ts.getTime()));
                    match.setLocation(rs.getString("location"));
                    match.setCategory(rs.getString("category"));
                    match.setType(rs.getString("type"));
                    match.setStatus(rs.getString("status"));
                    match.setHomeScore(rs.getInt("home_score"));
                    match.setAwayScore(rs.getInt("away_score"));

                    matches.add(match);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return matches;
    }

    /**
     * Retrieves matches by category and type
     * @param category The match category
     * @param type The match type
     * @return List of matches of the specified category and type
     */
    public static ArrayList<Match> doRetriveByCategoriaTipo(String category, String type) {
        ArrayList<Match> matches = new ArrayList<>();

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM matches WHERE category = ? AND type = ?")) {

            stmt.setString(1, category);
            stmt.setString(2, type);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Match match = new Match();
                    match.setId(rs.getInt("id"));
                    match.setTournamentId(rs.getInt("tournament_id"));
                    int createdBy = rs.getInt("created_by");
                    match.setCreatedBy(rs.wasNull() ? null : createdBy);
                    match.setHomeTeam(rs.getString("home_team"));
                    match.setAwayTeam(rs.getString("away_team"));
                    Timestamp ts = rs.getTimestamp("match_date");
                    if (ts != null) match.setMatchDate(new Date(ts.getTime()));
                    match.setLocation(rs.getString("location"));
                    match.setCategory(rs.getString("category"));
                    match.setType(rs.getString("type"));
                    match.setStatus(rs.getString("status"));
                    match.setHomeScore(rs.getInt("home_score"));
                    match.setAwayScore(rs.getInt("away_score"));

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
        clearLastError();
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO matches (tournament_id, created_by, home_team, away_team, match_date, location, category, type, status, home_score, away_score) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                     Statement.RETURN_GENERATED_KEYS)) {

            if (match.getTournamentId() > 0) {
                stmt.setInt(1, match.getTournamentId());
            } else {
                stmt.setNull(1, java.sql.Types.INTEGER);
            }
            if (match.getCreatedBy() != null) {
                stmt.setInt(2, match.getCreatedBy());
            } else {
                stmt.setNull(2, java.sql.Types.INTEGER);
            }
            stmt.setString(3, match.getHomeTeam());
            stmt.setString(4, match.getAwayTeam());
            stmt.setTimestamp(5, new java.sql.Timestamp(match.getMatchDate().getTime()));
            stmt.setString(6, match.getLocation());
            stmt.setString(7, match.getCategory());
            stmt.setString(8, match.getType());
            stmt.setString(9, match.getStatus());
            stmt.setInt(10, match.getHomeScore());
            stmt.setInt(11, match.getAwayScore());

            int affectedRows = stmt.executeUpdate();

            // Consider the operation successful if at least one row was inserted
            // Try to set the generated id if the driver returns it, but don't fail if it doesn't
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys != null && generatedKeys.next()) {
                        match.setId(generatedKeys.getInt(1));
                    }
                } catch (SQLException ignore) {
                    // Some drivers may not support getGeneratedKeys properly; ignore
                }
                return true;
            }
        } catch (SQLException e) {
            setLastError(e);
            System.err.println("[MatchDAO.doSave] SQL error while inserting match: SQLState=" + e.getSQLState() + ", ErrorCode=" + e.getErrorCode() + ", Message=" + e.getMessage());
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
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "UPDATE matches SET tournament_id = ?, home_team = ?, away_team = ?, match_date = ?, location = ?, category = ?, type = ?, status = ?, home_score = ?, away_score = ? WHERE id = ?")) {

            // Ensure NULL is stored when there is no tournament association (<= 0), to respect FK constraints
            if (match.getTournamentId() > 0) {
                stmt.setInt(1, match.getTournamentId());
            } else {
                stmt.setNull(1, java.sql.Types.INTEGER);
            }
            stmt.setString(2, match.getHomeTeam());
            stmt.setString(3, match.getAwayTeam());
            stmt.setTimestamp(4, new java.sql.Timestamp(match.getMatchDate().getTime()));
            stmt.setString(5, match.getLocation());
            stmt.setString(6, match.getCategory());
            stmt.setString(7, match.getType());
            stmt.setString(8, match.getStatus());
            stmt.setInt(9, match.getHomeScore());
            stmt.setInt(10, match.getAwayScore());
            stmt.setInt(11, match.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Retrieves matches created by a specific user (coach)
     * @param creatorId The creator user ID
     * @return list of matches created by the user
     */
    public static ArrayList<Match> doRetriveByCreator(int creatorId) {
        ArrayList<Match> matches = new ArrayList<>();
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM matches WHERE created_by = ?")) {
            stmt.setInt(1, creatorId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Match match = new Match();
                    match.setId(rs.getInt("id"));
                    match.setTournamentId(rs.getInt("tournament_id"));
                    int createdBy = rs.getInt("created_by");
                    match.setCreatedBy(rs.wasNull() ? null : createdBy);
                    match.setHomeTeam(rs.getString("home_team"));
                    match.setAwayTeam(rs.getString("away_team"));
                    Timestamp ts = rs.getTimestamp("match_date");
                    if (ts != null) match.setMatchDate(new Date(ts.getTime()));
                    match.setLocation(rs.getString("location"));
                    match.setCategory(rs.getString("category"));
                    match.setType(rs.getString("type"));
                    match.setStatus(rs.getString("status"));
                    match.setHomeScore(rs.getInt("home_score"));
                    match.setAwayScore(rs.getInt("away_score"));
                    matches.add(match);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return matches;
    }

    public static ArrayList<Match> doRetriveByTeamName(String teamName) {
        ArrayList<Match> matches = new ArrayList<>();
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM matches WHERE home_team = ? OR away_team = ?")) {
            stmt.setString(1, teamName);
            stmt.setString(2, teamName);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Match match = new Match();
                    match.setId(rs.getInt("id"));
                    match.setTournamentId(rs.getInt("tournament_id"));
                    int createdBy = rs.getInt("created_by");
                    match.setCreatedBy(rs.wasNull() ? null : createdBy);
                    match.setHomeTeam(rs.getString("home_team"));
                    match.setAwayTeam(rs.getString("away_team"));
                    Timestamp ts = rs.getTimestamp("match_date");
                    if (ts != null) match.setMatchDate(new Date(ts.getTime()));
                    match.setLocation(rs.getString("location"));
                    match.setCategory(rs.getString("category"));
                    match.setType(rs.getString("type"));
                    match.setStatus(rs.getString("status"));
                    match.setHomeScore(rs.getInt("home_score"));
                    match.setAwayScore(rs.getInt("away_score"));
                    matches.add(match);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return matches;
    }

    public static boolean doDelete(int id) {
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM matches WHERE id = ?")) {

            stmt.setInt(1, id);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Retrieves matches by tournament id
     * @param tournamentId tournament id
     * @return list of matches belonging to the tournament
     */
    public static ArrayList<Match> doRetriveByTournamentId(int tournamentId) {
        ArrayList<Match> matches = new ArrayList<>();
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM matches WHERE tournament_id = ?")) {
            stmt.setInt(1, tournamentId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Match match = new Match();
                    match.setId(rs.getInt("id"));
                    match.setTournamentId(rs.getInt("tournament_id"));
                    int createdBy = rs.getInt("created_by");
                    match.setCreatedBy(rs.wasNull() ? null : createdBy);
                    match.setHomeTeam(rs.getString("home_team"));
                    match.setAwayTeam(rs.getString("away_team"));
                    Timestamp ts = rs.getTimestamp("match_date");
                    if (ts != null) match.setMatchDate(new Date(ts.getTime()));
                    match.setLocation(rs.getString("location"));
                    match.setCategory(rs.getString("category"));
                    match.setType(rs.getString("type"));
                    match.setStatus(rs.getString("status"));
                    match.setHomeScore(rs.getInt("home_score"));
                    match.setAwayScore(rs.getInt("away_score"));
                    matches.add(match);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return matches;
    }
}