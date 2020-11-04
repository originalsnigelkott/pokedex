package pokedex.dtos.pokemon;

import java.util.List;

public class PokemonDto {
    private int id;
    private String name;
    private int height;
    private int weight;
    private List<PokemonDtoType> types;
    private List<PokemonDtoMove> moves;

    public PokemonDto() {
    }

    public PokemonDto(int id, String name, int height, int weight, List<PokemonDtoType> types, List<PokemonDtoMove> moves) {
        this.id = id;
        this.name = name;
        this.height = height;
        this.weight = weight;
        this.types = types;
        this.moves = moves;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public List<PokemonDtoType> getTypes() {
        return types;
    }

    public void setTypes(List<PokemonDtoType> types) {
        this.types = types;
    }

    public List<PokemonDtoMove> getMoves() {
        return moves;
    }

    public void setMoves(List<PokemonDtoMove> moves) {
        this.moves = moves;
    }
}
