package com.udemy.sportclub.controller.Admin;

import com.udemy.sportclub.model.Game;
import com.udemy.sportclub.service.CompetitionService;
import com.udemy.sportclub.service.GameService;
import com.udemy.sportclub.service.LocationService;
import com.udemy.sportclub.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Date;

/**
 * Created by Dell on 5-2-2017.
 */

@Secured("ROLE_ADMIN")
@Controller
public class AdminGameController {

    private GameService gameService;
    private TeamService teamService;
    private CompetitionService competitionService;
    private LocationService locationService;

    @Autowired
    public AdminGameController(GameService gameService, TeamService teamService, CompetitionService competitionService, LocationService locationService){
        this.gameService = gameService;
        this.teamService = teamService;
        this.competitionService = competitionService;
        this.locationService = locationService;
    }

    @RequestMapping("admin/games")
    public String list(Model model){
        model.addAttribute("adminController", "active");
        model.addAttribute("games", gameService.list());
        return "admin/games/list";
    }

    @RequestMapping("admin/game/create")
    public String create(Model model) {
        model.addAttribute("adminController", "active");
        model.addAttribute("game", new Game());
        model.addAttribute("teams", teamService.list());
        model.addAttribute("competitions", competitionService.list());
        model.addAttribute("locations", locationService.list());
        return "admin/games/newGameForm";
    }

    @RequestMapping(value = "/admin/game/save", method = RequestMethod.POST)
    public String save(@Valid Game game,
                       BindingResult bindingResult,
                       Model model,
                       RedirectAttributes redirectAttributes) {
        System.out.println(game.toString());
        if (bindingResult.hasErrors() ||
                (game.getTeams().get(0).getId()==game.getTeams().get(0).getId()) ||
                (game.getDate().before(new Date()))) {
            if (game.getTeams().get(0).getId()==game.getTeams().get(0).getId()){
                model.addAttribute("message", "Teams cannot be the same. Try again");
            }
            if ( (game.getDate().before(new Date()))){
                model.addAttribute("message", "Date must be in the future. Try again");
            }
            model.addAttribute("adminController", "active");
            model.addAttribute("teams", teamService.list());
            model.addAttribute("competitions", competitionService.list());
            model.addAttribute("locations", locationService.list());
            return "admin/games/newGameForm";
        } else {
            Game gameSaved = gameService.save(game);

            redirectAttributes.addFlashAttribute("message", "New game was created succesfully");
            return "redirect:/admin/games";
        }
    }




    @RequestMapping("/admin/game/delete/{id}")
    public String delete(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        gameService.delete(id);
        redirectAttributes.addFlashAttribute("message", "Game was deleted succesfully");
        return "redirect:/admin/games";
    }


}
