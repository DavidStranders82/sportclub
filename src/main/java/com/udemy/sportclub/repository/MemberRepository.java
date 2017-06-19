package com.udemy.sportclub.repository;

import com.udemy.sportclub.model.Member;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by DS on 14-1-2017.
 */
public interface MemberRepository extends CrudRepository<Member, Integer>{

    List<Member> findByLastName(String lastName);

    List<Member> findAllByOrderByLastNameAsc();

    Member findByEmail(String email);

    List<Member> findAll();


}
