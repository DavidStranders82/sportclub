package com.davidstranders.sportclub.service;

import com.davidstranders.sportclub.model.Member;
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
    Member getById(String id);
    Member getByEmail(String email);
    Member save(Member member, MultipartFile myFile);
    Member save(Member member);
    void delete(String id);
}
