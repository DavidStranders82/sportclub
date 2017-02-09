package com.udemy.sportclub.controller.Admin;

import com.udemy.sportclub.model.Game;
import com.udemy.sportclub.model.Location;
import com.udemy.sportclub.service.GameService;
import com.udemy.sportclub.service.LocationService;
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
import java.util.List;

/**
 * Created by Dell on 5-2-2017.
 */

@Secured("ROLE_ADMIN")
@Controller
public class AdminLocationController {

    private LocationService locationService;
    private GameService gameService;

    @Autowired
    public AdminLocationController(LocationService locationService, GameService gameService){
        this.locationService = locationService;
        this.gameService = gameService;
    }

    @RequestMapping("admin/locations")
    public String list(Model model){
        model.addAttribute("adminController", "active");
        model.addAttribute("locations", locationService.list());
        return "admin/locations/list";
    }

    @RequestMapping("admin/location/create")
    public String create(Model model) {
        model.addAttribute("adminController", "active");
        model.addAttribute("location", new Location());
        return "admin/locations/newLocationForm";
    }

    @RequestMapping(value = "/admin/location/save", method = RequestMethod.POST)
    public String save(@Valid Location location,
                       BindingResult bindingResult,
                       Model model,
                       RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("adminController", "active");
            return "admin/locations/newLocationForm";
        } else {
            Location locationSaved = locationService.save(location);

            redirectAttributes.addFlashAttribute("message", "New location was created succesfully");
            return "redirect:/admin/locations";
        }
    }

    @RequestMapping(value = "/admin/location/update", method = RequestMethod.POST)
    public String update(@Valid Location location,
                       BindingResult bindingResult,
                       Model model,
                       RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("adminController", "active");
            return "admin/locations/editLocationForm";
        } else {
            Location locationSaved = locationService.save(location);

            redirectAttributes.addFlashAttribute("message", "Location was updated succesfully");
            return "redirect:/admin/locations";
        }
    }

    @RequestMapping("/admin/location/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        model.addAttribute("adminController", "active");
        model.addAttribute("location", locationService.get(id));
        return "admin/locations/editLocationForm";
    }


    @RequestMapping("/admin/location/delete/{id}")
    public String delete(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        List<Game> games = gameService.list();
            for (Game game : games){
                if (game.getLocation().getId()== id){
                    game.setLocation(null);
                    gameService.save(game);
                }
            }
        locationService.delete(id);
        redirectAttributes.addFlashAttribute("message", "Location was deleted succesfully");
        return "redirect:/admin/locations";
    }


}
