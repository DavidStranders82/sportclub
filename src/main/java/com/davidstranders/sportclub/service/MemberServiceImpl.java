package com.davidstranders.sportclub.service;

import com.davidstranders.sportclub.model.Member;
import com.davidstranders.sportclub.model.Team;
import com.davidstranders.sportclub.repository.MemberRepository;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by DS on 14-1-2017.
 */
@Service
@Transactional
public class MemberServiceImpl implements UserDetailsService, MemberService {

    private MemberRepository memberRepository;
    private TeamServiceImpl teamService;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public MemberServiceImpl(MemberRepository memberRepository, PasswordEncoder passwordEncoder, TeamServiceImpl teamService) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.teamService = teamService;
    }

    public Iterable<Member> listAll() {
        return memberRepository.findAll();
    }

    public List<Member> findAllByOrderByLastName() {
        List<Member> members = memberRepository.findAllByOrderByLastNameAsc();
        for (Member member : members) {
            if (member.getImage() != null) {
                String base64Encoded = Base64.encodeBase64String(member.getImage());
                if (!base64Encoded.isEmpty()) {
                    member.setBase64image(base64Encoded);
                }
            }
        }
        return members;
    }

    public Member getById(String id) {
        Member member = null;
        if (id != null) {
            member = memberRepository.findOne(id);
        }
        if (member.getImage() != null) {
            String base64Encoded = Base64.encodeBase64String(member.getImage());
            if (!base64Encoded.isEmpty()) {
                member.setBase64image(base64Encoded);
            }
        }
        return member;
    }

    public Member getByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = getByEmail(username);
        if (member == null) {
            throw new UsernameNotFoundException(username);
        }
        return new UserDetailsImpl(member);
    }


    public Member save(Member member, MultipartFile myFile) {
        if (member.getId() != null && member.getPassword().isEmpty()) {
            member.setPassword(getById(member.getId()).getPassword());
        } else {
            member.setPassword(passwordEncoder.encode(member.getPassword()));
        }
        try {
            if (myFile.getBytes().length != 0) {
                member.setImage(myFile.getBytes());
            } else if (member.getId() != null) {
                member.setImage(getById(member.getId()).getImage());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return memberRepository.save(member);
    }

    public Member save(Member member) {
        if (member.getId() != null && member.getPassword().isEmpty()) {
            member.setPassword(getById(member.getId()).getPassword());
        } else {
            member.setPassword(passwordEncoder.encode(member.getPassword()));
        }
        return memberRepository.save(member);
    }

    public void delete(String id) {
        Member member = memberRepository.findOne(id);
        if (!member.getTeams().isEmpty()) {
            for (Team team : member.getTeams()) {
                Team teamTemp = teamService.getById(team.getId());
                teamTemp.getMembers().remove(member);
                teamService.save(teamTemp);
            }
        }
        if (member.getTeamCaptain() != null) {
            Team team = teamService.getById(member.getTeamCaptain().getId());
            team.setTeamCaptain(null);
            teamService.save(team);

        }
        memberRepository.delete(id);
    }


    public List<Member> listAvailableMembers() {
        List<Member> members = memberRepository.findAllByOrderByLastNameAsc();
        return members.stream().filter(member -> member.getTeams().isEmpty()).collect(Collectors.toList());
    }

    public List<Member> listAvailableTeamCaptains() {
        List<Member> availableTeamCaptains = new ArrayList<>();
        List<Member> members = memberRepository.findAllByOrderByLastNameAsc();
        for (Member member : members) {
            if (member.getTeamCaptain() == null) {
                availableTeamCaptains.add(member);
            }
        }
        return availableTeamCaptains;
    }

}
