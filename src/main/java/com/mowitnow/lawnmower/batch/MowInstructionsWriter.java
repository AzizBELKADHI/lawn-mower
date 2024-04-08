package com.mowitnow.lawnmower.batch;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import com.mowitnow.lawnmower.mower.MowerInstructions;

public class MowInstructionsWriter implements ItemWriter<MowerInstructions> {

    @Override
    public void write(Chunk<? extends MowerInstructions> items) {
        for (MowerInstructions item : items) {
            System.out.println(item.getPosition().getX() + " " + item.getPosition().getY() + " " + item.getDirection());
        }
    }

}
