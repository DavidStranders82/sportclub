package com.udemy.sportclub.controller.Admin;

import com.udemy.sportclub.model.Competition;
import com.udemy.sportclub.model.Game;
import com.udemy.sportclub.model.Member;
import com.udemy.sportclub.model.Team;
import com.udemy.sportclub.service.CompetitionService;
import com.udemy.sportclub.service.MemberService;
import com.udemy.sportclub.service.TeamService;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by Dell on 13-2-2017.
 */
public class AdminTeamControllerTest {

    @Mock
    private MemberService memberService;
    @Mock
    private TeamService teamService;
    @Mock
    private CompetitionService competitionService;

    @InjectMocks
    private AdminTeamController adminTeamController;

    private MockMvc mockMvc;

    private List<Competition> competitions;
    private List<Member> availableMembers;
    private List<Member> availableTeamCaptains;
    private Team team;

    @Before
    public void setup(){
        competitions = new ArrayList<>();
        competitions.add(new Competition());
        competitions.add(new Competition());

        availableMembers = new ArrayList<>();
        availableMembers.add(new Member());
        availableMembers.add(new Member());

        availableTeamCaptains = new ArrayList<>();
        availableTeamCaptains.add(new Member());
        availableTeamCaptains.add(new Member());

        team = new Team();

        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(adminTeamController).build();
    }

    @Test
    public void testList() throws Exception {

        List<Team> teams = new ArrayList<>();
        teams.add(new Team());
        teams.add(new Team());

        when(teamService.list()).thenReturn((List)teams);

        mockMvc.perform(get("/admin/teams"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/teams/list"))
                .andExpect(model().attribute("teams", hasSize(2)));
    }

    @Test
    public void testCreate() throws Exception {

        verifyZeroInteractions(teamService);

        when(competitionService.list()).thenReturn((List) competitions);
        when(memberService.listAvailableMembers()).thenReturn((List) availableMembers);
        when(memberService.listAvailableTeamCaptains()).thenReturn((List) availableTeamCaptains);

        mockMvc.perform(get("/admin/team/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/teams/newTeamForm"))
                .andExpect(model().attribute("team", instanceOf(Team.class)))
                .andExpect(model().attribute("competitions", hasSize(2)))
                .andExpect(model().attribute("availableMembers", hasSize(2)))
                .andExpect(model().attribute("teamCaptains", hasSize(2)));
    }

    @Ignore
    @Test
    public void testSaveOrUpdate() throws Exception {
        Integer id = 1;
        String name = "testTeam";
        Member teamCaptain = new Member();
        List<Member> members = new ArrayList<>();
        members.add(new Member());
        members.add(new Member());
        List<Game> games = new ArrayList<>();
        games.add(new Game());
        games.add(new Game());
        List<Competition> competitions = new ArrayList<>();
        competitions.add(new Competition());
        competitions.add(new Competition());
        byte[] image = parseImage("bert");

        MultipartFile file = new MockMultipartFile("testPicture", "bert.jpg", "image/jpg", parseImage("bert") );

        Team returnTeam = new Team();
        returnTeam.setId(id);
        returnTeam.setName(name);
        returnTeam.setTeamCaptain(teamCaptain);
        returnTeam.setMembers(members);
        returnTeam.setGames(games);
        returnTeam.setCompetitions(competitions);
        returnTeam.setImage(image);

        when(teamService.save(new Team(), file)).thenReturn(returnTeam);

        mockMvc.perform(post("/admin/team/save)")
                .param("id", "1")
                .param("name", "testTeam"))
//                    .andExpect(status().isOk())
//                    .andExpect(view().name("/admin/teams/editTeamForm"))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(view().name("redirect:/admin/teams"))
                    .andExpect(model().attribute("team", instanceOf(Team.class)))
                    .andExpect(model().attribute("team", hasProperty("id", is(id))))
                    .andExpect(model().attribute("team", hasProperty("name", is(name))));

    }

    @Test
    public void testEditWithTeamCaptain() throws Exception {
        Integer id = 1;

        team.setTeamCaptain(new Member());
        when(teamService.get(id)).thenReturn(team);
        when(competitionService.list()).thenReturn((List) competitions);
        when(memberService.listAvailableMembers()).thenReturn((List) availableMembers);
        when(memberService.listAvailableTeamCaptains()).thenReturn((List) availableTeamCaptains);

        mockMvc.perform(get("/admin/team/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/teams/editTeamForm"))
                .andExpect(model().attribute("team", instanceOf(Team.class)))
                .andExpect(model().attribute("competitions", hasSize(2)))
                .andExpect(model().attribute("availableMembers", hasSize(2)))
                .andExpect(model().attribute("teamCaptains", hasSize(3)));
    }

    @Test
    public void testEditWithoutTeamCaptain() throws Exception {
        Integer id = 1;

        when(teamService.get(id)).thenReturn(team);
        when(competitionService.list()).thenReturn((List) competitions);
        when(memberService.listAvailableMembers()).thenReturn((List) availableMembers);
        when(memberService.listAvailableTeamCaptains()).thenReturn((List) availableTeamCaptains);

        mockMvc.perform(get("/admin/team/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/teams/editTeamForm"))
                .andExpect(model().attribute("team", instanceOf(Team.class)))
                .andExpect(model().attribute("competitions", hasSize(2)))
                .andExpect(model().attribute("availableMembers", hasSize(2)))
                .andExpect(model().attribute("teamCaptains", hasSize(2)));
    }

    @Test
    public void delete() throws Exception {
        Integer id = 1;

        mockMvc.perform(get("/admin/team/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.flash().attribute("message", "Team was deleted succesfully"))
                .andExpect(view().name("redirect:/admin/teams"));

        verify(teamService, times(1)).delete(id);

    }

    private byte[] parseImage(String filename){
        Path path = Paths.get("C:/Users/Dell/Pictures/sportclubapp/" + filename + ".jpg");
        byte[] data = null;
        try {
            data = Files.readAllBytes(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

}