package com.udemy.sportclub.model;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by Dell on 14-1-2017.
 */
@Entity
public class Competition implements Comparable<Competition>{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotEmpty
    private String name;

    @Column (columnDefinition = "TEXT")
    private String description;

    private int maxTeams;

    @NotNull
    @DateTimeFormat(pattern="dd/MM/yyyy")
    private Date startDate;

    @NotNull
    @DateTimeFormat(pattern="dd/MM/yyyy")
    private Date endDate;

    @ManyToMany(mappedBy = "competitions")
    private List<Team> teams;

    @OneToMany(mappedBy = "competition")
    private List<Game> games;

    @Lob
    @Column(columnDefinition = "longblob")
    private byte[] image;

    @Transient
    private String base64image =null;

    private String teaser;

    public Competition(){}

    public Competition(String name, String description, int maxTeams, Date startDate, Date endDate, String teaser) {
        this.name = name;
        this.description = description;
        this.maxTeams = maxTeams;
        this.startDate = startDate;
        this.endDate = endDate;
        this.teams = new ArrayList<>();
        this.games = new ArrayList<>();
        this.teaser = teaser;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

    public List<Game> getGames() {
        return games;
    }

    public void setGames(List<Game> games) {
        this.games = games;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getMaxTeams() {
        return maxTeams;
    }

    public void setMaxTeams(int maxTeams) {
        this.maxTeams = maxTeams;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getBase64image() {
        return base64image;
    }

    public void setBase64image(String base64image) {
        this.base64image = base64image;
    }

    public String getTeaser() {
        return teaser;
    }

    public void setTeaser(String teaser) {
        this.teaser = teaser;
    }

    @Override
    public String toString() {
        return "Competition{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", maxTeams=" + maxTeams +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Competition ) {
            return this.id==((Competition) obj).getId();
        }
        return false;
    }

    public List<Team> calculateRanking (){
        for(Team team : teams){
            for(Game game : games){
                if(game.getTeams().contains(team)){
                    if(game.getTeams().get(0).getId()==team.getId()){
                        if(game.getScoreTeamA()!=null) {
                            setScores(team, game.getScoreTeamA(), game.getScoreTeamB());
                        }
                    }else{
                        if(game.getScoreTeamA()!=null) {
                            setScores(team, game.getScoreTeamB(), game.getScoreTeamA());
                        }
                    }
                }
            }
        }
        Collections.sort(teams);
        return teams;
    }

    private void setScores(Team team, int scoreTeamA, int scoreTeamB){
        team.setGoalsMade(team.getGoalsMade() + scoreTeamA);
        team.setGoalsAgainst(team.getGoalsAgainst() + scoreTeamB);
        if (scoreTeamA > scoreTeamB) {
            team.setTotalPoints(team.getTotalPoints()+3);
        } else if (scoreTeamA == scoreTeamB) {
            team.setTotalPoints(team.getTotalPoints()+1);
        }
    }

    @Override
    public int compareTo(Competition o) {
        return this.name.compareTo(o.getName());
    }
}
