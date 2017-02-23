package com.udemy.sportclub.service;

import com.udemy.sportclub.model.Member;
import com.udemy.sportclub.model.Team;
import com.udemy.sportclub.repository.MemberRepository;
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
 * Created by Dell on 14-1-2017.
 */
@Service
@Transactional
public class MemberService implements UserDetailsService {

    private MemberRepository memberRepository;
    private TeamService teamService;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder, TeamService teamService){
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.teamService = teamService;
    }

   public Iterable<Member> listAll(){
       return memberRepository.findAll();
   }

   public Member findByEmail(String email){
       return memberRepository.findByEmail(email);
   }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       Member member = findByEmail(username);
        if(member ==null){
            throw new UsernameNotFoundException(username);
        }
        return new UserDetailsImpl(member);
    }

   public List<Member> findAllByOrderByLastName(){
       List<Member> members = memberRepository.findAllByOrderByLastNameAsc();
       for (Member member : members) {
           if(member.getImage()!=null) {
               String base64Encoded = Base64.encodeBase64String(member.getImage());
               if (!base64Encoded.isEmpty()) {
                   member.setBase64image(base64Encoded);
               }
           }
       }
       return members;
   }

   public Member save(Member member, MultipartFile myFile){
       if(member.getId()!=0 && member.getPassword().isEmpty()){
           member.setPassword(get(member.getId()).getPassword());
       }
       else {
           member.setPassword(passwordEncoder.encode(member.getPassword()));
       }
       try {
           if (myFile.getBytes().length!=0) {
               member.setImage(myFile.getBytes());
           }else if (member.getId()!=0){
               member.setImage(get(member.getId()).getImage());
           }
       } catch (IOException e) {
           e.printStackTrace();
       }
       return memberRepository.save(member);
   }

   public void delete(Integer id){
       Member member  = memberRepository.findOne(id);
       if (!member.getTeams().isEmpty()) {
           for (Team team : member.getTeams()) {
               Team teamTemp = teamService.get(team.getId());
               teamTemp.getMembers().remove(member);
               teamService.save(teamTemp);
           }
       }
       if (member.getTeamCaptain()!=null){
               Team team = teamService.get(member.getTeamCaptain().getId());
               team.setTeamCaptain(null);
               teamService.save(team);

       }
       memberRepository.delete(id);
   }

    public Member save(Member member){
        if(member.getId()!=0 && member.getPassword().isEmpty()){
            member.setPassword(get(member.getId()).getPassword());
        }
        else {
            member.setPassword(passwordEncoder.encode(member.getPassword()));
        }
        return memberRepository.save(member);
    }

   public Member get(int id){
       Member member = null;
       if(id!=0) {
           member = memberRepository.findOne(id);
       }
       if(member.getImage()!=null) {
           String base64Encoded = Base64.encodeBase64String(member.getImage());
           if (!base64Encoded.isEmpty()) {
               member.setBase64image(base64Encoded);
           }
       }
       return member;
   }

   public List<Member> listAvailableMembers(){
        List<Member> members = memberRepository.findAllByOrderByLastNameAsc();
       return members.stream().filter(member -> member.getTeams().isEmpty()).collect(Collectors.toList());
   }

//   for(Member member : members){
//        if(member.getTeams().isEmpty()){
//            availableMembers.add(member);
//        }
//    }

   public List<Member> listAvailableTeamCaptains(){
       List<Member> availableTeamCaptains = new ArrayList<>();
       List<Member> members = memberRepository.findAllByOrderByLastNameAsc();
       for(Member member : members){
           if(member.getTeamCaptain()==null){
               availableTeamCaptains.add(member);
           }
       }
       return availableTeamCaptains;
   }

}
