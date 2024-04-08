package com.mowitnow.lawnmower.mower;

public class Mower {
    private Position position;
    private char direction;
    private final Lawn lawn;

    public Mower(Position position, char direction, Lawn lawn) {
        this.position = position;
        this.direction = direction;
        this.lawn = lawn;
    }

    public void move(String movements) {
        movements.chars().forEach(move -> {
            switch (move) {
                case 'D' -> turnRight();
                case 'G' -> turnLeft();
                case 'A' -> moveForward();
                default -> throw new IllegalArgumentException("Invalid movement: " + (char) move);
            }
        });
    }

    private void turnRight() {
        direction = switch (direction) {
            case 'N' -> 'E';
            case 'E' -> 'S';
            case 'S' -> 'W';
            case 'W' -> 'N';
            default -> throw new IllegalStateException("Invalid direction: " + direction);
        };
    }

    private void turnLeft() {
        direction = switch (direction) {
            case 'N' -> 'W';
            case 'W' -> 'S';
            case 'S' -> 'E';
            case 'E' -> 'N';
            default -> throw new IllegalStateException("Invalid direction: " + direction);
        };
    }

    private void moveForward() {
        int newX = position.getX();
        int newY = position.getY();
        switch (direction) {
            case 'N' -> newY++;
            case 'E' -> newX++;
            case 'S' -> newY--;
            case 'W' -> newX--;
        }
        if (lawn.isInside(newX, newY)) {
            position.setX(newX);
            position.setY(newY);
        }
    }

    public Position getPosition() {
        return position;
    }

    public char getDirection() {
        return direction;
    }
}
