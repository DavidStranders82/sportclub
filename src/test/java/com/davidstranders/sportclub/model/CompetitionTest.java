package com.davidstranders.sportclub.model;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DS on 9-2-2017.
 */
public class CompetitionTest {

    private Competition competition = new Competition();

    @Test
    public void calculateRanking4teams() {

        competition.setTeams(new ArrayList<>());
        Team teamA = new Team("Team A", "1");
        Team teamB = new Team("Team B", "2");
        Team teamC = new Team("Team C", "3");
        Team teamE = new Team("Team E", "5");
        competition.getTeams().add(teamA);
        competition.getTeams().add(teamB);
        competition.getTeams().add(teamC);
        competition.getTeams().add(teamE);
        Game teamAteamB = new Game(teamA, teamB, 6, 4);
        Game teamCteamE = new Game(teamC, teamE, 4, 4);
        Game teamAteamC = new Game(teamA, teamC, 3, 5);
        Game teamBteamE = new Game(teamB, teamE, 3, 3);
        Game teamAteamE = new Game(teamA, teamE, 8, 2);
        Game teamBteamC = new Game(teamB, teamC, 4, 6);
        competition.setGames(new ArrayList<>());
        competition.getGames().add(teamAteamB);
        competition.getGames().add(teamCteamE);
        competition.getGames().add(teamAteamC);
        competition.getGames().add(teamBteamE);
        competition.getGames().add(teamAteamE);
        competition.getGames().add(teamBteamC);

        List<Team> ranking = competition.calculateRanking();

        // Testing ranking teams
        Assert.assertTrue(ranking.get(0).equals(teamC));
        Assert.assertTrue(ranking.get(1).equals(teamA));
        Assert.assertTrue(ranking.get(2).equals(teamE));
        Assert.assertTrue(ranking.get(3).equals(teamB));

        // Testing scores Team C
        Assert.assertEquals(15,teamC.getGoalsMade() );
        Assert.assertEquals(11, teamC.getGoalsAgainst());
        Assert.assertEquals(7, teamC.getTotalPoints());

        // Testing scores Team A
        Assert.assertEquals(17, teamA.getGoalsMade());
        Assert.assertEquals(11, teamA.getGoalsAgainst());
        Assert.assertEquals(6, teamA.getTotalPoints());

        // Testing scores Team E
        Assert.assertEquals(9, teamE.getGoalsMade());
        Assert.assertEquals(15, teamE.getGoalsAgainst());
        Assert.assertEquals(2, teamE.getTotalPoints());

        // Testing scores Team B
        Assert.assertEquals(11, teamB.getGoalsMade());
        Assert.assertEquals(15, teamB.getGoalsAgainst());
        Assert.assertEquals(1, teamB.getTotalPoints());
    }


    @Test
    public void calculateRanking3Teams() {

        competition.setTeams(new ArrayList<>());
        Team teamA = new Team("Team A", "1");
        Team teamB = new Team("Team B", "2");
        Team teamC = new Team("Team C", "3");
        competition.getTeams().add(teamA);
        competition.getTeams().add(teamB);
        competition.getTeams().add(teamC);
        Game teamAteamB = new Game(teamA, teamB, 5, 3);
        Game teamAteamC = new Game(teamA, teamC, 3, 5);
        Game teamBteamC = new Game(teamB, teamC, 8, 4);
        competition.setGames(new ArrayList<>());
        competition.getGames().add(teamAteamB);
        competition.getGames().add(teamAteamC);
        competition.getGames().add(teamBteamC);

        List<Team> ranking = competition.calculateRanking();
        Assert.assertTrue("Team B not # 1", ranking.get(0).equals(teamB));
        Assert.assertTrue("Team C not # 2",ranking.get(1).equals(teamC));
        Assert.assertTrue("Team A not # 3",ranking.get(2).equals(teamA));

        //Testing scores Team A
        Assert.assertEquals(8, teamA.getGoalsMade());
        Assert.assertEquals(8, teamA.getGoalsAgainst());
        Assert.assertEquals(3, teamA.getTotalPoints());

        //Testing scores Team B
        Assert.assertEquals(11, teamB.getGoalsMade());
        Assert.assertEquals(9, teamB.getGoalsAgainst());
        Assert.assertEquals(3, teamB.getTotalPoints());

        //Testing scores Team C
        Assert.assertEquals(9, teamC.getGoalsMade());
        Assert.assertEquals(11, teamC.getGoalsAgainst());
        Assert.assertEquals(3, teamC.getTotalPoints());

    }

}