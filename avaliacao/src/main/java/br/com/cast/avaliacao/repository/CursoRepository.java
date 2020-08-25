/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cast.avaliacao.repository;

import br.com.cast.avaliacao.model.Curso;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author Upway-01
 */
public interface CursoRepository extends JpaRepository<Curso, Long>{
    @Query(value = "select * from curso where :dataInicio between DATA_INICIO and DATA_TERMINO or :dataTermino between DATA_INICIO and DATA_TERMINO", nativeQuery = true)
    Optional<Curso> verifyByDate(@Param("dataInicio") Date dataInicio, @Param("dataTermino") Date dataTermino);
    
    @Query(value = "select * from curso where (:dataInicio between DATA_INICIO and DATA_TERMINO or :dataTermino between DATA_INICIO and DATA_TERMINO) and ID != :id", nativeQuery = true)
    Optional<Curso> verifyByDateDeleteId(@Param("dataInicio") Date dataInicio, @Param("dataTermino") Date dataTermino, @Param("id") Long id);
    
    @Query(value = "select * from curso where DESCRICAO like :descricao", nativeQuery = true)
    Optional<List<Curso>> findByName(@Param("descricao") String descricao);
}
