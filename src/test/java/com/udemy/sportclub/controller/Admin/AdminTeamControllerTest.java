package com.udemy.sportclub.controller.Admin;

import com.udemy.sportclub.model.Competition;
import com.udemy.sportclub.model.Member;
import com.udemy.sportclub.model.Team;
import com.udemy.sportclub.service.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

        when(teamService.listAll()).thenReturn((List)teams);

        mockMvc.perform(get("/admin/teams"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/teams/list"))
                .andExpect(model().attribute("teams", hasSize(2)))
                .andExpect(model().attribute("adminController", "active"));
    }

    @Test
    public void testCreate() throws Exception {

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
    public void testSaveNewTeam() throws Exception {

        MultipartFile mockfile = new MockMultipartFile("file", "bert.jpg", "multipart/form-data", parseImage("bert.jpg") );

        when(teamService.save(new Team(), mockfile)).thenReturn(new Team());

        mockMvc.perform(MockMvcRequestBuilders.fileUpload("/admin/team/save")
                .file("file",  parseImage("bert.jpg"))
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
    public void testUpdateExistingTeam() throws Exception {

        MultipartFile mockfile = new MockMultipartFile("file", "bert.jpg", "multipart/form-data", parseImage("bert.jpg") );

        when(teamService.save(new Team(), mockfile)).thenReturn(new Team());

        mockMvc.perform(MockMvcRequestBuilders.fileUpload("/admin/team/save")
                .file("file", parseImage("bert.jpg"))
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
    public void testSaveNewTeamWithBindingErrors() throws Exception {

        when(competitionService.listAll()).thenReturn((List) competitions);
        when(memberService.listAvailableMembers()).thenReturn((List) availableMembers);
        when(memberService.listAvailableTeamCaptains()).thenReturn((List) availableTeamCaptains);

        mockMvc.perform(MockMvcRequestBuilders.fileUpload("/admin/team/save")
                .file("file", parseImage("bert.jpg")))
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
    public void testUpdateTeamWithBindingErrorsAndNoImage() throws Exception {

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
                .file("file", parseImage("bert.jpg"))
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
    public void testUpdateTeamWithBindingErrorsWithImage() throws Exception {

        Integer id = 1;

        Team returnTeam = new Team();
        returnTeam.setId(id);
        returnTeam.setName("testTeam");
        returnTeam.setTeamCaptain(new Member());
        returnTeam.setImage(parseImage("bert.jpg"));

        when(teamService.getById(id)).thenReturn(returnTeam);
        when(competitionService.listAll()).thenReturn((List) competitions);
        when(memberService.listAvailableMembers()).thenReturn((List) availableMembers);
        when(memberService.listAvailableTeamCaptains()).thenReturn((List) availableTeamCaptains);

        mockMvc.perform(MockMvcRequestBuilders.fileUpload("/admin/team/save")
                .file("file", parseImage("bert.jpg"))
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
    public void testEditWithTeamCaptain() throws Exception {
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
    public void testEditWithoutTeamCaptain() throws Exception {
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
    public void delete() throws Exception {
        Integer id = 1;

        mockMvc.perform(get("/admin/team/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.flash().attribute("message", "Team was deleted succesfully"))
                .andExpect(view().name("redirect:/admin/teams"));

        verify(teamService, times(1)).delete(id);
    }

    private byte[] parseImage(String filename){
        Path path = Paths.get("C:/Users/Dell/Pictures/sportclubapp/" + filename);
        byte[] data = null;
        try {
            data = Files.readAllBytes(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

}