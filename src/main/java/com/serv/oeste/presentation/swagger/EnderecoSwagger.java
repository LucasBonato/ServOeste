package com.serv.oeste.presentation.swagger;

import com.serv.oeste.application.dtos.reponses.EnderecoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Endereço")
public interface EnderecoSwagger extends SwaggerConfiguration {
    @Operation(description = "Forma de trazer as informações de um endereço através do CEP, utilizando a API ViaCEP.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "[Ok] Endereço devolvido com sucesso.", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "[BadRequest] Alguma informação foi passada de forma errada.", content = @Content(schema = @Schema(implementation = DummyResponse.class))),
            @ApiResponse(responseCode = "404", description = "[NotFound] O Endereço informado não foi encontrado.", content = @Content(schema = @Schema(implementation = DummyResponse.class))),
            @ApiResponse(responseCode = "500", description = "[ServiceUnavailable] A API ViaCep não se encontra online.", content = @Content(schema = @Schema(implementation = DummyResponse.class))),
            @ApiResponse(responseCode = "503", description = "[InternalServerError] Não foi possível acessar o serviço da ViaCep ou o RestTemplate não está funcionando corretamente(Chamar um técnico).", content = @Content(schema = @Schema(implementation = DummyResponse.class))),
    })
    ResponseEntity<EnderecoResponse> getFieldsEndereco(String cep);
}
