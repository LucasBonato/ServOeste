package com.serv.oeste.TecnicoControllerTest;

import com.serv.oeste.Exception.Tecnico.TecnicoNotFoundException;
import com.serv.oeste.OesteApplication;
import com.serv.oeste.Repository.TecnicoRepository;
import com.serv.oeste.Models.Especialidade;
import com.serv.oeste.Models.Tecnico;
import com.serv.oeste.Service.TecnicoService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = OesteApplication.class)
public class GetTecnicoQueryHandlerTest {

    @InjectMocks private TecnicoService tecnicoService;

    @Mock private TecnicoRepository tecnicoRepository;

    @Test
    public void getTecnicoQueryHandler_validId_returnsTecnico(){

        List<Especialidade> especialidades = new ArrayList<>();

        Especialidade esp = new Especialidade();
        esp.setId(1);
        esp.setConhecimento("Outros");

        especialidades.add(esp);

        Tecnico tecnico = new Tecnico();
        tecnico.setId(1);
        tecnico.setNome("Ana");
        tecnico.setSobrenome("Julia");
        tecnico.setTelefoneCelular("11938517043");
        tecnico.setEspecialidades(especialidades);

        List<Tecnico> tecnicos = new ArrayList<>();
        tecnicos.add(tecnico);

        when(tecnicoRepository.findByIdLike(tecnico.getId())).thenReturn(tecnicos);

        ResponseEntity<List<Tecnico>> response = tecnicoService.getLike(tecnico.getId(), null, null);
        assertEquals(tecnicos, response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void getTecnicoQueryHandler_validId_returnsOKStatusCode(){
        List<Especialidade> especialidades = new ArrayList<>();

        Especialidade esp = new Especialidade();
        esp.setId(1);
        esp.setConhecimento("Outros");

        especialidades.add(esp);

        Tecnico tecnico = new Tecnico();
        tecnico.setId(1);
        tecnico.setNome("Ana");
        tecnico.setSobrenome("Julia");
        tecnico.setTelefoneCelular("11938517043");
        tecnico.setEspecialidades(especialidades);

        List<Tecnico> tecnicos = new ArrayList<>();
        tecnicos.add(tecnico);

        when(tecnicoRepository.findByIdLike(tecnico.getId())).thenReturn(tecnicos);

        ResponseEntity<List<Tecnico>> response = tecnicoService.getLike(1, null, null);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void getTecnicoQueryHandler_invalidId_returns404AndException(){
        List<Especialidade> especialidades = new ArrayList<>();

        Especialidade esp = new Especialidade();
        esp.setId(1);
        esp.setConhecimento("Outros");

        especialidades.add(esp);

        Tecnico tecnico = new Tecnico();
        tecnico.setId(1);
        tecnico.setNome("Ana");
        tecnico.setSobrenome("Julia");
        tecnico.setTelefoneCelular("11938517043");
        tecnico.setEspecialidades(especialidades);

        List<Tecnico> tecnicos = new ArrayList<>();
        tecnicos.add(tecnico);

        when(tecnicoRepository.findByIdLike(tecnico.getId())).thenReturn(tecnicos);
        when(tecnicoRepository.findById(tecnico.getId())).thenReturn(Optional.empty());

        TecnicoNotFoundException exception = assertThrows(
                TecnicoNotFoundException.class,
                () -> tecnicoService.getLike(tecnico.getId(), null, null)
        );
        Assertions.assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }
}