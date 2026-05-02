package com.teste_admiss.api.dto;

import java.time.LocalDateTime;

public record AeronaveRequestDTO(

        Long id,
        String nome,
        String marca,
        Integer ano,
        String descricao,
        boolean vendido,
        LocalDateTime created,
        LocalDateTime updated

) { }
