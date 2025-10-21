package Model;

import java.sql.*;
import java.util.ArrayList;

public class TournamentDAO {
    /**
     * Retrieves all tournaments from the database
     * @return List of all tournaments
     */
    public static ArrayList<Tournament> doRetriveAll() {
        ArrayList<Tournament> tournaments = new ArrayList<>();

        try (Connection conn = ConnectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM tournaments")) {

            while (rs.next()) {
                Tournament tournament = new Tournament();
                tournament.setId(rs.getInt("id"));
                tournament.setName(rs.getString("name"));
                tournament.setType(rs.getString("type"));
                tournament.setTrophy(rs.getString("trophy"));
                tournament.setTeamCount(rs.getInt("team_count"));
                tournament.setMatchCount(rs.getInt("match_count"));
                tournament.setStartDate(rs.getDate("start_date"));
                tournament.setEndDate(rs.getDate("end_date"));
                tournament.setLocation(rs.getString("location"));
                tournament.setDescription(rs.getString("description"));
                tournament.setCategory(rs.getString("category"));
                tournament.setStatus(rs.getString("status"));

                tournaments.add(tournament);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tournaments;
    }

    /**
     * Retrieves a tournament by its ID
     * @param id The tournament ID
     * @return The tournament object or null if not found
     */
    public static Tournament doRetriveById(int id) {
        Tournament tournament = null;

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM tournaments WHERE id = ?")) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    tournament = new Tournament();
                    tournament.setId(rs.getInt("id"));
                    tournament.setName(rs.getString("name"));
                    tournament.setType(rs.getString("type"));
                    tournament.setTrophy(rs.getString("trophy"));
                    tournament.setTeamCount(rs.getInt("team_count"));
                    tournament.setMatchCount(rs.getInt("match_count"));
                    tournament.setStartDate(rs.getDate("start_date"));
                    tournament.setEndDate(rs.getDate("end_date"));
                    tournament.setLocation(rs.getString("location"));
                    tournament.setDescription(rs.getString("description"));
                    tournament.setCategory(rs.getString("category"));
                    tournament.setStatus(rs.getString("status"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tournament;
    }

    /**
     * Retrieves tournaments by type
     * @param type The tournament type
     * @return List of tournaments of the specified type
     */
    public static ArrayList<Tournament> doRetriveByType(String type) {
        ArrayList<Tournament> tournaments = new ArrayList<>();

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM tournaments WHERE type = ?")) {

            stmt.setString(1, type);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Tournament tournament = new Tournament();
                    tournament.setId(rs.getInt("id"));
                    tournament.setName(rs.getString("name"));
                    tournament.setType(rs.getString("type"));
                    tournament.setTrophy(rs.getString("trophy"));
                    tournament.setTeamCount(rs.getInt("team_count"));
                    tournament.setMatchCount(rs.getInt("match_count"));
                    tournament.setStartDate(rs.getDate("start_date"));
                    tournament.setEndDate(rs.getDate("end_date"));
                    tournament.setLocation(rs.getString("location"));
                    tournament.setDescription(rs.getString("description"));
                    tournament.setCategory(rs.getString("category"));
                    tournament.setStatus(rs.getString("status"));

                    tournaments.add(tournament);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tournaments;
    }

    /**
     * Inserts a new tournament into the database
     * @param tournament The tournament to insert
     * @return true if successful, false otherwise
     */
    public static boolean doSave(Tournament tournament) {
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO tournaments (name, type, trophy, team_count, match_count, start_date, end_date, location, description, category, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                     Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, tournament.getName());
            stmt.setString(2, tournament.getType());
            stmt.setString(3, tournament.getTrophy());
            stmt.setInt(4, tournament.getTeamCount());
            stmt.setInt(5, tournament.getMatchCount());
            stmt.setDate(6, new java.sql.Date(tournament.getStartDate().getTime()));
            stmt.setDate(7, new java.sql.Date(tournament.getEndDate().getTime()));
            stmt.setString(8, tournament.getLocation());
            stmt.setString(9, tournament.getDescription());
            stmt.setString(10, tournament.getCategory());
            stmt.setString(11, tournament.getStatus());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        tournament.setId(generatedKeys.getInt(1));
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
     * Updates an existing tournament in the database
     * @param tournament The tournament to update
     * @return true if successful, false otherwise
     */
    public static boolean doUpdate(Tournament tournament) {
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "UPDATE tournaments SET name = ?, type = ?, trophy = ?, team_count = ?, match_count = ?, start_date = ?, end_date = ?, location = ?, description = ?, category = ?, status = ? WHERE id = ?")) {

            stmt.setString(1, tournament.getName());
            stmt.setString(2, tournament.getType());
            stmt.setString(3, tournament.getTrophy());
            stmt.setInt(4, tournament.getTeamCount());
            stmt.setInt(5, tournament.getMatchCount());
            stmt.setDate(6, new java.sql.Date(tournament.getStartDate().getTime()));
            stmt.setDate(7, new java.sql.Date(tournament.getEndDate().getTime()));
            stmt.setString(8, tournament.getLocation());
            stmt.setString(9, tournament.getDescription());
            stmt.setString(10, tournament.getCategory());
            stmt.setString(11, tournament.getStatus());
            stmt.setInt(12, tournament.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Deletes a tournament from the database
     * @param id The ID of the tournament to delete
     * @return true if successful, false otherwise
     */
    public static boolean doDelete(int id) {
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM tournaments WHERE id = ?")) {

            stmt.setInt(1, id);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}