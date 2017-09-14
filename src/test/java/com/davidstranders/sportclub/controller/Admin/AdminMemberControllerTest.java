//package com.davidstranders.sportclub.controller.Admin;
//
//import com.davidstranders.sportclub.model.Member;
//import com.davidstranders.sportclub.service.TeamService;
//import com.davidstranders.sportclub.model.Role;
//import com.davidstranders.sportclub.model.Team;
//import com.davidstranders.sportclub.service.MemberService;
//import com.davidstranders.sportclub.service.RoleService;
//import com.davidstranders.sportclub.utils.Parsers;
//import org.apache.tomcat.util.codec.binary.Base64;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import org.mockito.*;
//import org.springframework.beans.support.PagedListHolder;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MvcResult;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import org.springframework.web.multipart.MultipartFile;
//
//import java.util.*;
//
//import static org.hamcrest.Matchers.*;
//import static org.junit.Assert.assertEquals;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
///**
// * Created by DS on 13-2-2017.
// */
//public class AdminMemberControllerTest {
//
//    @Mock
//    private MemberService memberService;
//    @Mock
//    private TeamService teamService;
//    @Mock
//    private RoleService roleService;
//
//    @InjectMocks
//    private AdminMemberController adminMemberController;
//    private MockMvc mockMvc;
//
//    private Member kees = new Member();
//    private Member jan = new Member();
//    private Member piet = new Member();
//    private List<Member> members = new ArrayList<>(Arrays.asList(piet, kees, jan));
//
//    private Role member = new Role();
//    private Role admin = new Role();
//    private List<Role> roles = new ArrayList<>(Arrays.asList(member, admin));
//
//    private Team teamA = new Team();
//    private Team teamB = new Team();
//    private Team teamC = new Team();
//    private List<Team> teams = new ArrayList<>(Arrays.asList(teamA, teamB, teamC));
//
//    private static final byte[]IMAGE = Parsers.parseImage("bert");
//
//    @Before
//    public void setup(){
//
//        MockitoAnnotations.initMocks(this);
//        mockMvc = MockMvcBuilders.standaloneSetup(adminMemberController).build();
//    }
//
//    @Test
//    public void list() throws Exception {
//        mockMvc.perform(get("/admin/members"))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(view().name("redirect:/admin/members/page/1/lastName/asc"));
//    }
//
//    @Test
//    public void listAllMembers() throws Exception {
//
//        jan.setLastName("Jansen");
//        kees.setLastName("Keesen");
//        piet.setLastName("Pietersen");
//
//        when(memberService.findAllByOrderByLastName()).thenReturn((List)members);
//
//        MvcResult result = mockMvc.perform(get("/admin/members/page/1/lastName/asc"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("admin/members/list"))
//                .andExpect(model().attribute("members", instanceOf(PagedListHolder.class)))
//                .andExpect(model().attribute("members", hasProperty("source", hasSize(3))))
//                .andExpect(model().attribute("beginIndex", 1))
//                .andExpect(model().attribute("endIndex", 1))
//                .andExpect(model().attribute("currentIndex", 1))
//                .andExpect(model().attribute("totalPageCount", 1))
//                .andExpect(model().attribute("pageNumber", 1))
//                .andExpect(model().attribute("sortColumn", "lastName"))
//                .andExpect(model().attribute("sortOrder", "asc"))
//                .andReturn();
//
//        PagedListHolder pagedListHolder = (PagedListHolder) result.getModelAndView().getModel().get("members");
//        List<Member> membersAscendingOrder = pagedListHolder.getSource();
//        assertEquals( "Jansen", membersAscendingOrder.get(0).getLastName());
//        assertEquals("Keesen", membersAscendingOrder.get(1).getLastName());
//        assertEquals("Pietersen", membersAscendingOrder.get(2).getLastName());
//    }
//
//    @Test
//    public void createMember() throws Exception {
//
//        verifyZeroInteractions(memberService);
//
//        when(teamService.listAll()).thenReturn((List) teams);
//        when(roleService.listAll()).thenReturn((List) roles);
//
//        mockMvc.perform(get("/admin/member/create"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("admin/members/newMemberForm"))
//                .andExpect(model().attribute("member", instanceOf(Member.class)))
//                .andExpect(model().attribute("adminController", "active"))
//                .andExpect(model().attribute("teams", hasSize(3)))
//                .andExpect(model().attribute("roles", hasSize(2)));
//    }
//
//    @Test
//    public void delete() throws Exception {
//
//        mockMvc.perform(get("/admin/member/delete/1"))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(MockMvcResultMatchers.flash().attribute("message", "Member was deleted succesfully"))
//                .andExpect(view().name("redirect:/admin/members/page/1/lastName/asc"));
//
//        verify(memberService, times(1)).delete("1");
//    }
//
//    @Test
//    public void saveNewMember() throws Exception {
//
//        mockMvc.perform(MockMvcRequestBuilders.fileUpload("/admin/member/save/0")
//                .file("file", IMAGE)
//                .param("firstName", "Piet")
//                .param("email", "test@email.com")
//                .param("lastName", "testMember")
//                .param("password", "12345")
//                .param("confirmPw", "12345")
//                .param("roles", "1")
//                .param("memberSince", "01/01/2010"))
//                    .andExpect(status().is3xxRedirection())
//                    .andExpect(view().name("redirect:/admin/members/page/1/lastName/asc"))
//                    .andExpect(MockMvcResultMatchers.flash().attribute("message", "Piet testMember was created succesfully"));
//
//        ArgumentCaptor<Member> boundMember = ArgumentCaptor.forClass(Member.class);
//        ArgumentCaptor<MultipartFile> boundMultipartFile = ArgumentCaptor.forClass(MultipartFile.class);
//        verify(memberService).save(boundMember.capture(), boundMultipartFile.capture());
//        verifyZeroInteractions(teamService);
//
//        Assert.assertEquals("testMember", boundMember.getValue().getLastName());
//    }
//
//    @Test
//    public void saveNewMemberWithBindingErrors() throws Exception {
//
//        when(teamService.listAll()).thenReturn((List) teams);
//        when(roleService.listAll()).thenReturn((List) roles);
//
//        mockMvc.perform(MockMvcRequestBuilders.fileUpload("/admin/member/save/0")
//                .file("file", IMAGE)
//                .param("firstName", "Piet"))
//                    .andExpect(status().isOk())
//                    .andExpect(view().name("admin/members/newMemberForm"))
//                    .andExpect(model().attribute("adminController", "active"))
//                    .andExpect(model().attribute("teams", hasSize(3)))
//                    .andExpect(model().attribute("roles", hasSize(2)));
//    }
//
//    @Test
//    public void saveNewMemberWithNoPassWord() throws Exception {
//
//        when(teamService.listAll()).thenReturn((List) teams);
//        when(roleService.listAll()).thenReturn((List) roles);
//
//        mockMvc.perform(MockMvcRequestBuilders.fileUpload("/admin/member/save/0")
//                .file("file", IMAGE)
//                .param("firstName", "Piet")
//                .param("email", "test@email.com")
//                .param("lastName", "testMember")
//                .param("password", "")
//                .param("roles", "1")
//                .param("memberSince", "01/01/2010"))
//                    .andExpect(status().isOk())
//                    .andExpect(view().name("admin/members/newMemberForm"))
//                    .andExpect(model().attribute("adminController", "active"))
//                    .andExpect(model().attribute("teams", hasSize(3)))
//                    .andExpect(model().attribute("roles", hasSize(2)))
//                    .andExpect(model().attribute("message", is("Password is required. Try again")));
//    }
//
//    @Test
//    public void saveNewMemberWithNoEqualPasswords() throws Exception {
//
//        when(teamService.listAll()).thenReturn((List) teams);
//        when(roleService.listAll()).thenReturn((List) roles);
//
//        mockMvc.perform(MockMvcRequestBuilders.fileUpload("/admin/member/save/0")
//                .file("file", IMAGE)
//                .param("firstName", "Piet")
//                .param("email", "test@email.com")
//                .param("lastName", "testMember")
//                .param("password", "12345")
//                .param("confirmPw", "1234")
//                .param("roles", "1")
//                .param("memberSince", "01/01/2010"))
//                    .andExpect(status().isOk())
//                    .andExpect(view().name("admin/members/newMemberForm"))
//                    .andExpect(model().attribute("adminController", "active"))
//                    .andExpect(model().attribute("teams", hasSize(3)))
//                    .andExpect(model().attribute("roles", hasSize(2)))
//                    .andExpect(model().attribute("message", is("Passwords are not equal. Try again")));
//    }
//
//    @Test
//    public void edit() throws Exception {
//
//        String id = "";
//        kees.setId(id);
//        when(memberService.getById(id)).thenReturn(kees);
//        when(teamService.listAll()).thenReturn((List) teams);
//        when(roleService.listAll()).thenReturn((List) roles);
//
//        mockMvc.perform(get("/admin/member/edit/1"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("admin/members/editMemberForm"))
//                .andExpect(model().attribute("member", instanceOf(Member.class)))
//                .andExpect(model().attribute("member", hasProperty("id", is(id))))
//                .andExpect(model().attribute("adminController", "active"))
//                .andExpect(model().attribute("teams", hasSize(3)))
//                .andExpect(model().attribute("roles", hasSize(2)));
//    }
//
//    @Test
//    public void updateMember() throws Exception {
//
//        when(teamService.listAll()).thenReturn((List) teams);
//
//        mockMvc.perform(MockMvcRequestBuilders.fileUpload("/admin/member/update/1")
//                .file("file", IMAGE)
//                .param("id", "1")
//                .param("firstName", "Piet")
//                .param("email", "test@email.com")
//                .param("lastName", "testMember")
//                .param("password", "12345")
//                .param("confirmPw", "12345")
//                .param("roles", "1")
//                .param("memberSince", "01/01/2010"))
//                    .andExpect(status().is3xxRedirection())
//                    .andExpect(view().name("redirect:/admin/members/page/1/lastName/asc"))
//                    .andExpect(MockMvcResultMatchers.flash().attribute("message", "Piet testMember was updated succesfully"));
//
//        ArgumentCaptor<Member> boundMember = ArgumentCaptor.forClass(Member.class);
//        ArgumentCaptor<MultipartFile> boundMultipartFile = ArgumentCaptor.forClass(MultipartFile.class);
//        ArgumentCaptor<Team> boundTeam = ArgumentCaptor.forClass(Team.class);
//        verify(memberService).save(boundMember.capture(), boundMultipartFile.capture());
//        verify(teamService, times(1)).listAll();
//        verify(teamService, times(3)).save(boundTeam.capture());
//
//        Assert.assertEquals("testMember", boundMember.getValue().getLastName());
//    }
//
//    @Test
//    public void updateMemberWithBindingErrors() throws Exception {
//
//        String id = "1";
//        kees.setImage(IMAGE);
//
//        when(memberService.getById(id)).thenReturn(kees);
//        when(teamService.listAll()).thenReturn((List) teams);
//        when(roleService.listAll()).thenReturn((List) roles);
//
//        mockMvc.perform(MockMvcRequestBuilders.fileUpload("/admin/member/update/1")
//                .file("file",IMAGE)
//                .param("id", "1")
//                .param("firstName", "Piet"))
//                    .andExpect(status().isOk())
//                    .andExpect(view().name("admin/members/editMemberForm"))
//                    .andExpect(model().attribute("adminController", "active"))
//                    .andExpect(model().attribute("teams", hasSize(3)))
//                    .andExpect(model().attribute("roles", hasSize(2)))
//                    .andExpect(model().attribute("member", hasProperty("id", is(id))))
//                    .andExpect(model().attribute("member", hasProperty("base64image", is(Base64.encodeBase64String(IMAGE)))));
//
//        verify(memberService, times(1)).getById(id);
//    }
//
//    @Test
//    public void updateMemberWithBindingErrorsNoImage() throws Exception {
//
//        String id = "1";
//
//        when(memberService.getById(id)).thenReturn(kees);
//        when(teamService.listAll()).thenReturn((List) teams);
//        when(roleService.listAll()).thenReturn((List) roles);
//
//        mockMvc.perform(MockMvcRequestBuilders.fileUpload("/admin/member/update/1")
//                .file("file", IMAGE)
//                .param("id", "1")
//                .param("firstName", "Piet"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("admin/members/editMemberForm"))
//                .andExpect(model().attribute("adminController", "active"))
//                .andExpect(model().attribute("teams", hasSize(3)))
//                .andExpect(model().attribute("roles", hasSize(2)))
//                .andExpect(model().attribute("member", hasProperty("id", is(id))))
//                .andExpect(model().attribute("member", hasProperty("base64image", nullValue())));
//
//        verify(memberService, times(1)).getById(id);
//    }
//
//    @Test
//    public void updateNewMemberWithNoEqualPasswords() throws Exception {
//
//        String id = "1";
//
//        when(memberService.getById(id)).thenReturn(kees);
//        when(teamService.listAll()).thenReturn((List) teams);
//        when(roleService.listAll()).thenReturn((List) roles);
//
//        mockMvc.perform(MockMvcRequestBuilders.fileUpload("/admin/member/update/0")
//                .file("file", IMAGE)
//                .param("id", "1")
//                .param("firstName", "Piet")
//                .param("email", "test@email.com")
//                .param("lastName", "testMember")
//                .param("password", "12345")
//                .param("confirmPw", "1234")
//                .param("roles", "1")
//                .param("memberSince", "01/01/2010"))
//                    .andExpect(status().isOk())
//                    .andExpect(view().name("admin/members/editMemberForm"))
//                    .andExpect(model().attribute("adminController", "active"))
//                    .andExpect(model().attribute("teams", hasSize(3)))
//                    .andExpect(model().attribute("roles", hasSize(2)))
//                    .andExpect(model().attribute("member", hasProperty("id", is(id))))
//                    .andExpect(model().attribute("member", hasProperty("base64image", nullValue())))
//                    .andExpect(model().attribute("message", is("Passwords are not equal. Try again")));
//    }
//
//}