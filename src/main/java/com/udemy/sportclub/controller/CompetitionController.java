package com.udemy.sportclub.controller;

import com.udemy.sportclub.model.Competition;
import com.udemy.sportclub.service.CompetitionService;
import com.udemy.sportclub.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Dell on 17-1-2017.
 */
@Controller
@RequestMapping("/competition")
public class CompetitionController {

    private CompetitionService competitionService;
    private GameService gameService;

    @Autowired
    public CompetitionController(CompetitionService competitionService, GameService gameService){
        this.competitionService = competitionService;
        this.gameService = gameService;
    }

    @RequestMapping("/list")
    public String teams(Model model){
        model.addAttribute("competitionController", "active");
        model.addAttribute("competitions", competitionService.list());
        return"/competition/list";
    }

    @Secured({"ROLE_USER","ROLE_ADMIN"})
    @RequestMapping("/show/{id}")
    public String showCompetition(Model model, @PathVariable Integer id){

        Competition competition = competitionService.get(id);
        model.addAttribute("competitionController", "active");
        model.addAttribute("competition", competition );
        model.addAttribute("games", gameService.listGamesByCompetitionId(id));
        model.addAttribute("ranking", competitionService.get(id).calculateRanking());
        return"/competition/showCompetition";
    }

}
