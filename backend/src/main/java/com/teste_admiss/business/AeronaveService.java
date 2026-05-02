package com.teste_admiss.business;

import com.teste_admiss.infraestruture.domain.Aeronave;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AeronaveService {

    Page<Aeronave> findAll(Pageable pageable);
    Aeronave findById(Long id);
    Page<Aeronave> findByFiltros(String marca, String nome, Integer ano, Boolean vendido, Pageable pageable);

}
