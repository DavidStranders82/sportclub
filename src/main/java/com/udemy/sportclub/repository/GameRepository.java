package com.udemy.sportclub.repository;

import com.udemy.sportclub.model.Game;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by DS on 4-2-2017.
 */
public interface GameRepository extends CrudRepository<Game, Integer> {

    List<Game> findAllByOrderByDateAsc();

    List<Game> findAllByCompetitionIdOrderByDateAsc (int id);

    List<Game> findAllByLocationId (int id);


}
