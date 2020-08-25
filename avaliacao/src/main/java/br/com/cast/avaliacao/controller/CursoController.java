/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cast.avaliacao.controller;

import br.com.cast.avaliacao.model.Categoria;
import br.com.cast.avaliacao.model.Curso;
import br.com.cast.avaliacao.service.CategoriaService;
import br.com.cast.avaliacao.service.CursoService;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/cursos")
public class CursoController {
    @Autowired
    private CursoService cursoService;
    
    @Autowired
    private CategoriaService categoriaService;
    
    @GetMapping
    public ResponseEntity<List<Curso>> findAll(){
        return ResponseEntity.ok(cursoService.findAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Curso> findById(@PathVariable Long id) {
        Optional<Curso> curso = cursoService.findById(id);
        if (!curso.isPresent()) {
            ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(curso.get());
    }
    
    @PostMapping("/find")
    public ResponseEntity<List<Curso>> findByName(@RequestBody String descricao) {
        Optional<List<Curso>> cursos = cursoService.findByName(descricao);
        if (!cursos.isPresent()) {
            ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(cursos.get());
    }
       
    @PostMapping
    public ResponseEntity create(@Valid @RequestBody Curso curso){
        boolean verificarPeriodo = cursoService.verifyByDate(curso.getDataInicio(), curso.getDataTermino());
        if ( verificarPeriodo ) {
            Optional<Categoria> c = categoriaService.findById(curso.getCategoria().getId());
            
            if (!c.isEmpty()) {
                curso.setCategoria(c.get());
            } else {
                curso.getCategoria().setId(null);
            }
            return ResponseEntity.ok(cursoService.save(curso));
        }
        return ResponseEntity.badRequest().body("Existe(m) curso(s) planejado(s) dentro do período informado");
    }
    
    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable Long id, @Valid @RequestBody Curso curso) {
        if (!cursoService.findById(id).isPresent()) {
            ResponseEntity.badRequest().build();
        }
        boolean verificarPeriodo = cursoService.verifyByDateDeleteId(curso.getDataInicio(), curso.getDataTermino(), curso.getId());
        if ( verificarPeriodo ) {
            Optional<Categoria> c = categoriaService.findById(curso.getCategoria().getId());
            
            if (!c.isEmpty()) {
                curso.setCategoria(c.get());
            } else {
                curso.getCategoria().setId(null);
            }
            return ResponseEntity.ok(cursoService.save(curso));
        }
        return ResponseEntity.badRequest().body("Existe(m) curso(s) planejado(s) dentro do período informado");
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Long> delete(@PathVariable Long id) {
        if (!cursoService.findById(id).isPresent()) {
            ResponseEntity.badRequest().build();
        }
        
        cursoService.deleteById(id);
        return ResponseEntity.ok(id);
    }
}
