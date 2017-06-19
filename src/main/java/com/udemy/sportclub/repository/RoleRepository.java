package com.udemy.sportclub.repository;

import com.udemy.sportclub.model.Role;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by DS on 22-1-2017.
 */
public interface RoleRepository extends CrudRepository<Role, Long> {

    List<Role> findAllByOrderByNameAsc();
}
