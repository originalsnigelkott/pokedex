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
    private PokemonConsumerService pokemonConsumerService;
    @Autowired
    private PokemonRepository pokemonRepository;
    @Autowired
    PokeApiResourceRepository pokemonResourceRepository;
    @Autowired
    private MongoTemplate mongoTemplate;

    public Pokemon create(Pokemon pokemon) {
        return save(pokemon);
    }

    public List<Pokemon> getPokemon(String name, Integer minWeight, Integer maxWeight) {
        if (name != null ) {
            var pokemon = pokemonRepository.findByName(name);
            var potentialPokemon = getAllPokemonNameMatches(name);
            if (pokemon.size() < potentialPokemon.size() || pokemon.isEmpty()) {
               getMissingPokemon(pokemon, potentialPokemon);
            }
        }
        if (name != null || minWeight != null || maxWeight != null) {
            return getPokemonByProperties(name, minWeight, maxWeight);
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
        return pokemonRepository.saveAll(pokemonConsumerService.getManyPokemonByName(pokemonToFetch));
    }

    private List<Pokemon> getPokemonByProperties(String name, Integer minWeight, Integer maxWeight) {
        var query = new Query();
        if(name != null && !name.isEmpty()) {
            var pattern = Pattern.compile(name, Pattern.CASE_INSENSITIVE);
            query.addCriteria(Criteria.where("name").regex(pattern));
        }
        if(minWeight != null) {
            query.addCriteria(Criteria.where("weight").gt(minWeight));
        }
        if(maxWeight != null) {
            query.addCriteria(Criteria.where("weight").lt(maxWeight));
        }
        return mongoTemplate.find(query, Pokemon.class);
    }
}
