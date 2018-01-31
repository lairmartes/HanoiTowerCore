/*
 * Created 09/26/2005
 * Refactored 01/18/2018
 *
 */
package com.gmail.lairmartes.game.hanoitower;

import com.gmail.lairmartes.game.hanoitower.event.GameOverEvent;
import com.gmail.lairmartes.game.hanoitower.event.GameStartEvent;
import com.gmail.lairmartes.game.hanoitower.event.HanoiTowerListener;
import com.gmail.lairmartes.game.hanoitower.event.PinEvent;
import com.gmail.lairmartes.game.hanoitower.exception.InvalidMoveException;

import java.util.ArrayList;
import java.util.List;

/** Hanoi Tower Control manages a Hanoi Tower Game.
 *
 * @author alunos
 */
public class HanoiTowerControl {

    private int _movesDone;
    private static final int PINS_AVAILABLE = 3;
    private Pin[] _gamePins;
    private Disk _currentDisk;
    private int _pinCapacity;
    private Disk[] _disksInTheGame;
    private double _score;
    private int _minimumMovesRequired;
    private List<HanoiTowerListener> _hanoiTowerListener;

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
     * @return the disk removed.
     * @throws InvalidMoveException
     */
    public Disk selectFromPin(PinPosition pinPosition) throws InvalidMoveException {
        Pin pinSelected = _gamePins[pinPosition.ordinal()];
        _currentDisk = pinSelected.removeDisk();

        fireDiskRemoved(new PinEvent(this._currentDisk, pinPosition, pinSelected, this._movesDone));

        return _currentDisk;
    }

    /** Include the given disk in the pin located in the given pin position.
     * <b>Info</b>: Broadcasts disk added event and game over event (when game is over, of course).
     *
     * @param pinPosition FIRST, SECOND or THIRD.
     * @throws InvalidMoveException
     */
    public void moveSelectedToPin(PinPosition pinPosition) throws InvalidMoveException {
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

    /** Returns the capacity of the game.  Created for testing purposes.  Do not use, except for tests!!!
     *
     * @param aPin pin position.
     * @return how many disks are available in the game.
     */
    protected int currentStackSize(PinPosition aPin) {
        return _gamePins[aPin.ordinal()].getStackSize();
    }

    // test if the game is over
    private boolean isGameOver() {

        for (Disk d : _gamePins[PinPosition.FIRST.ordinal()].getDisks()) {
            if (!Disk.DISK_ZERO.equals(d)) {
                return false;
            }
        }

        for (Disk d : _gamePins[PinPosition.THIRD.ordinal()].getDisks()) {
            if (Disk.DISK_ZERO.equals(d)) return false;
        }

        return true;
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

        for (HanoiTowerListener listener : _hanoiTowerListener) {
            listener.fireDiskAdded(event);
        }
    }

    private void broadCastEvent(GameOverEvent event) {

        for (HanoiTowerListener listener : _hanoiTowerListener) {
            listener.hanoiTowerEvent(event);
        }
    }

    private void fireDiskRemoved(PinEvent event) {

        for (HanoiTowerListener listener : _hanoiTowerListener) {
            listener.fireDiskRemoved(event);
        }
    }

    private void broadCastEvent(GameStartEvent event) {

        for (HanoiTowerListener listener : _hanoiTowerListener) {
            listener.hanoiTowerEvent(event);
        }
    }
}