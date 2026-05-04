package com.teste_admiss.tests;

import com.teste_admiss.api.dto.AeronaveResponseDTO;
import com.teste_admiss.infraestruture.domain.Aeronave;
import com.teste_admiss.infraestruture.domain.enums.MarcasEnum;

import java.time.LocalDateTime;

public class Factory {

    public static Aeronave createAeronave() {
        Aeronave aeronave = new Aeronave(
                1L,
                "E2-193",
                MarcasEnum.EMBRAER,
                2014,
                "Aeronave comercial de medio porte.",
                false,
                LocalDateTime.parse("2026-05-02T05:34:42"),
                LocalDateTime.parse("2026-05-02T05:34:42")
        );
        return aeronave;
    }

    public static AeronaveResponseDTO createAeronaveResponseDTO() {
        return new AeronaveResponseDTO(
                1L,
                MarcasEnum.EMBRAER,
                "E2-190",
                2014,
                false
        );
    }

}
