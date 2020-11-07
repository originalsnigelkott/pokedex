package pokedex.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pokedex.entities.Type;
import pokedex.exceptions.EntityNotFoundException;
import pokedex.repositories.TypeRepository;

import java.util.List;

@Service
public class TypeService {
    @Autowired
    private TypeRepository typeRepository;

    @Autowired
    private PokeApiConsumerService pokeApiConsumerService;

    public Type findById(String id) {
        return typeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("type", "id"));
    }

    public List<Type> find(String name) {
        if(name != null) {
            return List.of(findByName(name));
        }
        return typeRepository.findAll();
    }

    private Type findByName(String name) {
        var type = typeRepository.findByName(name);
        if(type.isPresent()) {
            return type.get();
        }
        return pokeApiConsumerService.getTypeByName(name);
    }
}
