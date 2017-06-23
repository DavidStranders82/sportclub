package com.davidstranders.sportclub.service;

import com.davidstranders.sportclub.model.Location;
import com.davidstranders.sportclub.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by DS on 5-2-2017.
 */

@Service
@Transactional
public class LocationServiceImpl implements LocationService {

    private LocationRepository locationRepository;

    @Autowired
    public LocationServiceImpl(LocationRepository locationRepository){
        this.locationRepository = locationRepository;
    }

    public List<Location> listAll(){
        return locationRepository.findAllByOrderByFieldAsc();
    }

    public Location save(Location location){
        return locationRepository.save(location);
    }

    public Location getById(int id){
        return locationRepository.findOne(id);
    }

    public void delete (int id){
        locationRepository.delete(id);
    }
}
