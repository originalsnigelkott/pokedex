package pokedex.dtos;

import pokedex.entities.Ability;
import pokedex.entities.Move;

import java.util.List;

public class PokeApiResourceDto {
    private String name;

    public PokeApiResourceDto() {
    }

    public PokeApiResourceDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
