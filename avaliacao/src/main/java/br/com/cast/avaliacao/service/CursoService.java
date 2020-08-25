/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cast.avaliacao.service;

import br.com.cast.avaliacao.model.Curso;
import br.com.cast.avaliacao.repository.CursoRepository;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class CursoService {
    private final CursoRepository cursoRepository;
    
    public CursoService(CursoRepository cursoRepository) {
        this.cursoRepository = cursoRepository;
    }
    
    public List<Curso> findAll() {
        return cursoRepository.findAll();
    }
    
    public Optional<Curso> findById(Long id) {
        return cursoRepository.findById(id);
    }
    
    public Optional<List<Curso>> findByName(String descricao) {
        return cursoRepository.findByName("%" + descricao + "%");
    }
    
    public boolean verifyByDateDeleteId(Date dataInicio, Date dataFim, Long id) {
        Optional<Curso> curso = cursoRepository.verifyByDateDeleteId(dataInicio, dataInicio, id);
        
        if(curso.isEmpty()) {
            return true;
        }
        return false;
    }
    
    public boolean verifyByDate(Date dataInicio, Date dataFim) {
        Optional<Curso> curso = cursoRepository.verifyByDate(dataInicio, dataInicio);
        
        if(curso.isEmpty()) {
            return true;
        }
        return false;
    }
    
    public Curso save(Curso curso){
        return cursoRepository.save(curso);
    }
    
    public void deleteById(Long id) {
        cursoRepository.deleteById(id);
    }
    
    
}
