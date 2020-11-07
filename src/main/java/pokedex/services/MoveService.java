package pokedex.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pokedex.entities.Move;
import pokedex.exceptions.EntityNotFoundException;
import pokedex.repositories.MoveRepository;

import java.util.List;

@Service
public class MoveService {
    @Autowired
    private MoveRepository moveRepository;

    @Autowired
    private PokeApiConsumerService pokeApiConsumerService;

    public Move findById(String id) {
        return moveRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("move", "id"));
    }

    public List<Move> find(String name) {
        if (name != null) {
            return List.of(findByName(name));
        }
        return moveRepository.findAll();
    }

    private Move findByName(String name) {
        var move = moveRepository.findByName(name);
        if (move.isPresent()) {
            return move.get();
        }
        return moveRepository.save(pokeApiConsumerService.getMoveByName(name));
    }
}
