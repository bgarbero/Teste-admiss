package com.teste_admiss.api.dto;

import com.teste_admiss.infraestruture.domain.enums.MarcasEnum;

public record AeronaveResponseDTO(

        Long id,
        MarcasEnum marca,
        String nome,
        Integer ano,
        boolean vendido

) { }
