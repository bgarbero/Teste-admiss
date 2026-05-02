package com.teste_admiss.api;

import com.teste_admiss.api.dto.AeronaveRequestDTO;
import com.teste_admiss.api.dto.AeronaveResponseDTO;
import com.teste_admiss.api.dto.AeronaveResponseFullDTO;
import com.teste_admiss.api.mapper.AeronaveMapper;
import com.teste_admiss.business.AeronaveService;
import com.teste_admiss.config.Messages;
import com.teste_admiss.infraestruture.domain.Aeronave;
import com.teste_admiss.infraestruture.exceptions.ResourceNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/aeronaves")
@RequiredArgsConstructor
@Tag(name = "Aeronave", description = "Contém as operações para controle de cadastro de aeronaves.")
public class AeronaveController {

    private final AeronaveService service;
    private final AeronaveMapper mapper;

    @GetMapping
    @Operation(
            summary = "Listar todas aeronaves",
            description = "Listar todas as aeronaves cadastradas",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista todas as aeronaves cadastradas",
                            content = @Content(mediaType = "application/json"))
            })
    public ResponseEntity<Page<AeronaveResponseDTO>> findAll(
            @PageableDefault(page = 0, size = 5, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(service.findAll(pageable).map(mapper::toDTO));
    }

    @GetMapping(value = "/{id}")
    @Operation(summary = "Recuperar uma aeronave pelo id", description = "Recuperar uma aeronave pelo id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Aeronave recuperada com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AeronaveResponseDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Aeronave não encontrada",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResourceNotFoundException.class)))
            })
    public ResponseEntity<AeronaveResponseFullDTO> findById(@PathVariable Long id){
        AeronaveResponseFullDTO dto = mapper.toFullDTO(service.findById(id));
        return ResponseEntity.ok().body(dto);
    }

    @GetMapping("/nome")
    @Operation(summary = "Recuperar uma aeronave pelo nome", description = "Recuperar uma aeronave pelo nome",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Aeronave recuperada com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AeronaveResponseDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Aeronave não encontrada",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResourceNotFoundException.class)))
            })
    public ResponseEntity<AeronaveResponseFullDTO> findByName(@RequestParam(name = "nome", required = true) String nome){
        AeronaveResponseFullDTO dto = mapper.toFullDTO(service.findByName(nome));
        if (dto == null) {
            throw new ResourceNotFoundException(Messages.RESOURCE_NOT_FOUND);
        } else {
            return ResponseEntity.ok().body(dto);
        }
    }

    @PostMapping
    @Operation(summary = "Cadastra uma nova aeronave", description = "Recurso para cadastrar aeronaves",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Aeronave cadastrada com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AeronaveResponseDTO.class)))
            })
    public ResponseEntity<AeronaveResponseFullDTO> insert ( @Valid @RequestBody AeronaveRequestDTO dto){
        Aeronave result = service.insert(mapper.toEntity(dto));
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(result.getId()).toUri();
        return ResponseEntity.created(uri).body(mapper.toFullDTO(result));
    }

    @PutMapping
    @Operation(summary = "Atualizar aeronave", description = "Atualizar registro de aeronave",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Aeronave atualizada com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AeronaveResponseDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Aeronave não encontrada",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResourceNotFoundException.class)))
            })
    public ResponseEntity<AeronaveResponseDTO> update ( @Valid @RequestBody AeronaveRequestDTO dto){
        Aeronave result = service.update(mapper.toEntity(dto));
        return ResponseEntity.ok().body(mapper.toDTO(result));
    }

    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Deleção de aeronave", description = "Deletar uma aeronave pelo ID",
            responses = {
                    @ApiResponse(responseCode = "202", description = "Aeronave deletada com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AeronaveResponseDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Aeronave não encontrada",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResourceNotFoundException.class)))
            })
    public ResponseEntity<Void> delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

}
