package pokedex.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pokedex.entities.Ability;
import pokedex.exceptions.EntityNotFoundException;
import pokedex.repositories.AbilityRepository;

import java.util.List;

@Service
public class AbilityService {
    @Autowired
    private AbilityRepository abilityRepository;

    @Autowired
    private PokeApiConsumerService pokeApiConsumerService;

    public Ability findById(String id) {
        return abilityRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("ability", "id"));
    }

    public List<Ability> find(String name) {
        if(name != null) {
            return List.of(findByName(name));
        }
        return abilityRepository.findAll();
    }

    private Ability findByName(String name) {
        var ability = abilityRepository.findByName(name);
        if(ability.isPresent()) {
            return ability.get();
        }
        return abilityRepository.save(pokeApiConsumerService.getAbilityByName(name));
    }
}
