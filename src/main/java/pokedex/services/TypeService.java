package pokedex.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pokedex.entities.Type;
import pokedex.exceptions.EntityNotFoundException;
import pokedex.repositories.TypeRepository;

import java.util.List;
import java.util.stream.Collectors;

import static pokedex.core.Constants.PAGE_SIZE;

@Service
public class TypeService {
    @Autowired
    private TypeRepository typeRepository;

    @Autowired
    private PokeApiConsumerService pokeApiConsumerService;

    public Type findById(String id) {
        return typeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("type", "id"));
    }

    public List<Type> find(String name, Integer page) {
        if (page == null) {
            page = 0;
        }
        if (name != null) {
            return List.of(findByName(name));
        }
        return typeRepository.findAll().stream()
                .skip(PAGE_SIZE * page)
                .limit(PAGE_SIZE)
                .collect(Collectors.toList());
    }

    private Type findByName(String name) {
        var type = typeRepository.findByName(name);
        if (type.isPresent()) {
            return type.get();
        }
        return typeRepository.save(pokeApiConsumerService.getTypeByName(name));
    }
}
