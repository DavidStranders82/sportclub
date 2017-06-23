package com.davidstranders.sportclub.controller.Admin;

import com.davidstranders.sportclub.DataLoader;
import com.davidstranders.sportclub.model.Competition;
import com.davidstranders.sportclub.model.Game;
import com.davidstranders.sportclub.model.Location;
import com.davidstranders.sportclub.model.Team;
import com.davidstranders.sportclub.service.CompetitionService;
import com.davidstranders.sportclub.service.GameService;
import com.davidstranders.sportclub.service.LocationService;
import com.davidstranders.sportclub.service.TeamService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by DS on 13-2-2017.
 */
public class AdminGameControllerTest {

    @Mock
    private CompetitionService competitionService;
    @Mock
    private TeamService teamService;
    @Mock
    private GameService gameService;
    @Mock
    private LocationService locationService;

    @InjectMocks
    private AdminGameController adminGameController;
    private MockMvc mockMvc;

    private Game game1 = new Game();
    private Game game2 = new Game();
    private Game game3 = new Game();
    private List<Game> games = new ArrayList<>(Arrays.asList(game1, game2, game3));

    private Team teamA = new Team();
    private Team teamB = new Team();
    private Team teamC = new Team();
    private List<Team> teams = new ArrayList<>(Arrays.asList(teamA, teamB, teamC));

    private Competition competition1 = new Competition();
    private Competition competition2 = new Competition();
    private List<Competition> competitions = new ArrayList<>(Arrays.asList(competition1, competition2));

