package com.martialdev.game.hanoitower.core.control.event;

import com.martialdev.game.hanoitower.core.control.Disk;
import com.martialdev.game.hanoitower.core.control.Pin;
import static com.martialdev.game.hanoitower.core.control.HanoiTowerControl.PinPosition;

public class PinEvent {

    private Disk _diskMoved;
    private PinPosition _pinPosition;
    private Pin _targetPin;
    private int _currentMoves;

    public PinEvent(Disk diskMoved, PinPosition position, Pin targetPin, int currentMoves) {
        this._diskMoved = diskMoved;
        this._pinPosition = position;
        this._targetPin = targetPin;
        this._currentMoves = currentMoves;
    }

    public Disk getDiskMoved() {
        return this._diskMoved;
    }

    public PinPosition getPinPosition() {
        return this._pinPosition;
    }

    public Pin getTargetPin() {
        return this._targetPin;
    }

    public int getCurrentMoves() {
        return this._currentMoves;
    }

}
