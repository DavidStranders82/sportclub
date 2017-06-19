package com.udemy.sportclub.controller.Admin;

import com.udemy.sportclub.model.Member;
import com.udemy.sportclub.model.Team;
import com.udemy.sportclub.service.*;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PagedListHolder;
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

import javax.servlet.http.HttpServletRequest;
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
    public String list(HttpServletRequest request) {
        return "redirect:/admin/members/page/1/lastName/asc";
    }

    @RequestMapping("/admin/members/page/{pageNumber}/{sortColumn}/{sortOrder}")
    public String list(Model model, HttpServletRequest request, @PathVariable Integer pageNumber, @PathVariable String sortColumn, @PathVariable String sortOrder) {
        model.addAttribute("adminController", "active");
        PagedListHolder<Member> pagedListHolder = (PagedListHolder<Member>)request.getSession().getAttribute("members");

        if(pagedListHolder == null || model.containsAttribute("message")){
            List<Member> members = memberService.findAllByOrderByLastName();
            pagedListHolder = new PagedListHolder<Member>(members);
            pagedListHolder.setPageSize(10);
        } else {
            final int goToPage = pageNumber -1;
            if(goToPage<=pagedListHolder.getPageCount() && goToPage>=0){
                pagedListHolder.setPage(goToPage);
            }
        }

        if (sortColumn != null){
            pagedListHolder.setSort(new MutableSortDefinition(sortColumn, true, sortOrder.equals("asc")));
            pagedListHolder.resort();
        }

        request.getSession().setAttribute("members", pagedListHolder);
        pagedListHolder.setPage(pageNumber-1);
        int current = pagedListHolder.getPage()+1;
        int begin = Math.max(1, current-10);
        int end = Math.min(begin + 5, pagedListHolder.getPageCount());
        int totalPageCount = pagedListHolder.getPageCount();

        model.addAttribute("beginIndex", begin);
        model.addAttribute("endIndex", end);
        model.addAttribute("currentIndex", current);
        model.addAttribute("totalPageCount", totalPageCount);
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
            model.addAttribute("teams", teamService.listAll());
            model.addAttribute("roles", roleService.listAll());
            model.addAttribute("adminController", "active");
            return "admin/members/newMemberForm";
        } else {
            Member memberSaved = memberService.save(member, myFile);
            if (!member.getTeams().isEmpty()){
                for (Team team : member.getTeams()){
                    Team teamTemp = teamService.getById(team.getId());
                    teamTemp.getMembers().add(memberSaved);
                    teamService.save(teamTemp);
                }
            }
            redirectAttributes.addFlashAttribute("message", member.getFirstName() + " " + member.getLastName() + " was created succesfully");
            return "redirect:/admin/members/page/1/lastName/asc";
        }
    }

    @RequestMapping(value = "/admin/member/update", method = RequestMethod.POST)
    public String update(@Valid Member member,
                         BindingResult bindingResult,
                         Model model,
                         @RequestParam("file") MultipartFile myFile,
                         RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors() || !(member.getPassword().equals(member.getConfirmPw()))) {
            if (!(member.getPassword().equals(member.getConfirmPw()))) {
                model.addAttribute("message", "Passwords are not equal. Try again");
            }
            model.addAttribute("teams", teamService.listAll());
            model.addAttribute("roles", roleService.listAll());
            model.addAttribute("adminController", "active");
            Member memberTemp = memberService.getById(member.getId());
            String base64Encoded = Base64.encodeBase64String(memberTemp.getImage());
            if (base64Encoded!=null) {
                member.setBase64image(base64Encoded);
            }
            return "admin/members/editMemberForm";
        } else {
            memberService.save(member, myFile);
            List<Team> teams = teamService.listAll();
            for(Team team : teams){
                if (team.getMembers().contains(member)){
                    team.getMembers().remove(member);
                }
                if(member.getTeams().contains(team)){
                    team.getMembers().add(member);
                }
                teamService.save(team);
            }
            redirectAttributes.addFlashAttribute("message",  member.getFirstName() + " " + member.getLastName() + " was updated succesfully");
            return "redirect:/admin/members/page/1/lastName/asc";
        }
    }

    @RequestMapping("/admin/member/delete/{id}")
    public String delete(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        memberService.delete(id);
        redirectAttributes.addFlashAttribute("message", "Member was deleted succesfully");
        return "redirect:/admin/members/page/1/lastName/asc";
    }

    @RequestMapping("/admin/member/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        model.addAttribute("adminController", "active");
        model.addAttribute("member",memberService.getById(id) );
        model.addAttribute("teams", teamService.listAll());
        model.addAttribute("roles", roleService.listAll());
        return "admin/members/editMemberForm";
    }
}



