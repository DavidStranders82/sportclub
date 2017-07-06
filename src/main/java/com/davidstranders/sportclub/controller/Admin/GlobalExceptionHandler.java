package com.davidstranders.sportclub.controller.Admin;

/**
 * Created by Dell on 5-7-2017.
 */
import com.davidstranders.sportclub.model.Competition;
import com.davidstranders.sportclub.model.Member;
import com.davidstranders.sportclub.model.Team;
import com.davidstranders.sportclub.service.CompetitionService;
import com.davidstranders.sportclub.service.MemberService;
import com.davidstranders.sportclub.service.RoleService;
import com.davidstranders.sportclub.service.TeamService;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartException;

import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    private MemberService memberService;
    private TeamService teamService;
    private RoleService roleService;
    private CompetitionService competitionService;

    @Autowired
    public GlobalExceptionHandler(MemberService memberService, TeamService teamService, RoleService roleService, CompetitionService competitionService) {
        this.memberService = memberService;
        this.teamService = teamService;
        this.roleService = roleService;
        this.competitionService = competitionService;
    }

    @ExceptionHandler(MultipartException.class)
    public String handleException(Model model, HttpRequest request) {
        model.addAttribute("message", "Selected image is more than 10MB. Please try again");
        String path = request.getURI().getPath();
        int id = Integer.parseInt(path.substring(path.lastIndexOf('/') + 1));
        path = path.substring(0, path.lastIndexOf('/'));

        if (path.equals("/admin/member/update")) {
            model.addAttribute("member", memberService.getById(id));
            model.addAttribute("teams", teamService.listAll());
            model.addAttribute("roles", roleService.listAll());
            model.addAttribute("adminController", "active");
            return "admin/members/editMemberForm";
        } else if (path.equals("/admin/member/save")) {
            model.addAttribute("member", new Member());
            model.addAttribute("teams", teamService.listAll());
            model.addAttribute("roles", roleService.listAll());
            model.addAttribute("adminController", "active");
            return "admin/members/newMemberForm";
        } else if (path.equals("/members/update")) {
            model.addAttribute("member", memberService.getById(id));
            model.addAttribute("teams", teamService.listAll());
            model.addAttribute("roles", roleService.listAll());
            model.addAttribute("memberController", "active");
            return "/member/memberForm";
        }
        else if (path.equals("/admin/team/save") && id>0) {
            model.addAttribute("adminController", "active");
            Team team =  teamService.getById(id);
            Hibernate.initialize(team.getCompetitions());
            List<Member> availableTeamCaptains = memberService.listAvailableTeamCaptains();
            if(team.getTeamCaptain()!=null) {
                availableTeamCaptains.add(team.getTeamCaptain());
            }
            model.addAttribute("team", team);
            model.addAttribute("competitions", competitionService.listAll());
            model.addAttribute("availableMembers", memberService.listAvailableMembers());
            model.addAttribute("teamCaptains", availableTeamCaptains);
            return "admin/teams/editTeamForm";
        }
        else if (path.equals("/admin/team/save") && id==0) {
            model.addAttribute("adminController", "active");
            model.addAttribute("team", new Team());
            model.addAttribute("availableMembers", memberService.listAvailableMembers());
            model.addAttribute("teamCaptains", memberService.listAvailableTeamCaptains());
            model.addAttribute("competitions", competitionService.listAll());
            return "admin/teams/newTeamForm";
        }
        else if (path.equals("/admin/competition/save")) {
            model.addAttribute("adminController", "active");
            model.addAttribute("competition", new Competition());
            model.addAttribute("teams", teamService.listAll());
            return "admin/competition/newCompetitionForm";
        }
        else if (path.equals("/admin/competition/update")) {
            model.addAttribute("adminController", "active");
            model.addAttribute("competition", competitionService.getById(id));
            model.addAttribute("teams", teamService.listAll());
            return "admin/competition/editCompetitionForm";
        }
        return null;
    }
}