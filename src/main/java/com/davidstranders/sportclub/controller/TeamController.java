package com.davidstranders.sportclub.controller;

import com.davidstranders.sportclub.service.GameService;
import com.davidstranders.sportclub.service.TeamService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by DS on 17-1-2017.
 */
@Controller
@RequestMapping("/teams")
public class TeamController {

    private TeamService teamService;
    private GameService gameService;

    public TeamController(TeamService teamService, GameService gameService){
        this.teamService = teamService;
        this.gameService = gameService;
    }

    @RequestMapping("/list")
    public String teams(Model model){

        model.addAttribute("teamController", "active");
        model.addAttribute("teams", teamService.listAll());
        return"/team/list";
    }

    @Secured({"ROLE_USER","ROLE_ADMIN"})
    @RequestMapping("/show/{id}")
    public String showTeam(Model model, @PathVariable Integer id){

        model.addAttribute("teamController", "active");
        model.addAttribute("team", teamService.getById(id));
        return"/team/showTeam";
    }

}
