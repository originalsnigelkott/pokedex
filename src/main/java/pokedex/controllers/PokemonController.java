package pokedex.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pokedex.entities.Pokemon;
import pokedex.services.PokemonService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/pokemon")
public class PokemonController {
    private final String ENDPOINT_NAME = "/api/v1/pokemon/";

    @Autowired
    private PokemonService pokemonService;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Pokemon> create(@Validated @RequestBody Pokemon pokemon) {
        var createdPokemon = pokemonService.create(pokemon);
        var uri = URI.create(ENDPOINT_NAME + createdPokemon.getId());
        return ResponseEntity.created(uri).body(createdPokemon);
    }

    @GetMapping
    public ResponseEntity<List<Pokemon>> find(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer minWeight,
            @RequestParam(required = false) Integer maxWeight
    ) {
        var pokemon = pokemonService.getPokemon(name, minWeight, maxWeight);
        return ResponseEntity.ok(pokemon);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pokemon> findById(@PathVariable String id) {
        return ResponseEntity.ok(pokemonService.getPokemonById(id));
    }

    @PutMapping("/{id}")
    @Secured("ROLE_ADMIN")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable String id, @RequestBody Pokemon pokemon) {
        pokemonService.update(id, pokemon);
    }

    @DeleteMapping("/{id}")
    @Secured("ROLE_ADMIN")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        pokemonService.delete(id);
    }
}
