package com.davidstranders.sportclub.service;

import com.davidstranders.sportclub.model.Team;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Created by DS on 14-1-2017.
 */
public interface TeamService {

    List<Team> listAll();
    Team save(Team team, MultipartFile myFile);
    Team save(Team team);
    Team getById(int id);
    void delete(int id);

}
