package com.davidstranders.sportclub.controller.Admin;

import com.davidstranders.sportclub.model.Member;
import com.davidstranders.sportclub.model.Team;
import com.davidstranders.sportclub.service.CompetitionService;
import com.davidstranders.sportclub.service.MemberService;
import com.davidstranders.sportclub.service.TeamService;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by DS on 22-1-2017.
 */
@Secured("ROLE_ADMIN")
@Controller
public class AdminTeamController {

    private MemberService memberService;
    private TeamService teamService;
    private CompetitionService competitionService;

    @Autowired
    public AdminTeamController(MemberService memberService, TeamService teamService, CompetitionService competitionService){
        this.memberService = memberService;
        this.teamService = teamService;
        this.competitionService = competitionService;
    }

    @RequestMapping("admin/teams")
    public String listAll(Model model){
        model.addAttribute("adminController", "active");
        model.addAttribute("teams", teamService.listAll());
        return "admin/teams/list";
    }

    @RequestMapping("admin/team/create")
    public String create(Model model) {
        model.addAttribute("adminController", "active");
        model.addAttribute("team", new Team());
        model.addAttribute("availableMembers", memberService.listAvailableMembers());
        model.addAttribute("teamCaptains", memberService.listAvailableTeamCaptains());
        model.addAttribute("competitions", competitionService.listAll());
        return "admin/teams/newTeamForm";
    }

    @RequestMapping(value = "/admin/team/save", method = RequestMethod.POST)
    public String saveOrUpdate(@Valid Team team,
                         BindingResult bindingResult,
                         Model model,
                         @RequestParam("file") MultipartFile myFile,
                         RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("adminController", "active");
            model.addAttribute("competitions", competitionService.listAll());
            model.addAttribute("availableMembers", memberService.listAvailableMembers());
            model.addAttribute("teamCaptains", memberService.listAvailableTeamCaptains());
            if(team.getId()!=0) {
                Team teamTemp = teamService.getById(team.getId());
                String base64Encoded = Base64.encodeBase64String(teamTemp.getImage());
                if (base64Encoded!=null) {
                    team.setBase64image(base64Encoded);
                }
                return "admin/teams/editTeamForm";
            }
            return "admin/teams/newTeamForm";
        } else {
            redirectAttributes.addFlashAttribute("message",(team.getId()!=0)? team.getName() + " was updated succesfully" : team.getName() + " was created succesfully");
            Team savedTeam = teamService.save(team, myFile);
            return "redirect:/admin/teams";
        }
    }

    @RequestMapping("/admin/team/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        model.addAttribute("adminController", "active");
        Team team =  teamService.getById(id);
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

    @RequestMapping("/admin/team/delete/{id}")
    public String delete(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        teamService.delete(id);
        redirectAttributes.addFlashAttribute("message", "Team was deleted succesfully");
        return "redirect:/admin/teams";
    }

}
