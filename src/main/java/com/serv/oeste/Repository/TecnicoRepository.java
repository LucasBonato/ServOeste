package com.serv.oeste.Repository;

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
}
