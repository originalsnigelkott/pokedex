package pokedex.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import pokedex.dtos.pokemon.PokemonDto;
import pokedex.dtos.type.TypeDto;
import pokedex.entities.Pokemon;
import pokedex.entities.Type;
import pokedex.exceptions.EntityNotFoundException;
import pokedex.exceptions.PokeApiException;
import pokedex.repositories.PokeApiResourceRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class PokeApiConsumerService {
    @Autowired
    private PokeApiResourceRepository pokeApiResourceRepository;

    private String baseUrl = "https://pokeapi.co/api/v2/";
    private final RestTemplate restTemplate;

    public PokeApiConsumerService(RestTemplateBuilder restTemplateBuilder) {
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
        var url = baseUrl + "pokemon/" + name;
        try {
            var pokemonDto = restTemplate.getForObject(url, PokemonDto.class);
            return new Pokemon(pokemonDto);
        } catch (HttpServerErrorException e) {
            throw new PokeApiException();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error.");
        }
    }

    public Type getTypeByName(String name) {
        var url = baseUrl + "type/" + name;
        try {
            var typeDto = restTemplate.getForObject(url, TypeDto.class);
            return new Type(typeDto);
        } catch (HttpServerErrorException e) {
            throw new PokeApiException();
        } catch (HttpClientErrorException e) {
            if(e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new EntityNotFoundException("type", "name");
            }
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error.");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error.");
        }
    }
}
