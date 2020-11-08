package pokedex.dtos.pokemon;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class PokemonDtoAbility {
    private String name;

    public PokemonDtoAbility() {
    }

    public PokemonDtoAbility(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("ability")
    private void unpack(Map<String, Object> ability) {
        this.name = (String)ability.get("name");
    }
}
