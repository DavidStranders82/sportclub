package com.udemy.sportclub.repository;

import com.udemy.sportclub.model.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by Dell on 14-1-2017.
 */
public interface MemberRepository extends CrudRepository<Member, Integer>{

    List<Member> findByLastName(String lastName);

    List<Member> findAllByOrderByLastNameAsc();

    Page<Member> findAll(Pageable pageable);

    Member findByEmail(String email);

    List<Member> findAll();

}
