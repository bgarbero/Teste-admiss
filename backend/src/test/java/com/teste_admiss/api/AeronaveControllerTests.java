package com.teste_admiss.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teste_admiss.api.dto.AeronaveRequestDTO;
import com.teste_admiss.api.dto.AeronaveResponseDTO;
import com.teste_admiss.api.dto.AeronaveResponseFullDTO;
import com.teste_admiss.api.mapper.AeronaveMapper;
import com.teste_admiss.business.AeronaveService;
import com.teste_admiss.infraestruture.domain.Aeronave;
import com.teste_admiss.infraestruture.domain.enums.MarcasEnum;
import com.teste_admiss.infraestruture.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        value = AeronaveController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class
)
public class AeronaveControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AeronaveService service;

    @MockBean
    private AeronaveMapper mapper;

    @Autowired
    private ObjectMapper objectMapper;

    private Long existingId;
    private Long nonExistingId;

    private Aeronave aeronave;
    private AeronaveRequestDTO requestDTO;
    private AeronaveResponseDTO responseDTO;
    private AeronaveResponseFullDTO responseFullDTO;
    private PageImpl<Aeronave> page;

    @BeforeEach
    public void setup() {
        existingId    = 1L;
        nonExistingId = 2L;

        aeronave = new Aeronave();
        aeronave.setId(existingId);
        aeronave.setNome("Boeing 737");
        aeronave.setMarca(MarcasEnum.BOEING);
        aeronave.setAno(2020);
        aeronave.setDescricao("Aeronave comercial");
        aeronave.setVendido(false);
        aeronave.setCreated(LocalDateTime.now());
        aeronave.setUpdated(LocalDateTime.now());

        requestDTO = new AeronaveRequestDTO(existingId, "Boeing 737", MarcasEnum.BOEING, 2020, "Aeronave comercial", false);

        responseDTO = new AeronaveResponseDTO(existingId, MarcasEnum.BOEING, "Boeing 737", 2020, false);

        responseFullDTO = new AeronaveResponseFullDTO(
                existingId, "Boeing 737", MarcasEnum.BOEING, 2020,
                "Aeronave comercial", false,
                LocalDateTime.now(), LocalDateTime.now()
        );

        page = new PageImpl<>(List.of(aeronave));

        when(service.findAll(any())).thenReturn(page);

        when(service.findById(existingId)).thenReturn(aeronave);
        when(service.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);

        when(service.update(any())).thenReturn(aeronave);

        when(service.insert(any())).thenReturn(aeronave);

        doNothing().when(service).delete(existingId);
        doThrow(ResourceNotFoundException.class).when(service).delete(nonExistingId);

        when(mapper.toDTO(any(Aeronave.class))).thenReturn(responseDTO);
        when(mapper.toFullDTO(any(Aeronave.class))).thenReturn(responseFullDTO);
        when(mapper.toEntity(any(AeronaveRequestDTO.class))).thenReturn(aeronave);
    }

    @Test
    public void findAllShouldReturnPage() throws Exception {
        mockMvc.perform(get("/aeronaves")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void findByIdShouldReturnAeronaveWhenIdExists() throws Exception {
        mockMvc.perform(get("/aeronaves/{id}", existingId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.id").exists(),
                        jsonPath("$.nome").exists(),
                        jsonPath("$.marca").exists()
                );
    }

    @Test
    public void findByIdShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
        mockMvc.perform(get("/aeronaves/{id}", nonExistingId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void findByNomeShouldReturnAeronaveWhenNomeExists() throws Exception {
        when(service.findByName(any())).thenReturn(aeronave);

        mockMvc.perform(get("/aeronaves/nome")
                        .param("nome", "Boeing 737")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void insertShouldReturnCreatedAndAeronaveResponseDTO() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(requestDTO);

        mockMvc.perform(post("/aeronaves")
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isCreated(),
                        jsonPath("$.id").exists(),
                        jsonPath("$.nome").exists(),
                        jsonPath("$.marca").exists()
                );
    }

    @Test
    public void updateShouldReturnOkAndAeronaveResponseDTO() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(requestDTO);

        mockMvc.perform(put("/aeronaves")
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.id").exists(),
                        jsonPath("$.nome").exists(),
                        jsonPath("$.marca").exists()
                );
    }

    @Test
    public void updateShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {

        Aeronave entidadeInexistente = new Aeronave();
        entidadeInexistente.setId(nonExistingId);

        when(mapper.toEntity(any(AeronaveRequestDTO.class))).thenReturn(entidadeInexistente);
        when(service.update(any())).thenThrow(ResourceNotFoundException.class);

        AeronaveRequestDTO dtoInexistente = new AeronaveRequestDTO(
                nonExistingId, "Aeronave Inexistente", MarcasEnum.BOEING, 2000, "desc", false
        );
        String jsonBody = objectMapper.writeValueAsString(dtoInexistente);

        mockMvc.perform(put("/aeronaves")
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteShouldReturnNoContentWhenIdExists() throws Exception {
        mockMvc.perform(delete("/aeronaves/{id}", existingId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
        mockMvc.perform(delete("/aeronaves/{id}", nonExistingId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}