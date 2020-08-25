package br.com.cast.avaliacao.controller;

import br.com.cast.avaliacao.model.Categoria;
import br.com.cast.avaliacao.service.CategoriaService;
import java.util.List;
import java.util.Optional;
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
@RequestMapping("/api/v1/categorias")

public class CategoriaController {
    @Autowired
    private CategoriaService categoriaService;
    
    @GetMapping
    public ResponseEntity<List<Categoria>> findAll(){
        return ResponseEntity.ok(categoriaService.findAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Categoria> findById(@PathVariable Long id) {
        Optional<Categoria> categoria = categoriaService.findById(id);
        if (!categoria.isPresent()) {
            ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(categoria.get());
    }
    
    @PostMapping
    public ResponseEntity create(@Valid @RequestBody Categoria categoria) {
        return ResponseEntity.ok(categoriaService.save(categoria));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Categoria> update(@PathVariable Long id, @Valid @RequestBody Categoria categoria) {
        if (!categoriaService.findById(id).isPresent()) {
            ResponseEntity.badRequest().build();
        }
        categoria.setId(id);
        return ResponseEntity.ok(categoriaService.save(categoria));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        if (!categoriaService.findById(id).isPresent()) {
            ResponseEntity.badRequest().build();
        }
        categoriaService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
