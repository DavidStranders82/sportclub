package com.udemy.sportclub.controller.Admin;

import com.udemy.sportclub.model.Member;
import com.udemy.sportclub.model.Team;
import com.udemy.sportclub.service.MemberService;
import com.udemy.sportclub.service.RoleService;
import com.udemy.sportclub.service.TeamService;
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
 * Created by Dell on 22-1-2017.
 */
@Secured("ROLE_ADMIN")
@Controller
public class AdminMemberController {

    private MemberService memberService;
    private TeamService teamService;
    private RoleService roleService;

    @Autowired
    public AdminMemberController(MemberService memberService, TeamService teamService, RoleService roleService) {
        this.memberService = memberService;
        this.teamService = teamService;
        this.roleService = roleService;
    }

    @RequestMapping("admin/members")
    public String list(Model model) {
        model.addAttribute("members", memberService.findAllByOrderByLastName());
        model.addAttribute("adminController", "active");
        return "admin/members/list";
    }

//    @RequestMapping("admin/members")
//    public String list(Model model, Pageable pageable) {
//        model.addAttribute("adminController", "active");
//        PageWrapper<Member> page = new PageWrapper<Member>(memberService.findAllByOrderByLastName(pageable), "admin/members");
//        model.addAttribute("page", page);
//        return "admin/members/list";
//    }

    @RequestMapping("admin/member/create")
    public String create(Model model) {
        model.addAttribute("adminController", "active");
        model.addAttribute("member", new Member());
        model.addAttribute("teams", teamService.list());
        model.addAttribute("roles", roleService.list());
        return "admin/members/newMemberForm";
    }

    @RequestMapping(value = "/admin/member/save", method = RequestMethod.POST)
    public String save(@Valid Member member,
                       BindingResult bindingResult,
                       Model model,
                       @RequestParam("file") MultipartFile myFile,
                       RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors() || !(member.getPassword().equals(member.getConfirmPw())) || member.getPassword().isEmpty()) {
            if (member.getPassword().isEmpty()) {
                model.addAttribute("message", "Password is required. Try again");
            }
            if (!(member.getPassword().equals(member.getConfirmPw()))) {
                model.addAttribute("message", "Passwords are not equal. Try again");
            }
            model.addAttribute("teams", teamService.list());
            model.addAttribute("roles", roleService.list());
            model.addAttribute("adminController", "active");
            return "admin/members/newMemberForm";
        } else {
            Member memberSaved = memberService.save(member, myFile);
            if (!member.getTeams().isEmpty()){
                for (Team team : member.getTeams()){
                    Team teamTemp = teamService.get(team.getId());
                    teamTemp.getMembers().add(memberSaved);
                    teamService.save(teamTemp);
                }
            }
            redirectAttributes.addFlashAttribute("message", "New member was created succesfully");
            return "redirect:/admin/members";
        }
    }

    @RequestMapping(value = "/admin/member/update", method = RequestMethod.POST)
    public String update(@Valid Member member,
                         BindingResult bindingResult,
                         Model model,
                         @RequestParam("file") MultipartFile myFile,
                         RedirectAttributes redirectAttributes) {

        System.out.println(member.getMemberSince());

        if (bindingResult.hasErrors() || !(member.getPassword().equals(member.getConfirmPw()))) {
            if (!(member.getPassword().equals(member.getConfirmPw()))) {
                model.addAttribute("message", "Passwords are not equal. Try again");
            }
            model.addAttribute("teams", teamService.list());
            model.addAttribute("roles", roleService.list());
            model.addAttribute("adminController", "active");
            Member memberTemp = memberService.get(member.getId());
            String base64Encoded = Base64.encodeBase64String(memberTemp.getImage());
            if (!base64Encoded.isEmpty()) {
                member.setBase64image(base64Encoded);
            }
            return "admin/members/editMemberForm";
        } else {
            memberService.save(member, myFile);
            List<Team> teams = teamService.list();
            for(Team team : teams){
                if (team.getMembers().contains(member)){
                    team.getMembers().remove(member);
                }
                if(member.getTeams().contains(team)){
                    team.getMembers().add(member);
                }
                teamService.save(team);
            }
            redirectAttributes.addFlashAttribute("message", "Member was updated succesfully");
            return "redirect:/admin/members";
        }
    }

    @RequestMapping("/admin/member/delete/{id}")
    public String delete(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        memberService.delete(id);
        redirectAttributes.addFlashAttribute("message", "Member was deleted succesfully");
        return "redirect:/admin/members";
    }

    @RequestMapping("/admin/member/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        model.addAttribute("adminController", "active");
        model.addAttribute("member",memberService.get(id) );
        model.addAttribute("teams", teamService.list());
        model.addAttribute("roles", roleService.list());
        return "admin/members/editMemberForm";
    }
}



