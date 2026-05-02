package com.teste_admiss.api.dto;

public record AeronaveResponseDTO(

        Long id,
        String marca,
        String nome,
        Integer ano,
        boolean vendido

) { }
