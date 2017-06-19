package com.udemy.sportclub.service;

import com.udemy.sportclub.model.Competition;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Created by DS on 1-2-2017.
 */
public interface CompetitionService {

    List<Competition> listAll();
    Competition getById(int id);
    Competition save(Competition competition, MultipartFile myFile);
    void delete(int id);
}
