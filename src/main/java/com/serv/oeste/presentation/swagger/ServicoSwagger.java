package com.serv.oeste.presentation.swagger;

import com.serv.oeste.application.dtos.reponses.ServicoResponse;
import com.serv.oeste.application.dtos.requests.*;
import com.serv.oeste.domain.valueObjects.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "Serviço")
public interface ServicoSwagger extends SwaggerConfiguration {
    @Operation(description = "Forma de trazer o registro de um serviço através de seu id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "[Ok] Serviço devolvido com sucesso.", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "404", description = "[NotFound] O Serviço informado não foi encontrado.", content = @Content(schema = @Schema(implementation = DummyResponse.class)))
    })
    ResponseEntity<ServicoResponse> fetchOneById(Integer id);

    @Operation(description = "Filtro para coletar uma lista de serviços a partir das informações passadas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "[Ok] Lista de serviços voltada com sucesso.", useReturnTypeSchema = true),
    })
    ResponseEntity<PageResponse<ServicoResponse>> fetchListByFilter(ServicoRequestFilter servicoRequestFilter, PageFilterRequest pageFilter);

    @Operation(description = "Forma de registrar um novo serviço em um cliente já existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "[Created] Serviço registrado com sucesso.", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "[BadRequest] Alguma informação foi passada de forma errada.", content = @Content(schema = @Schema(implementation = DummyResponse.class))),
            @ApiResponse(responseCode = "404", description = "[NotFound] O Cliente informado não foi encontrado.", content = @Content(schema = @Schema(implementation = DummyResponse.class))),
    })
    ResponseEntity<ServicoResponse> cadastrarComClienteExistente(ServicoRequest servicoRequest);

    @Operation(description = "Forma de registrar um novo serviço e um novo cliente ao mesmo tempo.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "[Created] Serviço e cliente registrados com sucesso.", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "[BadRequest] Alguma informação foi passada de forma errada.", content = @Content(schema = @Schema(implementation = DummyResponse.class)))
    })
    ResponseEntity<ServicoResponse> cadastrarComClienteNaoExistente(ClienteServicoRequest clienteServicoRequest);

    @Operation(description = "Forma de atualizar um serviço pré existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "[Ok] Serviço foi atualizado com sucesso.", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "[BadRequest] Alguma informação foi passada de forma errada.", content = @Content(schema = @Schema(implementation = DummyResponse.class))),
            @ApiResponse(responseCode = "404", description = "[NotFound] O serviço informado não foi encontrado.", content = @Content(schema = @Schema(implementation = DummyResponse.class))),
    })
    ResponseEntity<ServicoResponse> update(Integer id, ServicoUpdateRequest servicoUpdateRequest);

    @Operation(description = "Endpoint para deletar uma lista de serviços a partir de seus ids.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "[Ok] Lista de serviços deletada com sucesso.", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "[BadRequest] Alguma informação foi passada de forma errada.", content = @Content(schema = @Schema(implementation = DummyResponse.class))),
            @ApiResponse(responseCode = "404", description = "[NotFound] Algum serviço informado não foi encontrado.", content = @Content(schema = @Schema(implementation = DummyResponse.class))),
    })
    ResponseEntity<Void> deleteListById(List<Integer> ids);
}