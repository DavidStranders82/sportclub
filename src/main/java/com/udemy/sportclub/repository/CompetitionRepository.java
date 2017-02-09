package com.udemy.sportclub.repository;

import com.udemy.sportclub.model.Competition;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by Dell on 1-2-2017.
 */
public interface CompetitionRepository extends CrudRepository<Competition, Integer> {

    List<Competition> findAllByOrderByNameAsc();

    @Query(value="INSERT INTO sportclub.team_competitions (teams_id, competitions_id) \n" +
            "VALUES (?1,?2)", nativeQuery = true)
    public void saveEnrolledTeam(int team_id, int competition_id);

}
