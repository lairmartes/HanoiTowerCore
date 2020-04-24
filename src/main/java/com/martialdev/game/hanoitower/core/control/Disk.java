/*
 * Created in 09/26/2005
 * Refactored in 01/18/2018
 */
package com.martialdev.game.hanoitower.core.control;

/** Disk element is designed to be put in Pin.
 *  The Disk element has a size to be compared with another disks.
 *  In Hanoi Tower game, a greater disk can't be put above a lesser disk.
 *
 * @see Pin
 * @author Lair Martes Junior - 2CCPN - USJT
 *
 */
public class Disk implements Comparable<Disk>{
	/** DISK_ZERO is the Null Disk object that is used to avoid NullPointerException.
	 */
	public final static Disk DISK_ZERO = new Disk(0);
	private final int _size;
	private final String _toString;

	/**
	 * A Disk element must be constructed with a size.
	 *
	 * @param size
	 */
	public Disk(int size) {
		_size = size;
		_toString = "disk sizing " + size;
	}

	/** Returns the size of the disk.
	 *
	 * @return the size of the disk.
	 */
	public int getSize() {
		return _size;
	}

	/** Returns the size of the object minus the size of the compared object it.
	 * For example:
	 * 	- if both elements have the same size, it returns zero.
	 * 	- if the current element has size 10 and compared element has size 4, the result is 6 (positive).
	 * 	- if the current element has size 3 and compared element has size 11, the result is -8 (negative).
	 *  <b>Warning: </b> if the compared object isn't an instance of Disk, the result is 0.
	 *
	 *  @override Comparable.compareTo
	 *
	 * @param aDisk a Disk to be compared with the current disk.
	 * @return the value obtained from the operation of this element size minus the compared element size.
	 */
	public int compareTo(Disk aDisk) {
		return _size - aDisk.getSize();
	}

	/** Returns a string containing the size of this Disk.
	 *
	 * @override Object.toString
	 * @return a string with the size of the disk
	 */
	public String toString() {
		return _toString;
	}

	/** Returns true if the current disk has the same size of the given disk.
	 *
	 * @override Object.equals
	 * @param aDisk Disk to be compared.
	 * @return true if the disks have the same size.
	 */
	public boolean equals(Disk aDisk) {
		return aDisk.getSize() == this.getSize();
	}
}