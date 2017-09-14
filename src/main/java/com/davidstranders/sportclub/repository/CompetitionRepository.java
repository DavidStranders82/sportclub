package com.davidstranders.sportclub.repository;

import com.davidstranders.sportclub.model.Competition;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by DS on 1-2-2017.
 */
public interface CompetitionRepository extends CrudRepository<Competition, String> {

    List<Competition> findAllByOrderByNameAsc();

}
