package dao;

import Model.Match;
import Model.MatchDAO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import testutil.TestDb;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class MatchDAOTest {

    @BeforeAll
    static void initDb() {
        TestDb.init();
    }

    @Test
    @DisplayName("doRetriveAll returns seeded matches")
    void testRetrieveAll() {
        ArrayList<Match> all = MatchDAO.doRetriveAll();
        assertNotNull(all);
        assertTrue(all.size() >= 2, "Expected at least 2 seeded matches");
    }

    @Test
    @DisplayName("doRetriveByCreator finds matches created by coach id=1")
    void testRetrieveByCreator() {
        ArrayList<Match> byCreator = MatchDAO.doRetriveByCreator(1);
        assertNotNull(byCreator);
        assertFalse(byCreator.isEmpty(), "Expected matches created by coach 1");
        assertTrue(byCreator.stream().allMatch(m -> m.getCreatedBy() != null && m.getCreatedBy() == 1));
    }

    @Test
    @DisplayName("doRetriveByTeamName returns matches of Milan")
    void testRetrieveByTeamName() {
        ArrayList<Match> list = MatchDAO.doRetriveByTeamName("Milan");
        assertNotNull(list);
        assertFalse(list.isEmpty(), "Expected matches involving Milan");
        assertTrue(list.stream().allMatch(m -> "Milan".equals(m.getHomeTeam()) || "Milan".equals(m.getAwayTeam())));
    }
}
