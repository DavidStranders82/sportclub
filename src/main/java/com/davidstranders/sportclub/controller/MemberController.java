package com.davidstranders.sportclub.controller;

import com.davidstranders.sportclub.model.Member;
import com.davidstranders.sportclub.service.MemberService;
import com.davidstranders.sportclub.service.RoleService;
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
import java.security.Principal;

/**
 * Created by DS on 15-1-2017.
 */
@Controller
@RequestMapping("/members")
public class MemberController {

    private MemberService memberService;
    private TeamService teamService;
    private RoleService roleService;

    @Autowired
    public MemberController(MemberService memberService, TeamService teamService, RoleService roleService) {
        this.memberService = memberService;
        this.teamService = teamService;
        this.roleService = roleService;
    }


    @RequestMapping("/list")
    public String members(Model model) {
        model.addAttribute("members", memberService.findAllByOrderByLastName());
        model.addAttribute("memberController", "active");
        return "/member/list";
    }

    @Secured({"ROLE_USER","ROLE_ADMIN"})
    @RequestMapping("/show/{id}")
    public String showMember(@PathVariable Integer id,
                             Model model,
                             Principal principal) {
        model.addAttribute("memberController", "active");
        Member member = memberService.getById(id);
        String currentUser = principal.getName();
        if (currentUser.equals(member.getEmail())) {
            model.addAttribute("currentLoggedIn", true);
        }
        model.addAttribute("member", member);
        return "/member/showMember";
    }

    @Secured({"ROLE_USER","ROLE_ADMIN"})
    @RequestMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        model.addAttribute("memberController", "active");
        model.addAttribute("teams", teamService.listAll());
        model.addAttribute("roles", roleService.listAll());
        model.addAttribute("member", memberService.getById(id));
        return "/member/memberForm";
    }

    @Secured({"ROLE_USER","ROLE_ADMIN"})
    @RequestMapping(value = "/update", method = RequestMethod.POST)
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
            model.addAttribute("memberController", "active");
            Member memberTemp = memberService.getById(member.getId());
            String base64Encoded = Base64.encodeBase64String(memberTemp.getImage());
            if (!base64Encoded.isEmpty()) {
                member.setBase64image(base64Encoded);
            }
            return "member/memberForm";
        } else {
            memberService.save(member, myFile);
            redirectAttributes.addFlashAttribute("message", "Profile was updated succesfully!");
            int memberId = member.getId();
            return "redirect:/members/show/" + memberId;
        }
    }
}