    private Location location1 = new Location();
    private Location location2 = new Location();
    private List<Location> locations = new ArrayList<>(Arrays.asList(location1, location2));

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(adminGameController).build();
    }

    @Test
    public void list() throws Exception {
        mockMvc.perform(get("/admin/games"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/admin/games/page/1/date/asc"));
    }

    @Test
    public void listAllGames() throws Exception {

        Date date_01_08_2017 = DataLoader.parseDate("01/08/2017");
        Date date_12_05_2017 = DataLoader.parseDate("12/05/2017");
        Date date_29_05_2017 = DataLoader.parseDate("29/05/2017");

        game1.setDate(date_01_08_2017);
        game2.setDate(date_12_05_2017);
        game3.setDate(date_29_05_2017);

        when(gameService.listAll()).thenReturn((List)games);

        MvcResult result = mockMvc.perform(get("/admin/games/page/1/date/asc"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/games/list"))
                .andExpect(model().attribute("games", instanceOf(PagedListHolder.class)))
                .andExpect(model().attribute("games", hasProperty("source", hasSize(3))))
                .andExpect(model().attribute("beginIndex", 1))
                .andExpect(model().attribute("endIndex", 1))
                .andExpect(model().attribute("currentIndex", 1))
                .andExpect(model().attribute("totalPageCount", 1))
                .andExpect(model().attribute("pageNumber", 1))
                .andExpect(model().attribute("sortColumn", "date"))
                .andExpect(model().attribute("sortOrder", "asc"))
                .andReturn();

        PagedListHolder pagedListHolder = (PagedListHolder) result.getModelAndView().getModel().get("games");
        List<Game> gamesAscendingOrder = pagedListHolder.getSource();
        assertEquals( date_12_05_2017, gamesAscendingOrder.get(0).getDate());
        assertEquals( date_29_05_2017, gamesAscendingOrder.get(1).getDate());
        assertEquals( date_01_08_2017, gamesAscendingOrder.get(2).getDate());
    }

    @Test
    public void createGame() throws Exception {

        verifyZeroInteractions(gameService);

        when(teamService.listAll()).thenReturn((List) teams);
        when(locationService.listAll()).thenReturn((List) locations);
        when(competitionService.listAll()).thenReturn((List) competitions);

        mockMvc.perform(get("/admin/game/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/games/newGameForm"))
                .andExpect(model().attribute("game", instanceOf(Game.class)))
                .andExpect(model().attribute("adminController", "active"))
                .andExpect(model().attribute("teams", hasSize(3)))
                .andExpect(model().attribute("competitions", hasSize(2)))
                .andExpect(model().attribute("locations", hasSize(2)));
    }

    @Test
    public void delete() throws Exception {

        mockMvc.perform(get("/admin/game/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.flash().attribute("message", "Game was deleted succesfully"))
                .andExpect(view().name("redirect:/admin/games/page/1/date/asc"));

        verify(gameService, times(1)).delete(1);
    }

    @Test
    public void edit() throws Exception {

        game1.setId(1);
        teamA.setId(2);
        teamB.setId(3);
        game1.setTeams(new ArrayList<>(Arrays.asList(teamA, teamB)));

        when(gameService.getById(1)).thenReturn(game1);
        when(teamService.listAll()).thenReturn((List) teams);
        when(competitionService.listAll()).thenReturn((List) competitions);
        when(locationService.listAll()).thenReturn((List) locations);

        mockMvc.perform(get("/admin/game/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/games/editGameForm"))
                .andExpect(model().attribute("game", instanceOf(Game.class)))
                .andExpect(model().attribute("game", hasProperty("id", is(1))))
                .andExpect(model().attribute("idTeamA", is(2)))
                .andExpect(model().attribute("idTeamB", is(3)))
                .andExpect(model().attribute("adminController", "active"))
                .andExpect(model().attribute("teams", hasSize(3)))
                .andExpect(model().attribute("competitions", hasSize(2)))
                .andExpect(model().attribute("locations", hasSize(2)));
    }

    @Test
    public void saveNewGame() throws Exception{

        mockMvc.perform(post("/admin/game/save")
                .param("date", "01/10/2018"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/admin/games/page/1/date/asc"))
                .andExpect(MockMvcResultMatchers.flash().attribute("message", "New game was created succesfully"));

        ArgumentCaptor<Game> boundGame = ArgumentCaptor.forClass(Game.class);
        verify(gameService).save(boundGame.capture());

        Assert.assertEquals(DataLoader.parseDate("01/10/2018"), boundGame.getValue().getDate());
    }

    @Test
    public void saveNewGameWithBindingErrors() throws Exception{

        when(teamService.listAll()).thenReturn((List) teams);
        when(competitionService.listAll()).thenReturn((List) competitions);
        when(locationService.listAll()).thenReturn((List) locations);

        mockMvc.perform(post("/admin/game/save")
                .param("id", "0"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/games/newGameForm"))
                .andExpect(model().attribute("adminController", "active"))
                .andExpect(model().attribute("teams", hasSize(3)))
                .andExpect(model().attribute("competitions", hasSize(2)))
                .andExpect(model().attribute("locations", hasSize(2)))
                .andExpect(model().attribute("message", "Please fill in all necessary fields"));
    }

    @Test
    public void editGameWithBindingErrors() throws Exception{

        when(teamService.listAll()).thenReturn((List) teams);
        when(competitionService.listAll()).thenReturn((List) competitions);
        when(locationService.listAll()).thenReturn((List) locations);

        mockMvc.perform(post("/admin/game/save")
                .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/games/editGameForm"))
                .andExpect(model().attribute("adminController", "active"))
                .andExpect(model().attribute("teams", hasSize(3)))
                .andExpect(model().attribute("competitions", hasSize(2)))
                .andExpect(model().attribute("locations", hasSize(2)))
                .andExpect(model().attribute("message", "Please fill in all necessary fields"));
    }

    @Test
    public void saveNewGameWithWrongDate() throws Exception{

        when(teamService.listAll()).thenReturn((List) teams);
        when(competitionService.listAll()).thenReturn((List) competitions);
        when(locationService.listAll()).thenReturn((List) locations);

        mockMvc.perform(post("/admin/game/save")
                .param("id", "0")
                .param("date", "01/10/2016"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/games/newGameForm"))
                .andExpect(model().attribute("adminController", "active"))
                .andExpect(model().attribute("teams", hasSize(3)))
                .andExpect(model().attribute("competitions", hasSize(2)))
                .andExpect(model().attribute("locations", hasSize(2)))
                .andExpect(model().attribute("message", "Date must be in the future. Try again"));
    }

    @Test
    public void editGameWithWrongDate() throws Exception{

        when(teamService.listAll()).thenReturn((List) teams);
        when(competitionService.listAll()).thenReturn((List) competitions);
        when(locationService.listAll()).thenReturn((List) locations);

        mockMvc.perform(post("/admin/game/save")
                .param("id", "1")
                .param("date", "01/10/2016"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/games/editGameForm"))
                .andExpect(model().attribute("adminController", "active"))
                .andExpect(model().attribute("teams", hasSize(3)))
                .andExpect(model().attribute("competitions", hasSize(2)))
                .andExpect(model().attribute("locations", hasSize(2)))
                .andExpect(model().attribute("message", "Date must be in the future. Try again"));
    }

    // TODO: adding tests for checking selected teams. I haven't figured out how to set teams as parameters to the game.teams list...

}