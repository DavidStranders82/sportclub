package com.udemy.sportclub.controller.Admin;

import com.udemy.sportclub.model.Competition;
import com.udemy.sportclub.model.Team;
import com.udemy.sportclub.service.CompetitionService;
import com.udemy.sportclub.service.CompetitionServiceImpl;
import com.udemy.sportclub.service.TeamService;
import com.udemy.sportclub.service.TeamServiceImpl;
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
 * Created by DS on 22-1-2017.
 */
@Secured("ROLE_ADMIN")
@Controller
public class AdminCompetitionController {

    private CompetitionService competitionService;
    private TeamService teamService;

    @Autowired
    public AdminCompetitionController(CompetitionService competitionService, TeamService teamService) {
        this.competitionService = competitionService;
        this.teamService = teamService;
    }

    @RequestMapping("admin/competitions")
    public String list(Model model) {
        model.addAttribute("adminController", "active");
        model.addAttribute("competitions", competitionService.listAll());
        return "admin/competition/list";
    }

    @RequestMapping("admin/competition/create")
    public String create(Model model) {
        model.addAttribute("adminController", "active");
        model.addAttribute("competition", new Competition());
        model.addAttribute("teams", teamService.listAll());
        return "admin/competition/newCompetitionForm";
    }

    @RequestMapping(value = "/admin/competition/save", method = RequestMethod.POST)
    public String save(@Valid Competition competition,
                       BindingResult bindingResult,
                       Model model,
                       @RequestParam("file") MultipartFile myFile,
                       RedirectAttributes redirectAttributes) {


        if (bindingResult.hasErrors()) {
            addAttributes(model);
            return "admin/competition/newCompetitionForm";
        }

        if (competition.getTeams() != null && competition.getMaxTeams() < competition.getTeams().size()) {
            model.addAttribute("message", "Number of enrolled teams is greater than maximum number of allowed teams");
            addAttributes(model);
            return "admin/competition/newCompetitionForm";
        }

        if (competition.getEndDate() != null && competition.getStartDate() != null &&
                (competition.getEndDate().before(competition.getStartDate()))) {
            model.addAttribute("message", "Enddate cannot before startdate. Try again");
            addAttributes(model);
            return "admin/competition/newCompetitionForm";
        }

        Competition competitionSaved = competitionService.save(competition, myFile);
        if (competition.getTeams() != null && !competition.getTeams().isEmpty()) {
            for (Team team : competition.getTeams()) {
                Team teamTemp = teamService.getById(team.getId());
                teamTemp.getCompetitions().add(competitionSaved);
                teamService.save(teamTemp);
            }
        }
        redirectAttributes.addFlashAttribute("message", competition.getName() + " was created succesfully");
        return "redirect:/admin/competitions";

    }

    @RequestMapping(value = "/admin/competition/update", method = RequestMethod.POST)
    public String update(@Valid Competition competition,
                         BindingResult bindingResult,
                         Model model,
                         @RequestParam("file") MultipartFile myFile,
                         RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            addAttributes(model);
            resetBase64Image(competition);
            return "admin/competition/editCompetitionForm";
        }
        if (competition.getTeams() != null && competition.getMaxTeams() < competition.getTeams().size()) {
            model.addAttribute("message", "Number of enrolled teams is greater than maximum number of allowed teams");
            addAttributes(model);
            resetBase64Image(competition);
            return "admin/competition/editCompetitionForm";
        }

        if (competition.getEndDate() != null && competition.getStartDate() != null &&
                (competition.getEndDate().before(competition.getStartDate()))) {
            model.addAttribute("message", "Enddate cannot before startdate. Try again");
            addAttributes(model);
            resetBase64Image(competition);
            return "admin/competition/editCompetitionForm";
        }

        competitionService.save(competition, myFile);
        List<Team> teams = teamService.listAll();
        for (Team team : teams) {
            if (team.getCompetitions().contains(competition)) {
                team.getCompetitions().remove(competition);
            }
            if (competition.getTeams().contains(team)) {
                team.getCompetitions().add(competition);
            }
            teamService.save(team);
        }
        redirectAttributes.addFlashAttribute("message", competition.getName() + " was succesfully updated");
        return "redirect:/admin/competitions";
    }

    @RequestMapping("/admin/competition/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        model.addAttribute("adminController", "active");
        model.addAttribute("competition", competitionService.getById(id));
        model.addAttribute("teams", teamService.listAll());
        return "admin/competition/editCompetitionForm";
    }


    @RequestMapping("/admin/competition/delete/{id}")
    public String delete(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        Competition competition = competitionService.getById(id);
        if (!competition.getTeams().isEmpty())
            for (Team team : competition.getTeams()) {
                Team teamTemp = teamService.getById(team.getId());
                teamTemp.getCompetitions().remove(competition);
                teamService.save(teamTemp);
            }
        competitionService.delete(id);
        redirectAttributes.addFlashAttribute("message", "Competition was deleted succesfully");
        return "redirect:/admin/competitions";
    }

    private void addAttributes(Model model) {
        model.addAttribute("adminController", "active");
        model.addAttribute("teams", teamService.listAll());
    }

    private void resetBase64Image(Competition competition) {
        Competition competitionTemp = competitionService.getById(competition.getId());
        if (competitionTemp.getImage() != null) {
            String base64Encoded = Base64.encodeBase64String(competitionTemp.getImage());
            competition.setBase64image(base64Encoded);
        }
    }

}
