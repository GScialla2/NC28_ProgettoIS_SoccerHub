package Model;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Base64;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UserDAO
{
    /**
     * Returns the user_type for a given email (coach, player, fan, user) or null if not found
     */
    public static String getUserTypeByEmail(String email) {
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT user_type FROM users WHERE email = ?")) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("user_type");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Retrieves email and hashed password for all users (admin use only)
     * Note: returns hashed passwords as stored in DB; plaintext cannot be retrieved.
     */
    public static ArrayList<String[]> doRetrieveAllCredentials() {
        ArrayList<String[]> creds = new ArrayList<>();
        try (Connection conn = ConnectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT email, password FROM users ORDER BY email")) {
            while (rs.next()) {
                creds.add(new String[]{rs.getString("email"), rs.getString("password")});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return creds;
    }

    /**
     * Retrieves all users from the database
     * @return List of all users
     */
    public static ArrayList<User> doRetriveAll() {
        ArrayList<User> users = new ArrayList<>();

        try (Connection conn = ConnectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM users")) {

            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setSurname(rs.getString("surname"));
                user.setEmail(rs.getString("email"));
                // We don't set the password directly as it's already hashed in the database
                user.setBirthDate(rs.getDate("birth_date"));
                user.setNationality(rs.getString("nationality"));

                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    /**
     * Retrieves a user by ID
     * @param id The user ID
     * @return The user object or null if not found
     */
    public static User doRetriveById(int id) {
        User user = null;

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE id = ?")) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    user = new User();
                    user.setId(rs.getInt("id"));
                    user.setName(rs.getString("name"));
                    user.setSurname(rs.getString("surname"));
                    user.setEmail(rs.getString("email"));
                    // We don't set the password directly as it's already hashed in the database
                    user.setBirthDate(rs.getDate("birth_date"));
                    user.setNationality(rs.getString("nationality"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

    /**
     * Retrieves a user by email
     * @param email The user email
     * @return The user object or null if not found
     */
    public static User doRetriveByEmail(String email) {
        User user = null;

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE email = ?")) {

            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    user = new User();
                    user.setId(rs.getInt("id"));
                    user.setName(rs.getString("name"));
                    user.setSurname(rs.getString("surname"));
                    user.setEmail(rs.getString("email"));
                    // We don't set the password directly as it's already hashed in the database
                    user.setBirthDate(rs.getDate("birth_date"));
                    user.setNationality(rs.getString("nationality"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

    /**
     * Authenticates a user with email and password
     * @param email The user email
     * @param password The user password (will be hashed before comparison)
     * @return The user object if authentication is successful, null otherwise
     */
    public static User doAuthenticate(String email, String password) {
        User user = null;

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE email = ?")) {

            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String storedHash = rs.getString("password");
                    String storedSalt = rs.getString("salt");

                    boolean passwordMatches = false;

                    // Primary: SHA-256(salt + password) Base64 as defined in Model.User
                    if (storedHash != null && storedSalt != null) {
                        try {
                            byte[] saltBytes = Base64.getDecoder().decode(storedSalt);
                            MessageDigest digest = MessageDigest.getInstance("SHA-256");
                            digest.reset();
                            digest.update(saltBytes);
                            digest.update(password.getBytes(StandardCharsets.UTF_8));
                            String hashedInput = Base64.getEncoder().encodeToString(digest.digest());
                            passwordMatches = storedHash.equals(hashedInput);
                        } catch (IllegalArgumentException | NoSuchAlgorithmException ex) {
                            // Fallback checks below may still work
                            passwordMatches = false;
                        }
                    }

                    // Backward-compatibility: check legacy formats from seed data
                    if (!passwordMatches) {
                        String base64Plain = Base64.getEncoder().encodeToString(password.getBytes(StandardCharsets.UTF_8));
                        if (storedHash != null && (storedHash.equals(base64Plain) || storedHash.equals(password))) {
                            passwordMatches = true;
                        }
                    }

                    if (passwordMatches) {
                        int userId = rs.getInt("id");
                        String userType = rs.getString("user_type");

                        // Create the appropriate user type based on the user_type field
                        if ("coach".equals(userType)) {
                            user = getCoachById(userId);
                        } else if ("player".equals(userType)) {
                            user = getPlayerById(userId);
                        } else if ("fan".equals(userType)) {
                            user = getFanById(userId);
                        }

                        // Fallback: if specialized record not found, return base user from current row
                        if (user == null) {
                            user = new User();
                            user.setId(userId);
                            user.setName(rs.getString("name"));
                            user.setSurname(rs.getString("surname"));
                            user.setEmail(rs.getString("email"));
                            user.setBirthDate(rs.getDate("birth_date"));
                            user.setNationality(rs.getString("nationality"));
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }
    
    /**
     * Retrieves a coach by ID
     * @param id The coach ID
     * @return The coach object or null if not found
     */
    private static Coach getCoachById(int id) {
        Coach coach = null;
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT u.*, c.license_number, c.experience_years, c.specialization, c.team_name " +
                     "FROM users u JOIN coach c ON u.id = c.id " +
                     "WHERE u.id = ?")) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    coach = new Coach();
                    coach.setId(rs.getInt("id"));
                    coach.setName(rs.getString("name"));
                    coach.setSurname(rs.getString("surname"));
                    coach.setEmail(rs.getString("email"));
                    coach.setBirthDate(rs.getDate("birth_date"));
                    coach.setNationality(rs.getString("nationality"));
                    coach.setLicenseNumber(rs.getString("license_number"));
                    coach.setExperienceYears(rs.getInt("experience_years"));
                    coach.setSpecialization(rs.getString("specialization"));
                    coach.setTeamName(rs.getString("team_name"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return coach;
    }
    
    /**
     * Retrieves a player by ID
     * @param id The player ID
     * @return The player object or null if not found
     */
    private static Player getPlayerById(int id) {
        Player player = null;
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT u.*, p.position, p.height, p.weight, p.preferred_foot, p.team_name " +
                     "FROM users u JOIN player p ON u.id = p.id " +
                     "WHERE u.id = ?")) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    player = new Player();
                    player.setId(rs.getInt("id"));
                    player.setName(rs.getString("name"));
                    player.setSurname(rs.getString("surname"));
                    player.setEmail(rs.getString("email"));
                    player.setBirthDate(rs.getDate("birth_date"));
                    player.setNationality(rs.getString("nationality"));
                    player.setPosition(rs.getString("position"));
                    player.setHeight(rs.getDouble("height"));
                    player.setWeight(rs.getDouble("weight"));
                    player.setPreferredFoot(rs.getString("preferred_foot"));
                    player.setTeamName(rs.getString("team_name"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return player;
    }
    
    /**
     * Retrieves a fan by ID
     * @param id The fan ID
     * @return The fan object or null if not found
     */
    private static Fan getFanById(int id) {
        Fan fan = null;
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT u.*, f.favorite_team, f.membership_level " +
                     "FROM users u JOIN fan f ON u.id = f.id " +
                     "WHERE u.id = ?")) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    fan = new Fan();
                    fan.setId(rs.getInt("id"));
                    fan.setName(rs.getString("name"));
                    fan.setSurname(rs.getString("surname"));
                    fan.setEmail(rs.getString("email"));
                    fan.setBirthDate(rs.getDate("birth_date"));
                    fan.setNationality(rs.getString("nationality"));
                    fan.setFavoriteTeam(rs.getString("favorite_team"));
                    fan.setMembershipLevel(rs.getString("membership_level"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return fan;
    }

    /**
     * Inserts a new user into the database
     * @param user The user to insert
     * @return true if successful, false otherwise
     */
    public static boolean doSave(User user) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = ConnectionManager.getConnection();
            conn.setAutoCommit(false);
            stmt = conn.prepareStatement(
                    "INSERT INTO users (name, surname, email, password, salt, birth_date, nationality, user_type) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);

            stmt.setString(1, user.getName());
            stmt.setString(2, user.getSurname());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getPassword());
            stmt.setString(5, user.getSalt());
            stmt.setDate(6, new java.sql.Date(user.getBirthDate().getTime()));
            stmt.setString(7, user.getNationality());
            
            // Determine user type
            String userType = "user";
            if (user instanceof Coach) {
                userType = "coach";
            } else if (user instanceof Player) {
                userType = "player";
            } else if (user instanceof Fan) {
                userType = "fan";
            }
            stmt.setString(8, userType);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int newUserId = generatedKeys.getInt(1);
                        user.setId(newUserId);

                        // Insert into role-specific table when applicable
                        if (user instanceof Player) {
                            Player p = (Player) user;
                            try (PreparedStatement ps = conn.prepareStatement(
                                    "INSERT INTO player (id, position, height, weight, preferred_foot, team_name) VALUES (?, ?, ?, ?, ?, ?)")) {
                                ps.setInt(1, newUserId);
                                ps.setString(2, p.getPosition());
                                if (p.getHeight() > 0) {
                                    ps.setDouble(3, p.getHeight());
                                } else {
                                    ps.setNull(3, Types.DECIMAL);
                                }
                                if (p.getWeight() > 0) {
                                    ps.setDouble(4, p.getWeight());
                                } else {
                                    ps.setNull(4, Types.DECIMAL);
                                }
                                ps.setString(5, p.getPreferredFoot());
                                ps.setString(6, p.getTeamName());
                                ps.executeUpdate();
                            }
                        } else if (user instanceof Fan) {
                            Fan f = (Fan) user;
                            try (PreparedStatement ps = conn.prepareStatement(
                                    "INSERT INTO fan (id, favorite_team, membership_level) VALUES (?, ?, ?)")) {
                                ps.setInt(1, newUserId);
                                ps.setString(2, f.getFavoriteTeam());
                                String membership = f.getMembershipLevel();
                                if (membership == null || membership.trim().isEmpty()) membership = "basic";
                                ps.setString(3, membership);
                                ps.executeUpdate();
                            }
                        } else if (user instanceof Coach) {
                            // Minimal insertion into coach table requires not-null fields per schema
                            // Use placeholders or default minimal values if not provided
                            Coach c = (Coach) user;
                            String licenseNumber = (c.getLicenseNumber() != null && !c.getLicenseNumber().isEmpty()) ? c.getLicenseNumber() : "PENDING-LIC-" + newUserId;
                            int expYears = (c.getExperienceYears() > 0) ? c.getExperienceYears() : 0;
                            String specialization = (c.getSpecialization() != null) ? c.getSpecialization() : null;
                            try (PreparedStatement ps = conn.prepareStatement(
                                    "INSERT INTO coach (id, license_number, experience_years, specialization, team_name) VALUES (?, ?, ?, ?, ?)")) {
                                ps.setInt(1, newUserId);
                                ps.setString(2, licenseNumber);
                                ps.setInt(3, expYears);
                                if (specialization != null) {
                                    ps.setString(4, specialization);
                                } else {
                                    ps.setNull(4, Types.VARCHAR);
                                }
                                String coachTeam = c.getTeamName();
                                if (coachTeam != null && !coachTeam.trim().isEmpty()) {
                                    ps.setString(5, coachTeam.trim());
                                } else {
                                    ps.setNull(5, Types.VARCHAR);
                                }
                                ps.executeUpdate();
                            }
                        }

                        conn.commit();
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ignore) {}
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) conn.setAutoCommit(true);
            } catch (SQLException ignore) {}
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException ignore) {}
            try {
                if (conn != null) conn.close();
            } catch (SQLException ignore) {}
        }

        return false;
    }

    /**
     * Updates an existing user in the database
     * @param user The user to update
     * @return true if successful, false otherwise
     */
    public static boolean doUpdate(User user) {
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "UPDATE users SET name = ?, surname = ?, email = ?, birth_date = ?, nationality = ? WHERE id = ?")) {

            stmt.setString(1, user.getName());
            stmt.setString(2, user.getSurname());
            stmt.setString(3, user.getEmail());
            stmt.setDate(4, new java.sql.Date(user.getBirthDate().getTime()));
            stmt.setString(5, user.getNationality());
            stmt.setInt(6, user.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Updates a user's password
     * @param userId The user ID
     * @param newPassword The new password (will be hashed before storage)
     * @return true if successful, false otherwise
     */
    public static boolean doUpdatePassword(int userId, String newPassword) {
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement("UPDATE users SET password = ?, salt = ? WHERE id = ?")) {

            // Create a temporary user to hash the password (generates new salt)
            User tempUser = new User();
            tempUser.setPassword(newPassword);

            stmt.setString(1, tempUser.getPassword());
            stmt.setString(2, tempUser.getSalt());
            stmt.setInt(3, userId);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Deletes a user from the database
     * @param id The ID of the user to delete
     * @return true if successful, false otherwise
     */
    public static boolean doDelete(int id) {
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM users WHERE id = ?")) {

            stmt.setInt(1, id);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Updates coach-specific details
     */
    public static boolean updateCoachDetails(int id, String licenseNumber, Integer experienceYears, String specialization) {
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement("UPDATE coach SET license_number = ?, experience_years = ?, specialization = ? WHERE id = ?")) {
            ps.setString(1, licenseNumber);
            if (experienceYears != null) {
                ps.setInt(2, experienceYears);
            } else {
                ps.setNull(2, Types.INTEGER);
            }
            ps.setString(3, specialization);
            ps.setInt(4, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Updates player-specific details
     */
    public static boolean updatePlayerDetails(int id, String position, Double height, Double weight, String preferredFoot) {
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement("UPDATE player SET position = ?, height = ?, weight = ?, preferred_foot = ? WHERE id = ?")) {
            ps.setString(1, position);
            if (height != null) {
                ps.setDouble(2, height);
            } else {
                ps.setNull(2, Types.DECIMAL);
            }
            if (weight != null) {
                ps.setDouble(3, weight);
            } else {
                ps.setNull(3, Types.DECIMAL);
            }
            ps.setString(4, preferredFoot);
            ps.setInt(5, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Updates fan-specific details
     */
    public static boolean updateFanDetails(int id, String favoriteTeam, String membershipLevel) {
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement("UPDATE fan SET favorite_team = ?, membership_level = ? WHERE id = ?")) {
            ps.setString(1, favoriteTeam);
            ps.setString(2, membershipLevel);
            ps.setInt(3, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}