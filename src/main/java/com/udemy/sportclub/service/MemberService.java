package com.udemy.sportclub.service;

import com.udemy.sportclub.model.Member;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Created by DS on 14-1-2017.
 */
public interface MemberService {

    Iterable<Member> listAll();
    List<Member> listAvailableMembers();
    List<Member> listAvailableTeamCaptains();
    List<Member> findAllByOrderByLastName();
    Member getById(int id);
    Member getByEmail(String email);
    Member save(Member member, MultipartFile myFile);
    Member save(Member member);
    void delete(Integer id);
}
