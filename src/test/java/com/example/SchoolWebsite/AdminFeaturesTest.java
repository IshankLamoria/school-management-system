package com.example.SchoolWebsite;

import com.example.SchoolWebsite.controller.AdminController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class AdminFeaturesTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    private MockHttpSession createAdminSession() {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("userId", "admin");
        session.setAttribute("role", "admin");
        return session;
    }

    @Test
    void testAdminTransactionsPageRendersSuccessfully() throws Exception {
        mockMvc.perform(get("/admin/transactions").session(createAdminSession()))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/transactions"))
                .andExpect(model().attributeExists("transactions", "stats", "availableTypes", "availableModes"));
    }

    @Test
    void testAdminTransactionsWithFilters() throws Exception {
        mockMvc.perform(get("/admin/transactions")
                        .param("type", "Fee")
                        .param("mode", "UPI")
                        .param("startDate", "2026-04-01")
                        .param("endDate", "2026-04-30")
                        .param("search", "A001")
                        .param("page", "1")
                        .session(createAdminSession()))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/transactions"))
                .andExpect(model().attributeExists("transactions", "startDate", "endDate"))
                .andExpect(model().attribute("startDate", "2026-04-01"))
                .andExpect(model().attribute("endDate", "2026-04-30"));
    }

    @Test
    void testStudentFeesWithDateRangeFilter() throws Exception {
        MockHttpSession studentSession = new MockHttpSession();
        studentSession.setAttribute("userId", "A001");
        studentSession.setAttribute("role", "student");

        mockMvc.perform(get("/student/fees")
                        .param("startDate", "2026-04-01")
                        .param("endDate", "2026-04-30")
                        .session(studentSession))
                .andExpect(status().isOk())
                .andExpect(view().name("student/fees"))
                .andExpect(model().attributeExists("student", "payments", "startDate", "endDate"));
    }

    @Test
    void testAdminTransportPageRendersSuccessfully() throws Exception {
        mockMvc.perform(get("/admin/transport").session(createAdminSession()))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/transport"))
                .andExpect(model().attributeExists("vehicles", "stats", "passengers"));
    }

    @Test
    void testAdminTransportVehicleWithSpecificSelection() throws Exception {
        mockMvc.perform(get("/admin/transport")
                        .param("selectedVehicle", "V01")
                        .session(createAdminSession()))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/transport"))
                .andExpect(model().attributeExists("vehicles", "activeVehicle", "passengers"));
    }

    @Test
    void testAdminVehicleNewFormRenders() throws Exception {
        mockMvc.perform(get("/admin/transport/vehicle/new").session(createAdminSession()))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/vehicle-form"))
                .andExpect(model().attributeExists("vehicle", "drivers", "mode"));
    }

    @Test
    void testAdminVehicleEditFormRenders() throws Exception {
        mockMvc.perform(get("/admin/transport/vehicle/edit/V01").session(createAdminSession()))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/vehicle-form"))
                .andExpect(model().attributeExists("vehicle", "drivers", "mode"));
    }

    @Test
    void testAdminAddAndRemovePassengerFlow() throws Exception {
        // Add student to vehicle V01
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/admin/transport/add-passenger")
                        .param("vehicleId", "V01")
                        .param("admissionNo", "A001")
                        .session(createAdminSession()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/transport?selectedVehicle=V01#passengers"))
                .andExpect(flash().attributeExists("successMessage"));

        // Remove student from vehicle V01
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/admin/transport/remove-passenger")
                        .param("vehicleId", "V01")
                        .param("admissionNo", "A001")
                        .session(createAdminSession()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/transport?selectedVehicle=V01"))
                .andExpect(flash().attributeExists("successMessage"));

        // Re-assign A001 back so database state remains pristine
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/admin/transport/add-passenger")
                        .param("vehicleId", "V01")
                        .param("admissionNo", "A001")
                        .session(createAdminSession()))
                .andExpect(status().is3xxRedirection());
    }
}
