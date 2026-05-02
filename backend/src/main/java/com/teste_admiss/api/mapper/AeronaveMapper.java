package com.teste_admiss.api.mapper;

import com.teste_admiss.api.dto.AeronaveRequestDTO;
import com.teste_admiss.api.dto.AeronaveResponseDTO;
import com.teste_admiss.api.dto.AeronaveResponseFullDTO;
import com.teste_admiss.infraestruture.domain.Aeronave;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "Spring")
public interface AeronaveMapper {

    @Mapping(target = "created", ignore = true)
    @Mapping(target = "updated", ignore = true)
    Aeronave toEntity(AeronaveRequestDTO dto);

    AeronaveResponseDTO toDTO(Aeronave entity);

    AeronaveResponseFullDTO toFullDTO(Aeronave entity);

}
