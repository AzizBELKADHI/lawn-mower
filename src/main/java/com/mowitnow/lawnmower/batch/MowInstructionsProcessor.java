package com.mowitnow.lawnmower.batch;

import com.mowitnow.lawnmower.mower.Lawn;
import com.mowitnow.lawnmower.mower.Mower;
import com.mowitnow.lawnmower.mower.MowerInstructions;
import com.mowitnow.lawnmower.mower.Position;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

public class MowInstructionsProcessor implements ItemProcessor<MowerInstructions, MowerInstructions> {

    private final Lawn lawn;

    @Autowired
    public MowInstructionsProcessor(Lawn lawn) {
        this.lawn = lawn;
    }

    @Override
    public MowerInstructions process(MowerInstructions instructions) {

        if(instructions.getMovements() == null){
            return null;
        }else{
            Position position = new Position(instructions.getPosition().getX(), instructions.getPosition().getY());
            Mower mower = new Mower(position, instructions.getDirection(), lawn);
            mower.move(instructions.getMovements());
            Position positionExpected = new Position(mower.getPosition().getX(), mower.getPosition().getY());
            return new MowerInstructions(positionExpected, mower.getDirection(), null);
            }
        }
}
