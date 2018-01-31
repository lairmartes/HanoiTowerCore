package com.gmail.lairmartes.game.hanoitower.event;

public class GameStartEvent {

    private int _definedPinCapacity;

    public GameStartEvent(int pinCapacity) {
        this._definedPinCapacity = pinCapacity;

    }

    public int getPinCapacityDefined() {
        return this._definedPinCapacity;
    }

}
