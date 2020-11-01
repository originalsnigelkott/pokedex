package pokedex.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
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
        user.setId(id);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public void delete(String id) {
        checkExistenceById(id);
        userRepository.deleteById(id);
    }

    private void checkExistenceById(String id) {
        if(!userRepository.existsById(id)) {
            throw new EntityNotFoundException("user", "id");
        }
    }
}
