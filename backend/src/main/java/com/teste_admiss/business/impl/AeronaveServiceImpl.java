package com.teste_admiss.business.impl;

import com.teste_admiss.business.AeronaveService;
import com.teste_admiss.config.Messages;
import com.teste_admiss.infraestruture.domain.Aeronave;
import com.teste_admiss.infraestruture.exceptions.ResourceNotFoundException;
import com.teste_admiss.infraestruture.repository.AeronaveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AeronaveServiceImpl implements AeronaveService {

    private final AeronaveRepository repository;

    @Override
    public Page<Aeronave> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Aeronave findById(Long id){
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Messages.RESOURCE_NOT_FOUND));
    }

    @Override
    public Page<Aeronave> findByFiltros(String marca, String nome, Integer ano, Boolean vendido, Pageable pageable) {
        return repository.findByFiltros(marca, nome, ano, vendido, pageable);
    }

    @Override
    public Aeronave insert(Aeronave entity){
        return repository.save(entity);
    }

    @Override
    public Aeronave update(Aeronave entity){
        if(!repository.existsById(entity.getId())){
            throw new ResourceNotFoundException(Messages.RESOURCE_NOT_FOUND);
        }
        return repository.save(entity);
    }

    @Override
    public void delete(Long id){
        if(!repository.existsById(id)){
            throw new ResourceNotFoundException(Messages.RESOURCE_NOT_FOUND);
        }
    }

}
