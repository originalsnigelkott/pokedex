package pokedex.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pokedex.entities.User;
import pokedex.exceptions.EntityNotFoundException;
import pokedex.repositories.UserRepository;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User findByUsername(String username) {
        var user = userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException("user", "username"));
        return user;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(String id) {
        return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("user", "id"));
    }

    public User create(User user) {
        return userRepository.save(user);
    }
}
