package com.davidstranders.sportclub.controller.Admin;

import com.davidstranders.sportclub.DataLoader;
import com.davidstranders.sportclub.model.Competition;
import com.davidstranders.sportclub.model.Member;
import com.davidstranders.sportclub.model.Team;
import com.davidstranders.sportclub.service.CompetitionService;
import com.davidstranders.sportclub.service.MemberService;
import com.davidstranders.sportclub.service.TeamService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by DS on 13-2-2017.
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

    private static final byte[]IMAGE = DataLoader.parseImage("bert");

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
    public void listAllTeams() throws Exception {

        List<Team> teams = new ArrayList<>();
        teams.add(new Team());
        teams.add(new Team());

        when(teamService.listAll()).thenReturn((List)teams);

        mockMvc.perform(get("/admin/teams"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/teams/list"))
                .andExpect(model().attribute("teams", hasSize(2)))
                .andExpect(model().attribute("adminController", "active"));
    }

    @Test
    public void createTeam() throws Exception {

        verifyZeroInteractions(teamService);

        when(competitionService.listAll()).thenReturn((List) competitions);
        when(memberService.listAvailableMembers()).thenReturn((List) availableMembers);
        when(memberService.listAvailableTeamCaptains()).thenReturn((List) availableTeamCaptains);

        mockMvc.perform(get("/admin/team/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/teams/newTeamForm"))
                .andExpect(model().attribute("team", instanceOf(Team.class)))
                .andExpect(model().attribute("adminController", "active"))
                .andExpect(model().attribute("competitions", hasSize(2)))
                .andExpect(model().attribute("availableMembers", hasSize(2)))
                .andExpect(model().attribute("teamCaptains", hasSize(2)));
    }

    @Test
    public void saveNewTeam() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.fileUpload("/admin/team/save")
                .file("file", IMAGE)
                .param("name", "testTeam"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/admin/teams"))
                .andExpect(MockMvcResultMatchers.flash().attribute("message", "testTeam was created succesfully"));

        ArgumentCaptor<Team> boundTeam = ArgumentCaptor.forClass(Team.class);
        ArgumentCaptor<MultipartFile> boundMultipartFile = ArgumentCaptor.forClass(MultipartFile.class);
        verify(teamService).save(boundTeam.capture(), boundMultipartFile.capture());

        Assert.assertEquals("testTeam", boundTeam.getValue().getName());
    }

    @Test
    public void updateExistingTeam() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.fileUpload("/admin/team/save")
                .file("file", IMAGE)
                .param("id", "1")
                .param("name", "testTeam"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/admin/teams"))
                .andExpect(MockMvcResultMatchers.flash().attribute("message", "testTeam was updated succesfully"));

        ArgumentCaptor<Team> boundTeam = ArgumentCaptor.forClass(Team.class);
        ArgumentCaptor<MultipartFile> boundMultipartFile = ArgumentCaptor.forClass(MultipartFile.class);
        verify(teamService).save(boundTeam.capture(), boundMultipartFile.capture());

        Assert.assertEquals("testTeam", boundTeam.getValue().getName());

        verify(teamService, times(1)).save(boundTeam.capture(), boundMultipartFile.capture());
    }

    @Test
    public void saveNewTeamWithBindingErrors() throws Exception {

        when(competitionService.listAll()).thenReturn((List) competitions);
        when(memberService.listAvailableMembers()).thenReturn((List) availableMembers);
        when(memberService.listAvailableTeamCaptains()).thenReturn((List) availableTeamCaptains);

        mockMvc.perform(MockMvcRequestBuilders.fileUpload("/admin/team/save")
                .file("file", IMAGE))
                    .andExpect(status().isOk())
                    .andExpect(view().name("admin/teams/newTeamForm"))
                    .andExpect(model().attribute("adminController", "active"))
                    .andExpect(model().attribute("competitions", hasSize(2)))
                    .andExpect(model().attribute("availableMembers", hasSize(2)))
                    .andExpect(model().attribute("teamCaptains", hasSize(2)))
                        .andExpect(model().attribute("team", instanceOf(Team.class)))
                        .andExpect(model().attribute("team", hasProperty("id", is(0))));

    }

    @Test
    public void updateTeamWithBindingErrorsAndNoImage() throws Exception {

        Integer id = 1;

        Team returnTeam = new Team();
        returnTeam.setId(id);
        returnTeam.setName("testTeam");
        returnTeam.setTeamCaptain(new Member());

        when(teamService.getById(id)).thenReturn(returnTeam);
        when(competitionService.listAll()).thenReturn((List) competitions);
        when(memberService.listAvailableMembers()).thenReturn((List) availableMembers);
        when(memberService.listAvailableTeamCaptains()).thenReturn((List) availableTeamCaptains);

        mockMvc.perform(MockMvcRequestBuilders.fileUpload("/admin/team/save")
                .file("file", IMAGE)
                .param("id", "1"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("admin/teams/editTeamForm"))
                    .andExpect(model().attribute("adminController", "active"))
                    .andExpect(model().attribute("competitions", hasSize(2)))
                    .andExpect(model().attribute("availableMembers", hasSize(2)))
                    .andExpect(model().attribute("teamCaptains", hasSize(2)))
                        .andExpect(model().attribute("team", instanceOf(Team.class)))
                        .andExpect(model().attribute("team", hasProperty("id", is(1))))
                        .andExpect(model().attribute("team", hasProperty("base64image", is(nullValue()))));;

    }

    @Test
    public void updateTeamWithBindingErrorsWithImage() throws Exception {

        Integer id = 1;

        Team returnTeam = new Team();
        returnTeam.setId(id);
        returnTeam.setName("testTeam");
        returnTeam.setTeamCaptain(new Member());
        returnTeam.setImage(IMAGE);

        when(teamService.getById(id)).thenReturn(returnTeam);
        when(competitionService.listAll()).thenReturn((List) competitions);
        when(memberService.listAvailableMembers()).thenReturn((List) availableMembers);
        when(memberService.listAvailableTeamCaptains()).thenReturn((List) availableTeamCaptains);

        mockMvc.perform(MockMvcRequestBuilders.fileUpload("/admin/team/save")
                .file("file", IMAGE)
                .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/teams/editTeamForm"))
                .andExpect(model().attribute("adminController", "active"))
                .andExpect(model().attribute("competitions", hasSize(2)))
                .andExpect(model().attribute("availableMembers", hasSize(2)))
                .andExpect(model().attribute("teamCaptains", hasSize(2)))
                .andExpect(model().attribute("team", instanceOf(Team.class)))
                .andExpect(model().attribute("team", hasProperty("id", is(1))))
                .andExpect(model().attribute("team", hasProperty("base64image", is(notNullValue()))));;

        verify(teamService, times(0)).save(returnTeam);
    }

    @Test
    public void editWithTeamCaptain() throws Exception {
        Integer id = 1;

        team.setTeamCaptain(new Member());
        when(teamService.getById(id)).thenReturn(team);
        when(competitionService.listAll()).thenReturn((List) competitions);
        when(memberService.listAvailableMembers()).thenReturn((List) availableMembers);
        when(memberService.listAvailableTeamCaptains()).thenReturn((List) availableTeamCaptains);

        mockMvc.perform(get("/admin/team/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/teams/editTeamForm"))
                .andExpect(model().attribute("team", instanceOf(Team.class)))
                .andExpect(model().attribute("adminController", "active"))
                .andExpect(model().attribute("competitions", hasSize(2)))
                .andExpect(model().attribute("availableMembers", hasSize(2)))
                .andExpect(model().attribute("teamCaptains", hasSize(3)));
    }

    @Test
    public void editWithoutTeamCaptain() throws Exception {
        Integer id = 1;

        when(teamService.getById(id)).thenReturn(team);
        when(competitionService.listAll()).thenReturn((List) competitions);
        when(memberService.listAvailableMembers()).thenReturn((List) availableMembers);
        when(memberService.listAvailableTeamCaptains()).thenReturn((List) availableTeamCaptains);

        mockMvc.perform(get("/admin/team/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/teams/editTeamForm"))
                .andExpect(model().attribute("team", instanceOf(Team.class)))
                .andExpect(model().attribute("adminController", "active"))
                .andExpect(model().attribute("competitions", hasSize(2)))
                .andExpect(model().attribute("availableMembers", hasSize(2)))
                .andExpect(model().attribute("teamCaptains", hasSize(2)));
    }

    @Test
    public void deleteTeam() throws Exception {
        Integer id = 1;

        mockMvc.perform(get("/admin/team/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.flash().attribute("message", "Team was deleted succesfully"))
                .andExpect(view().name("redirect:/admin/teams"));

        verify(teamService, times(1)).delete(id);
    }
}