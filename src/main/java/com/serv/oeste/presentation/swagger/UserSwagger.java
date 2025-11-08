package com.serv.oeste.presentation.swagger;

import com.serv.oeste.application.dtos.requests.UserRegisterRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "User", description = "Endpoints responsáveis pelo usuário")
public interface UserSwagger extends SwaggerConfiguration {
    /**
     * Registra um novo usuário com base nas credenciais informadas.
     *
     * @param registerRequest Objeto contendo nome de usuário, senha e role.
     * @return Status 201 Created sem conteúdo no corpo.
     */
    @Operation(description = "Método para registrar um novo usuário.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "[Created] Usuário registrado com sucesso.", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "[BadRequest] Dados inválidos ou já existentes.", content = @Content(schema = @Schema(implementation = DummyResponse.class)))
    })
    ResponseEntity<Void> register(UserRegisterRequest registerRequest);
}
