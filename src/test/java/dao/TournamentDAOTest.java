package dao;

import Model.Tournament;
import Model.TournamentDAO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import testutil.TestDb;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class TournamentDAOTest {

    @BeforeAll
    static void initDb() {
        TestDb.init();
    }

    @Test
    @DisplayName("doRetriveAll returns list (seed present)")
    void testRetrieveAll() {
        ArrayList<Tournament> all = TournamentDAO.doRetriveAll();
        assertNotNull(all);
        assertFalse(all.isEmpty(), "Expected seeded tournaments");
    }

    @Test
    @DisplayName("doRetriveByCreator returns coach tournaments")
    void testRetrieveByCreator() {
        ArrayList<Tournament> byCreator = TournamentDAO.doRetriveByCreator(1);
        assertNotNull(byCreator);
        assertFalse(byCreator.isEmpty(), "Expected tournaments created by coach 1");
        assertTrue(byCreator.stream().allMatch(t -> t.getCreatedBy() == null || t.getCreatedBy() == 1));
    }

    @Test
    @DisplayName("doRetriveByTeamName returns tournaments by team involvement (may be empty depending on implementation)")
    void testRetrieveByTeamName() {
        ArrayList<Tournament> byTeam = TournamentDAO.doRetriveByTeamName("Milan");
        assertNotNull(byTeam);
    }
}
