package com.udemy.sportclub.repository;

import com.udemy.sportclub.model.Location;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by Dell on 5-2-2017.
 */
public interface LocationRepository extends CrudRepository<Location, Integer> {

    List<Location> findAllByOrderByFieldAsc();
}
