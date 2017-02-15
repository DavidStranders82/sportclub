package com.udemy.sportclub.controller.Admin;

import com.udemy.sportclub.model.Competition;
import com.udemy.sportclub.model.Team;
import com.udemy.sportclub.service.CompetitionService;
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
public class AdminCompetitionController {

    private CompetitionService competitionService;
    private TeamService teamService;

    @Autowired
    public AdminCompetitionController(CompetitionService competitionService, TeamService teamService){
        this.competitionService = competitionService;
        this.teamService = teamService;
    }

    @RequestMapping("admin/competitions")
    public String list(Model model){
        model.addAttribute("adminController", "active");
        model.addAttribute("competitions", competitionService.list());
        return "admin/competition/list";
    }

    @RequestMapping("admin/competition/create")
    public String create(Model model) {
        model.addAttribute("adminController", "active");
        model.addAttribute("competition", new Competition());
        model.addAttribute("teams", teamService.list());
        return "admin/competition/newCompetitionForm";
    }

    @RequestMapping(value = "/admin/competition/save", method = RequestMethod.POST)
    public String save(@Valid Competition competition,
                       BindingResult bindingResult,
                       Model model,
                       @RequestParam("file") MultipartFile myFile,
                       RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors() || (competition.getMaxTeams()<competition.getTeams().size()) || (competition.getEndDate().before(competition.getStartDate()))) {
            if (competition.getMaxTeams()<competition.getTeams().size()){
                model.addAttribute("message", "Number of enrolled teams is greater than maximum number of allowed teams");
            }
            if (competition.getEndDate().before(competition.getStartDate())){
                model.addAttribute("message", "Enddate cannot before startdate. Try again");
            }
            model.addAttribute("adminController", "active");
            model.addAttribute("teams", teamService.list());
            return "admin/competition/newCompetitionForm";
        } else {
            Competition competitionSaved = competitionService.save(competition, myFile);
            if (!competition.getTeams().isEmpty()){
                for (Team team : competition.getTeams()){
                    Team teamTemp = teamService.get(team.getId());
                    teamTemp.getCompetitions().add(competitionSaved);
                    teamService.save(teamTemp);
                }
            }
            redirectAttributes.addFlashAttribute("message", competition.getName() + " was created succesfully");
            return "redirect:/admin/competitions";
        }
    }

    @RequestMapping(value = "/admin/competition/update", method = RequestMethod.POST)
    public String update(@Valid Competition competition,
                       BindingResult bindingResult,
                       Model model,
                       @RequestParam("file") MultipartFile myFile,
                       RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors() || (competition.getMaxTeams()<competition.getTeams().size()) || (competition.getEndDate().before(competition.getStartDate()))) {
            if (competition.getMaxTeams()<competition.getTeams().size()){
                model.addAttribute("message", "Number of enrolled teams is greater than maximum number of allowed teams");
            }
            if (competition.getEndDate().before(competition.getStartDate())){
                model.addAttribute("message", "Enddate cannot before startdate. Try again");
            }
            model.addAttribute("adminController", "active");
            model.addAttribute("teams", teamService.list());
            Competition competitionTemp = competitionService.get(competition.getId());
            String base64Encoded = Base64.encodeBase64String(competitionTemp.getImage());
            if (!base64Encoded.isEmpty()) {
                competition.setBase64image(base64Encoded);
            }
            return "admin/competition/editCompetitionForm";
        } else {
            competitionService.save(competition, myFile);
            List<Team> teams = teamService.list();
            for(Team team : teams){
                if (team.getCompetitions().contains(competition)){
                    team.getCompetitions().remove(competition);
                }
                if(competition.getTeams().contains(team)){
                    team.getCompetitions().add(competition);
                }
                teamService.save(team);
            }
            redirectAttributes.addFlashAttribute("message", competition.getName() + " was succesfully updated");
            return "redirect:/admin/competitions";
        }
    }

    @RequestMapping("/admin/competition/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        model.addAttribute("adminController", "active");
        model.addAttribute("competition", competitionService.get(id));
        model.addAttribute("teams", teamService.list());
        return "admin/competition/editCompetitionForm";
    }


    @RequestMapping("/admin/competition/delete/{id}")
    public String delete(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        Competition competition = competitionService.get(id);
        if(!competition.getTeams().isEmpty())
            for (Team team : competition.getTeams()){
                 Team teamTemp = teamService.get(team.getId());
                 teamTemp.getCompetitions().remove(competition);
                 teamService.save(teamTemp);
            }
        competitionService.delete(id);
        redirectAttributes.addFlashAttribute("message", "Competition was deleted succesfully");
        return "redirect:/admin/competitions";
    }

}
