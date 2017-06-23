package com.udemy.sportclub.controller;

import com.udemy.sportclub.service.GameService;
import com.udemy.sportclub.service.GameServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by DS on 14-1-2017.
 */
@Controller
public class HomeController {

    private GameService gameService;

    @Autowired
    public HomeController(GameService gameService){
        this.gameService = gameService;
    }


    @RequestMapping("/")
    public String home(Model model){
        model.addAttribute("homeController", "active");
        model.addAttribute("games", gameService.listUpcomingGames());
        return "home";
    }

}
