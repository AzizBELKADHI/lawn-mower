package com.mowitnow.lawnmower.mower;

public class MowerInstructions {
    private Position position;
    private char direction;
    private String movements;

    public MowerInstructions(Position position) {
        this.position = position;
    }

    public MowerInstructions(Position position, char direction, String movements) {
        this.position = position;
        this.direction = direction;
        this.movements = movements;
    }

    public Position getPosition(){
        return position;
    }

    public char getDirection() {
        return direction;
    }

    public String getMovements() {
        return movements;
    }

}
