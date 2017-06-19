package com.udemy.sportclub.model;


import org.junit.Assert;
import org.junit.Test;

/**
 * Created by DS on 9-2-2017.
 */
public class TeamTest {

    private Team teamA = new Team();
    private Team teamB = new Team();

    @Test
    public void testCompareTotalPoints() throws Exception {
        teamA.setTotalPoints(10);
        teamB.setTotalPoints(12);
        Assert.assertTrue(teamA.compareTo(teamB)>0);
    }

    @Test
    public void testCompareGoalsMade() throws Exception {
        teamA.setTotalPoints(10);
        teamB.setTotalPoints(10);
        teamA.setGoalsMade(13);
        teamB.setGoalsMade(14);
        Assert.assertTrue(teamA.compareTo(teamB)>0);
    }

    @Test
    public void testCompareGoalsAgainst() throws Exception {
        teamA.setTotalPoints(10);
        teamB.setTotalPoints(10);
        teamA.setGoalsMade(13);
        teamB.setGoalsMade(13);
        teamA.setGoalsAgainst(15);
        teamB.setGoalsAgainst(16);
        Assert.assertTrue(teamA.compareTo(teamB)>0);
    }

    @Test
    public void testCompareNames() throws Exception {
        teamA.setTotalPoints(10);
        teamB.setTotalPoints(10);
        teamA.setGoalsMade(13);
        teamB.setGoalsMade(13);
        teamA.setGoalsAgainst(15);
        teamB.setGoalsAgainst(15);
        teamA.setName("Team A");
        teamB.setName("Team B");
        Assert.assertTrue(teamA.compareTo(teamB)<0);
    }

    @Test
    public void testCompareAll() throws Exception {
        teamA.setTotalPoints(10);
        teamB.setTotalPoints(12);
        teamA.setGoalsMade(15);
        teamB.setGoalsMade(13);
        teamA.setGoalsAgainst(16);
        teamB.setGoalsAgainst(15);
        teamA.setName("Team A");
        teamB.setName("Team B");
        Assert.assertTrue(teamA.compareTo(teamB)>0);
    }




}