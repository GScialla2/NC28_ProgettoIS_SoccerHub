-- Seed minimal data for tests

-- Users
INSERT INTO users (name, surname, email, password, birth_date, nationality) VALUES
 ('Carlo','Ancelotti','coach@example.com','secret','1970-01-01','Italia'),
 ('Paolo','Rossi','player@example.com','secret','1995-02-02','Italia'),
 ('Gino','Bianchi','fan@example.com','secret','1990-03-03','Italia');

-- Map IDs (H2 AUTO_INCREMENT starts at 1)
-- 1 = coach, 2 = player, 3 = fan

-- Specialized
INSERT INTO coach (id, license_number, experience_years, specialization, team_name) VALUES
 (1,'LIC-123',10,'Tattica','Milan');

INSERT INTO player (id, position, height, weight, preferred_foot, team_name) VALUES
 (2,'Attaccante',180,75,'right','Milan');

INSERT INTO fan (id, favorite_team, membership_level) VALUES
 (3,'Inter','basic');

-- Tournaments
INSERT INTO tournaments (created_by, name, type, trophy, team_count, match_count, start_date, end_date, location, description, category, status)
VALUES (1,'Coppa Test','A eliminazione diretta','Coppa Test',4,2,'2025-01-01','2025-02-01','Italia','Torneo di test','Professional','upcoming');

-- Matches (one standalone, one in tournament)
INSERT INTO matches (tournament_id, created_by, home_team, away_team, match_date, location, category, type, status, home_score, away_score)
VALUES (NULL,1,'Milan','Inter','2025-12-31 20:45:00','Milano','Club','Amichevole','scheduled',0,0);

INSERT INTO matches (tournament_id, created_by, home_team, away_team, match_date, location, category, type, status, home_score, away_score)
VALUES (1,1,'Milan','Roma','2025-12-25 18:00:00','Milano','Club','No Amichevole','scheduled',0,0);

-- Follow links
INSERT INTO fan_follow_matches (fan_id, match_id) VALUES (3, 1);
INSERT INTO fan_follow_tournaments (fan_id, tournament_id) VALUES (3, 1);
