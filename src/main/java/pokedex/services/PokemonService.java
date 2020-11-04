package pokedex.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import pokedex.entities.PokeApiResource;
import pokedex.entities.Pokemon;
import pokedex.exceptions.EntityNotFoundException;
import pokedex.repositories.PokeApiResourceRepository;
import pokedex.repositories.PokemonRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class PokemonService {
    @Autowired
    private PokeApiConsumerService pokeApiConsumerService;
    @Autowired
    private PokemonRepository pokemonRepository;
    @Autowired
    PokeApiResourceRepository pokemonResourceRepository;
    @Autowired
    private MongoTemplate mongoTemplate;

    public Pokemon create(Pokemon pokemon) {
        return save(pokemon);
    }

    public List<Pokemon> getPokemon(String name, Integer minWeight, Integer maxWeight, Integer minHeight, Integer maxHeight) {
        if (name != null) {
            var pokemon = pokemonRepository.findByName(name);
            var potentialPokemon = getAllPokemonNameMatches(name);
            if (pokemon.size() < potentialPokemon.size() || pokemon.isEmpty()) {
                getMissingPokemon(pokemon, potentialPokemon);
            }
        }
        if (name != null || minWeight != null || maxWeight != null || minHeight != null || maxHeight != null) {
            return getPokemonByProperties(name, minWeight, maxWeight, minHeight, maxHeight);
        }
        return pokemonRepository.findAllByOrderByNumberAsc();
    }

    public Pokemon getPokemonById(String id) {
        return pokemonRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("pokemon", "id"));
    }

    public void update(String id, Pokemon pokemon) {
        checkExistenceById(id);
        pokemon.setId(id);
        save(pokemon);
    }

    public void delete(String id) {
        checkExistenceById(id);
        pokemonRepository.deleteById(id);
    }

    private Pokemon save(Pokemon pokemon) {
        return pokemonRepository.save(pokemon);
    }

    private void checkExistenceById(String id) {
        if (!pokemonRepository.existsById(id)) {
            throw new EntityNotFoundException("pokemon", "id");
        }
    }

    private List<PokeApiResource> getAllPokemonNameMatches(String name) {
        var resources = pokemonResourceRepository.findByName(name);
        return resources;
    }

    private List<Pokemon> getMissingPokemon(List<Pokemon> pokemon, List<PokeApiResource> potentialPokemon) {
        List<String> pokemonToFetch = new ArrayList<>();

        List<String> potentialNames = potentialPokemon.stream()
                .map(p -> p.getName())
                .collect(Collectors.toList());
        List<String> databaseNames = pokemon.stream()
                .map(p -> p.getName())
                .collect(Collectors.toList());

        potentialNames.forEach(name -> {
            if (!databaseNames.contains(name)) {
                pokemonToFetch.add(name);
            }
        });
        return pokemonRepository.saveAll(pokeApiConsumerService.getManyPokemonByName(pokemonToFetch));
    }

    private List<Pokemon> getPokemonByProperties(String name, Integer minWeight, Integer maxWeight, Integer minHeight, Integer maxHeight) {
        var query = new Query();
        if (name != null && !name.isEmpty()) {
            var pattern = Pattern.compile(name, Pattern.CASE_INSENSITIVE);
            query.addCriteria(Criteria.where("name").regex(pattern));
        }
        if (minWeight != null || maxWeight != null) {
            query.addCriteria(getSizeComparisonCriteria("weight", minWeight, maxWeight));
        }
        if (minHeight != null || maxHeight != null) {
           query.addCriteria(getSizeComparisonCriteria("height", minHeight, maxHeight));
        }
        return mongoTemplate.find(query, Pokemon.class);
    }

    private Criteria getSizeComparisonCriteria(String fieldName, Integer minValue, Integer maxValue) {
        if(minValue != null && maxValue != null) {
            return Criteria.where(fieldName).gte(minValue).lte(maxValue);
        } else if (minValue != null) {
            return Criteria.where(fieldName).gte(minValue);
        } else {
            return Criteria.where(fieldName).lte(maxValue);
        }
    }
}
