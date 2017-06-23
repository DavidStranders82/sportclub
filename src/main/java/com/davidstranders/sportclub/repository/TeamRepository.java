package com.davidstranders.sportclub.repository;

import com.davidstranders.sportclub.model.Team;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by DS on 14-1-2017.
 */
public interface TeamRepository extends CrudRepository<Team, Integer> {

    List<Team> findAllByOrderByNameAsc();

}
