package com.udemy.sportclub.service;

import com.udemy.sportclub.model.Location;
import com.udemy.sportclub.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Dell on 5-2-2017.
 */

@Service
@Transactional
public class LocationService {

    private LocationRepository locationRepository;

    @Autowired
    public LocationService(LocationRepository locationRepository){
        this.locationRepository = locationRepository;
    }

    public List<Location> list(){
        return locationRepository.findAllByOrderByFieldAsc();
    }

    public Location save(Location location){
        return locationRepository.save(location);
    }

    public Location get(int id){
        return locationRepository.findOne(id);
    }

    public void delete (int id){
        locationRepository.delete(id);
    }
}
