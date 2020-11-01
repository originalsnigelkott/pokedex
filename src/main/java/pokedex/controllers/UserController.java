package pokedex.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pokedex.entities.User;
import pokedex.services.UserService;

import java.net.URI;
import java.util.List;

@Controller
@RequestMapping("/api/v1/users/")
public class UserController {
    private final String ENDPOINT_NAME = "/api/v1/users";

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<User> create(@RequestBody User user) {
        var createdUser = userService.create(user);
        var uri = URI.create(ENDPOINT_NAME + createdUser.getId());
        return ResponseEntity.created(uri).body(createdUser);
    }

    @GetMapping
    public ResponseEntity<List<User>> find() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> findById(@PathVariable String id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable String id, @RequestBody User user) {
        userService.update(id, user);
    }
}
