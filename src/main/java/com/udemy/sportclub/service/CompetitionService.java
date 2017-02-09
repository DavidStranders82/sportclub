package com.udemy.sportclub.service;

import com.udemy.sportclub.model.Competition;
import com.udemy.sportclub.repository.CompetitionRepository;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * Created by Dell on 1-2-2017.
 */
@Service
@Transactional
public class CompetitionService {

    private CompetitionRepository competitionRepository;

    @Autowired
    public CompetitionService(CompetitionRepository competitionRepository){
        this.competitionRepository = competitionRepository;
    }

    public List<Competition> list(){
        List<Competition> competitions = competitionRepository.findAllByOrderByNameAsc();

       for (Competition competition : competitions) {
           if(competition.getImage()!=null) {
               String base64Encoded = Base64.encodeBase64String(competition.getImage());
               if (!base64Encoded.isEmpty()) {
                   competition.setBase64image(base64Encoded);
               }
           }
       }
       return competitions;
    }

    public Competition save(Competition competition, MultipartFile myFile){

        System.out.println(competition.getEndDate());

        try {
            if (myFile.getBytes().length!=0) {
                competition.setImage(myFile.getBytes());
            }else if (competition.getId()!=0){
                competition.setImage(get(competition.getId()).getImage());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return competitionRepository.save(competition);
    }

    public Competition get(int id){
        Competition competition = null;
        if(id!=0) {
            competition = competitionRepository.findOne(id);
        }
        if(competition.getImage()!=null) {
            String base64Encoded = Base64.encodeBase64String(competition.getImage());
            if (!base64Encoded.isEmpty()) {
                competition.setBase64image(base64Encoded);
            }
        }
        return competition;
    }

    public void delete(int id){
        competitionRepository.delete(id);
    }

    public void saveEnrolledTeam(int team_id, int competition_id){
        competitionRepository.saveEnrolledTeam(team_id, competition_id);
    }
}
