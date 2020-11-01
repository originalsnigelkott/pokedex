package pokedex.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pokedex.entities.User;
import pokedex.exceptions.EntityNotFoundException;
import pokedex.repositories.UserRepository;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User findByUsername(String username) {
        var user = userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException("user", "username"));
        return user;
    }

    public User create(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(String id) {
        return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("user", "id"));
    }

    public void update(String id, User user) {
        checkExistenceById(id);
        checkUserPermission(id);
        user.setId(id);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public void delete(String id) {
        checkExistenceById(id);
        userRepository.deleteById(id);
    }

    private void checkExistenceById(String id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("user", "id");
        }
    }

    private void checkUserPermission(String id) {
        var currentUser = userRepository.findByUsername(getCurrentUser()).get();
        if (!userIsAdmin(currentUser.getId()) && !currentUser.getId().equals(id)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User lacks permission to update the user with this id.");
        }
    }

    private String getCurrentUser() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    private Boolean userIsAdmin(String id) {
        var user = userRepository.findById(id).get();
        return user.getRoles().contains("ADMIN");
    }
}
