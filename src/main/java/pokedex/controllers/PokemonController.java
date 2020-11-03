package pokedex.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Creates a new pokemon.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created the pokemon.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Pokemon.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid request body.",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Request lacks authentication.",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "User lacks permission to complete request.",
                    content = @Content)})
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Pokemon> create(@Validated @RequestBody Pokemon pokemon) {
        var createdPokemon = pokemonService.create(pokemon);
        var uri = URI.create(ENDPOINT_NAME + createdPokemon.getId());
        return ResponseEntity.created(uri).body(createdPokemon);
    }

    @Operation(summary = "Find pokemon by id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found matching pokemon.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Pokemon.class))}),
            @ApiResponse(responseCode = "404", description = "Did not find any pokemon.",
                    content = @Content)})
    @GetMapping("/{id}")
    public ResponseEntity<Pokemon> findById(@PathVariable String id) {
        return ResponseEntity.ok(pokemonService.getPokemonById(id));
    }

    @Operation(summary = "Finds pokemon matching the request.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found matching pokemon.",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Pokemon.class)))}),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters.",
                    content = @Content),
            @ApiResponse(responseCode = "503", description = "Third party service is not available.")
    })
    @GetMapping
    public ResponseEntity<List<Pokemon>> find(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer minWeight,
            @RequestParam(required = false) Integer maxWeight,
            @RequestParam(required = false) Integer minHeight,
            @RequestParam(required = false) Integer maxHeight
    ) {
        var pokemon = pokemonService.getPokemon(name, minWeight, maxWeight, minHeight, maxHeight);
        return ResponseEntity.ok(pokemon);
    }

    @Operation(summary = "Updates pokemon by id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Updated the pokemon.",
                    content = {@Content}),
            @ApiResponse(responseCode = "400", description = "Invalid request body.",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Request lacks authentication.",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "User lacks permission to complete request.",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Did not find any pokemon.",
                    content = @Content)})
    @PutMapping("/{id}")
    @Secured("ROLE_ADMIN")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable String id, @Validated @RequestBody Pokemon pokemon) {
        pokemonService.update(id, pokemon);
    }

    @Operation(summary = "Deletes pokemon by id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Deleted the pokemon.",
                    content = {@Content}),
            @ApiResponse(responseCode = "401", description = "Request lacks authentication.",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "User lacks permission to complete request.",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Did not find any pokemon.",
                    content = @Content)})
    @DeleteMapping("/{id}")
    @Secured("ROLE_ADMIN")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        pokemonService.delete(id);
    }
}
