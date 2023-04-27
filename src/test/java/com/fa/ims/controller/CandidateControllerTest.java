package com.fa.ims.controller;


import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class CandidateControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(value = "anNV", roles = {"RECRUITER", "ADMIN", "INTERVIEWER", "MANAGER"})
    public void testList() throws Exception {
        this.mockMvc.perform(get("/candidate/list")).
                andExpect(view().name("candidate/list-candidate")).
                andExpect(status().isOk());
    }

    @Test
    @WithMockUser(value = "anNV", roles = {"RECRUITER", "ADMIN", "MANAGER"})
    public void testCreate() throws Exception {
        this.mockMvc.
                perform(get("/candidate/create")).
                andExpect(view().name("candidate/create-candidate")).
                andExpect(status().isOk());
    }

    @Test
    @WithMockUser(value = "anNV", roles = {"RECRUITER", "ADMIN", "MANAGER"})
    public void testDelete() throws Exception {
        this.mockMvc.
                perform(get("/candidate/delete/{id}", 12L)).
                andExpect(view().name("redirect:/candidate/list")).
                andExpect(status().is3xxRedirection());
    }
    @Test
    @WithMockUser(value = "anNV", roles = {"RECRUITER", "ADMIN", "MANAGER", "INTERVIEWER"})
    public void testView() throws Exception {
        this.mockMvc.perform(get("/candidate/detail/{id}", 12L))
                .andExpect(view().name("candidate/view-candidate"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(value = "anNV", roles = {"RECRUITER", "ADMIN", "MANAGER"})
    public void testUpdate() throws Exception {
        this.mockMvc.perform(get("/candidate/update/{id}", 12L))
                .andExpect(view().name("candidate/update-candidate"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(value = "dungNV", roles = {"INTERVIEWER"})
    public void testUpdateWithInterviewer() throws Exception {
        this.mockMvc.perform(get("/candidate/update/{id}", 12L))
                .andExpect(status().is(302));
    }

    @Test
    @WithMockUser(value = "dungNV", roles = {"INTERVIEWER"})
    public void testDeleteWithInterviewer() throws Exception {
        this.mockMvc.perform(get("/candidate/delete/{id}", 12L))
                .andExpect(status().is(302));
    }

    @Test
    @WithMockUser(value = "dungNV", roles = {"INTERVIEWER"})
    public void testCreateWithInterviewer() throws Exception {
        this.mockMvc.perform(get("/candidate/create"))
                .andExpect(status().is(302));
    }
}
