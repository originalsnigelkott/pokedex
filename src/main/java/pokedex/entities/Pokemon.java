package pokedex.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import pokedex.dtos.pokemon.PokemonDto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.stream.Collectors;

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
    private List<String> types;
    private List<String> moves;
    private List<String> abilities;

    public Pokemon() {
    }

    public Pokemon(int number, String name, int height, int weight, List<String> types, List<String> moves, List<String> abilities) {
        this.number = number;
        this.name = name;
        this.height = height;
        this.weight = weight;
        this.moves = moves;
        this.types = types;
        this.abilities = abilities;
    }

    public Pokemon(PokemonDto pokemonDto) {
        this.number = pokemonDto.getId();
        this.name = pokemonDto.getName();
        this.height = pokemonDto.getHeight();
        this.weight = pokemonDto.getWeight();
        this.types = pokemonDto.getTypes().stream()
                .map(t -> t.getName())
                .collect(Collectors.toList());
        this.moves = pokemonDto.getMoves().stream()
                .map(t -> t.getName())
                .collect(Collectors.toList());
        this.abilities = pokemonDto.getAbilities().stream()
                .map(a -> a.getName())
                .collect(Collectors.toList());
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

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public List<String> getMoves() {
        return moves;
    }

    public void setMoves(List<String> moves) {
        this.moves = moves;
    }

    public List<String> getAbilities() {
        return abilities;
    }

    public void setAbilities(List<String> abilities) {
        this.abilities = abilities;
    }
}
