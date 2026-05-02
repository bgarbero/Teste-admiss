package com.teste_admiss.business;

import com.teste_admiss.api.dto.AeronaveResponseDTO;
import com.teste_admiss.business.impl.AeronaveServiceImpl;
import com.teste_admiss.infraestruture.domain.Aeronave;
import com.teste_admiss.infraestruture.exceptions.ResourceNotFoundException;
import com.teste_admiss.infraestruture.repository.AeronaveRepository;
import com.teste_admiss.tests.Factory;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class AeronaveServiceImplTest {

    @Mock
    private AeronaveRepository repository;

    @InjectMocks
    private AeronaveServiceImpl service;

    private long existingId;
    private long nonExistingId;
    private PageImpl<Aeronave> page;
    private Aeronave aeronave;

    @BeforeEach
    void setUp() {
        existingId = 1L;
        nonExistingId = 2L;
        aeronave = Factory.createAeronave();
        page = new PageImpl<>(List.of(aeronave));

        Mockito.when(repository.findAll((Pageable) ArgumentMatchers.any())).thenReturn(page);

        Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(aeronave);

        Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(aeronave));
        Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());

        Mockito.doNothing().when(repository).deleteById(existingId);

        Mockito.when(repository.existsById(existingId)).thenReturn(true);
        Mockito.when(repository.existsById(nonExistingId)).thenReturn(false);

        Mockito.when(repository.getReferenceById(existingId)).thenReturn(aeronave);
        Mockito.when(repository.getReferenceById(nonExistingId)).thenThrow(EntityNotFoundException.class);
    }

    @Test
    public void findByIdShouldReturnAeronaveWhenExistsId() {
        Aeronave result = service.findById(existingId);

        Assertions.assertNotNull(result);
        Assertions.assertInstanceOf(Aeronave.class, result);
    }

    @Test
    public void findByIdShouldReturnAeronaveWhenNotFoundId() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> service.findById(nonExistingId));
    }

    @Test
    public void updateShouldUpdateAeronaveWhenExistsId() {
        Aeronave result = service.update(aeronave);

        Assertions.assertInstanceOf(Aeronave.class, result);
    }

    @Test
    public void updateShouldReturnAeronaveWhenNotFoundId() {
        aeronave.setId(nonExistingId);
        Assertions.assertThrows(ResourceNotFoundException.class, () -> service.update(aeronave));
    }

    @Test
    public void finAllPagedShouldReturnPage() {
        Pageable pageable = PageRequest.of(0, 4);
        Page<Aeronave> result = service.findAll(pageable);

        Assertions.assertNotNull(result);
        Mockito.verify(repository).findAll(pageable);
    }

    @Test
    public void deleteShouldDoNothingWhenIdExists() {
        Assertions.assertDoesNotThrow(() -> service.delete(existingId));
        Mockito.verify(repository).deleteById(existingId);
    }

    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> service.delete(nonExistingId));
    }

}
