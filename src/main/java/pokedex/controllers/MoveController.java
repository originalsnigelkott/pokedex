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
import pokedex.entities.Move;
import pokedex.services.MoveService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/moves")
public class MoveController {
    @Autowired
    private MoveService moveService;

    @Operation(summary = "Find move by id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found matching move.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Move.class))}),
            @ApiResponse(responseCode = "404", description = "Did not find any move.",
                    content = @Content)})
    @GetMapping("/{id}")
    public ResponseEntity<Move> findById(@PathVariable String id) {
        return ResponseEntity.ok(moveService.findById(id));
    }

    @Operation(summary = "Finds moves matching the request.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found matching moves.",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Move.class)))}),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters.",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Did not find any move matching name. A full match with name is required",
                    content = @Content),
            @ApiResponse(responseCode = "503", description = "Third party service is not available.")
    })
    @GetMapping
    public ResponseEntity<List<Move>> findAll(@RequestParam(required = false) String name) {
        return ResponseEntity.ok(moveService.find(name));
    }
}
