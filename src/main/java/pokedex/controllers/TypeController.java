package pokedex.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pokedex.entities.Type;
import pokedex.services.TypeService;

import java.util.List;

@RestController
public class TypeController {
    @Autowired
    private TypeService typeService;

    @GetMapping("/{id}")
    public ResponseEntity<Type> findById(@PathVariable String id) {
        return ResponseEntity.ok(typeService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<Type>> findAll(@RequestParam String name) {
        return ResponseEntity.ok(typeService.find(name));
    }
}
