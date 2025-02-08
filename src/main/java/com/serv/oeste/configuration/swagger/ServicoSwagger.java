package com.serv.oeste.configuration.swagger;

import com.serv.oeste.exception.ExceptionResponse;
import com.serv.oeste.models.dtos.reponses.ServicoResponse;
import com.serv.oeste.models.dtos.requests.ClienteServicoRequest;
import com.serv.oeste.models.dtos.requests.ServicoRequest;
import com.serv.oeste.models.dtos.requests.ServicoRequestFilter;
import com.serv.oeste.models.dtos.requests.ServicoUpdateRequest;
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

    @Operation(description = "Filtro para coletar uma lista de serviços a partir das informações passadas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "[Ok] Lista de serviços voltada com sucesso.", useReturnTypeSchema = true),
    })
    ResponseEntity<List<ServicoResponse>> getByFilter(ServicoRequestFilter servicoRequestFilter);

    @Operation(description = "Forma de registrar um novo serviço em um cliente já existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "[Created] Serviço registrado com sucesso.", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "[BadRequest] Alguma informação foi passada de forma errada.", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "404", description = "[NotFound] O Cliente informado não foi encontrado.", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
    })
    ResponseEntity<ServicoResponse> cadastrarComClienteExistente(ServicoRequest servicoRequest);

    @Operation(description = "Forma de registrar um novo serviço e um novo cliente ao mesmo tempo.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "[Created] Serviço e cliente registrados com sucesso.", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "[BadRequest] Alguma informação foi passada de forma errada.", content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    ResponseEntity<ServicoResponse> cadastrarComClienteNaoExistente(ClienteServicoRequest clienteServicoRequest);

    @Operation(description = "Forma de atualizar um serviço pré existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "[Ok] Serviço foi atualizado com sucesso.", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "[BadRequest] Alguma informação foi passada de forma errada.", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "404", description = "[NotFound] O serviço informado não foi encontrado.", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
    })
    ResponseEntity<ServicoResponse> update(Integer id, ServicoUpdateRequest servicoUpdateRequest);

    @Operation(description = "Endpoint para deletar uma lista de serviços a partir de seus ids.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "[Ok] Lista de serviços deletada com sucesso.", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "[BadRequest] Alguma informação foi passada de forma errada.", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "404", description = "[NotFound] Algum serviço informado não foi encontrado.", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
    })
    ResponseEntity<Void> deleteListOfServicesById(List<Integer> ids);
}