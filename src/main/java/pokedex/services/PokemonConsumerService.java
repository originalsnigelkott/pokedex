package pokedex.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import pokedex.dtos.PokemonDto;
import pokedex.entities.Pokemon;
import pokedex.repositories.PokeApiResourceRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class PokemonConsumerService {
    @Autowired
    private PokeApiResourceRepository pokeApiResourceRepository;

    private String baseUrl = "https://pokeapi.co/api/v2/pokemon/";
    private final RestTemplate restTemplate;

    public PokemonConsumerService(RestTemplateBuilder restTemplateBuilder) {
        restTemplate = restTemplateBuilder.build();
    }

    public List<Pokemon> getManyPokemonByName(List<String> names) {
        List<Pokemon> pokemon = new ArrayList<>();
        names.forEach(name -> {
            pokemon.add(getOnePokemonByName(name));
        });
        return pokemon;
    }

    private Pokemon getOnePokemonByName(String name) {
        var url = baseUrl + name;
        try {
            var pokemonDto = restTemplate.getForObject(url, PokemonDto.class);
            return new Pokemon(pokemonDto);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "PokeApi is did not respond.");
        }
    }
}
