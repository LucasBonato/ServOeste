package com.serv.oeste.TecnicoControllerTest;

import com.serv.oeste.Exception.Tecnico.TecnicoNotFoundException;
import com.serv.oeste.OesteApplication;
import com.serv.oeste.Tecnico.Command.CommandHandler.Models.UpdateTecnicoCommand;
import com.serv.oeste.Tecnico.Command.CommandHandler.UpdateTecnicoCommandHandler;
import com.serv.oeste.Tecnico.Models.DTOs.TecnicoDTO;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = OesteApplication.class)
public class UpdateTecnicoCommandHandlerTest extends BaseTest {
    @InjectMocks private UpdateTecnicoCommandHandler updateTecnico;

    @Test
    public void UpdateTecnico_validObject_returnsOk(){
        ids.add(1);
        conhecimentos.add("Outros");
        TecnicoDTO tecnicoDTO = getTecnicoDTO(id, "João", "Pedro", "11972761092", "", "ativo", ids, conhecimentos);

        UpdateTecnicoCommand updateTecnicoCommand = new UpdateTecnicoCommand(id, tecnicoDTO);
        ResponseEntity response = updateTecnico.execute(updateTecnicoCommand);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
    @Test
    public void UpdateTecnico_invalidId_returnsNotFoundException(){
        ids.add(1);
        conhecimentos.add("Outros");
        TecnicoDTO tecnicoDTO = getTecnicoDTO(id, "João", "Pedro", "11972761092", "", "ativo", ids, conhecimentos);

        UpdateTecnicoCommand updateTecnicoCommand = new UpdateTecnicoCommand(0, tecnicoDTO);

        TecnicoNotFoundException exception = assertThrows(
                TecnicoNotFoundException.class,
                () -> updateTecnico.execute(updateTecnicoCommand)
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Técnico não encontrado!", exception.getExceptionResponse().getMessage());
    }

}
