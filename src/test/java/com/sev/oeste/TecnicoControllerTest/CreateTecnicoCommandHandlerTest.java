package com.sev.oeste.TecnicoControllerTest;

import com.sev.oeste.OesteApplication;
import com.sev.oeste.Repository.EspecialidadeRepository;
import com.sev.oeste.Repository.TecnicoRepository;
import com.sev.oeste.Tecnico.Command.CommandHandler.CreateTecnicoCommandHandler;
import com.sev.oeste.Tecnico.Models.DTOs.TecnicoDTO;
import com.sev.oeste.Tecnico.Models.Especialidade;
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
import static org.mockito.Mockito.when;

@SpringBootTest(classes = OesteApplication.class)
public class CreateTecnicoCommandHandlerTest {
    @InjectMocks private CreateTecnicoCommandHandler createTecnico;

    @Mock private TecnicoRepository tecnicoRepository;
    @Mock private EspecialidadeRepository especialidadeRepository;

    @Test
    public void createTecnico_validTecnico_returnsCREATED(){
        TecnicoDTO tecnicoDTO = new TecnicoDTO();
        tecnicoDTO.setNome("Bla");
        tecnicoDTO.setSobrenome("Bla");
        tecnicoDTO.setTelefoneCelular("11976258312");
        tecnicoDTO.setTelefoneFixo("");
        List<Integer> ids = new ArrayList<>();
        ids.add(1);
        tecnicoDTO.setEspecialidades_Ids(ids);

        Especialidade especialidade = new Especialidade();
        especialidade.setId(3);
        especialidade.setConhecimento("Outros");

        List<Especialidade> especialidades = new ArrayList<>();
        especialidades.add(especialidade);

        for(int i = 0; i < tecnicoDTO.getEspecialidades_Ids().size(); i++){
            when(especialidadeRepository.findById(tecnicoDTO.getEspecialidades_Ids().get(i))).thenReturn(Optional.of(especialidades.get(i)));
        }

        ResponseEntity response = createTecnico.execute(tecnicoDTO);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }


}
