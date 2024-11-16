package com.serv.oeste.configuration.database;

import com.serv.oeste.models.tecnico.Especialidade;
import com.serv.oeste.repository.EspecialidadeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

import java.util.ArrayList;
import java.util.List;

public class DataBaseSetValuesConfiguration implements CommandLineRunner {
    @Autowired private EspecialidadeRepository especialidadeRepository;
    List<Especialidade> especialidades = new ArrayList<>();

    @Override
    public void run(String... args) throws Exception {
        List<Especialidade> especialidade = especialidadeRepository.findAll();
        if (especialidade.isEmpty()) {
            addListOfEspecialidades();
            especialidadeRepository.saveAll(especialidades);
            System.out.println("Conhecimentos cadastrados com sucesso!");
        }
    }

    private void addListOfEspecialidades() {
        especialidades.add(new Especialidade("Adega"));
        especialidades.add(new Especialidade("Bebedouro"));
        especialidades.add(new Especialidade("Climatizador"));
        especialidades.add(new Especialidade("Cooler"));
        especialidades.add(new Especialidade("Frigobar"));
        especialidades.add(new Especialidade("Geladeira"));
        especialidades.add(new Especialidade("Lava Louça"));
        especialidades.add(new Especialidade("Lava Roupa"));
        especialidades.add(new Especialidade("Microondas"));
        especialidades.add(new Especialidade("Purificador"));
        especialidades.add(new Especialidade("Secadora"));
        especialidades.add(new Especialidade("Outros"));
    }
}
