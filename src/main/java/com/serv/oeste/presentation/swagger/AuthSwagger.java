package com.serv.oeste.presentation.swagger;

import com.serv.oeste.application.dtos.reponses.AuthAccessTokenResponse;
import com.serv.oeste.application.dtos.requests.AuthLoginRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

@Tag(name = "Auth", description = "Endpoints responsáveis pela autenticação e autorização de usuários.")
public interface AuthSwagger extends SwaggerConfiguration {
    /**
     * Realiza o login de um usuário e devolve o access token.
     *
     * @param loginRequest Credenciais de login (usuário e senha).
     * @return Access token JWT em {@link AuthAccessTokenResponse}.
     */
    @Operation(description = "Método para autenticar um usuário e gerar o access token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "[Ok] Login realizado com sucesso.", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "401", description = "[Unauthorized] Credenciais inválidas.", content = @Content(schema = @Schema(implementation = DummyResponse.class)))
    })
    ResponseEntity<AuthAccessTokenResponse> login(AuthLoginRequest loginRequest, HttpServletResponse response);

    /**
     * Atualiza o access token utilizando o refresh token armazenado no cookie.
     *
     * @param refreshToken Cookie de refresh token (injetado automaticamente).
     * @return Novo access token em {@link AuthAccessTokenResponse}.
     */
    @Operation(description = "Método para renovar o access token utilizando o refresh token armazenado no cookie.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "[Ok] Token atualizado com sucesso.", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "401", description = "[Unauthorized] Refresh token ausente ou inválido.", content = @Content(schema = @Schema(implementation = DummyResponse.class)))
    })
    @Parameter(in = ParameterIn.COOKIE, name = "refreshToken", description = "Refresh token JWT", required = true)
    ResponseEntity<AuthAccessTokenResponse> refresh(String refreshToken, HttpServletResponse response);

    /**
     * Realiza o logout de um usuário e remove o refresh token.
     *
     * @param refreshToken Cookie de refresh token (injetado automaticamente).
     * @return Status 202 Accepted sem conteúdo no corpo.
     */
    @Operation(description = "Método para realizar logout e invalidar o refresh token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "[Accepted] Logout realizado com sucesso.", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "[BadRequest] Token inválido ou já expirado.", content = @Content(schema = @Schema(implementation = DummyResponse.class)))
    })
    ResponseEntity<Void> logout(String refreshToken, HttpServletResponse response);
}