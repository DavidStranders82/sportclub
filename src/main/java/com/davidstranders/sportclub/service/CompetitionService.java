package com.davidstranders.sportclub.service;

import com.davidstranders.sportclub.model.Competition;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Created by DS on 1-2-2017.
 */
public interface CompetitionService {

    List<Competition> listAll();
    Competition getById(String id);
    Competition save(Competition competition, MultipartFile myFile);
    void delete(String id);
}
