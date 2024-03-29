package com.serv.oeste.TecnicoControllerTest;

import com.serv.oeste.Exception.Tecnico.EspecialidadeNotFoundException;
import com.serv.oeste.Exception.Tecnico.EspecialidadesTecnicoEmptyException;
import com.serv.oeste.Exception.Tecnico.TecnicoNotValidException;
import com.serv.oeste.OesteApplication;
import com.serv.oeste.Tecnico.Command.CommandHandler.CreateTecnicoCommandHandler;
import com.serv.oeste.Tecnico.Models.DTOs.TecnicoDTO;
import org.junit.jupiter.api.Assertions;
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
public class CreateTecnicoCommandHandlerTest extends BaseTest{
    @InjectMocks private CreateTecnicoCommandHandler createTecnico;

    @Test
    public void createTecnico_validTecnico_returnsCREATED(){
        List<Integer> ids = new ArrayList<>();
        List<String> conhecimentos = new ArrayList<>();
        ids.add(1);
        conhecimentos.add("Outros");
        ids.add(2);
        conhecimentos.add("Geladeira");
        TecnicoDTO tecnicoDTO = getTecnicoDTO("Bla", "Bla", "11976258312", "", ids, conhecimentos);

        ResponseEntity response = createTecnico.execute(tecnicoDTO);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }
    @Test
    public void createTecnico_invalidTecnico_returnsTecnicoNotValidExceptionNome(){
        List<Integer> ids = new ArrayList<>();
        List<String> conhecimentos = new ArrayList<>();
        ids.add(1);
        conhecimentos.add("Outros");
        TecnicoDTO tecnicoDTO = getTecnicoDTO("", "Cleiton", "", "11853475235", ids, conhecimentos);

        TecnicoNotValidException exception = assertThrows(
                TecnicoNotValidException.class,
                () -> createTecnico.execute(tecnicoDTO)
        );
        Assertions.assertEquals("O 'nome' do técnico não pode ser vazio!", exception.getExceptionResponse().getMessage());
    }
    @Test
    public void createTecnico_invalidTecnico_returnsTecnicoNotValidExceptionMinimoDeCaracteres(){
        List<Integer> ids = new ArrayList<>();
        List<String> conhecimentos = new ArrayList<>();
        ids.add(1);
        conhecimentos.add("Outros");
        TecnicoDTO tecnicoDTO = getTecnicoDTO("J", "Cleiton", "", "11853475235", ids, conhecimentos);

        TecnicoNotValidException exception = assertThrows(
                TecnicoNotValidException.class,
                () -> createTecnico.execute(tecnicoDTO)
        );
        Assertions.assertEquals("O 'nome' do técnico precisa ter no mínimo 2 caracteres!", exception.getExceptionResponse().getMessage());
    }
    @Test
    public void createTecnico_invalidTecnico_returnsEspeciliadesEmptyException() {
        List<Integer> ids = new ArrayList<>();
        List<String> conhecimentos = new ArrayList<>();
        conhecimentos.add("Outros");
        TecnicoDTO tecnicoDTO = getTecnicoDTO("Bla", "Bla", "11976258312", "", ids, conhecimentos);

        EspecialidadesTecnicoEmptyException exception = assertThrows(
                EspecialidadesTecnicoEmptyException.class,
                () -> createTecnico.execute(tecnicoDTO)
        );
        Assertions.assertEquals("Técnico precisa possuir no mínimo uma especialidade!", exception.getExceptionResponse().getMessage());
    }
    @Test
    public void createTecnico_invalidTecnico_returnsEspeciliadeNotFound() {
        List<Integer> ids = new ArrayList<>();
        ids.add(1);
        TecnicoDTO tecnicoDTO = getTecnicoDTO("Bla", "Bla", "11976258312", "", ids);

        EspecialidadeNotFoundException exception = assertThrows(
                EspecialidadeNotFoundException.class,
                () -> createTecnico.execute(tecnicoDTO)
        );
        Assertions.assertEquals("Especialidade não encontrada!", exception.getExceptionResponse().getMessage());
    }
}
