package com.davidstranders.sportclub.repository;

import com.davidstranders.sportclub.model.Location;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by DS on 5-2-2017.
 */
public interface LocationRepository extends CrudRepository<Location, Integer> {

    List<Location> findAllByOrderByFieldAsc();
}
