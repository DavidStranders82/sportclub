package com.udemy.sportclub.controller.Admin;

import com.udemy.sportclub.model.Member;
import com.udemy.sportclub.model.Role;
import com.udemy.sportclub.model.Team;
import com.udemy.sportclub.service.MemberService;
import com.udemy.sportclub.service.RoleService;
import com.udemy.sportclub.service.TeamService;
import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by DS on 13-2-2017.
 */
public class AdminMemberControllerTest {

    @Mock
    private MemberService memberService;
    @Mock
    private TeamService teamService;
    @Mock
    private RoleService roleService;

    @InjectMocks
    private AdminMemberController adminMemberController;
    private MockMvc mockMvc;

    private Member kees;
    private Member jan;
    private Member piet;
    private List<Member> members;

    private Role member;
    private Role admin;
    private List<Role> roles;

    private Team teamA;
    private Team teamB;
    private Team teamC;
    private List<Team> teams;

    @Before
    public void setup(){

        kees = new Member();
        jan = new Member();
        piet = new Member();
        members = new ArrayList<>(Arrays.asList(piet, kees, jan));

        member = new Role("member");
        admin = new Role("admin");
        roles = new ArrayList<>(Arrays.asList(member, admin));

        teamA = new Team();
        teamB = new Team();
        teamC = new Team();
        teams = new ArrayList<>(Arrays.asList(teamA, teamB, teamC));

        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(adminMemberController).build();
    }

