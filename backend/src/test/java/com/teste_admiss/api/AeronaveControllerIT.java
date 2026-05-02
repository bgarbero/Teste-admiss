package com.teste_admiss.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teste_admiss.api.dto.AeronaveRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class AeronaveControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Long existingId;
    private Long nonExistingId;
    private Long countTotalAeronaves;

    @BeforeEach
    void setup() {
        existingId = 1L;
        nonExistingId = 9999L;

        countTotalAeronaves = 4L;
    }

    @Test
    public void findAllShouldReturnSortedPageWhenSortById() throws Exception {
        mockMvc.perform(get("/aeronaves?page=0&size=5&sort=id,asc")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.totalElements").value(countTotalAeronaves),
                        jsonPath("$.content").exists(),
                        jsonPath("$.content[0].nome").value("E2-190"),
                        jsonPath("$.content[1].nome").value("737-100"),
                        jsonPath("$.content[2].nome").value("KC-390")
                );
    }

    @Test
    public void updateShouldReturnAeronaveResponseDTOWhenIdExists() throws Exception {
        AeronaveRequestDTO requestDTO = new AeronaveRequestDTO(
                existingId,
                "E2-190 Atualizado",
                "Embraer Atualizada",
                2024,
                "Aeronave atualizada no teste de integração",
                false
        );
        String jsonBody = objectMapper.writeValueAsString(requestDTO);

        mockMvc.perform(put("/aeronaves")
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.id").value(existingId),
                        jsonPath("$.nome").value("E2-190 Atualizado"),
                        jsonPath("$.marca").value("Embraer Atualizada")
                );
    }

    @Test
    public void updateShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
        AeronaveRequestDTO requestDTO = new AeronaveRequestDTO(
                nonExistingId,
                "Aeronave Fantasma",
                "Fabricante Fantasma",
                2000,
                "Esta aeronave nao existe no banco",
                false
        );
        String jsonBody = objectMapper.writeValueAsString(requestDTO);

        mockMvc.perform(put("/aeronaves")
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}