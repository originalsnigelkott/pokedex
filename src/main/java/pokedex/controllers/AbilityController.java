package pokedex.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Find ability by id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found matching ability.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Ability.class))}),
            @ApiResponse(responseCode = "404", description = "Did not find any ability.",
                    content = @Content)})
    @GetMapping("/{id}")
    public ResponseEntity<Ability> findById(@PathVariable String id) {
        return ResponseEntity.ok(abilityService.findById(id));
    }

    @Operation(summary = "Finds abilities matching the request.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found matching abilities.",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Ability.class)))}),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters.",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Did not find any ability matching name. A full match with name is required",
                    content = @Content),
            @ApiResponse(responseCode = "503", description = "Third party service is not available.")
    })
    @GetMapping
    public ResponseEntity<List<Ability>> findAll(@RequestParam(required = false) String name) {
        return ResponseEntity.ok(abilityService.find(name));
    }
}
