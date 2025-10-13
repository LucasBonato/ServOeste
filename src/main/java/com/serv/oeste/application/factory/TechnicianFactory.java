package com.serv.oeste.application.factory;

import com.serv.oeste.domain.entities.specialty.Specialty;
import com.serv.oeste.domain.entities.technician.Technician;
import com.serv.oeste.domain.enums.Situacao;

import java.util.Collections;
import java.util.List;

public class TechnicianFactory {
    public static Technician createDefault() {
        return new Technician(
                1,
                "Carlos",
                "Silva",
                "1134567890",
                "11998765432",
                Situacao.ATIVO,
                Collections.emptyList()
        );
    }

    public static Technician createWithSpecialties(List<Specialty> specialties) {
        return new Technician(
                2,
                "Fernanda",
                "Lima",
                "2122220000",
                "21987654321",
                Situacao.ATIVO,
                specialties
        );
    }

    public static Technician createCustom(
            Integer id,
            String nome,
            String sobrenome,
            String telefoneFixo,
            String telefoneCelular,
            Situacao situacao,
            List<Specialty> especialidades
    ) {
        return new Technician(id, nome, sobrenome, telefoneFixo, telefoneCelular, situacao, especialidades);
    }

    public static Technician createMinimal(
            String nome,
            String sobrenome,
            String telefoneFixo,
            String telefoneCelular
    ) {
        return Technician.create(
                nome,
                sobrenome,
                telefoneFixo,
                telefoneCelular,
                List.of()
        );
    }
}
