package pokedex.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Find type by id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found matching type.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Type.class))}),
            @ApiResponse(responseCode = "404", description = "Did not find any type.",
                    content = @Content)})
    @GetMapping("/{id}")
    public ResponseEntity<Type> findById(@PathVariable String id) {
        return ResponseEntity.ok(typeService.findById(id));
    }

    @Operation(summary = "Finds types matching the request.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found matching types.",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Type.class)))}),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters.",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Did not find any type matching name. A full match with name is required",
                    content = @Content),
            @ApiResponse(responseCode = "503", description = "Third party service is not available.")
    })
    @GetMapping
    public ResponseEntity<List<Type>> findAll(
            @Parameter(description = "A full match with name is required.") @RequestParam(required = false) String name,
            @Parameter(description = "If page is not given, the first page (page 0) is return.") @RequestParam(required = false) Integer page
    ) {
        return ResponseEntity.ok(typeService.find(name, page));
    }
}
