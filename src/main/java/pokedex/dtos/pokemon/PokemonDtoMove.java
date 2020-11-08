package pokedex.dtos.pokemon;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class PokemonDtoMove {
    private String name;

    public PokemonDtoMove() {
    }

    public PokemonDtoMove(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("move")
    private void unpack(Map<String, Object> move) {
        this.name = (String)move.get("name");
    }
}
