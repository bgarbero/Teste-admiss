package com.teste_admiss.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record AeronaveResponseFullDTO(

         Long id,
         String nome,
         String marca,
         Integer ano,
         String descricao,
         boolean vendido,

         @JsonFormat(pattern = "dd/MM/yyyy")
         LocalDateTime created,

         @JsonFormat(pattern = "dd/MM/yyyy")
         LocalDateTime updated

) { }
