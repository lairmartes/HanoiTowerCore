package com.martialdev.game.hanoitower.core.control.event;

public class GameStartEvent {

    private int _definedPinCapacity;

    public GameStartEvent(int pinCapacity) {
        this._definedPinCapacity = pinCapacity;
    }
}
