package com.serv.oeste.presentation.swagger;

import com.serv.oeste.application.dtos.reponses.EspecialidadeResponse;
import com.serv.oeste.application.dtos.requests.SpecialtyRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "Especialidade")
public interface SpecialtySwagger extends SwaggerConfiguration {

    @Operation(description = "Forma de trazer a lista de especialidades (conhecimentos) cadastradas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "[Ok] Lista de especialidades trazida com sucesso.", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "[BadRequest] Alguma informação foi passada de forma errada.", content = @Content(schema = @Schema(implementation = DummyResponse.class)))
    })
    ResponseEntity<List<EspecialidadeResponse>> findAll();

    @Operation(description = "Forma de criar uma nova especialidade (somente admin).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "[Created] Especialidade registrada com sucesso.", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "[BadRequest] Alguma informação foi passada de forma errada.", content = @Content(schema = @Schema(implementation = DummyResponse.class))),
            @ApiResponse(responseCode = "403", description = "[Forbidden] Apenas administradores podem realizar essa ação.", content = @Content(schema = @Schema(implementation = DummyResponse.class)))
    })
    ResponseEntity<EspecialidadeResponse> create(SpecialtyRequest request);

    @Operation(description = "Forma de atualizar o nome de uma especialidade (somente admin).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "[Ok] Especialidade atualizada com sucesso.", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "[BadRequest] Alguma informação foi passada de forma errada.", content = @Content(schema = @Schema(implementation = DummyResponse.class))),
            @ApiResponse(responseCode = "403", description = "[Forbidden] Apenas administradores podem realizar essa ação.", content = @Content(schema = @Schema(implementation = DummyResponse.class))),
            @ApiResponse(responseCode = "404", description = "[NotFound] A especialidade informada não foi encontrada.", content = @Content(schema = @Schema(implementation = DummyResponse.class)))
    })
    ResponseEntity<EspecialidadeResponse> update(Integer id, SpecialtyRequest request);

    @Operation(description = "Forma de remover (desativar) uma especialidade que não esteja em uso por técnicos (somente admin).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "[Ok] Especialidade desativada com sucesso.", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "[BadRequest] Especialidade em uso ou protegida.", content = @Content(schema = @Schema(implementation = DummyResponse.class))),
            @ApiResponse(responseCode = "403", description = "[Forbidden] Apenas administradores podem realizar essa ação.", content = @Content(schema = @Schema(implementation = DummyResponse.class))),
            @ApiResponse(responseCode = "404", description = "[NotFound] A especialidade informada não foi encontrada.", content = @Content(schema = @Schema(implementation = DummyResponse.class)))
    })
    ResponseEntity<Void> deactivate(Integer id);
}