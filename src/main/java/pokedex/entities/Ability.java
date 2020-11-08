package pokedex.entities;

import org.springframework.data.annotation.Id;
import pokedex.dtos.ability.AbilityDto;

import java.util.List;

public class Ability {
    @Id
    private String id;
    private String name;
    private List<String> pokemon;

    public Ability() {
    }

    public Ability(String name, List<String> pokemon) {
        this.name = name;
        this.pokemon = pokemon;
    }

    public Ability(AbilityDto abilityDto) {
        this.name = abilityDto.getName();
        this.pokemon = abilityDto.getPokemon();
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
