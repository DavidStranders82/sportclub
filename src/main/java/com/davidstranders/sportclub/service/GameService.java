package com.davidstranders.sportclub.service;

import com.davidstranders.sportclub.model.Game;

import java.util.List;

/**
 * Created by DS on 4-2-2017.
 */
public interface GameService {

    List<Game> listAll();
    List<Game> listGamesByLocationId (int id);
    List<Game> listUpcomingGames();
    List<Game> listGamesByCompetitionId(int id);
    Game save(Game game);
    Game getById(int id);
    void delete (int id);

}