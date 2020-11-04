package pokedex.entities;

import org.springframework.data.annotation.Id;
import pokedex.dtos.type.TypeDto;

import java.util.List;

public class Type {
    @Id
    private String id;
    private String name;
    private List<String> pokemon;

    public Type() {
    }

    public Type(String name, List<String> pokemon) {
        this.name = name;
        this.pokemon = pokemon;
    }

    public Type(TypeDto typeDto) {
        this.name = typeDto.getName();
        this.pokemon = typeDto.getPokemon();
    }
}
