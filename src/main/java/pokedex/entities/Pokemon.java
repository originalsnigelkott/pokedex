package pokedex.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import pokedex.dtos.PokemonDto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

public class Pokemon {
    @Id
    private String id;
    @Indexed(unique = true)
    @Min(1)
    private int number;
    @NotBlank(message = "Name must be present.")
    private String name;
    @Min(1)
    private int height;
    @Min(1)
    private int weight;

    public Pokemon() {
    }

    public Pokemon(int number, String name, int height, int weight) {
        this.number = number;
        this.name = name;
        this.height = height;
        this.weight = weight;
    }

    public Pokemon(PokemonDto pokemonDto) {
        this.number = pokemonDto.getId();
        this.name = pokemonDto.getName();
        this.height = pokemonDto.getHeight();
        this.weight = pokemonDto.getWeight();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
