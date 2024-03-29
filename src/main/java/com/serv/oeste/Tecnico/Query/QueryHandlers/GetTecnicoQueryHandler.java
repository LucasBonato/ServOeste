package com.serv.oeste.Tecnico.Query.QueryHandlers;

import com.serv.oeste.Exception.Tecnico.TecnicoNotFoundException;
import com.serv.oeste.Tecnico.Models.Tecnico;
import com.serv.oeste.Repository.TecnicoRepository;
import com.serv.oeste.Tecnico.Query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GetTecnicoQueryHandler implements Query<Integer, Tecnico> {

    @Autowired private TecnicoRepository tecnicoRepository;
    @Override
    @Cacheable("tecnicoCache")
    public ResponseEntity<Tecnico> execute(Integer id) {
        Optional<Tecnico> tecnicoOptional = tecnicoRepository.findById(id);
        if(tecnicoOptional.isEmpty()){
            throw new TecnicoNotFoundException();
        }
        return ResponseEntity.ok(tecnicoOptional.get());
    }
}
