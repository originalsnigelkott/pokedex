package pokedex.dtos.ability;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AbilityDto {
    private String name;
    private List<String> pokemon;


    public AbilityDto() {
    }

    public AbilityDto(String name, List<String> pokemon) {
        this.name = name;
        this.pokemon = pokemon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getPokemon() {
        return pokemon;
    }

    public void setPokemon(List<String> pokemon) {
        this.pokemon = pokemon;
    }


    @JsonProperty("pokemon")
    private void unpack(List<Map<String, Object>> pokemonMapList) {
        this.pokemon = pokemonMapList.stream()
                .map(p -> {
                    var pokemon = (Map<String, String>) p.get("pokemon");
                    return pokemon.get("name");
                })
                .collect(Collectors.toList());
    }
}
