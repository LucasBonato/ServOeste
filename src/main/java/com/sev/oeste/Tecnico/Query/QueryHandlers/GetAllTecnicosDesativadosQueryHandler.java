package com.sev.oeste.Tecnico.Query.QueryHandlers;

import com.sev.oeste.Repository.TecnicoRepository;
import com.sev.oeste.Tecnico.Models.Situacao;
import com.sev.oeste.Tecnico.Models.Tecnico;
import com.sev.oeste.Tecnico.Query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetAllTecnicosDesativadosQueryHandler implements Query<Void, List<Tecnico>> {
    @Autowired private TecnicoRepository tecnicoRepository;
    @Override
    public ResponseEntity<List<Tecnico>> execute(Void unused) {
        List<Tecnico> tecnicos = tecnicoRepository.findAllBySituacao(Situacao.DESATIVADO);
        return ResponseEntity.ok(tecnicos);
    }
}
