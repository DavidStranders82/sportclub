package com.udemy.sportclub.model;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * Created by Dell on 14-1-2017.
 */
@Entity
public class Member {

    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    private int id;

    @NotEmpty
    private String firstName;

    @NotEmpty
    private String lastName;

    @NotEmpty
    @Column(unique= true, nullable = false)
    private String email;

    @NotNull
    @Column(nullable = false)
    private String password;

    @Transient
    private String confirmPw;

    @NotNull
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles = new HashSet<Role>();

    @Column (columnDefinition = "TEXT")
    private String profile;

    @NotNull
    @DateTimeFormat(pattern="dd/MM/yyyy")
    private Date memberSince;

    @ManyToMany(mappedBy = "members", cascade = CascadeType.REFRESH)
    private List<Team> teams;

    @OneToOne(mappedBy = "teamCaptain")
    private Team teamCaptain;

    @Lob
    @Column(columnDefinition = "longblob")
    private byte[] image;

    @Transient
    private String base64image =null;

    public Member(){}

    public Member(String firstName, String lastName, String email, String password, String confirmPw, String profile, Date memberSince, Team teamCaptain) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.confirmPw = confirmPw;
        this.roles = new HashSet<>();
        this.profile = profile;
        this.memberSince = memberSince;
        this.teams = new ArrayList<>();
        this.teamCaptain = teamCaptain;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

    public Date getMemberSince() {
        return memberSince;
    }

    public void setMemberSince(Date memberSince) {
        this.memberSince = memberSince;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public String getConfirmPw() {
        return confirmPw;
    }

    public void setConfirmPw(String confirmPw) {
        this.confirmPw = confirmPw;
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

    public Team getTeamCaptain() {
        return teamCaptain;
    }

    public void setTeamCaptain(Team teamCaptain) {
        this.teamCaptain = teamCaptain;
    }

    @Override
    public String toString() {
        return "Member{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Member ) {
            return this.id==((Member) obj).getId();
        }
        return false;
    }
}
