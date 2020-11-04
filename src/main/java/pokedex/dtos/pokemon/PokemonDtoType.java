package pokedex.dtos.pokemon;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class PokemonDtoType {
    @JsonProperty("type")
    private String name;

    public PokemonDtoType() {
    }

    public PokemonDtoType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("type")
    private void unpack(Map<String, Object> type) {
        this.name = (String)type.get("name");
    }
}
