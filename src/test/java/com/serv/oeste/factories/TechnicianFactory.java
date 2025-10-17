package com.serv.oeste.factories;

import com.serv.oeste.domain.valueObjects.Specialty;
import com.serv.oeste.domain.entities.technician.Technician;
import com.serv.oeste.domain.enums.Situacao;

import java.util.Collections;
import java.util.List;

public class TechnicianFactory {
    public static Technician createDefault() {
        return Technician.restore(
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
        return Technician.restore(
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
        return Technician.restore(id, nome, sobrenome, telefoneFixo, telefoneCelular, situacao, especialidades);
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
