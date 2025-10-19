package com.serv.oeste.presentation.swagger;

import com.serv.oeste.application.dtos.reponses.ClienteResponse;
import com.serv.oeste.application.dtos.requests.ClienteRequest;
import com.serv.oeste.application.dtos.requests.ClienteRequestFilter;
import com.serv.oeste.application.dtos.requests.PageFilterRequest;
import com.serv.oeste.domain.valueObjects.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "Cliente")
public interface ClienteSwagger extends SwaggerConfiguration{

    /**
     * A way to fetch data from one client using its id to encounter on the database.
     *
     * @param id a number that represents the identifier of the client
     * @return The data from {@link ClienteResponse} class.
     */
    @Operation(description = "Método para pegar um cliente útilizando seu id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "[Ok] Cliente devolvido com sucesso.", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "404", description = "[NotFound] Cliente não foi encontrado.", content = @Content(schema = @Schema(implementation = DummyResponse.class))),
    })
    ResponseEntity<ClienteResponse> fetchOneById(Integer id);

    @Operation(description = "Método para pegar uma lista cliente útilizando um filtro com campos opcionais: Nome, Telefone e Endereço.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "[Ok] Clientes devolvidos com sucesso.", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "[BadRequest] Alguma informação foi passada de forma errada.", content = @Content(schema = @Schema(implementation = DummyResponse.class))),
    })
    ResponseEntity<PageResponse<ClienteResponse>> fetchListByFilter(ClienteRequestFilter filter, PageFilterRequest pageFilter);

    @Operation(description = "Forma de criar um novo registro de um cliente sem nenhum serviço conectado ao cliente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "[Created] Cliente registrado com sucesso.", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "[BadRequest] Alguma informação foi passada de forma errada.", content = @Content(schema = @Schema(implementation = DummyResponse.class))),
    })
    ResponseEntity<ClienteResponse> create(ClienteRequest clienteRequest);

    @Operation(description = "Forma de atualizar o registro de um cliente, com os campos sendo opcionais, atualizando só o necessário.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "[Ok] Cliente atualizado com sucesso.", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "[BadRequest] Alguma informação foi passada de forma errada.", content = @Content(schema = @Schema(implementation = DummyResponse.class))),
            @ApiResponse(responseCode = "404", description = "[NotFound] O Cliente informado não foi encontrado.", content = @Content(schema = @Schema(implementation = DummyResponse.class))),
    })
    ResponseEntity<ClienteResponse> update(Integer id, ClienteRequest clienteRequest);

    @Operation(description = "Forma de deletar uma lista de registros de clientes que não possuem serviços conectados aos clientes.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "[Ok] A lista de clientes foi deletada com sucesso.", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "[BadRequest] Alguma informação foi passada de forma errada.", content = @Content(schema = @Schema(implementation = DummyResponse.class))),
            @ApiResponse(responseCode = "404", description = "[NotFound] Algum cliente informado não foi encontrado.", content = @Content(schema = @Schema(implementation = DummyResponse.class))),
    })
    ResponseEntity<Void> deleteListByIds(List<Integer> ids);
}
