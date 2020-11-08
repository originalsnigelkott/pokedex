package pokedex.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pokedex.entities.PokeApiResource;
import pokedex.entities.Pokemon;
import pokedex.exceptions.EntityNotFoundException;
import pokedex.repositories.PokeApiResourceRepository;
import pokedex.repositories.PokemonRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static pokedex.core.Constants.PAGE_SIZE;

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

    public List<Pokemon> getPokemon(String name, Integer minWeight, Integer maxWeight, Integer minHeight, Integer maxHeight, Integer page) {
        if (page == null) {
            page = 0;
        }
        if (name != null) {
            if (name.trim().length() < 3) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name must be at least 3 characters long.");
            }
            checkForMissingPokemon(name);
        }
        if (name != null || minWeight != null || maxWeight != null || minHeight != null || maxHeight != null) {
            return getPokemonByProperties(name, minWeight, maxWeight, minHeight, maxHeight, page);
        }
        return pokemonRepository.findAllByOrderByNumberAsc().stream().skip(page * PAGE_SIZE).limit(PAGE_SIZE).collect(Collectors.toList());
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

    private void checkForMissingPokemon(String name) {
        var pokemon = pokemonRepository.findByName(name);
        var potentialPokemon = getAllPokemonNameMatches(name);
        if (pokemon.size() < potentialPokemon.size() || pokemon.isEmpty()) {
            getMissingPokemon(pokemon, potentialPokemon);
        }
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
        return pokeApiConsumerService.getManyPokemonByName(pokemonToFetch);
    }

    private List<Pokemon> getPokemonByProperties(String name, Integer minWeight, Integer maxWeight, Integer minHeight, Integer maxHeight, Integer page) {
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
        query.limit(PAGE_SIZE).skip(page * PAGE_SIZE);
        return mongoTemplate.find(query, Pokemon.class);
    }

    private Criteria getSizeComparisonCriteria(String fieldName, Integer minValue, Integer maxValue) {
        if (minValue != null && maxValue != null) {
            return Criteria.where(fieldName).gte(minValue).lte(maxValue);
        } else if (minValue != null) {
            return Criteria.where(fieldName).gte(minValue);
        } else {
            return Criteria.where(fieldName).lte(maxValue);
        }
    }
}
