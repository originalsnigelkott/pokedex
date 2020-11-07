package pokedex.entities;

import org.springframework.data.annotation.Id;
import pokedex.dtos.move.MoveDto;

import java.util.List;

public class Move {
    @Id
    private String id;
    private String name;
    private List<String> pokemon;

    public Move() {
    }

    public Move(String name, List<String> pokemon) {
        this.name = name;
        this.pokemon = pokemon;
    }

    public Move(MoveDto moveDto) {
        this.name = moveDto.getName();
        this.pokemon = moveDto.getPokemon();
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
