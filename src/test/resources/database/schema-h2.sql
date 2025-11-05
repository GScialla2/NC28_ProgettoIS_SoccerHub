-- H2 schema aligned to MySQL mode for tests
-- Enable MySQL compatibility
-- (Set by JDBC URL: MODE=MySQL)

DROP TABLE IF EXISTS fan_follow_matches;
DROP TABLE IF EXISTS fan_follow_tournaments;
DROP TABLE IF EXISTS matches;
DROP TABLE IF EXISTS tournaments;
DROP TABLE IF EXISTS coach;
DROP TABLE IF EXISTS player;
DROP TABLE IF EXISTS fan;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  surname VARCHAR(100) NOT NULL,
  email VARCHAR(150) NOT NULL UNIQUE,
  password VARCHAR(255),
  birth_date DATE,
  nationality VARCHAR(100)
);

CREATE TABLE coach (
  id INT PRIMARY KEY,
  license_number VARCHAR(50),
  experience_years INT,
  specialization VARCHAR(100),
  team_name VARCHAR(100),
  CONSTRAINT fk_coach_user FOREIGN KEY (id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE player (
  id INT PRIMARY KEY,
  position VARCHAR(50),
  height DOUBLE,
  weight DOUBLE,
  preferred_foot VARCHAR(20),
  team_name VARCHAR(100),
  CONSTRAINT fk_player_user FOREIGN KEY (id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE fan (
  id INT PRIMARY KEY,
  favorite_team VARCHAR(100),
  membership_level VARCHAR(20),
  CONSTRAINT fk_fan_user FOREIGN KEY (id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE tournaments (
  id INT AUTO_INCREMENT PRIMARY KEY,
  created_by INT NULL,
  name VARCHAR(200) NOT NULL,
  type VARCHAR(100) NOT NULL,
  trophy VARCHAR(100) NOT NULL,
  team_count INT DEFAULT 0,
  match_count INT DEFAULT 0,
  start_date DATE,
  end_date DATE,
  location VARCHAR(100),
  description VARCHAR(500),
  category VARCHAR(50),
  status VARCHAR(30),
  CONSTRAINT fk_tournament_creator FOREIGN KEY (created_by) REFERENCES users(id) ON DELETE SET NULL
);

CREATE TABLE matches (
  id INT AUTO_INCREMENT PRIMARY KEY,
  tournament_id INT NULL,
  created_by INT NULL,
  home_team VARCHAR(100) NOT NULL,
  away_team VARCHAR(100) NOT NULL,
  match_date TIMESTAMP NULL,
  location VARCHAR(100),
  category VARCHAR(50),
  type VARCHAR(50),
  status VARCHAR(30),
  home_score INT DEFAULT 0,
  away_score INT DEFAULT 0,
  CONSTRAINT fk_matches_tournament FOREIGN KEY (tournament_id) REFERENCES tournaments(id) ON DELETE SET NULL,
  CONSTRAINT fk_matches_creator FOREIGN KEY (created_by) REFERENCES users(id) ON DELETE SET NULL
);

CREATE TABLE fan_follow_matches (
  fan_id INT NOT NULL,
  match_id INT NOT NULL,
  PRIMARY KEY (fan_id, match_id),
  CONSTRAINT fk_ffm_fan FOREIGN KEY (fan_id) REFERENCES users(id) ON DELETE CASCADE,
  CONSTRAINT fk_ffm_match FOREIGN KEY (match_id) REFERENCES matches(id) ON DELETE CASCADE
);

CREATE TABLE fan_follow_tournaments (
  fan_id INT NOT NULL,
  tournament_id INT NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (fan_id, tournament_id),
  CONSTRAINT fk_fft_fan FOREIGN KEY (fan_id) REFERENCES users(id) ON DELETE CASCADE,
  CONSTRAINT fk_fft_tournament FOREIGN KEY (tournament_id) REFERENCES tournaments(id) ON DELETE CASCADE
);
