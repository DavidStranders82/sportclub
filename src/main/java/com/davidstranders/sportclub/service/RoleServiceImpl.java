package com.davidstranders.sportclub.service;

import com.davidstranders.sportclub.model.Role;
import com.davidstranders.sportclub.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by DS on 22-1-2017.
 */
@Service
public class RoleServiceImpl implements RoleService {

    private RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository){
        this.roleRepository = roleRepository;
    }

    public List<Role> listAll(){
        return roleRepository.findAllByOrderByNameAsc();
    }

    public Role save(Role role){
        return roleRepository.save(role);
    }

    public Role getById(long id){
        return roleRepository.findOne(id);
    }

    public void delete(long id){
        roleRepository.delete(id);
    }

}
