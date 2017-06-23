package com.davidstranders.sportclub.controller;

import com.davidstranders.sportclub.model.Competition;
import com.davidstranders.sportclub.service.CompetitionService;
import com.davidstranders.sportclub.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by DS on 17-1-2017.
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
        model.addAttribute("competitions", competitionService.listAll());
        return"/competition/list";
    }

    @Secured({"ROLE_USER","ROLE_ADMIN"})
    @RequestMapping("/show/{id}")
    public String showCompetition(Model model, @PathVariable Integer id){

        Competition competition = competitionService.getById(id);
        model.addAttribute("competitionController", "active");
        model.addAttribute("competition", competition );
        model.addAttribute("games", gameService.listGamesByCompetitionId(id));
        model.addAttribute("ranking", competitionService.getById(id).calculateRanking());
        return"/competition/showCompetition";
    }

}
