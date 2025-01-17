package com.serv.oeste.TecnicoControllerTest;

import com.serv.oeste.ServOesteApplication;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = ServOesteApplication.class)
public class UpdateTecnicoCommandHandlerTest extends BaseTest {
//    @InjectMocks private TecnicoService tecnicoService;
//
//    @Test
//    public void UpdateTecnico_validObject_returnsOk(){
//        ids.add(1);
//        conhecimentos.add("Outros");
//        TecnicoDTO tecnicoDTO = getTecnicoDTO(id, "João", "Pedro", "11972761092", "", "ativo", ids, conhecimentos);
//
//        ResponseEntity response = tecnicoService.update(id, tecnicoDTO);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//    }
//    @Test
//    public void UpdateTecnico_invalidId_returnsNotFoundException(){
//        ids.add(1);
//        conhecimentos.add("Outros");
//        TecnicoDTO tecnicoDTO = getTecnicoDTO(id, "João", "Pedro", "11972761092", "", "ativo", ids, conhecimentos);
//
//        TecnicoNotFoundException exception = assertThrows(
//                TecnicoNotFoundException.class,
//                () -> tecnicoService.update(2, tecnicoDTO)
//        );
//
//        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
//        assertEquals("Técnico não encontrado!", exception.getExceptionResponse().getMessage());
//    }

}
