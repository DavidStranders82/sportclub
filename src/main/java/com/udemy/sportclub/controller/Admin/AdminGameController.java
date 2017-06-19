package com.udemy.sportclub.controller.Admin;

import com.udemy.sportclub.model.Game;
import com.udemy.sportclub.service.*;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;

/**
 * Created by DS on 5-2-2017.
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
    public String list(Model model, RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("message", "external redirect");
        return "redirect:/admin/games/page/1/date/asc";
    }

    @RequestMapping("/admin/games/page/{pageNumber}/{sortColumn}/{sortOrder}")
    public String list(Model model, HttpServletRequest request, @PathVariable Integer pageNumber, @PathVariable String sortColumn, @PathVariable String sortOrder) {

        model.addAttribute("adminController", "active");
        PagedListHolder<Game> pagedListHolder = (PagedListHolder<Game>)request.getSession().getAttribute("games");

        if(model.containsAttribute("message")){
            List<Game> games = gameService.listAll();
            pagedListHolder = new PagedListHolder<Game>(games);
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

        request.getSession().setAttribute("games", pagedListHolder);
        pagedListHolder.setPage(pageNumber-1);
        int current = pagedListHolder.getPage()+1;
        int begin = Math.max(1, current-10);
        int end = Math.min(begin + 5, pagedListHolder.getPageCount());
        int totalPageCount = pagedListHolder.getPageCount();

        model.addAttribute("beginIndex", begin);
        model.addAttribute("endIndex", end);
        model.addAttribute("currentIndex", current);
        model.addAttribute("totalPageCount", totalPageCount);
        model.addAttribute("games", pagedListHolder);
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("sortColumn", sortColumn);
        model.addAttribute("sortOrder", sortOrder);

        return "admin/games/list";
    }

    @RequestMapping("admin/game/create")
    public String create(Model model) {
        model.addAttribute("adminController", "active");
        model.addAttribute("game", new Game());
        model.addAttribute("teams", teamService.listAll());
        model.addAttribute("competitions", competitionService.listAll());
        model.addAttribute("locations", locationService.listAll());
        return "admin/games/newGameForm";
    }

    @RequestMapping(value = "/admin/game/save", method = RequestMethod.POST)
    public String saveOrUpdate(@Valid Game game,
                       BindingResult bindingResult,
                       Model model,
                       RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors() ||
                (game.getTeams().get(0).getId()==game.getTeams().get(1).getId()) ||
                (game.getDate().before(new Date()))) {

            if(game.getTeams().get(0)!=null && game.getTeams().get(1)!=null) {
                if (game.getTeams().get(0).getId() == game.getTeams().get(1).getId()) {
                    model.addAttribute("message", "Teams cannot be the same. Try again");
                }
            }
            if(game.getDate()!=null) {
                if ((game.getDate().before(new Date()))) {
                    model.addAttribute("message", "Date must be in the future. Try again");
                }
            }
            model.addAttribute("adminController", "active");
            model.addAttribute("teams", teamService.listAll());
            model.addAttribute("competitions", competitionService.listAll());
            model.addAttribute("locations", locationService.listAll());
            return (game.getId()==0) ? "admin/games/newGameForm" : "admin/games/editGameForm";
        } else {
            redirectAttributes.addFlashAttribute("message", (game.getId()==0) ? "New game was created succesfully" : "Game was updated succesfully");
            Game gameSaved = gameService.save(game);
            return "redirect:/admin/games/page/1/date/asc";
        }
    }

    @RequestMapping("/admin/game/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        Game game = gameService.getById(id);
        model.addAttribute("idTeamA", game.getTeams().get(0).getId());
        model.addAttribute("idTeamB", game.getTeams().get(1).getId());
        model.addAttribute("adminController", "active");
        model.addAttribute("teams", teamService.listAll());
        model.addAttribute("competitions", competitionService.listAll());
        model.addAttribute("locations", locationService.listAll());
        model.addAttribute("game", game);
        return "admin/games/editGameForm";
    }

    @RequestMapping("/admin/game/delete/{id}")
    public String delete(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        gameService.delete(id);
        redirectAttributes.addFlashAttribute("message", "Game was deleted succesfully");
        return "redirect:/admin/games/page/1/date/asc";
    }


}
