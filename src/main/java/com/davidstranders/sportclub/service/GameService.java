package com.davidstranders.sportclub.service;

import com.davidstranders.sportclub.model.Game;

import java.util.List;

/**
 * Created by DS on 4-2-2017.
 */
public interface GameService {

    List<Game> listAll();
    List<Game> listGamesByLocationId (String id);
    List<Game> listUpcomingGames();
    List<Game> listGamesByCompetitionId(String id);
    Game save(Game game);
    Game getById(String id);
    void delete (String id);

}