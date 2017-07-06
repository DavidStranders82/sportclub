package com.davidstranders.sportclub.controller.Admin;

import com.davidstranders.sportclub.model.Member;
import com.davidstranders.sportclub.model.Team;
import com.davidstranders.sportclub.service.MemberService;
import com.davidstranders.sportclub.service.RoleService;
import com.davidstranders.sportclub.service.TeamService;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.validation.Valid;
import java.util.List;

/**
 * Created by DS on 22-1-2017.
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
    public String list() {
        return "redirect:/admin/members/page/1/lastName/asc";
    }

    @RequestMapping("/admin/members/page/{pageNumber}/{sortColumn}/{sortOrder}")
    public String listAll(Model model, @PathVariable Integer pageNumber, @PathVariable String sortColumn, @PathVariable String sortOrder) {

        PagedListHolder<Member> pagedListHolder = new PagedListHolder<Member>(memberService.findAllByOrderByLastName());

        pagedListHolder.setSort(new MutableSortDefinition(sortColumn, true, sortOrder.equals("asc")));
        pagedListHolder.resort();
        pagedListHolder.setPage(pageNumber - 1);
        pagedListHolder.setPageSize(10);

        int begin = Math.max(1, pageNumber - 10);
        int end = Math.min(begin + 5, pagedListHolder.getPageCount());

        model.addAttribute("adminController", "active");
        model.addAttribute("beginIndex", begin);
        model.addAttribute("endIndex", end);
        model.addAttribute("currentIndex", pageNumber);
        model.addAttribute("totalPageCount", pagedListHolder.getPageCount());
        model.addAttribute("members", pagedListHolder);
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("sortColumn", sortColumn);
        model.addAttribute("sortOrder", sortOrder);
        return "admin/members/list";
    }

    @RequestMapping("/admin/member/create")
    public String create(Model model) {
        model.addAttribute("adminController", "active");
        model.addAttribute("member", new Member());
        model.addAttribute("teams", teamService.listAll());
        model.addAttribute("roles", roleService.listAll());
        return "admin/members/newMemberForm";
    }

    @RequestMapping(value = "/admin/member/save/{id}", method = RequestMethod.POST)
    public String save(@Valid Member member,
                       BindingResult bindingResult,
                       Model model,
                       @RequestParam("file") MultipartFile myFile,
                       RedirectAttributes redirectAttributes){

        if (bindingResult.hasErrors()) {
            addAttributes(model);
            return "admin/members/newMemberForm";
        }

        if (member.getPassword() == null || member.getPassword().isEmpty()) {
            model.addAttribute("message", "Password is required. Try again");
            addAttributes(model);
            return "admin/members/newMemberForm";
        }

        if (member.getPassword() != null && member.getConfirmPw() != null &&
                !(member.getPassword().equals(member.getConfirmPw()))) {
            model.addAttribute("message", "Passwords are not equal. Try again");
            addAttributes(model);
            return "admin/members/newMemberForm";
        }
        Member memberSaved = memberService.save(member, myFile);

        if (member.getTeams() != null && !member.getTeams().isEmpty()) {
            for (Team team : member.getTeams()) {
                Team teamTemp = teamService.getById(team.getId());
                teamTemp.getMembers().add(memberSaved);
                teamService.save(teamTemp);
            }
        }
        redirectAttributes.addFlashAttribute("message", member.getFirstName() + " " + member.getLastName() + " was created succesfully");
        return "redirect:/admin/members/page/1/lastName/asc";
    }

    @RequestMapping(value = "/admin/member/update/{id}", method = RequestMethod.POST)
    public String update(@Valid Member member,
                         BindingResult bindingResult,
                         Model model,
                         @RequestParam("file") MultipartFile myFile,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            addAttributes(model);
            resetBase64Image(member);
            return "admin/members/editMemberForm";
        }

        if (member.getPassword() != null && member.getConfirmPw() != null &&
                !(member.getPassword().equals(member.getConfirmPw()))) {
            model.addAttribute("message", "Passwords are not equal. Try again");
            addAttributes(model);
            resetBase64Image(member);
            return "admin/members/editMemberForm";
        }

        memberService.save(member, myFile);
        List<Team> teams = teamService.listAll();
        for (Team team : teams) {
            if (team.getMembers() != null) {
                if (team.getMembers().contains(member)) {
                    team.getMembers().remove(member);
                }
                if (member.getTeams().contains(team)) {
                    team.getMembers().add(member);
                }
            }
            teamService.save(team);
        }
        redirectAttributes.addFlashAttribute("message", member.getFirstName() + " " + member.getLastName() + " was updated succesfully");
        return "redirect:/admin/members/page/1/lastName/asc";
    }

    @RequestMapping("/admin/member/delete/{id}")
    public String delete(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        memberService.delete(id);
        redirectAttributes.addFlashAttribute("message", "Member was deleted succesfully");
        return "redirect:/admin/members/page/1/lastName/asc";
    }

    @RequestMapping("/admin/member/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        model.addAttribute("member", memberService.getById(id));
        addAttributes(model);
        return "admin/members/editMemberForm";
    }

    private void addAttributes(Model model) {
        model.addAttribute("teams", teamService.listAll());
        model.addAttribute("roles", roleService.listAll());
        model.addAttribute("adminController", "active");
    }

    private void resetBase64Image(Member member) {
        Member memberTemp = memberService.getById(member.getId());
        if (memberTemp.getImage() != null) {
            member.setBase64image(Base64.encodeBase64String(memberTemp.getImage()));
        }
    }
}



