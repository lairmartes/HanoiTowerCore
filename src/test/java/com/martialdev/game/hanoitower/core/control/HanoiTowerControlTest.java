package com.martialdev.game.hanoitower.core.control;

import com.martialdev.game.hanoitower.core.control.event.GameOverEvent;
import com.martialdev.game.hanoitower.core.control.event.GameStartEvent;
import com.martialdev.game.hanoitower.core.control.event.HanoiTowerListener;
import com.martialdev.game.hanoitower.core.control.event.PinEvent;
import com.martialdev.game.hanoitower.core.control.exception.InvalidMoveException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.martialdev.game.hanoitower.core.control.HanoiTowerControl.PinPosition;
import static com.martialdev.game.hanoitower.core.control.HanoiTowerControl.PinPosition.FIRST_PIN;
import static com.martialdev.game.hanoitower.core.control.HanoiTowerControl.PinPosition.SECOND_PIN;
import static com.martialdev.game.hanoitower.core.control.HanoiTowerControl.PinPosition.THIRD_PIN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class HanoiTowerControlTest {

    private HanoiTowerControl _matchTest;
    private GameOverEvent _gameOverEvent;
    private GameStartEvent _gameStartEvent;
    private PinEvent _pinEventAdded;
    private PinEvent _pinEventRemoved;

    private final HanoiTowerListener _listener = new HanoiTowerListener() {

        @Override
        public void fireDiskAdded(PinEvent event) {
            _pinEventAdded = event;
        }

        @Override
        public void hanoiTowerEvent(GameOverEvent event) {
            _gameOverEvent = event;
        }

        @Override
        public void fireDiskRemoved(PinEvent event) {
            _pinEventRemoved = event;
        }

        @Override
        public void hanoiTowerEvent(GameStartEvent event) {
            _gameStartEvent = event;
        }
    };

    @BeforeEach
    public void setupTest() {
        _matchTest = new HanoiTowerControl();
        _matchTest.addListener(_listener);
        _gameOverEvent = new GameOverEvent(0, 0);
        _gameStartEvent = new GameStartEvent(0);
    }

    @Test
    @DisplayName("An 'game started' event must be broadcast when game start")
    public void checkStartGameEventBroadcast() {
        _matchTest.startGame(5);
        assertEquals(5, _gameStartEvent.capacity);
    }

    @Test
    @DisplayName("Play with no errors and minimum movements - must have a flawless victory")
    public void verifyFlawlessVictory() {
        try {
            playPerfectGameWithThreeDisks();
            assertEquals(1d, _gameOverEvent.score, "This should be a flawless victory.");
        } catch (InvalidMoveException e) {
            fail("Unexpected error: " + e);
        }
    }

    @Test
    @DisplayName("Play with more moves than necessary - not a flawless victory")
    public void verifyFlawVictory() {
        try {
            playNotPerfectGameWithThreeDisks();
            assertNotEquals(1d, _gameOverEvent.score, "This is NOT a flawless victory.");
        } catch (InvalidMoveException e) {
            fail("Unexpected error: " + e);
        }
    }

    @Test
    @DisplayName("Check movement counting")
    public void verifyHowManyMovementHaveBeenDone() {
        try {
            playNotPerfectGameWithThreeDisks();
            assertEquals(11, _gameOverEvent.totalMoves);
        } catch (InvalidMoveException e) {
            fail("Unexpected error: " + e);
        }
    }

    @Test
    @DisplayName("Check if it notifies disk moves correctly")
    public void verifyIfRemovedAndAddedDisksAreEquals() {
        try {
            _matchTest.startGame(3);
            _matchTest.grabDisk(FIRST_PIN);
            _matchTest.dropDisk(SECOND_PIN);
            final PinEvent removedExpected = new PinEvent(new Disk(1), FIRST_PIN, new Pin(3), 0);
            final PinEvent addedExpected = new PinEvent(new Disk(1), SECOND_PIN, new Pin(3), 1);
            assertTrue(comparePinEvents(_pinEventRemoved, removedExpected));
            assertTrue(comparePinEvents(_pinEventAdded, addedExpected));
        } catch (InvalidMoveException e) {
            fail("Unexpected error" + e);
        }
    }

    @Test
    @DisplayName("Check if control let client know when the game ends")
    public void checkGameOverNotification() {
        try {
            playPerfectGameWithThreeDisks();
            assertEquals(1d, _gameOverEvent.score);
        } catch (InvalidMoveException e) {
            fail("Unexpected error" + e);
        }
    }

    @Test
    @DisplayName("Check if controller detects an invalid move")
    void makeInvalidMoveAndCheckIfItIsInvalid() {

        final HanoiTowerControl _matchTest = new HanoiTowerControl();
        _matchTest.startGame(5);

        try {
            _matchTest.grabDisk(FIRST_PIN);
            _matchTest.dropDisk(SECOND_PIN);

            _matchTest.grabDisk(FIRST_PIN);
            assertThrows(InvalidMoveException.class, () -> _matchTest.dropDisk(SECOND_PIN),
                    "An exception should be thrown when trying to put a greater disk above a lesser.");
        } catch (InvalidMoveException e) {
            fail("A invalid move has been detected incorrectly: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Receive Game Start event when game is restarted")
    public void checkIfRestartGameLaunchesNewGameEvent() {
        _matchTest.startGame(3);
        _matchTest.restartGame(5);
        assertEquals(5, _gameStartEvent.capacity);
    }

    @Test
    @DisplayName("Launch event when disk is grabbed")
    public void checkIfEventIsLaunchedWhenDiskIsGrabbed() {
        _matchTest.startGame(3);
        final PinEvent removedExpected = new PinEvent(new Disk(1), FIRST_PIN, new Pin(3), 0);
        try {
            _matchTest.grabDisk(FIRST_PIN);
            assertTrue(comparePinEvents(removedExpected, _pinEventRemoved));
        } catch (InvalidMoveException e) {
            fail("Unexpected exception: " + e);
        }
    }

    @Test
    @DisplayName("Launch event when disk is dropped")
    public void checkIfEventIsLaunchedWhenDiskIsDropped() {
        _matchTest.startGame(3);
        final PinEvent addedExpected = new PinEvent(new Disk(1), SECOND_PIN, new Pin(3), 1);
        try {
            _matchTest.grabDisk(FIRST_PIN);
            _matchTest.dropDisk(SECOND_PIN);
            assertTrue(comparePinEvents(addedExpected, _pinEventAdded));
        } catch (InvalidMoveException e) {
            fail("Unexpected exception: " + e);
        }
    }

    @Test
    @DisplayName("Do not allow to grab a disk after end game")
    public void checkIfDoesNotAllowGrabDiskAfterGameEnds() {
        try {
            playPerfectGameWithThreeDisks();
            assertThrows(InvalidMoveException.class, () -> _matchTest.grabDisk(THIRD_PIN));
        } catch (InvalidMoveException e) {
            fail("Unexpected exception: " + e);
        }
    }

    @Test
    @DisplayName("Do not allow to drop a disk after end game")
    public void checkIfDoesNotAllowDropDiskAfterGameEnds() {
        try {
            playPerfectGameWithThreeDisks();
            assertThrows(InvalidMoveException.class, () -> _matchTest.dropDisk(SECOND_PIN));
        } catch (InvalidMoveException e) {
            fail("Unexpected exception: " + e);
        }
    }

    @Test
    @DisplayName("Do not allow drop disk when disk is not grabbed")
    public void checkIfDoesNotAllowDropDiskWhenNotGrabbed() {
        _matchTest.startGame(3);
        try {
            _matchTest.grabDisk(FIRST_PIN);
            _matchTest.dropDisk(SECOND_PIN);
            assertThrows(InvalidMoveException.class, () -> _matchTest.dropDisk(THIRD_PIN));
        } catch (InvalidMoveException e) {
            fail("Unexpected exception: " + e);
        }
    }

    @Test
    @DisplayName("Do not allow grab a disk when a disk has grabbed already")
    public void checkIfDoesNotAllowDropDiskWhenAnotherIsGrabbed() {
        try {
            _matchTest.startGame(3);
            _matchTest.grabDisk(FIRST_PIN);
            assertThrows(InvalidMoveException.class, () ->_matchTest.grabDisk(FIRST_PIN));
        } catch (InvalidMoveException e) {
            fail("Unexpected exception: " + e);
        }
    }

    private void playPerfectGameWithThreeDisks() throws InvalidMoveException {
        _matchTest.startGame(3);
        move(FIRST_PIN, THIRD_PIN);
        move(FIRST_PIN, SECOND_PIN);
        move(THIRD_PIN, SECOND_PIN);
        move(FIRST_PIN, THIRD_PIN);
        move(SECOND_PIN, FIRST_PIN);
        move(SECOND_PIN, THIRD_PIN);
        move(FIRST_PIN, THIRD_PIN);
    }

    private void playNotPerfectGameWithThreeDisks() throws InvalidMoveException {
        _matchTest.startGame(3);
        move(FIRST_PIN, SECOND_PIN);
        move(FIRST_PIN, THIRD_PIN);
        move(SECOND_PIN, THIRD_PIN);
        move(FIRST_PIN, SECOND_PIN);
        move(THIRD_PIN, SECOND_PIN);
        move(THIRD_PIN, FIRST_PIN);
        move(SECOND_PIN, FIRST_PIN);
        move(SECOND_PIN, THIRD_PIN);
        move(FIRST_PIN, SECOND_PIN);
        move(FIRST_PIN, THIRD_PIN);
        move(SECOND_PIN, THIRD_PIN);
    }

    private void move(PinPosition pin1, PinPosition pin2) throws InvalidMoveException{
        _matchTest.grabDisk(pin1);
        _matchTest.dropDisk(pin2);
    }

    private boolean comparePinEvents(PinEvent pinEvent1, PinEvent pinEvent2) {
        return (
                pinEvent1.diskMoved.equals(pinEvent2.diskMoved) &&
                pinEvent1.pinPosition.equals(pinEvent2.pinPosition) &&
                pinEvent1.currentMoves == pinEvent2.currentMoves
        );
    }
}


