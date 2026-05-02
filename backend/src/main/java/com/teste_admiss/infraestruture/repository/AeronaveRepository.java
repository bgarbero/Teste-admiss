package com.teste_admiss.infraestruture.repository;

import com.teste_admiss.infraestruture.domain.Aeronave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AeronaveRepository extends JpaRepository<Aeronave, Long> {

    @Query("SELECT a FROM Aeronave a WHERE a.nome = :nome")
    Aeronave buscaPorNome(@Param("nome") String nome);

}
