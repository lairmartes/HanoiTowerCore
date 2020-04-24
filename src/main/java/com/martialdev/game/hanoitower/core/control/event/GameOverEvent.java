package com.martialdev.game.hanoitower.core.control.event;

public class GameOverEvent {

    private int _moveQuantity;
    private double _score;

    public GameOverEvent(int moves, double score) {
        this._moveQuantity = moves;
        this._score = score;
    }

    public double getScore() {
        return this._score;
    }
}
