package com.davidstranders.sportclub.service;

import com.davidstranders.sportclub.model.Role;

import java.util.List;

/**
 * Created by DS on 22-1-2017.
 */
public interface RoleService {

    List<Role> listAll();
    Role save(Role role);
    Role getById(String id);
    void delete(String id);

}
