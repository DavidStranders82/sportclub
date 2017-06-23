package com.udemy.sportclub.service;

import com.udemy.sportclub.model.Role;

import java.util.List;

/**
 * Created by DS on 22-1-2017.
 */
public interface RoleService {

    List<Role> listAll();
    Role save(Role role);
    Role getById(long id);
    void delete(long id);

}
