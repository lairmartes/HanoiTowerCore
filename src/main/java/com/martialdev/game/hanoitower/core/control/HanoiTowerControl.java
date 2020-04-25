/*
 * Created 09/26/2005
 * Refactored 01/18/2018
 *
 */
package com.martialdev.game.hanoitower.core.control;

import com.martialdev.game.hanoitower.core.control.event.GameOverEvent;
import com.martialdev.game.hanoitower.core.control.event.GameStartEvent;
import com.martialdev.game.hanoitower.core.control.event.HanoiTowerListener;
import com.martialdev.game.hanoitower.core.control.event.PinEvent;
import com.martialdev.game.hanoitower.core.control.exception.InvalidMoveException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** Hanoi Tower Control manages a Hanoi Tower Game.
 *
 * @author alunos
 */
public class HanoiTowerControl {

    private int _movesDone;
    private static final int PINS_AVAILABLE = 3;
    private final Pin[] _gamePins;
    private Disk _currentDisk;
    private int _pinCapacity;
    private Disk[] _disksInTheGame;
    private double _score;
    private int _minimumMovesRequired;
    private final List<HanoiTowerListener> _hanoiTowerListener;

    /** Enumeration of pins indicating it's position.
     */
    public enum PinPosition {
        FIRST, SECOND, THIRD
    }

    /** Constructs a Hanoi Tower game manager with no capacity.
     *
     */
    public HanoiTowerControl() {

        this._pinCapacity = -1;

        _gamePins = new Pin[PINS_AVAILABLE];
        _currentDisk = Disk.DISK_ZERO;
        _disksInTheGame = new Disk[0];

        _hanoiTowerListener = new ArrayList<>();

    }

    /** Indicate how many disks a Hanoi Tower game will have initially.
     *
     * @param pinCapacity how many disks will be in stake during the game in the first moment.
     */
    public void startGame(int pinCapacity) {
        restartGame(pinCapacity);
    }

    /** Indicate how many disks will be moved during the Hanoi Tower game now.
     * <b>Info</b> -> Broadcasts a GameStartEvent!!!
     *
     * @param pinCapacity how many disks will be moved during the game now.
     */
    public void restartGame(int pinCapacity) {
        // set disk capacity of the pins
        this._pinCapacity = pinCapacity;

        //initiate pins
        for (int i = 0; i < PINS_AVAILABLE; i++) {
            this._gamePins[i] = new Pin(this._pinCapacity);
        }
        // no disks are selected, then set it to Disk size zero
        _currentDisk = new Disk(0);
        _movesDone = 0; // no moves done yet

        //including disks in the game based on pin capacity
        _disksInTheGame = new Disk[this._pinCapacity];
        // initialize disks with size from 1 to pin capacity
        for (int i = 0; i < this._pinCapacity; i++) {
            _disksInTheGame[i] = new Disk(i + 1);
        }

        // indicate that pins will be able to stack the given pin capacity
        for (int i = 0; i < PINS_AVAILABLE; i++) {
            _gamePins[i].reset(this._pinCapacity);
        }

        // include all disks in the first pin
        try {
            for (int i = this._pinCapacity - 1; i >= 0; i--)
                _gamePins[PinPosition.FIRST.ordinal()].add(_disksInTheGame[i]);
        } catch (InvalidMoveException e) {
            throw new RuntimeException("No exception were expected here.  Something goes wrong and requires immediate action.");
        }

        //start score and moves
        _movesDone = 0;
        _score = 0.0d;
        _minimumMovesRequired = (int) Math.pow(2, _pinCapacity) - 1;

        broadCastEvent(new GameStartEvent(this._pinCapacity));
    }

    /** Remove a disk from a given pin position.  Returns the removed disk.
     * <b>Info</b>: Broadcasts disk removed event.
     *
     * @param pinPosition FIRST, SECOND or THIRD.
     */
    private void selectFromPin(PinPosition pinPosition) throws InvalidMoveException {
        Pin pinSelected = _gamePins[pinPosition.ordinal()];
        _currentDisk = pinSelected.removeDisk();

        fireDiskRemoved(new PinEvent(this._currentDisk, pinPosition, pinSelected, this._movesDone));
    }

    /** Include the given disk in the pin located in the given pin position.
     * <b>Info</b>: Broadcasts disk added event and game over event (when game is over, of course).
     *
     * @param pinPosition FIRST, SECOND or THIRD.
     */
    private void moveSelectedToPin(PinPosition pinPosition) throws InvalidMoveException {
        Pin pinSelected = _gamePins[pinPosition.ordinal()];
        pinSelected.add(_currentDisk);
        _movesDone++;

        // calculating rating
        if (_movesDone >= _minimumMovesRequired) {
            _score = (double) _minimumMovesRequired / (double) _movesDone;
        }

        fireDiskAdded(new PinEvent(_currentDisk, pinPosition, pinSelected, this._movesDone));

        if (isGameOver()) {
            broadCastEvent(new GameOverEvent(this._movesDone, this._score));
        }
    }

    public void move(final PinPosition from, final PinPosition to) throws InvalidMoveException {
        selectFromPin(from);
        moveSelectedToPin(to);
    }

    // test if the game is over
    private boolean isGameOver() {
        if (Arrays.stream(_gamePins[PinPosition.FIRST.ordinal()].getDisks())
                .anyMatch(disk -> !Disk.DISK_ZERO.equals(disk))) {
            return false; 
        }

        return Arrays.stream(_gamePins[PinPosition.THIRD.ordinal()].getDisks())
                .noneMatch(Disk.DISK_ZERO::equals);
    }

    /** Include an event listener that will receive Hanoi Tower game notifications.
     *
     * @param listener the object that will be called when an event is risen.
     */
    public void addListener(HanoiTowerListener listener) {
        this._hanoiTowerListener.add(listener);
    }

    // event broadcaster
    private void fireDiskAdded(PinEvent event) {
        _hanoiTowerListener.forEach(listener -> listener.fireDiskAdded(event));
    }

    private void broadCastEvent(GameOverEvent event) {
        _hanoiTowerListener.forEach(listener -> listener.hanoiTowerEvent(event));
    }

    private void fireDiskRemoved(PinEvent event) {
        _hanoiTowerListener.forEach(listener -> listener.fireDiskRemoved(event));
    }

    private void broadCastEvent(GameStartEvent event) {
        _hanoiTowerListener.forEach(listener -> listener.hanoiTowerEvent(event));
    }
}