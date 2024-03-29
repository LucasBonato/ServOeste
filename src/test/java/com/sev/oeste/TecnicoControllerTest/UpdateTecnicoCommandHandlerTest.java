package com.sev.oeste.TecnicoControllerTest;

import com.sev.oeste.OesteApplication;
import com.sev.oeste.Repository.EspecialidadeRepository;
import com.sev.oeste.Repository.TecnicoRepository;
import com.sev.oeste.Tecnico.Command.CommandHandler.Models.UpdateTecnicoCommand;
import com.sev.oeste.Tecnico.Command.CommandHandler.UpdateTecnicoCommandHandler;
import com.sev.oeste.Tecnico.Models.DTOs.TecnicoDTO;
import com.sev.oeste.Tecnico.Models.Especialidade;
import com.sev.oeste.Tecnico.Models.Tecnico;
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
public class UpdateTecnicoCommandHandlerTest {
    @InjectMocks private UpdateTecnicoCommandHandler updateTecnico;

    @Mock private TecnicoRepository tecnicoRepository;
    @Mock private EspecialidadeRepository especialidadeRepository;

    @Test
    public void UpdateTecnico_validObject_returnsOk(){
        Integer id = 1;
        List<Integer> ids = new ArrayList<>();
        List<String> conhecimentos = new ArrayList<>();
        ids.add(1);
        conhecimentos.add("Outros");
        TecnicoDTO tecnicoDTO = getTecnicoDTO("João", "Pedro", "11972761092", "", ids, conhecimentos);
        tecnicoDTO.setSituacao("ativo");
        UpdateTecnicoCommand updateTecnicoCommand = new UpdateTecnicoCommand(id, tecnicoDTO);

        when(tecnicoRepository.findById(id)).thenReturn(Optional.of(new Tecnico(tecnicoDTO)));
        for(int i = 0; i < ids.size(); i++){
            when(especialidadeRepository.findById(ids.get(i))).thenReturn(Optional.of(new Especialidade(ids.get(i), conhecimentos.get(i))));
        }

        ResponseEntity response = updateTecnico.execute(updateTecnicoCommand);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    private TecnicoDTO getTecnicoDTO(String nome, String sobrenome, String telefoneC, String telefoneF, List<Integer> ids, List<String> conhecimentos) {
        TecnicoDTO tecnicoDTO = new TecnicoDTO();
        tecnicoDTO.setNome(nome);
        tecnicoDTO.setSobrenome(sobrenome);
        tecnicoDTO.setTelefoneCelular(telefoneC);
        tecnicoDTO.setTelefoneFixo(telefoneF);
        tecnicoDTO.setEspecialidades_Ids(ids);

        List<Especialidade> especialidades = new ArrayList<>();
        Especialidade especialidade = new Especialidade();
        for(int i = 0; i < ids.size(); i++){
            especialidade.setId(ids.get(i));
            especialidade.setConhecimento(conhecimentos.get(i));
            especialidades.add(especialidade);
        }

        for(int i = 0; i < tecnicoDTO.getEspecialidades_Ids().size(); i++){
            when(especialidadeRepository.findById(tecnicoDTO.getEspecialidades_Ids().get(i))).thenReturn(Optional.of(especialidades.get(i)));
        }
        return tecnicoDTO;
    }

}
