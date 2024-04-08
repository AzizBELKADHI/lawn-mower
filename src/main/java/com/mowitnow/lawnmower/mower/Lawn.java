package com.mowitnow.lawnmower.mower;

import org.springframework.stereotype.Component;

@Component
public class Lawn {
    private Position position;

    public Lawn() {
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public boolean isInside(int x, int y) {
        return x >= 0 && x <= position.getX() && y >= 0 && y <= position.getY();
    }
}
