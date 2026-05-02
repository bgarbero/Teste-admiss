package com.teste_admiss.repositories;

import com.teste_admiss.infraestruture.domain.Aeronave;
import com.teste_admiss.infraestruture.repository.AeronaveRepository;
import com.teste_admiss.tests.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
public class AeronaveRepositoryTests {

    @Autowired
    private AeronaveRepository repository;

    private long existingId;
    private long nonExistingId;
    private long countTotalProducts;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 1000L;
        countTotalProducts = 4;
    }

    @Test
    public void saveShouldPersistWithAutoIncrementWhenIdIsNull(){
        Aeronave aeronave = Factory.createAeronave();
        aeronave.setId(null);
        aeronave =  repository.save(aeronave);

        Assertions.assertNotNull(aeronave.getId());
        Assertions.assertEquals(countTotalProducts + 1, aeronave.getId());
    }

    @Test
    public void deleteShouldDeleteObjectWhenIdExists() {
        repository.deleteById(existingId);
        Optional<Aeronave> optional = repository.findById(existingId);
        Assertions.assertFalse(optional.isPresent());
    }

    @Test
    public void findByIdShouldReturnOptionalNotEmptyWhenIdExists() {
        Aeronave aeronave = Factory.createAeronave();
        aeronave.setId(existingId);
        Optional<Aeronave> optional = repository.findById(aeronave.getId());
        Assertions.assertTrue(optional.isPresent());
    }

    @Test
    public void findByIdShouldReturnOptionalEmptyWhenIdNotExists() {
        Aeronave aeronave = Factory.createAeronave();
        aeronave.setId(nonExistingId);
        Optional<Aeronave> optional = repository.findById(aeronave.getId());
        Assertions.assertFalse(optional.isPresent());
    }

}
