package com.serv.oeste.TecnicoControllerTest;

import com.serv.oeste.OesteApplication;
import com.serv.oeste.Tecnico.Command.CommandHandler.DeleteTecnicoCommandHandler;
import com.serv.oeste.Tecnico.Models.DTOs.TecnicoDTO;
import com.serv.oeste.Tecnico.Models.Situacao;
import com.serv.oeste.Tecnico.Models.Tecnico;
import com.serv.oeste.Tecnico.Query.QueryHandlers.GetTecnicoQueryHandler;
import org.junit.jupiter.api.Test;
import org.junit.platform.engine.TestExecutionResult;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.print.attribute.standard.Sides;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = OesteApplication.class)
public class DeleteTecnicoCommnadHandlerTest extends BaseTest{
    @InjectMocks private DeleteTecnicoCommandHandler deleteTecnico;
    @InjectMocks private GetTecnicoQueryHandler getTecnico;

    @Test
    public void deleteTecnico_validId_returnsOk(){
        ids.add(1);
        ids.add(2);
        conhecimentos.add("Outros");
        conhecimentos.add("Máquina de Lavar");
        TecnicoDTO tecnicoDTO = getTecnicoDTO(id, "Lucas", "Bonato", "1192738567", "", "ativo", ids, conhecimentos);


        ResponseEntity response = deleteTecnico.execute(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
