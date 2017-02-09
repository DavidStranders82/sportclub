package com.udemy.sportclub.model;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dell on 9-2-2017.
 */
public class CompetitionTest {

    Competition competition = new Competition();

    @Test
    public void calculateRanking4teams() throws Exception {

        competition.setTeams(new ArrayList<>());
        Team teamA = new Team("Team A", 1);
        Team teamB = new Team("Team B", 2);
        Team teamC = new Team("Team C", 3);
        Team teamE = new Team("Team E", 5);
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
        Assert.assertTrue(ranking.get(0).equals(teamC));
        Assert.assertTrue(ranking.get(1).equals(teamA));
        Assert.assertTrue(ranking.get(2).equals(teamE));
        Assert.assertTrue(ranking.get(3).equals(teamB));
//        Assert.assertEquals(5, )
    }


    @Test
    public void calculateRanking3TeamsTest() throws Exception {

        competition.setTeams(new ArrayList<>());
        Team teamA = new Team("Team A", 1);
        Team teamB = new Team("Team B", 2);
        Team teamC = new Team("Team C", 3);
        competition.getTeams().add(teamA);
        competition.getTeams().add(teamB);
        competition.getTeams().add(teamC);
        Game teamAteamB = new Game(teamA, teamB, 5, 3);
        Game teamAteamC = new Game(teamA, teamC, null, null);
        Game teamBteamC = new Game(teamB, teamC, 8, 4);
        competition.setGames(new ArrayList<>());
        competition.getGames().add(teamAteamB);
        competition.getGames().add(teamAteamC);
        competition.getGames().add(teamBteamC);

        List<Team> ranking = competition.calculateRanking();
        Assert.assertTrue(ranking.get(0).equals(teamB));
        Assert.assertTrue(ranking.get(1).equals(teamC));
        Assert.assertTrue(ranking.get(2).equals(teamA));

    }


}