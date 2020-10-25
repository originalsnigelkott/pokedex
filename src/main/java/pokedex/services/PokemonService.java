package pokedex.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pokedex.entities.PokeApiResource;
import pokedex.entities.Pokemon;
import pokedex.repositories.PokeApiResourceRepository;
import pokedex.repositories.PokemonRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PokemonService {
    @Autowired
    private PokemonConsumerService pokemonConsumerService;
    @Autowired
    private PokemonRepository pokemonRepository;
    @Autowired
    PokeApiResourceRepository pokemonResourceRepository;

    public List<Pokemon> getPokemonByName(String name) {
        var pokemon = pokemonRepository.findByName(name);
        System.out.println();
        var potentialPokemon = getAllPokemonNameMatches(name);
        if (pokemon.size() < potentialPokemon.size() || pokemon.size() == 0) {
            pokemon.addAll(getMissingPokemon(pokemon, potentialPokemon));
        }
        return pokemon;
    }

    public Pokemon getPokemonById(String id) {
        return pokemonRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find a pokemon!"));
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
}
