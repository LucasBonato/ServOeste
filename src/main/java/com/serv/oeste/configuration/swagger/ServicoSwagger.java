package com.serv.oeste.configuration.swagger;

import com.serv.oeste.exception.ExceptionResponse;
import com.serv.oeste.models.dtos.requests.ClienteServicoRequest;
import com.serv.oeste.models.dtos.requests.ServicoRequest;
import com.serv.oeste.models.servico.TecnicoDisponibilidade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "Serviço")
public interface ServicoSwagger extends SwaggerConfiguration{
    @Operation(description = "Forma de registrar um novo serviço em um cliente já existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "[Created] Serviço registrado com sucesso.", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "[BadRequest] Alguma informação foi passada de forma errada.", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "404", description = "[NotFound] O Cliente informado não foi encontrado.", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
    })
    ResponseEntity<Void> cadastrarComClienteExistente(ServicoRequest servicoRequest);

    @Operation(description = "Forma de registrar um novo serviço e um novo cliente ao mesmo tempo.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "[Created] Serviço e cliente registrados com sucesso.", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "[BadRequest] Alguma informação foi passada de forma errada.", content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    ResponseEntity<Void> cadastrarComClienteNaoExistente(ClienteServicoRequest clienteServicoRequest);

    @Operation(description = "Forma de pegar informações para prencheer a tabela de disponibilidade de técnicos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "[Ok] Informações devolvidas com sucesso.", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "[BadRequest] Alguma informação foi passada de forma errada.", content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    ResponseEntity<List<TecnicoDisponibilidade>> getDadosDisponibilidadeTecnicos(String conhecimento);
}
