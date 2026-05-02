package com.teste_admiss.infraestruture.repository;

import com.teste_admiss.infraestruture.domain.Aeronave;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;



public interface AeronaveRepository extends JpaRepository<Aeronave, Long> {

    @Query("SELECT a FROM Aeronave a WHERE " +
            "(:marca IS NULL OR LOWER(a.marca) = LOWER(:marca)) AND " +
            "(:nome IS NULL OR LOWER(a.nome) LIKE LOWER(CONCAT('%', :nome, '%'))) AND " +
            "(:ano IS NULL OR a.ano = :ano) AND " +
            "(:vendido IS NULL OR a.vendido = :vendido)")
    Page<Aeronave> findByFiltros(
            @Param("marca") String marca,
            @Param("nome") String nome,
            @Param("ano") Integer ano,
            @Param("vendido") Boolean vendido,
            Pageable pageable
    );

}
