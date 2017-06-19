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
 * Created by DS on 1-2-2017.
 */
@Service
@Transactional
public class CompetitionServiceImpl implements CompetitionService{

    private CompetitionRepository competitionRepository;

    @Autowired
    public CompetitionServiceImpl(CompetitionRepository competitionRepository){
        this.competitionRepository = competitionRepository;
    }

    public List<Competition> listAll(){
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
                competition.setImage(getById(competition.getId()).getImage());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return competitionRepository.save(competition);
    }

    public Competition getById(int id){
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

}
