package pokedex.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import pokedex.dtos.ability.AbilityDto;
import pokedex.dtos.move.MoveDto;
import pokedex.dtos.pokemon.PokemonDto;
import pokedex.dtos.type.TypeDto;
import pokedex.entities.Ability;
import pokedex.entities.Move;
import pokedex.entities.Pokemon;
import pokedex.entities.Type;
import pokedex.exceptions.EntityNotFoundException;
import pokedex.exceptions.PokeApiException;
import pokedex.repositories.PokemonRepository;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

@Service
public class PokeApiConsumerService {
    private final String typeEntity = "type";
    private final String abilityEntity = "ability";
    private final String moveEntity = "move";

    @Autowired
    private PokemonRepository pokemonRepository;

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
            return pokemonRepository.save(new Pokemon(pokemonDto));
        } catch (Exception e) {
            if (e.getCause() instanceof ConnectException) {
                throw new PokeApiException();
            }
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error.");
        }

    }

    public Type getTypeByName(String name) {
        return (Type) getEntityByName(name, typeEntity, TypeDto.class);
    }

    public Ability getAbilityByName(String name) {
        return (Ability) getEntityByName(name, abilityEntity, AbilityDto.class);
    }

    public Move getMoveByName(String name) {
        return (Move) getEntityByName(name, moveEntity, MoveDto.class);
    }

    private Object getEntityByName(String name, String entity, Class dtoClass) {
        var url = baseUrl + entity + "/" + name;
        try {
            var dto = restTemplate.getForObject(url, dtoClass);
            return getNewEntity(entity, dto);
        } catch (HttpServerErrorException e) {
            throw new PokeApiException();
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new EntityNotFoundException(entity, "name");
            }
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error.");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error.");
        }

    }

    private Object getNewEntity(String entity, Object dto) {
        switch (entity) {
            case typeEntity: {
                return new Type((TypeDto) dto);
            }
            case abilityEntity: {
                return new Ability((AbilityDto) dto);
            }
            case moveEntity: {
                return new Move((MoveDto) dto);
            }
            default: {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error.");
            }
        }
    }
}
