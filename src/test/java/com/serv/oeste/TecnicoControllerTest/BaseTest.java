package com.serv.oeste.TecnicoControllerTest;

import com.serv.oeste.repository.EspecialidadeRepository;
import com.serv.oeste.repository.TecnicoRepository;
import com.serv.oeste.models.dtos.TecnicoDTO;
import com.serv.oeste.models.tecnico.Especialidade;
import com.serv.oeste.models.tecnico.Tecnico;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

public class BaseTest {
//    @Mock private EspecialidadeRepository especialidadeRepository;
//    @Mock protected TecnicoRepository tecnicoRepository;
//
//    protected Integer id = 1;
//    protected List<Integer> ids = new ArrayList<>();
//    protected List<String> conhecimentos = new ArrayList<>();
//
//    protected TecnicoDTO getTecnicoDTO(String nome, String sobrenome, String telefoneC, String telefoneF, List<Integer> ids, List<String> conhecimentos) {
//        TecnicoDTO tecnicoDTO = new TecnicoDTO();
//        tecnicoDTO.setNome(nome);
//        tecnicoDTO.setSobrenome(sobrenome);
//        tecnicoDTO.setTelefoneCelular(telefoneC);
//        tecnicoDTO.setTelefoneFixo(telefoneF);
//        tecnicoDTO.setEspecialidades_Ids(ids);
//
//        List<Especialidade> especialidades = new ArrayList<>();
//        Especialidade especialidade = new Especialidade();
//        for(int i = 0; i < ids.size(); i++){
//            especialidade.setId(ids.get(i));
//            especialidade.setConhecimento(conhecimentos.get(i));
//            especialidades.add(especialidade);
//        }
//
//        for(int i = 0; i < tecnicoDTO.getEspecialidades_Ids().size(); i++){
//            when(especialidadeRepository.findById(tecnicoDTO.getEspecialidades_Ids().get(i))).thenReturn(Optional.of(especialidades.get(i)));
//        }
//        return tecnicoDTO;
//    }
//
//    protected TecnicoDTO getTecnicoDTO(String nome, String sobrenome, String telefoneC, String telefoneF, List<Integer> ids) {
//        TecnicoDTO tecnicoDTO = new TecnicoDTO();
//        tecnicoDTO.setNome(nome);
//        tecnicoDTO.setSobrenome(sobrenome);
//        tecnicoDTO.setTelefoneCelular(telefoneC);
//        tecnicoDTO.setTelefoneFixo(telefoneF);
//        tecnicoDTO.setEspecialidades_Ids(ids);
//
//        List<Especialidade> especialidades = new ArrayList<>();
//        Especialidade especialidade = new Especialidade();
//        for(int i = 0; i < ids.size(); i++){
//            especialidade.setId(ids.get(i));
//            especialidades.add(especialidade);
//        }
//
//        for(int i = 0; i < tecnicoDTO.getEspecialidades_Ids().size(); i++){
//            when(especialidadeRepository.findById(tecnicoDTO.getEspecialidades_Ids().get(i))).thenReturn(Optional.empty());
//        }
//        return tecnicoDTO;
//    }
//
//    protected TecnicoDTO getTecnicoDTO(Integer id, String nome, String sobrenome, String telefoneC, String telefoneF, String situaco, List<Integer> ids, List<String> conhecimentos) {
//        TecnicoDTO tecnicoDTO = new TecnicoDTO();
//        tecnicoDTO.setNome(nome);
//        tecnicoDTO.setSobrenome(sobrenome);
//        tecnicoDTO.setTelefoneCelular(telefoneC);
//        tecnicoDTO.setTelefoneFixo(telefoneF);
//        tecnicoDTO.setEspecialidades_Ids(ids);
//        tecnicoDTO.setSituacao(situaco);
//
//        List<Especialidade> especialidades = new ArrayList<>();
//        Especialidade especialidade = new Especialidade();
//        for(int i = 0; i < ids.size(); i++){
//            especialidade.setId(ids.get(i));
//            especialidade.setConhecimento(conhecimentos.get(i));
//            especialidades.add(especialidade);
//        }
//
//        for(int i = 0; i < tecnicoDTO.getEspecialidades_Ids().size(); i++){
//            when(especialidadeRepository.findById(tecnicoDTO.getEspecialidades_Ids().get(i))).thenReturn(Optional.of(especialidades.get(i)));
//        }
//
//        when(tecnicoRepository.findById(id)).thenReturn(Optional.of(new Tecnico(tecnicoDTO)));
//        for(int i = 0; i < ids.size(); i++){
//            when(especialidadeRepository.findById(ids.get(i))).thenReturn(Optional.of(new Especialidade(ids.get(i), conhecimentos.get(i))));
//        }
//        return tecnicoDTO;
//    }
}
