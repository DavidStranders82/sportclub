package com.udemy.sportclub.repository;

import com.udemy.sportclub.model.Competition;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by DS on 1-2-2017.
 */
public interface CompetitionRepository extends CrudRepository<Competition, Integer> {

    List<Competition> findAllByOrderByNameAsc();

}
