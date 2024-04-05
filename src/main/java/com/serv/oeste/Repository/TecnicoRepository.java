package com.serv.oeste.Repository;

import com.serv.oeste.Models.DTOs.TecnicoDTO;
import com.serv.oeste.Models.Situacao;
import com.serv.oeste.Models.Tecnico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TecnicoRepository extends JpaRepository<Tecnico, Integer> {
    List<Tecnico> findAllBySituacao(Situacao situacao);
    @Query("""
            SELECT new com.serv.oeste.Models.Tecnico(id, nome, sobrenome, telefoneFixo, telefoneCelular, situacao)
            FROM Tecnico
            WHERE CONCAT(nome, ' ', sobrenome)
            LIKE %:nomeOuSobrenome%
        """)
    List<Tecnico> findByNomeOuSobrenome(@Param("nomeOuSobrenome") String nomeOuSobrenome);

    @Query(value = """
            SELECT te.id_especialidade FROM Tecnico t
            INNER JOIN Tecnico_Especialidade te ON t.Id = te.Id_Tecnico
            INNER JOIN Especialidade e ON e.Id = te.Id_Especialidade
            WHERE t.Id = :Id_Especialidade
        """, nativeQuery = true)
    List<Integer> findByIdEspecialidade(@Param("Id_Especialidade") Integer id_Especialidade);
}
