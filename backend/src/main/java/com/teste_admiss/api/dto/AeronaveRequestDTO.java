package com.teste_admiss.api.dto;

public record AeronaveRequestDTO(

        Long id,
        String nome,
        String marca,
        Integer ano,
        String descricao,
        boolean vendido

) { }
