package com.xiaomi.servicegateway.controller;

import com.xiaomi.domain.warning.dto.AlertMessageDTO;
import com.xiaomi.servicegateway.service.RocketMQListenerService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(AlarmController.class)
public class AlarmControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RocketMQListenerService mockRocketMQListenerService;

    @Test
    public void testReportWarnings() throws Exception {
        // Setup
        // Configure RocketMQListenerService.getProcessedMessage(...).
        final AlertMessageDTO alertMessageDTO = new AlertMessageDTO(0, 0, 0, "batteryType");
        when(mockRocketMQListenerService.getProcessedMessage(0, 0)).thenReturn(alertMessageDTO);

        // Run the test and verify the results
        mockMvc.perform(post("/api/report")
                        .content("content").contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{}", true));
    }

    @Test
    public void testReportWarnings_RocketMQListenerServiceReturnsNull() throws Exception {
        // Setup
        when(mockRocketMQListenerService.getProcessedMessage(0, 0)).thenReturn(null);

        // Run the test and verify the results
        mockMvc.perform(post("/api/report")
                        .content("content").contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("", true));
    }

    @Test
    public void testGetAlertMessages() throws Exception {
        // Setup
        // Configure RocketMQListenerService.getProcessedMessage(...).
        final AlertMessageDTO alertMessageDTO = new AlertMessageDTO(0, 0, 0, "batteryType");
        when(mockRocketMQListenerService.getProcessedMessage(0, 1)).thenReturn(alertMessageDTO);

        // Run the test and verify the results
        mockMvc.perform(get("/api/car")
                        .param("carId", "0")
                        .param("warnId", "0")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{}", true));
    }

    @Test
    public void testGetAlertMessages_RocketMQListenerServiceReturnsNull() throws Exception {
        // Setup
        when(mockRocketMQListenerService.getProcessedMessage(0, 1)).thenReturn(null);

        // Run the test and verify the results
        mockMvc.perform(get("/api/car")
                        .param("carId", "0")
                        .param("warnId", "0")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("", true));
    }
}
