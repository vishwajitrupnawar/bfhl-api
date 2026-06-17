package com.example.bfhl.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class BfhlControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("POST /bfhl – full spec payload returns 200 with expected structure")
    void fullPayload() throws Exception {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("data", Arrays.asList("A", "1", "A1B2", "$", "25.5", "-10", "Test123", "", null));

        mockMvc.perform(post("/bfhl")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Request-Id", "integration-test-001")
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.is_success").value(true))
                .andExpect(jsonPath("$.request_id").value("integration-test-001"))
                .andExpect(jsonPath("$.odd_numbers").isArray())
                .andExpect(jsonPath("$.even_numbers").isArray())
                .andExpect(jsonPath("$.sum").isString())
                .andExpect(jsonPath("$.sorted_numbers").isArray())
                .andExpect(jsonPath("$.alphabets").isArray())
                .andExpect(jsonPath("$.alphabet_count").isNumber())
                .andExpect(jsonPath("$.alphabet_frequency").isMap())
                .andExpect(jsonPath("$.vowel_count").isNumber())
                .andExpect(jsonPath("$.consonant_count").isNumber())
                .andExpect(jsonPath("$.special_characters").isArray())
                .andExpect(jsonPath("$.special_character_count").isNumber())
                .andExpect(jsonPath("$.unique_element_count").isNumber())
                .andExpect(jsonPath("$.contains_duplicates").isBoolean())
                .andExpect(jsonPath("$.processing_time_ms").isNumber())
                .andExpect(jsonPath("$.summary.total_elements_received").value(9))
                .andExpect(jsonPath("$.summary.invalid_elements_ignored").value(2));
    }

    @Test
    @DisplayName("POST /bfhl – generates request id when header missing")
    void generatesRequestId() throws Exception {
        Map<String, Object> body = Map.of("data", List.of("1"));

        mockMvc.perform(post("/bfhl")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.request_id").isNotEmpty());
    }

    @Test
    @DisplayName("POST /bfhl – empty data array returns 400")
    void emptyDataArray() throws Exception {
        Map<String, Object> body = Map.of("data", Collections.emptyList());

        mockMvc.perform(post("/bfhl")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.is_success").value(false));
    }

    @Test
    @DisplayName("POST /bfhl – missing data field returns 400")
    void missingDataField() throws Exception {
        mockMvc.perform(post("/bfhl")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.is_success").value(false));
    }

    @Test
    @DisplayName("POST /bfhl – malformed JSON returns 400")
    void malformedJson() throws Exception {
        mockMvc.perform(post("/bfhl")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("not-json"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.is_success").value(false));
    }

    @Test
    @DisplayName("POST /bfhl – numeric-only payload")
    void numericOnly() throws Exception {
        Map<String, Object> body = Map.of("data", List.of("1", "2", "3"));

        mockMvc.perform(post("/bfhl")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sum").value("6"))
                .andExpect(jsonPath("$.largest_number").value(3))
                .andExpect(jsonPath("$.smallest_number").value(1));
    }

    @Test
    @DisplayName("POST /bfhl – alphabet-only payload")
    void alphabetOnly() throws Exception {
        Map<String, Object> body = Map.of("data", List.of("A", "E", "B"));

        mockMvc.perform(post("/bfhl")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.vowel_count").value(2))
                .andExpect(jsonPath("$.consonant_count").value(1))
                .andExpect(jsonPath("$.alphabet_count").value(3));
    }
}
