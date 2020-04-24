package com.martialdev.game.hanoitower.core.control.event;

public class GameStartEvent {

    public final int capacity;

    public GameStartEvent(int capacity) {
        this.capacity = capacity;
    }
}
