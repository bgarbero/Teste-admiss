package com.teste_admiss.infraestruture.domain;

import com.teste_admiss.infraestruture.domain.enums.MarcasEnum;
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

    @Column(unique = true)
    private String nome;

    @Column
    @Enumerated(EnumType.STRING)
    private MarcasEnum marca;

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

    @PostPersist
    public void prePersist(){
        this.created = LocalDateTime.now();
    }

    @PostUpdate
    public void preUpdate(){
        this.updated = LocalDateTime.now();
    }

}
