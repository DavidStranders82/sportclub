package com.udemy.sportclub.service;

import com.udemy.sportclub.model.Role;
import com.udemy.sportclub.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Dell on 22-1-2017.
 */
@Service
public class RoleService {

    private RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository){
        this.roleRepository = roleRepository;
    }

    public List<Role> list(){
        return roleRepository.findAllByOrderByNameAsc();
    }

    public Role save(Role role){
        return roleRepository.save(role);
    }

    public Role get(long id){
        return roleRepository.findOne(id);
    }

    public void delete(long id){
        roleRepository.delete(id);
    }

}
