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
 * Created by DS on 14-1-2017.
 */
@Service
@Transactional
public class TeamServiceImpl implements TeamService {

    private TeamRepository teamRepository;

    @Autowired
    public TeamServiceImpl(TeamRepository teamRepository){
        this.teamRepository = teamRepository;
    }

    public List<Team> listAll(){
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


    public Team save(Team team, MultipartFile myFile){

        try {
            if (myFile.getBytes().length!=0) {
                team.setImage(myFile.getBytes());
            }else if (team.getId()!=0){
                team.setImage(getById(team.getId()).getImage());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return teamRepository.save(team);
    }

    public Team save(Team team){

        team.setImage(getById(team.getId()).getImage());
        return teamRepository.save(team);
    }


    public Team getById(int id){
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
