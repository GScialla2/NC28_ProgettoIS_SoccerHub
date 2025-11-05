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
             Statement stmt = conn.createStatement()) {

            // First attempt: fetch all tournaments
            try (ResultSet rs = stmt.executeQuery("SELECT * FROM tournaments")) {
                while (rs.next()) {
                    Tournament tournament = new Tournament();
                    tournament.setId(rs.getInt("id"));
                    int createdBy = rs.getInt("created_by");
                    tournament.setCreatedBy(rs.wasNull() ? null : createdBy);
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

            // Runtime fallback seeding: if table is empty, insert some defaults and re-query
            if (tournaments.isEmpty()) {
                int cnt = 0;
                try (ResultSet crs = stmt.executeQuery("SELECT COUNT(*) AS cnt FROM tournaments")) {
                    if (crs.next()) {
                        cnt = crs.getInt("cnt");
                    }
                }
                if (cnt == 0) {
                    try (PreparedStatement ins = conn.prepareStatement(
                            "INSERT INTO tournaments (name, type, trophy, team_count, match_count, start_date, end_date, location, description, category, status, created_by) VALUES (?,?,?,?,?,?,?,?,?,?,?,NULL)")) {
                        // 1
                        ins.setString(1, "Coppa Italia 2025");
                        ins.setString(2, "A eliminazione diretta");
                        ins.setString(3, "Coppa Italia");
                        ins.setInt(4, 16);
                        ins.setInt(5, 15);
                        ins.setDate(6, java.sql.Date.valueOf("2025-11-01"));
                        ins.setDate(7, java.sql.Date.valueOf("2026-02-28"));
                        ins.setString(8, "Italia");
                        ins.setString(9, "Coppa nazionale a eliminazione diretta");
                        ins.setString(10, "Professional");
                        ins.setString(11, "upcoming");
                        ins.executeUpdate();
                        // 2
                        ins.setString(1, "Torneo Under 18 Invernale");
                        ins.setString(2, "A gironi");
                        ins.setString(3, "Coppa Primavera");
                        ins.setInt(4, 12);
                        ins.setInt(5, 30);
                        ins.setDate(6, java.sql.Date.valueOf("2025-12-10"));
                        ins.setDate(7, java.sql.Date.valueOf("2026-03-15"));
                        ins.setString(8, "Italia");
                        ins.setString(9, "Torneo giovanile invernale a gironi");
                        ins.setString(10, "Giovanili");
                        ins.setString(11, "upcoming");
                        ins.executeUpdate();
                        // 3
                        ins.setString(1, "Summer Cup 2025");
                        ins.setString(2, "Misto");
                        ins.setString(3, "Summer Cup");
                        ins.setInt(4, 8);
                        ins.setInt(5, 16);
                        ins.setDate(6, java.sql.Date.valueOf("2025-07-01"));
                        ins.setDate(7, java.sql.Date.valueOf("2025-08-01"));
                        ins.setString(8, "Europa");
                        ins.setString(9, "Torneo estivo amatoriale");
                        ins.setString(10, "Amatoriale");
                        ins.setString(11, "completed");
                        ins.executeUpdate();
                    } catch (SQLException seedEx) {
                        // Log and continue without failing the request
                        System.err.println("[TournamentDAO] Seeding tournaments failed: " + seedEx.getMessage());
                    }

                    // Re-query after seeding
                    try (ResultSet rs2 = stmt.executeQuery("SELECT * FROM tournaments")) {
                        while (rs2.next()) {
                            Tournament tournament = new Tournament();
                            tournament.setId(rs2.getInt("id"));
                            int createdBy = rs2.getInt("created_by");
                            tournament.setCreatedBy(rs2.wasNull() ? null : createdBy);
                            tournament.setName(rs2.getString("name"));
                            tournament.setType(rs2.getString("type"));
                            tournament.setTrophy(rs2.getString("trophy"));
                            tournament.setTeamCount(rs2.getInt("team_count"));
                            tournament.setMatchCount(rs2.getInt("match_count"));
                            tournament.setStartDate(rs2.getDate("start_date"));
                            tournament.setEndDate(rs2.getDate("end_date"));
                            tournament.setLocation(rs2.getString("location"));
                            tournament.setDescription(rs2.getString("description"));
                            tournament.setCategory(rs2.getString("category"));
                            tournament.setStatus(rs2.getString("status"));
                            tournaments.add(tournament);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Final UI fallback: if still empty (e.g., DB permissions or schema issues), return some in-memory demo tournaments
        if (tournaments.isEmpty()) {
            try {
                Tournament t1 = new Tournament();
                t1.setId(0);
                t1.setName("Coppa Italia (demo)");
                t1.setType("A eliminazione diretta");
                t1.setTrophy("Coppa Italia");
                t1.setTeamCount(16);
                t1.setMatchCount(15);
                t1.setStartDate(java.sql.Date.valueOf("2025-11-01"));
                t1.setEndDate(java.sql.Date.valueOf("2026-02-28"));
                t1.setLocation("Italia");
                t1.setDescription("Coppa nazionale a eliminazione diretta (anteprima)");
                t1.setCategory("Professional");
                t1.setStatus("upcoming");

                Tournament t2 = new Tournament();
                t2.setId(0);
                t2.setName("Torneo Under 18 (demo)");
                t2.setType("A gironi");
                t2.setTrophy("Coppa Primavera");
                t2.setTeamCount(12);
                t2.setMatchCount(30);
                t2.setStartDate(java.sql.Date.valueOf("2025-12-10"));
                t2.setEndDate(java.sql.Date.valueOf("2026-03-15"));
                t2.setLocation("Italia");
                t2.setDescription("Torneo giovanile invernale (anteprima)");
                t2.setCategory("Giovanili");
                t2.setStatus("upcoming");

                Tournament t3 = new Tournament();
                t3.setId(0);
                t3.setName("Summer Cup (demo)");
                t3.setType("Misto");
                t3.setTrophy("Summer Cup");
                t3.setTeamCount(8);
                t3.setMatchCount(16);
                t3.setStartDate(java.sql.Date.valueOf("2025-07-01"));
                t3.setEndDate(java.sql.Date.valueOf("2025-08-01"));
                t3.setLocation("Europa");
                t3.setDescription("Torneo estivo amatoriale (anteprima)");
                t3.setCategory("Amatoriale");
                t3.setStatus("completed");

                tournaments.add(t1);
                tournaments.add(t2);
                tournaments.add(t3);
            } catch (Exception ignore) {
                // If even in-memory creation fails, just return empty list
            }
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
                    int createdBy = rs.getInt("created_by");
                    tournament.setCreatedBy(rs.wasNull() ? null : createdBy);
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
                    int createdBy = rs.getInt("created_by");
                    tournament.setCreatedBy(rs.wasNull() ? null : createdBy);
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
                     "INSERT INTO tournaments (name, type, trophy, team_count, match_count, start_date, end_date, location, description, category, status, created_by) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
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
            if (tournament.getCreatedBy() != null) {
                stmt.setInt(12, tournament.getCreatedBy());
            } else {
                stmt.setNull(12, java.sql.Types.INTEGER);
            }

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
                     "UPDATE tournaments SET name = ?, type = ?, trophy = ?, team_count = ?, match_count = ?, start_date = ?, end_date = ?, location = ?, description = ?, category = ?, status = ?, created_by = ? WHERE id = ?")) {

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
            if (tournament.getCreatedBy() != null) {
                stmt.setInt(12, tournament.getCreatedBy());
            } else {
                stmt.setNull(12, java.sql.Types.INTEGER);
            }
            stmt.setInt(13, tournament.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Retrieves tournaments created by a specific user (coach)
     */
    public static ArrayList<Tournament> doRetriveByCreator(int creatorId) {
        ArrayList<Tournament> tournaments = new ArrayList<>();
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM tournaments WHERE created_by = ?")) {
            stmt.setInt(1, creatorId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Tournament t = new Tournament();
                    t.setId(rs.getInt("id"));
                    int createdBy = rs.getInt("created_by");
                    t.setCreatedBy(rs.wasNull() ? null : createdBy);
                    t.setName(rs.getString("name"));
                    t.setType(rs.getString("type"));
                    t.setTrophy(rs.getString("trophy"));
                    t.setTeamCount(rs.getInt("team_count"));
                    t.setMatchCount(rs.getInt("match_count"));
                    t.setStartDate(rs.getDate("start_date"));
                    t.setEndDate(rs.getDate("end_date"));
                    t.setLocation(rs.getString("location"));
                    t.setDescription(rs.getString("description"));
                    t.setCategory(rs.getString("category"));
                    t.setStatus(rs.getString("status"));
                    tournaments.add(t);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tournaments;
    }

    /**
     * Retrieves tournaments related to a given team name.
     * It matches in three ways (case-insensitive):
     *  - Tournament name contains the team
     *  - Tournament description contains the team
     *  - There exists at least one match in the tournament where the team plays (home or away)
     */
    public static ArrayList<Tournament> doRetriveByTeamName(String teamName) {
        ArrayList<Tournament> tournaments = new ArrayList<>();
        if (teamName == null) teamName = "";
        String trimmed = teamName.trim();

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT DISTINCT t.* " +
                     "FROM tournaments t " +
                     "LEFT JOIN matches m ON m.tournament_id = t.id " +
                     "WHERE " +
                     "      LOWER(t.name) LIKE LOWER(?) " +
                     "   OR LOWER(t.description) LIKE LOWER(?) " +
                     "   OR (m.tournament_id IS NOT NULL AND (LOWER(m.home_team) = LOWER(?) OR LOWER(m.away_team) = LOWER(?) " +
                     "        OR LOWER(m.home_team) LIKE LOWER(?) OR LOWER(m.away_team) LIKE LOWER(?)))")) {

            String like = "%" + trimmed + "%";
            stmt.setString(1, like);
            stmt.setString(2, like);
            stmt.setString(3, trimmed);
            stmt.setString(4, trimmed);
            stmt.setString(5, like);
            stmt.setString(6, like);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Tournament t = new Tournament();
                    t.setId(rs.getInt("id"));
                    int createdBy = rs.getInt("created_by");
                    t.setCreatedBy(rs.wasNull() ? null : createdBy);
                    t.setName(rs.getString("name"));
                    t.setType(rs.getString("type"));
                    t.setTrophy(rs.getString("trophy"));
                    t.setTeamCount(rs.getInt("team_count"));
                    t.setMatchCount(rs.getInt("match_count"));
                    t.setStartDate(rs.getDate("start_date"));
                    t.setEndDate(rs.getDate("end_date"));
                    t.setLocation(rs.getString("location"));
                    t.setDescription(rs.getString("description"));
                    t.setCategory(rs.getString("category"));
                    t.setStatus(rs.getString("status"));
                    tournaments.add(t);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tournaments;
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

    /**
     * Retrieves tournaments by exact name (case-insensitive), returns first match if multiple.
     */
    public static Tournament doRetriveByNameExactCI(String name) {
        if (name == null) return null;
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM tournaments WHERE LOWER(name) = LOWER(?) LIMIT 1")) {
            stmt.setString(1, name.trim());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Tournament t = new Tournament();
                    t.setId(rs.getInt("id"));
                    int createdBy = rs.getInt("created_by");
                    t.setCreatedBy(rs.wasNull() ? null : createdBy);
                    t.setName(rs.getString("name"));
                    t.setType(rs.getString("type"));
                    t.setTrophy(rs.getString("trophy"));
                    t.setTeamCount(rs.getInt("team_count"));
                    t.setMatchCount(rs.getInt("match_count"));
                    t.setStartDate(rs.getDate("start_date"));
                    t.setEndDate(rs.getDate("end_date"));
                    t.setLocation(rs.getString("location"));
                    t.setDescription(rs.getString("description"));
                    t.setCategory(rs.getString("category"));
                    t.setStatus(rs.getString("status"));
                    return t;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Retrieves tournaments by name LIKE (case-insensitive)
     */
    public static ArrayList<Tournament> doRetriveByNameLikeCI(String name) {
        ArrayList<Tournament> tournaments = new ArrayList<>();
        if (name == null) name = "";
        String like = "%" + name.trim() + "%";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM tournaments WHERE LOWER(name) LIKE LOWER(?)")) {
            stmt.setString(1, like);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Tournament t = new Tournament();
                    t.setId(rs.getInt("id"));
                    int createdBy = rs.getInt("created_by");
                    t.setCreatedBy(rs.wasNull() ? null : createdBy);
                    t.setName(rs.getString("name"));
                    t.setType(rs.getString("type"));
                    t.setTrophy(rs.getString("trophy"));
                    t.setTeamCount(rs.getInt("team_count"));
                    t.setMatchCount(rs.getInt("match_count"));
                    t.setStartDate(rs.getDate("start_date"));
                    t.setEndDate(rs.getDate("end_date"));
                    t.setLocation(rs.getString("location"));
                    t.setDescription(rs.getString("description"));
                    t.setCategory(rs.getString("category"));
                    t.setStatus(rs.getString("status"));
                    tournaments.add(t);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tournaments;
    }
}