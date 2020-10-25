package pokedex.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pokedex.entities.Pokemon;
import pokedex.services.PokemonService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/pokemon")
public class PokemonController {
    @Autowired
    private PokemonService pokemonService;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Pokemon> create(@RequestBody Pokemon pokemon) {
        var createdPokemon = pokemonService.create(pokemon);
        var uri = URI.create("/api/v1/pokemon" + createdPokemon.getId());
        return ResponseEntity.created(uri).body(createdPokemon);
    }

    @GetMapping
    public ResponseEntity<List<Pokemon>> find(@RequestParam(required = false) String name) {
        var pokemon = pokemonService.getPokemonByName(name);
        return ResponseEntity.ok(pokemon);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pokemon> findById(@PathVariable String id) {
        return ResponseEntity.ok(pokemonService.getPokemonById(id));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable String id, @RequestBody Pokemon pokemon) {
        pokemonService.update(id, pokemon);
    }
}
