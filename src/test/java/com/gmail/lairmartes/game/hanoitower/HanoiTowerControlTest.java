package com.gmail.lairmartes.game.hanoitower;

import com.gmail.lairmartes.game.hanoitower.event.GameOverEvent;
import com.gmail.lairmartes.game.hanoitower.event.GameStartEvent;
import com.gmail.lairmartes.game.hanoitower.event.HanoiTowerListener;
import com.gmail.lairmartes.game.hanoitower.event.PinEvent;
import com.gmail.lairmartes.game.hanoitower.exception.InvalidMoveException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.function.Executable;

import static com.gmail.lairmartes.game.hanoitower.HanoiTowerControl.PinPosition.FIRST;
import static com.gmail.lairmartes.game.hanoitower.HanoiTowerControl.PinPosition.SECOND;
import static com.gmail.lairmartes.game.hanoitower.HanoiTowerControl.PinPosition.THIRD;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertThrows;

class HanoiTowerControlTest {

    private HanoiTowerControl _matchTest;
    private int _disksAdded;
    private int _disksRemoved;
    private boolean _gameOverEventDetected;
    private boolean _gameStartEventDetected;
    private double _finalGameScore;

    private final HanoiTowerListener _listener = new HanoiTowerListener() {

        @Override
        public void fireDiskAdded(PinEvent event) {
            _disksAdded++;
        }

        @Override
        public void hanoiTowerEvent(GameOverEvent event) {
            _gameOverEventDetected = true;

            _finalGameScore = event.getScore();
        }

        @Override
        public void fireDiskRemoved(PinEvent event) {
            _disksRemoved++;
        }

        @Override
        public void hanoiTowerEvent(GameStartEvent event) {
            _gameStartEventDetected = true;
        }
    };

    @BeforeEach
    public void setupTest() {
        _disksAdded = 0;
        _disksRemoved = 0;
        _gameOverEventDetected = false;
        _gameStartEventDetected = false;
        _matchTest = new HanoiTowerControl();
        _matchTest.addListener(_listener);
    }

    @Test
    @DisplayName("An 'game started' event must be broadcast when game start")
    public void checkStartGameEventBroadcast() {
        _matchTest.startGame(3);
        assertTrue(_gameStartEventDetected, "A game start event should be broadcast.");
    }

    @Test
    @DisplayName("Play with no errors and minimum movements - must have a flawless victory")
    public void verifyFlawlessVictory() {
        try {
            playPerfectGameWithThreeDisks();
            assertEquals(1d, this._finalGameScore, "This should be a flawless victory.");
        } catch (InvalidMoveException e) {
            fail("Unexpected error: " + e);
        }
    }

    @Test
    @DisplayName("Play with more moves than necessary - not a flawless victory")
    public void verifyFlawVictory() {
        try {
            playNotPerfectGameWithThreeDisks();
            assertNotEquals(1d, _finalGameScore, "This is NOT a flawless victory.");
        } catch (InvalidMoveException e) {
            fail("Unexpected error: " + e);
        }
    }

    @Test
    @DisplayName("Flawless Victory - number of disks removed and added must be the same")
    public void verifyIfRemovedAndAddedDisksAreEquals() {
        try {
            playPerfectGameWithThreeDisks();
            assertEquals(_disksRemoved, _disksAdded);
        } catch (InvalidMoveException e) {
            fail("Unexpected error" + e);
        }
    }

    @Test
    @DisplayName("Check if control let client know when the game ends")
    public void checkGameOverNotification() {
        try {
            playPerfectGameWithThreeDisks();
            assertTrue(_gameOverEventDetected);
        } catch (InvalidMoveException e) {
            fail("Unexpected error" + e);
        }
    }

    @Test
    @DisplayName("Check if controller detects an invalid move")
    void makeInvalidMoveAndCheckIfItIsInvalid() {

        HanoiTowerControl _matchTest = new HanoiTowerControl();
        _matchTest.startGame(5);

        try {
            _matchTest.selectFromPin(FIRST);
            _matchTest.moveSelectedToPin(SECOND);

            _matchTest.selectFromPin(FIRST);
            Executable executeInvalidDiskMove = () -> _matchTest.moveSelectedToPin(SECOND);

            assertThrows(InvalidMoveException.class, executeInvalidDiskMove,
                    "An exception should be thrown when trying to put a greater disk above a lesser.");
        } catch (InvalidMoveException e) {
            fail("A invalid move has been detected incorrectly: " + e.getMessage());
        }
    }

    private void playPerfectGameWithThreeDisks() throws InvalidMoveException {
        _matchTest.startGame(3);
        move(FIRST, THIRD);
        move(FIRST, SECOND);
        move(THIRD, SECOND);
        move(FIRST, THIRD);
        move(SECOND, FIRST);
        move(SECOND, THIRD);
        move(FIRST, THIRD);
    }

    private void playNotPerfectGameWithThreeDisks() throws InvalidMoveException {
        _matchTest.startGame(3);
        move(FIRST, SECOND);
        move(FIRST, THIRD);
        move(SECOND, THIRD);
        move(FIRST, SECOND);
        move(THIRD, SECOND);
        move(THIRD, FIRST);
        move(SECOND, FIRST);
        move(SECOND, THIRD);
        move(FIRST, SECOND);
        move(FIRST, THIRD);
        move(SECOND, THIRD);
    }

    private void move(HanoiTowerControl.PinPosition from, HanoiTowerControl.PinPosition to) throws InvalidMoveException {
        _matchTest.selectFromPin(from);
        _matchTest.moveSelectedToPin(to);
    }
}


