package pokedex.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pokedex.entities.Ability;
import pokedex.services.AbilityService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/abilities")
public class AbilityController {
    @Autowired
    private AbilityService abilityService;

    @GetMapping("/{id}")
    public ResponseEntity<Ability> findById(@PathVariable String id) {
        return ResponseEntity.ok(abilityService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<Ability>> findAll(@RequestParam(required = false) String name) {
        return ResponseEntity.ok(abilityService.find(name));
    }
}
