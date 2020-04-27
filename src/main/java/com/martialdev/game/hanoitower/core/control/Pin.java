/*
 * Created 09/26/2005
 * Refactored 01/18/2018
 *
 */
package com.martialdev.game.hanoitower.core.control;

import com.martialdev.game.hanoitower.core.control.exception.InvalidMoveException;

/** Pin is used to implement Hanoi Tower's rules: a greater disk can't be put above a lesser disk.
 *
 * @see Disk
 * @see DiskStack
 * @author Lair Martes Junior - 2CCPN - USJT
 */
public class Pin {
    private final DiskStack diskStack;

    /** Initialize a Pin with a capacity.
     *
     * @param howManyDisks - provide how many disks a pin must have
     */
    public Pin(int howManyDisks) {

        diskStack = new DiskStack(howManyDisks);
    }

    /** Add a disk in the Pin.
     *  If a disk has size zero, throws an InvalidMoveException.
     *  If the disk on the top of this pin is lesser than the disk to be stacked, throws an InvalidMoveException.
     *
     * @param diskToBeStacked - disk that must be put in the pin
     * @throws InvalidMoveException - when disk is zero or disk is bigger than last disk added
     */
    public void add(Disk diskToBeStacked) throws InvalidMoveException {
        if (diskToBeStacked.getSize() == 0)
            throw new InvalidMoveException("You can't include a disk with zero length.");
        if (diskStack.size() == 0) {
            diskStack.push(diskToBeStacked);
            return;
        }
        if (diskStack.top().compareTo(diskToBeStacked) < 1) {
            throw new InvalidMoveException(
                    "This pin can't receive " + diskToBeStacked + " since it's greater than " + diskStack.top());
        } else diskStack.push(diskToBeStacked);
    }

    /** Remove a disk from pin.  If the pin has no disks, throws an InvalidMoveException.
     *
     * @return the last disk included in the stack.
     * @throws InvalidMoveException when pin has no disks
     */
    public Disk removeDisk() throws InvalidMoveException {
        if (diskStack.size() == 0) {
            throw new InvalidMoveException("This pin have no disks");
        } else {
            return diskStack.pop();
        }
    }

    /** Return a list of the disks in the pin.
     *
     * @return the list of the pins.  No changes in the elements of the returned list affect the original list.
     */
    public Disk[] getDisks() {
        return diskStack.content();
    }

    /** Provide a new capacity for the pin.
     *
     * @param howManyDisks the new capacity of the pin.
     */
    public void reset(int howManyDisks) {
        diskStack.reset(howManyDisks);
    }
}
