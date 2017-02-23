package com.udemy.sportclub.model;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dell on 14-1-2017.
 */
@Entity
public class Team implements Comparable<Team> {

    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    private int id;

    @NotEmpty
    private String name;

    private String yell;

    @OneToOne(fetch = FetchType.EAGER)
    private Member teamCaptain;

    @ManyToMany
    private List<Member> members;

    @ManyToMany (mappedBy = "teams", cascade = CascadeType.ALL)
    private List<Game> games;

    @ManyToMany
    private List<Competition> competitions;

    @Lob
    @Column(columnDefinition = "longblob")
    private byte[] image;

    @Transient
    private String base64image =null;

    @Transient
    private int goalsMade;

    @Transient
    private int goalsAgainst;

    @Transient
    private int totalPoints;

    public Team(){}

    // Constructor for data loading
    public Team(String name, String yell, Member teamCaptain) {
        this.name = name;
        this.yell = yell;
        this.teamCaptain = teamCaptain;
        this.members = new ArrayList<>();
        this.games = new ArrayList<>();
        this.competitions = new ArrayList<>();
    }

    // Constructor for testing
    public Team(String name, int id){
        this.name = name;
        this.id = id;
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

    public String getYell() {
        return yell;
    }

    public void setYell(String yell) {
        this.yell = yell;
    }

    public List<Member> getMembers() {
        return members;
    }

    public void setMembers(List<Member> members) {
        this.members = members;
    }

    public Member getTeamCaptain() {
        return teamCaptain;
    }

    public void setTeamCaptain(Member teamCaptain) {
        this.teamCaptain = teamCaptain;
    }

    public List<Competition> getCompetitions() {
        return competitions;
    }

    public void setCompetitions(List<Competition> competitions) {
        this.competitions = competitions;
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

    public List<Game> getGames() {
        return games;
    }

    public void setGames(List<Game> games) {
        this.games = games;
    }

    public int getGoalsMade() {
        return goalsMade;
    }

    public void setGoalsMade(int goalsMade) {
        this.goalsMade = goalsMade;
    }

    public int getGoalsAgainst() {
        return goalsAgainst;
    }

    public void setGoalsAgainst(int goalsAgainst) {
        this.goalsAgainst = goalsAgainst;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }

    @Override
    public String toString() {
        return "Team{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", yell='" + yell + '\'' +
                ", teamCaptain=" + teamCaptain +
                ", competitions=" + competitions +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Team ) {
            return this.id==((Team) obj).getId();
        }
        return false;
    }

    @Override
    public int compareTo(Team o) {

        int totalDif = o.totalPoints - this.totalPoints;
        if (totalDif != 0) {
            return totalDif;
        }

        int goalsMadeDif = o.goalsMade - this.goalsMade;
        if (goalsMadeDif != 0){
            return goalsMadeDif;
        }

        int goalsAgainstDif = o.goalsAgainst - this.goalsAgainst;
        if (goalsAgainstDif != 0 ){
            return goalsAgainstDif;
        }

        return this.name.compareTo(o.name);
    }
}
