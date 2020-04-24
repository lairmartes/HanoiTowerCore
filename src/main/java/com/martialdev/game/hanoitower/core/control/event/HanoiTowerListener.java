package com.martialdev.game.hanoitower.core.control.event;

public interface HanoiTowerListener {

    void hanoiTowerEvent(GameOverEvent event);
    void fireDiskRemoved(PinEvent event);
    void fireDiskAdded(PinEvent event);
    void hanoiTowerEvent(GameStartEvent event);
}
