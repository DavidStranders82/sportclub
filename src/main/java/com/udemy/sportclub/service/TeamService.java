package com.udemy.sportclub.service;

import com.udemy.sportclub.model.Team;
import com.udemy.sportclub.repository.TeamRepository;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * Created by Dell on 14-1-2017.
 */
@Service
@Transactional
public class TeamService {

    private TeamRepository teamRepository;
    private MemberService memberService;

    @Autowired
    public TeamService (TeamRepository teamRepository, MemberService memberService){
        this.teamRepository = teamRepository;
        this.memberService = memberService;
    }

    public List<Team> list(){
        List<Team> teams = teamRepository.findAllByOrderByNameAsc();
        for (Team team : teams) {
            if(team.getImage()!=null) {
                String base64Encoded = Base64.encodeBase64String(team.getImage());
                if (!base64Encoded.isEmpty()) {
                    team.setBase64image(base64Encoded);
                }
            }
        }
        return teams;
    }

    public void save(Team team, MultipartFile myFile){

        try {
            if (myFile.getBytes().length!=0) {
                team.setImage(myFile.getBytes());
            }else if (team.getId()!=0){
                team.setImage(get(team.getId()).getImage());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        teamRepository.save(team);
    }

    public void save(Team team){

        team.setImage(get(team.getId()).getImage());
        teamRepository.save(team);
    }


    public Team get(int id){
        Team team = null;
        if(id!=0) {
            team = teamRepository.findOne(id);
        }
        if(team.getImage()!=null) {
            String base64Encoded = Base64.encodeBase64String(team.getImage());
            if (!base64Encoded.isEmpty()) {
                team.setBase64image(base64Encoded);
            }
        }
        return team;
    }

    public void delete(int id){
        teamRepository.delete(id);
    }
}
