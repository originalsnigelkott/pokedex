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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
}
