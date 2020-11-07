package pokedex.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pokedex.entities.Move;
import pokedex.services.MoveService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/moves")
public class MoveController {
    @Autowired
    private MoveService moveService;

    @GetMapping("/{id}")
    public ResponseEntity<Move> findById(@PathVariable String id) {
        return ResponseEntity.ok(moveService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<Move>> findAll(@RequestParam(required = false) String name) {
        return ResponseEntity.ok(moveService.find(name));
    }
}
