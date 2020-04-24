package com.martialdev.game.hanoitower.core.control.event;

import com.martialdev.game.hanoitower.core.control.Disk;
import com.martialdev.game.hanoitower.core.control.Pin;
import static com.martialdev.game.hanoitower.core.control.HanoiTowerControl.PinPosition;

public class PinEvent {

    public final Disk diskMoved;
    public final PinPosition pinPosition;
    public final Pin targetPin;
    public final int currentMoves;

    public PinEvent(Disk diskMoved, PinPosition position, Pin targetPin, int currentMoves) {
        this.diskMoved = diskMoved;
        this.pinPosition = position;
        this.targetPin = targetPin;
        this.currentMoves = currentMoves;
    }
}
