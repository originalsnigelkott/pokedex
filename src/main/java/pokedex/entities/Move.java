package pokedex.entities;

import org.springframework.data.annotation.Id;
import pokedex.dtos.move.MoveDto;

public class Move {
    @Id
    private String id;
    private String name;
    private int power;

    public Move() {
    }

    public Move(String name, int power) {
        this.name = name;
        this.power = power;
    }

    public Move(MoveDto moveDto) {
        this.name = moveDto.getName();
        this.power = moveDto.getPower();
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

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }
}
