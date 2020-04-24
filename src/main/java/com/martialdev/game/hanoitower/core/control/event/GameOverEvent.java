package com.martialdev.game.hanoitower.core.control.event;

public class GameOverEvent {

    public final double score;
    public final int totalMoves;

    public GameOverEvent(int totalMoves, double score) {
        this.totalMoves = totalMoves;
        this.score = score;
    }
}
