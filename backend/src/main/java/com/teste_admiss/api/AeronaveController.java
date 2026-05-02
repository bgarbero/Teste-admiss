package com.teste_admiss.api;

import com.teste_admiss.api.dto.AeronaveResponseDTO;
import com.teste_admiss.api.dto.AeronaveResponseFullDTO;
import com.teste_admiss.api.mapper.AeronaveMapper;
import com.teste_admiss.business.AeronaveService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/aeronaves")
@RequiredArgsConstructor
public class AeronaveController {

    private final AeronaveService service;
    private final AeronaveMapper mapper;

    @GetMapping
    public ResponseEntity<Page<AeronaveResponseDTO>> findAll(
            @PageableDefault(page = 0, size = 5, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(service.findAll(pageable).map(mapper::toDTO));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<AeronaveResponseFullDTO> findById(@PathVariable Long id){
        AeronaveResponseFullDTO dto = mapper.toFullDTO(service.findById(id));
        return ResponseEntity.ok().body(dto);
    }

    @GetMapping("/find")
    public ResponseEntity<Page<AeronaveResponseDTO>> findByFiltros(
            @RequestParam(required = false) String marca,
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) Integer ano,
            @RequestParam(required = false) Boolean vendido,
            Pageable pageable) {
        return ResponseEntity.ok(
                service.findByFiltros(marca, nome, ano, vendido, pageable)
                        .map(mapper::toDTO)
        );
    }

}
