package com.example.wsbfinalproject2022.projects;

import com.example.wsbfinalproject2022.config.CustomUserDetailsService;
import com.example.wsbfinalproject2022.config.SecurityConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@ContextConfiguration(classes = SecurityConfig.class)
@WebAppConfiguration
@WebMvcTest(ProjectController.class)
class ProjectControllerTest {

    @Autowired
    private WebApplicationContext context;

    MockMvc mvc;

    @MockBean
    CustomUserDetailsService users;

    @MockBean
    ProjectService projectService;
    @MockBean
    ProjectRepository projectRepository;
    @BeforeEach
    void setup(){
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Test
    @WithMockUser()
    void index() throws Exception {
        Project project = new Project();
        project.setCode("CODE");

        List<Project> projects = List.of(
                project
        );
        Page<Project> page = new PageImpl<>(projects);

        doReturn(page).when(projectService).findAll(any(), any());
        doReturn(Collections.emptySet()).when(projectService).findAllCreators();

        mvc.perform(get("/projects"))
                .andExpect(status().isOk())
                .andExpect(view().name("projects/index"))
                .andExpect(model().attribute("projects", page))
                .andExpect(model().attribute("creators", Collections.emptySet()));
    }
}