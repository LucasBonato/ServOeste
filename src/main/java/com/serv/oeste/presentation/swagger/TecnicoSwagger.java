package com.serv.oeste.presentation.swagger;

import com.serv.oeste.application.dtos.requests.PageFilterRequest;
import com.serv.oeste.application.dtos.reponses.TecnicoWithSpecialityResponse;
import com.serv.oeste.application.dtos.reponses.TecnicoDisponibilidadeResponse;
import com.serv.oeste.application.dtos.reponses.TecnicoResponse;
import com.serv.oeste.application.dtos.requests.TecnicoDisponibilidadeRequest;
import com.serv.oeste.application.dtos.requests.TecnicoRequest;
import com.serv.oeste.application.dtos.requests.TecnicoRequestFilter;
import com.serv.oeste.domain.valueObjects.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "Técnico")
public interface TecnicoSwagger extends SwaggerConfiguration {

    @Operation(description = "Forma de trazer uma lista de técnicos a partir de um filtro com campos opcionais.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "[Ok] Lista de técnicos trazida com sucesso.", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "[BadRequest] Alguma informação foi passada de forma errada.", content = @Content(schema = @Schema(implementation = DummyResponse.class)))
    })
    ResponseEntity<PageResponse<TecnicoResponse>> fetchListByFilter(TecnicoRequestFilter filter, PageFilterRequest pageFilter);

    @Operation(description = "Forma de trazer o registro de um técnico atravez de seu id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "[Ok] Técnico devolvido com sucesso.", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "404", description = "[NotFound] O Técnico informado não foi encontrado.", content = @Content(schema = @Schema(implementation = DummyResponse.class)))
    })
    ResponseEntity<TecnicoWithSpecialityResponse> fetchOneById(Integer id);

    @Operation(description = "Forma de pegar informações para preencher a tabela de disponibilidade de técnicos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "[Ok] Informações devolvidas com sucesso.", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "[BadRequest] Alguma informação foi passada de forma errada.", content = @Content(schema = @Schema(implementation = DummyResponse.class)))
    })
    ResponseEntity<List<TecnicoDisponibilidadeResponse>> fetchListAvailability(TecnicoDisponibilidadeRequest tecnicoDisponibilidadeRequest);

    @Operation(description = "Forma de criar um novo registro de um técnico passando suas informações.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "[Created] Técnico registrado com sucesso.", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "[BadRequest] Alguma informação foi passada de forma errada.", content = @Content(schema = @Schema(implementation = DummyResponse.class)))
    })
    ResponseEntity<TecnicoWithSpecialityResponse> create(TecnicoRequest tecnicoResponse);

    @Operation(description = "Forma de atualizar o registro de um técnico, com os campos sendo opcionais, atualizando só o necessário.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "[Ok] Técnico atualizado com sucesso.", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "[BadRequest] Alguma informação foi passada de forma errada.", content = @Content(schema = @Schema(implementation = DummyResponse.class))),
            @ApiResponse(responseCode = "404", description = "[NotFound] O Técnico informado não foi encontrado.", content = @Content(schema = @Schema(implementation = DummyResponse.class))),
    })
    ResponseEntity<TecnicoWithSpecialityResponse> update(Integer id, TecnicoRequest tecnicoResponse);

    @Operation(description = "Forma de desativar uma lista de técnicos, alterando o `status` para: `Desativado`.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "[Ok] Lista de técnicos desativada sucesso.", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "[BadRequest] Alguma informação foi passada de forma errada.", content = @Content(schema = @Schema(implementation = DummyResponse.class))),
            @ApiResponse(responseCode = "404", description = "[NotFound] Algum técnico informado não foi encontrado.", content = @Content(schema = @Schema(implementation = DummyResponse.class))),
    })
    ResponseEntity<Void> disableListByIds(List<Integer> ids);
}
