package com.teste_admiss.api.mapper;

import com.teste_admiss.api.dto.AeronaveRequestDTO;
import com.teste_admiss.api.dto.AeronaveResponseDTO;
import com.teste_admiss.api.dto.AeronaveResponseFullDTO;
import com.teste_admiss.infraestruture.domain.Aeronave;
import org.mapstruct.Mapper;

@Mapper(componentModel = "Spring")
public interface AeronaveMapper {

    Aeronave toEntity(AeronaveRequestDTO dto);

    AeronaveResponseDTO toDTO(Aeronave entity);

    AeronaveResponseFullDTO toFullDTO(Aeronave entity);

}
