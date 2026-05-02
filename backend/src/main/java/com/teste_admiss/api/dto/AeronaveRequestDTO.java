package com.teste_admiss.api.dto;

import com.teste_admiss.config.Messages;
import com.teste_admiss.infraestruture.domain.enums.MarcasEnum;
import com.teste_admiss.infraestruture.validation.AnoMaximo;
import jakarta.validation.constraints.*;

public record AeronaveRequestDTO(

        Long id,

        @NotBlank(message = Messages.NOT_BLANK)
        @Size(min = 3, max = 70, message = Messages.FIELD_SIZE_MESSAGE)
        String nome,

        @NotNull(message = Messages.NOT_NULL)
        MarcasEnum marca,

        @NotNull(message = Messages.NOT_NULL)
        @Min(value = 1900, message = Messages.YEAR_MIN)
        @AnoMaximo
        Integer ano,

        @NotBlank(message = Messages.NOT_BLANK)
        @Size(min = 3, max = 70, message = Messages.FIELD_SIZE_MESSAGE)
        String descricao,
        boolean vendido

) { }
