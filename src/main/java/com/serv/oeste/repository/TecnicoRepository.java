package com.serv.oeste.repository;

import com.serv.oeste.models.tecnico.Tecnico;
import com.serv.oeste.models.tecnico.TecnicoDisponibilidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TecnicoRepository extends JpaRepository<Tecnico, Integer>, JpaSpecificationExecutor<Tecnico> {
    @Query(value = "CALL GetTabelaDisponibilidade(:days, :especialidadeId)", nativeQuery = true)
    Optional<List<TecnicoDisponibilidade>> getDisponibilidadeTecnicosPeloConhecimento(@Param("days") Integer days, @Param("especialidadeId") Integer especialidadeId);
//    List<Tecnico> findAllBySituacao(Situacao situacao);
//    @Query("""
//            SELECT new com.serv.oeste.models.tecnico.Tecnico(id, nome, sobrenome, telefoneFixo, telefoneCelular, situacao)
//            FROM Tecnico
//            WHERE CONCAT(nome, ' ', sobrenome)
//            LIKE %:nomeOuSobrenome%
//        """)
//    List<Tecnico> findByNomeOuSobrenome(@Param("nomeOuSobrenome") String nomeOuSobrenome);
//
//    @Query(value = """
//            SELECT * FROM Tecnico
//            WHERE Id Like %:id%
//        """, nativeQuery = true)
//    List<Tecnico> findByIdLike(@Param("id") Integer id);
//
//    @Query(value = """
//            SELECT te.id_especialidade FROM Tecnico t
//            INNER JOIN Tecnico_Especialidade te ON t.Id = te.Id_Tecnico
//            INNER JOIN Especialidade e ON e.Id = te.Id_Especialidade
//            WHERE t.Id = :Id_Especialidade
//        """, nativeQuery = true)
//    List<Integer> findByIdEspecialidade(@Param("Id_Especialidade") Integer id_Especialidade);
//
//    @Query(value = """
//            SELECT * FROM Tecnico
//            WHERE Id LIKE %:id%
//            AND CONCAT(nome, ' ', sobrenome) LIKE %:nomeOuSobrenome%
//        """, nativeQuery = true)
//    List<Tecnico> findByIdAndNomeLike(@Param(value = "id") Integer id, @Param(value = "nomeOuSobrenome")  String nome);
//
//    @Query(value = """
//            SELECT * FROM Tecnico
//            WHERE Id LIKE %:id%
//            AND CONCAT(nome, ' ', sobrenome) LIKE %:nomeOuSobrenome%
//            AND situacao = :situacao
//        """, nativeQuery = true)
//    List<Tecnico> findByIdAndNomeAndSituacaoLike(@Param(value = "id") Integer id, @Param(value = "nomeOuSobrenome") String nome, @Param(value = "situacao") String situacao);
//
//    @Query(value = """
//            SELECT * FROM Tecnico
//                WHERE CONCAT(nome, ' ', sobrenome) LIKE %:nomeOuSobrenome%
//                AND situacao = :situacao
//        """, nativeQuery = true)
//    List<Tecnico> findByNomeAndSituacaoLike(@Param(value = "nomeOuSobrenome") String nome, @Param(value = "situacao") String situacao);
//
//    @Query(value = """
//            SELECT * FROM Tecnico
//            WHERE Id LIKE %:id%
//            AND situacao = :situacao
//        """, nativeQuery = true)
//    List<Tecnico> findByIdAndSituacaoLike(@Param(value = "id") Integer id, @Param(value = "situacao") String situacao);
}