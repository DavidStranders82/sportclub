package com.davidstranders.sportclub.service;

import com.davidstranders.sportclub.model.Location;

import java.util.List;

/**
 * Created by DS on 5-2-2017.
 */
public interface LocationService {

    List<Location> listAll();
    Location getById(int id);
    Location save(Location location);
    void delete(int id);



}
