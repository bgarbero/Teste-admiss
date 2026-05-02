package com.teste_admiss.infraestruture.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_aeronaves")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Aeronave {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String nome;

    @Column
    private String marca;

    @Column
    private Integer ano;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean vendido = false;

    @Column
    @CreationTimestamp
    private LocalDateTime created;

    @Column
    @UpdateTimestamp
    private LocalDateTime updated;

}
