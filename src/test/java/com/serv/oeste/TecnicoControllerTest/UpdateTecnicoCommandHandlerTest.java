package com.serv.oeste.TecnicoControllerTest;

import com.serv.oeste.Exception.Tecnico.TecnicoNotFoundException;
import com.serv.oeste.OesteApplication;
import com.serv.oeste.Repository.EspecialidadeRepository;
import com.serv.oeste.Repository.TecnicoRepository;
import com.serv.oeste.Tecnico.Command.CommandHandler.Models.UpdateTecnicoCommand;
import com.serv.oeste.Tecnico.Command.CommandHandler.UpdateTecnicoCommandHandler;
import com.serv.oeste.Tecnico.Models.DTOs.TecnicoDTO;
import com.serv.oeste.Tecnico.Models.Tecnico;
import com.serv.oeste.Tecnico.Models.Especialidade;
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
        TecnicoDTO tecnicoDTO = getTecnicoDTO(id, "João", "Pedro", "11972761092", "", "ativo", ids, conhecimentos);

        UpdateTecnicoCommand updateTecnicoCommand = new UpdateTecnicoCommand(id, tecnicoDTO);
        ResponseEntity response = updateTecnico.execute(updateTecnicoCommand);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
    @Test
    public void UpdateTecnico_invalidId_returnsNotFoundException(){
        Integer id = 1;
        List<Integer> ids = new ArrayList<>();
        List<String> conhecimentos = new ArrayList<>();
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

    private TecnicoDTO getTecnicoDTO(Integer id, String nome, String sobrenome, String telefoneC, String telefoneF, String situaco, List<Integer> ids, List<String> conhecimentos) {
        TecnicoDTO tecnicoDTO = new TecnicoDTO();
        tecnicoDTO.setNome(nome);
        tecnicoDTO.setSobrenome(sobrenome);
        tecnicoDTO.setTelefoneCelular(telefoneC);
        tecnicoDTO.setTelefoneFixo(telefoneF);
        tecnicoDTO.setEspecialidades_Ids(ids);
        tecnicoDTO.setSituacao(situaco);

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

        when(tecnicoRepository.findById(id)).thenReturn(Optional.of(new Tecnico(tecnicoDTO)));
        for(int i = 0; i < ids.size(); i++){
            when(especialidadeRepository.findById(ids.get(i))).thenReturn(Optional.of(new Especialidade(ids.get(i), conhecimentos.get(i))));
        }
        return tecnicoDTO;
    }

}
