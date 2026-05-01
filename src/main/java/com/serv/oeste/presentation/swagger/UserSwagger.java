package com.serv.oeste.presentation.swagger;

import com.serv.oeste.application.dtos.reponses.UserResponse;
import com.serv.oeste.application.dtos.requests.PageFilterRequest;
import com.serv.oeste.application.dtos.requests.UserRegisterRequest;
import com.serv.oeste.application.dtos.requests.UserUpdateRequest;
import com.serv.oeste.domain.valueObjects.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "User", description = "Endpoints responsáveis pelo usuário")
public interface UserSwagger extends SwaggerConfiguration {

    /**
     * Encontra todos os usuários de forma paginada.
     *
     * @param pageFilter Objeto contendo os parâmetros de paginação (página e tamanho).
     * @return Status 200 OK com a lista paginada de usuários.
     */
    @Operation(description = "Retorna uma lista paginada de usuários.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "[OK] Usuários encontrados com sucesso.", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "[BadRequest] Parâmetros inválidos.", content = @Content(schema = @Schema(implementation = DummyResponse.class)))
    })
    ResponseEntity<PageResponse<UserResponse>> findAll(PageFilterRequest pageFilter);

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
    ResponseEntity<Void> create(UserRegisterRequest registerRequest);

/**
 * Atualiza os dados de um usuário existente.
 *
 * @param userUpdateRequest Objeto contendo os dados a serem atualizados do usuário.
 * @return Status 200 OK sem conteúdo no corpo.
 */
    @Operation(description = "Atualiza os dados de um usuário existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "[OK] Usuário atualizado com sucesso."),
            @ApiResponse(responseCode = "400", description = "[BadRequest] Dados inválidos.", content = @Content(schema = @Schema(implementation = DummyResponse.class))),
            @ApiResponse(responseCode = "404", description = "[NotFound] Usuário não encontrado.", content = @Content(schema = @Schema(implementation = DummyResponse.class)))
    })
    ResponseEntity<Void> update(UserUpdateRequest userUpdateRequest);

    /**
     * Remove um usuário pelo nome de usuário.
     *
     * @param username Nome de usuário a ser removido.
     * @return Status 200 OK sem conteúdo no corpo.
     */
    @Operation(description = "Remove um usuário pelo nome de usuário.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "[OK] Usuário removido com sucesso."),
            @ApiResponse(responseCode = "404", description = "[NotFound] Usuário não encontrado.", content = @Content(schema = @Schema(implementation = DummyResponse.class)))
    })
    ResponseEntity<Void> delete(@Parameter(description = "Nome de usuário a ser removido.", example = "joao.silva") String username);
}
