package com.serv.oeste.TecnicoControllerTest;

import com.serv.oeste.ServOesteApplication;
import com.serv.oeste.models.dtos.TecnicoDTO;
import com.serv.oeste.service.TecnicoService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = ServOesteApplication.class)
public class DeleteTecnicoCommnadHandlerTest extends BaseTest{
    @InjectMocks private TecnicoService tecnicoService;

    @Test
    public void deleteTecnico_validId_returnsOk(){
        ids.add(1);
        ids.add(2);
        conhecimentos.add("Outros");
        conhecimentos.add("Máquina de Lavar");
        TecnicoDTO tecnicoDTO = getTecnicoDTO(id, "Lucas", "Bonato", "1192738567", "", "ativo", ids, conhecimentos);

        List<Integer> ids = new ArrayList<>();
        ids.add(id);
        ResponseEntity response = tecnicoService.disableAList(ids);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
