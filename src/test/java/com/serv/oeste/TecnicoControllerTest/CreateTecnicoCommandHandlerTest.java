package com.serv.oeste.TecnicoControllerTest;

import com.serv.oeste.ServOesteApplication;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = ServOesteApplication.class)
public class CreateTecnicoCommandHandlerTest extends BaseTest{
//    @InjectMocks private TecnicoService tecnicoService;
//
//    @Test
//    public void createTecnico_validTecnico_returnsCREATED(){
//        List<Integer> ids = new ArrayList<>();
//        List<String> conhecimentos = new ArrayList<>();
//        ids.add(1);
//        conhecimentos.add("Outros");
//        ids.add(2);
//        conhecimentos.add("Geladeira");
//        TecnicoDTO tecnicoDTO = getTecnicoDTO("Bla", "Bla", "11976258312", "", ids, conhecimentos);
//
//        ResponseEntity response = tecnicoService.create(tecnicoDTO);
//        assertEquals(HttpStatus.CREATED, response.getStatusCode());
//    }
//    @Test
//    public void createTecnico_invalidTecnico_returnsTecnicoNotValidExceptionNome(){
//        List<Integer> ids = new ArrayList<>();
//        List<String> conhecimentos = new ArrayList<>();
//        ids.add(1);
//        conhecimentos.add("Outros");
//        TecnicoDTO tecnicoDTO = getTecnicoDTO("", "Cleiton", "", "11853475235", ids, conhecimentos);
//
//        TecnicoNotValidException exception = assertThrows(
//                TecnicoNotValidException.class,
//                () -> tecnicoService.create(tecnicoDTO)
//        );
//        Assertions.assertEquals("O 'nome' do técnico não pode ser vazio!", exception.getExceptionResponse().getMessage());
//    }
//    @Test
//    public void createTecnico_invalidTecnico_returnsTecnicoNotValidExceptionMinimoDeCaracteres(){
//        List<Integer> ids = new ArrayList<>();
//        List<String> conhecimentos = new ArrayList<>();
//        ids.add(1);
//        conhecimentos.add("Outros");
//        TecnicoDTO tecnicoDTO = getTecnicoDTO("J", "Cleiton", "", "11853475235", ids, conhecimentos);
//
//        TecnicoNotValidException exception = assertThrows(
//                TecnicoNotValidException.class,
//                () -> tecnicoService.create(tecnicoDTO)
//        );
//        Assertions.assertEquals("O 'nome' do técnico precisa ter no mínimo 2 caracteres!", exception.getExceptionResponse().getMessage());
//    }
//    @Test
//    public void createTecnico_invalidTecnico_returnsEspeciliadesEmptyException() {
//        List<Integer> ids = new ArrayList<>();
//        List<String> conhecimentos = new ArrayList<>();
//        conhecimentos.add("Outros");
//        TecnicoDTO tecnicoDTO = getTecnicoDTO("Bla", "Bla", "11976258312", "", ids, conhecimentos);
//
//        EspecialidadesTecnicoEmptyException exception = assertThrows(
//                EspecialidadesTecnicoEmptyException.class,
//                () -> tecnicoService.create(tecnicoDTO)
//        );
//        Assertions.assertEquals("Técnico precisa possuir no mínimo uma especialidade!", exception.getExceptionResponse().getMessage());
//    }
//    @Test
//    public void createTecnico_invalidTecnico_returnsEspeciliadeNotFound() {
//        List<Integer> ids = new ArrayList<>();
//        ids.add(1);
//        TecnicoDTO tecnicoDTO = getTecnicoDTO("Bla", "Bla", "11976258312", "", ids);
//
//        EspecialidadeNotFoundException exception = assertThrows(
//                EspecialidadeNotFoundException.class,
//                () -> tecnicoService.create(tecnicoDTO)
//        );
//        Assertions.assertEquals("Especialidade não encontrada!", exception.getExceptionResponse().getMessage());
//    }
}
