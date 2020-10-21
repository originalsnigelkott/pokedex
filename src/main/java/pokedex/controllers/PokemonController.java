package pokedex.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pokedex.entities.Pokemon;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/pokemon")
public class PokemonController {
    private List<Pokemon> dummyData = List.of(new Pokemon(1, "Bulbasaur"), new Pokemon(2, "Charmander"), new Pokemon(3, "Charmelion"));

    @GetMapping
    public ResponseEntity<List<Pokemon>> find(@RequestParam String name) {
        return ResponseEntity.ok(dummyData.stream().filter(x -> x.getName().toLowerCase().contains(name.toLowerCase())).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pokemon> findById(@PathVariable int id) {
        return ResponseEntity.ok(dummyData.stream().filter(x -> x.getId() == id).findFirst().get());
    }
}
