package com.sev.oeste.TecnicoControllerTest;

import com.sev.oeste.Exception.Tecnico.TecnicoNotFoundException;
import com.sev.oeste.OesteApplication;
import com.sev.oeste.Repository.TecnicoRepository;
import com.sev.oeste.Tecnico.Models.Especialidade;
import com.sev.oeste.Tecnico.Models.Tecnico;
import com.sev.oeste.Tecnico.Query.QueryHandlers.GetTecnicoQueryHandler;
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

    @InjectMocks private GetTecnicoQueryHandler getTecnicoQueryHandler;

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

        when(tecnicoRepository.findById(tecnico.getId())).thenReturn(Optional.of(tecnico));

        ResponseEntity<Tecnico> response = getTecnicoQueryHandler.execute(tecnico.getId());
        assertEquals(tecnico, response.getBody());
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

        when(tecnicoRepository.findById(tecnico.getId())).thenReturn(Optional.of(tecnico));

        ResponseEntity<Tecnico> response = getTecnicoQueryHandler.execute(1);
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

        //when(tecnicoRepository.findById(tecnico.getId())).thenReturn(Optional.of(tecnico));

        TecnicoNotFoundException exception = assertThrows(
                TecnicoNotFoundException.class,
                () -> getTecnicoQueryHandler.execute(tecnico.getId())
        );
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

}