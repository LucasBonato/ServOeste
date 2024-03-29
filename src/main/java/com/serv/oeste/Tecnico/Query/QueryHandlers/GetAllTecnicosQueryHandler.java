package com.serv.oeste.Tecnico.Query.QueryHandlers;

import com.serv.oeste.Tecnico.Models.Situacao;
import com.serv.oeste.Tecnico.Models.Tecnico;
import com.serv.oeste.Repository.TecnicoRepository;
import com.serv.oeste.Tecnico.Query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetAllTecnicosQueryHandler implements Query<Void, List<Tecnico>> {

    @Autowired private TecnicoRepository tecnicoRepository;
    @Override
    public ResponseEntity<List<Tecnico>> execute(Void input) {
        List<Tecnico> tecnicos = tecnicoRepository.findAllBySituacao(Situacao.ATIVO);
        return ResponseEntity.ok().body(tecnicos);
    }
}
