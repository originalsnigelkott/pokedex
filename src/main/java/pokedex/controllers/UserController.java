package pokedex.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pokedex.entities.User;
import pokedex.services.UserService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final String ENDPOINT_NAME = "/api/v1/users/";

    @Autowired
    private UserService userService;

    @Operation(summary = "Creates a new user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created the user.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class))}),
            @ApiResponse(responseCode = "400", description = "If invalid request body.",
                    content = @Content)})
    @PostMapping
    public ResponseEntity<User> create(@Validated @RequestBody User user) {
        var createdUser = userService.create(user);
        var uri = URI.create(ENDPOINT_NAME + createdUser.getId());
        return ResponseEntity.created(uri).body(createdUser);
    }

    @Operation(summary = "Gets all users.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found users.",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = User.class)))}),
            @ApiResponse(responseCode = "401", description = "Request lacks authentication.",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "User lacks permission to complete request. Admin role required.",
                    content = @Content)})
    @GetMapping
    @Secured("ROLE_ADMIN")
    public ResponseEntity<List<User>> find() {
        return ResponseEntity.ok(userService.findAll());
    }

    @Operation(summary = "Gets user by id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found user.",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = User.class)))}),
            @ApiResponse(responseCode = "401", description = "Request lacks authentication.",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "User lacks permission to complete request. Admin or member role required.",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "No user found.",
                    content = @Content)})
    @GetMapping("/{id}")
    @Secured({"ROLE_ADMIN", "ROLE_MEMBER"})
    public ResponseEntity<User> findById(@PathVariable String id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @Operation(summary = "Updates user by id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Updated user.",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid request body.",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Request lacks authentication.",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "User lacks permission to complete request. Admin or member role required. Member role can only change themselves",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "No user found.",
                    content = @Content)})
    @PutMapping("/{id}")
    @Secured({"ROLE_ADMIN", "ROLE_MEMBER"})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable String id, @Validated @RequestBody User user) {
        userService.update(id, user);
    }

    @Operation(summary = "Deletes a user by id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Deletes user.",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Request lacks authentication.",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "User lacks permission to complete request. Admin role required.",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "No user found.",
                    content = @Content)})
    @DeleteMapping("/{id}")
    @Secured("ROLE_ADMIN")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        userService.delete(id);
    }
}
