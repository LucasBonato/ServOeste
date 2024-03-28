package com.sev.oeste.TecnicoControllerTest;

import com.sev.oeste.Exception.Tecnico.EspecialidadeNotFoundException;
import com.sev.oeste.Exception.Tecnico.EspecialidadesTecnicoEmptyException;
import com.sev.oeste.Exception.Tecnico.TecnicoNotValidException;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = OesteApplication.class)
public class CreateTecnicoCommandHandlerTest {
    @InjectMocks private CreateTecnicoCommandHandler createTecnico;

    @Mock private TecnicoRepository tecnicoRepository;
    @Mock private EspecialidadeRepository especialidadeRepository;

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
        assertEquals("O 'nome' do técnico não pode ser vazio!", exception.getExceptionResponse().getMessage());
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
        assertEquals("O 'nome' do técnico precisa ter no mínimo 2 caracteres!", exception.getExceptionResponse().getMessage());
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
        assertEquals("Técnico precisa possuir no mínimo uma especialidade!", exception.getExceptionResponse().getMessage());
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
        assertEquals("Especialidade não encontrada!", exception.getExceptionResponse().getMessage());
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
    private TecnicoDTO getTecnicoDTO(String nome, String sobrenome, String telefoneC, String telefoneF, List<Integer> ids) {
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
            especialidades.add(especialidade);
        }

        for(int i = 0; i < tecnicoDTO.getEspecialidades_Ids().size(); i++){
            when(especialidadeRepository.findById(tecnicoDTO.getEspecialidades_Ids().get(i))).thenReturn(Optional.empty());
        }
        return tecnicoDTO;
    }
}
