package com.davidstranders.sportclub.service;

import com.davidstranders.sportclub.repository.GameRepository;
import com.davidstranders.sportclub.model.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by DS on 4-2-2017.
 */

@Service
@Transactional
public class GameServiceImpl implements GameService{

    private GameRepository gameRepository;

    @Autowired
    public GameServiceImpl(GameRepository gameRepository){
        this.gameRepository = gameRepository;
    }

    public List<Game> listAll(){
        return gameRepository.findAllByOrderByDateAsc();
    }

    public List<Game> listGamesByLocationId (int id){
        return gameRepository.findAllByLocationId(id);
    }

    public List<Game> listUpcomingGames(){
        List<Game> upcomingGames = new ArrayList<>();
        List<Game> allGames = gameRepository.findAllByOrderByDateAsc();
        Date currentDate = new Date();
        for(Game game: allGames){
            if (currentDate.before(game.getDate())){
                upcomingGames.add(game);
            }
        }
        return upcomingGames;
    }

    public List<Game> listGamesByCompetitionId(int id){
        return gameRepository.findAllByCompetitionIdOrderByDateAsc(id);
    }

    public Game save(Game game){
        return gameRepository.save(game);
    }

    public Game getById(int id){
        return gameRepository.findOne(id);
    }

    public void delete (int id){
      gameRepository.delete(id);
    }


}