    @Test
    public void list() throws Exception {
        mockMvc.perform(get("/admin/members"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/admin/members/page/1/lastName/asc"));
    }

    @Test
    public void listAllMembers() throws Exception {

        jan.setLastName("Jansen");
        kees.setLastName("Keesen");
        piet.setLastName("Pietersen");

        when(memberService.findAllByOrderByLastName()).thenReturn((List)members);

        MvcResult result = mockMvc.perform(get("redirect:/admin/members/page/1/lastName/asc"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/members/list"))
                .andExpect(model().attribute("members", instanceOf(PagedListHolder.class)))
                .andExpect(model().attribute("members", hasProperty("source", hasSize(3))))
                .andExpect(model().attribute("members", hasProperty("source", hasSize(3))))
                .andExpect(model().attribute("beginIndex", 1))
                .andExpect(model().attribute("endIndex", 1))
                .andExpect(model().attribute("currentIndex", 1))
                .andExpect(model().attribute("totalPageCount", 1))
                .andExpect(model().attribute("pageNumber", 1))
                .andExpect(model().attribute("sortColumn", "lastName"))
                .andExpect(model().attribute("sortOrder", "asc"))
                .andReturn();

        PagedListHolder pagedListHolder = (PagedListHolder) result.getModelAndView().getModel().get("members");
        List<Member> membersAscendingOrder = pagedListHolder.getSource();
        assertEquals( "Jansen", membersAscendingOrder.get(0).getLastName());
        assertEquals("Keesen", membersAscendingOrder.get(1).getLastName());
        assertEquals("Pietersen", membersAscendingOrder.get(2).getLastName());
    }

    @Test
    public void createMember() throws Exception {

        verifyZeroInteractions(memberService);

        when(teamService.listAll()).thenReturn((List) teams);
        when(roleService.listAll()).thenReturn((List) roles);

        mockMvc.perform(get("/admin/member/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/members/newMemberForm"))
                .andExpect(model().attribute("member", instanceOf(Member.class)))
                .andExpect(model().attribute("adminController", "active"))
                .andExpect(model().attribute("teams", hasSize(3)))
                .andExpect(model().attribute("roles", hasSize(2)));
    }

    @Test
    public void delete() throws Exception {

        mockMvc.perform(get("/admin/member/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.flash().attribute("message", "Member was deleted succesfully"))
                .andExpect(view().name("redirect:/admin/members/page/1/lastName/asc"));

        verify(memberService, times(1)).delete(1);
    }

    @Test
    public void saveNewMember() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.fileUpload("/admin/member/save")
                .file("file",  parseImage("bert.jpg"))
                .param("firstName", "Piet")
                .param("email", "test@email.com")
                .param("lastName", "testMember")
                .param("password", "12345")
                .param("confirmPw", "12345")
                .param("roles", "1")
                .param("memberSince", "01/01/2010"))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(view().name("redirect:/admin/members/page/1/lastName/asc"))
                    .andExpect(MockMvcResultMatchers.flash().attribute("message", "Piet testMember was created succesfully"));

        ArgumentCaptor<Member> boundMember = ArgumentCaptor.forClass(Member.class);
        ArgumentCaptor<MultipartFile> boundMultipartFile = ArgumentCaptor.forClass(MultipartFile.class);
        verify(memberService).save(boundMember.capture(), boundMultipartFile.capture());
        verifyZeroInteractions(teamService);

        Assert.assertEquals("testMember", boundMember.getValue().getLastName());
    }

    @Test
    public void saveNewMemberWithBindingErrors() throws Exception {

        when(teamService.listAll()).thenReturn((List) teams);
        when(roleService.listAll()).thenReturn((List) roles);

        mockMvc.perform(MockMvcRequestBuilders.fileUpload("/admin/member/save")
                .file("file",  parseImage("bert.jpg"))
                .param("firstName", "Piet"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("admin/members/newMemberForm"))
                    .andExpect(model().attribute("adminController", "active"))
                    .andExpect(model().attribute("teams", hasSize(3)))
                    .andExpect(model().attribute("roles", hasSize(2)));
    }

    @Test
    public void saveNewMemberWithNoPassWord() throws Exception {

        when(teamService.listAll()).thenReturn((List) teams);
        when(roleService.listAll()).thenReturn((List) roles);

        mockMvc.perform(MockMvcRequestBuilders.fileUpload("/admin/member/save")
                .file("file",  parseImage("bert.jpg"))
                .param("firstName", "Piet")
                .param("email", "test@email.com")
                .param("lastName", "testMember")
                .param("roles", "1")
                .param("memberSince", "01/01/2010"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("admin/members/newMemberForm"))
                    .andExpect(model().attribute("adminController", "active"))
                    .andExpect(model().attribute("teams", hasSize(3)))
                    .andExpect(model().attribute("roles", hasSize(2)))
                    .andExpect(model().attribute("message", is("Password is required. Try again")));
    }

    @Test
    public void saveNewMemberWithNoEqualPasswords() throws Exception {

        when(teamService.listAll()).thenReturn((List) teams);
        when(roleService.listAll()).thenReturn((List) roles);

        mockMvc.perform(MockMvcRequestBuilders.fileUpload("/admin/member/save")
                .file("file",  parseImage("bert.jpg"))
                .param("firstName", "Piet")
                .param("email", "test@email.com")
                .param("lastName", "testMember")
                .param("password", "12345")
                .param("confirmPw", "1234")
                .param("roles", "1")
                .param("memberSince", "01/01/2010"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("admin/members/newMemberForm"))
                    .andExpect(model().attribute("adminController", "active"))
                    .andExpect(model().attribute("teams", hasSize(3)))
                    .andExpect(model().attribute("roles", hasSize(2)))
                    .andExpect(model().attribute("message", is("Passwords are not equal. Try again")));
    }

    @Test
    public void edit() throws Exception {

        Integer id = 1;
        kees.setId(id);
        when(memberService.getById(id)).thenReturn(kees);
        when(teamService.listAll()).thenReturn((List) teams);
        when(roleService.listAll()).thenReturn((List) roles);

        mockMvc.perform(get("/admin/member/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/members/editMemberForm"))
                .andExpect(model().attribute("member", instanceOf(Member.class)))
                .andExpect(model().attribute("member", hasProperty("id", is(id))))
                .andExpect(model().attribute("adminController", "active"))
                .andExpect(model().attribute("teams", hasSize(3)))
                .andExpect(model().attribute("roles", hasSize(2)));
    }

    @Test
    public void updateMember() throws Exception {

        when(teamService.listAll()).thenReturn((List) teams);

        mockMvc.perform(MockMvcRequestBuilders.fileUpload("/admin/member/update")
                .file("file",  parseImage("bert.jpg"))
                .param("id", "1")
                .param("firstName", "Piet")
                .param("email", "test@email.com")
                .param("lastName", "testMember")
                .param("password", "12345")
                .param("confirmPw", "12345")
                .param("roles", "1")
                .param("memberSince", "01/01/2010"))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(view().name("redirect:/admin/members/page/1/lastName/asc"))
                    .andExpect(MockMvcResultMatchers.flash().attribute("message", "Piet testMember was updated succesfully"));

        ArgumentCaptor<Member> boundMember = ArgumentCaptor.forClass(Member.class);
        ArgumentCaptor<MultipartFile> boundMultipartFile = ArgumentCaptor.forClass(MultipartFile.class);
        ArgumentCaptor<Team> boundTeam = ArgumentCaptor.forClass(Team.class);
        verify(memberService).save(boundMember.capture(), boundMultipartFile.capture());
        verify(teamService, times(1)).listAll();
        verify(teamService, times(3)).save(boundTeam.capture());

        Assert.assertEquals("testMember", boundMember.getValue().getLastName());
    }

    @Test
    public void updateMemberWithBindingErrors() throws Exception {

        Integer id = 1;
        kees.setImage(parseImage("bert.jpg"));

        when(memberService.getById(id)).thenReturn(kees);
        when(teamService.listAll()).thenReturn((List) teams);
        when(roleService.listAll()).thenReturn((List) roles);

        mockMvc.perform(MockMvcRequestBuilders.fileUpload("/admin/member/update")
                .file("file",  parseImage("bert.jpg"))
                .param("id", "1")
                .param("firstName", "Piet"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("admin/members/editMemberForm"))
                    .andExpect(model().attribute("adminController", "active"))
                    .andExpect(model().attribute("teams", hasSize(3)))
                    .andExpect(model().attribute("roles", hasSize(2)))
                    .andExpect(model().attribute("member", hasProperty("id", is(id))))
                    .andExpect(model().attribute("member", hasProperty("base64image", is(Base64.encodeBase64String(parseImage("bert.jpg"))))));

        verify(memberService, times(1)).getById(id);
    }

    @Test
    public void updateMemberWithBindingErrorsNoImage() throws Exception {

        Integer id = 1;

        when(memberService.getById(id)).thenReturn(kees);
        when(teamService.listAll()).thenReturn((List) teams);
        when(roleService.listAll()).thenReturn((List) roles);

        mockMvc.perform(MockMvcRequestBuilders.fileUpload("/admin/member/update")
                .file("file",  parseImage("bert.jpg"))
                .param("id", "1")
                .param("firstName", "Piet"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/members/editMemberForm"))
                .andExpect(model().attribute("adminController", "active"))
                .andExpect(model().attribute("teams", hasSize(3)))
                .andExpect(model().attribute("roles", hasSize(2)))
                .andExpect(model().attribute("member", hasProperty("id", is(id))))
                .andExpect(model().attribute("member", hasProperty("base64image", nullValue())));

        verify(memberService, times(1)).getById(id);
    }

    @Test
    public void updateNewMemberWithNoEqualPasswords() throws Exception {

        Integer id = 1;

        when(memberService.getById(id)).thenReturn(kees);
        when(teamService.listAll()).thenReturn((List) teams);
        when(roleService.listAll()).thenReturn((List) roles);

        mockMvc.perform(MockMvcRequestBuilders.fileUpload("/admin/member/update")
                .file("file",  parseImage("bert.jpg"))
                .param("id", "1")
                .param("firstName", "Piet")
                .param("email", "test@email.com")
                .param("lastName", "testMember")
                .param("password", "12345")
                .param("confirmPw", "1234")
                .param("roles", "1")
                .param("memberSince", "01/01/2010"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("admin/members/editMemberForm"))
                    .andExpect(model().attribute("adminController", "active"))
                    .andExpect(model().attribute("teams", hasSize(3)))
                    .andExpect(model().attribute("roles", hasSize(2)))
                    .andExpect(model().attribute("member", hasProperty("id", is(id))))
                    .andExpect(model().attribute("member", hasProperty("base64image", nullValue())))
                    .andExpect(model().attribute("message", is("Passwords are not equal. Try again")));
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