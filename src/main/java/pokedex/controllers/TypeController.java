package pokedex.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pokedex.entities.Type;
import pokedex.services.TypeService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/types")
public class TypeController {
    @Autowired
    private TypeService typeService;

    @GetMapping("/{id}")
    public ResponseEntity<Type> findById(@PathVariable String id) {
        return ResponseEntity.ok(typeService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<Type>> findAll(@RequestParam(required = false) String name) {
        return ResponseEntity.ok(typeService.find(name));
    }
}
