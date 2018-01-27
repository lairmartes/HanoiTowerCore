/*
 * Created 08/16/2005
 * Refactored 01/18/2018
 */
package com.gmail.lairmartes.game.hanoitower;

import com.gmail.lairmartes.datastructure.stack.GenericStack;

/** DiskStack is an implementation of GenericStack designed to receive Disk elements.
 *
 * @see Disk
 * @author Lair Martes Junior RA 200105514
 */
public class DiskStack implements GenericStack<Disk, Disk> {

	private int _capacity;
	private int iPos;
	private Disk[] _stack;

	/** DiskStack needs to have an initial capacity provided in constructor.
	 *
	 * @param initialCapacity how many elements the stack can hold initially.
	 */
	public DiskStack(int initialCapacity) {
		reset(initialCapacity);
	}

	/** Redefines disk stack capacity
	 *
	 * @param newCapacity how many elements the stack can hold now.
	 */
	public void reset(int newCapacity) {
		_capacity = newCapacity;
		_stack = new Disk[_capacity];
		fullFilStackWithZeroDisks();
		iPos = 0;
	}

	private void fullFilStackWithZeroDisks() {
		for (int i = 0; i< _capacity; i++)
			_stack[i] = Disk.DISK_ZERO;
	}

	/** Include a disk in the stack. Returns DISK_ZERO if the disk stack is already full.
	 *
	 * @param aDisk The disk being included in the disk stack.
	 * @return the disk included or DISK_ZERO if the stack is already full.
	 */
	public Disk push(Disk aDisk) {
		if (iPos >= _capacity) return Disk.DISK_ZERO;
		return _stack[iPos++] = aDisk;
	}

	/** Remove the last disk included in the stack.
	 *
	 * @return the disk included.  Returns DISK_ZERO if the stack is empty.
	 */
	public Disk pop() {
		if (iPos == 0) return Disk.DISK_ZERO;
        Disk result = _stack[--iPos];
		_stack[iPos] = Disk.DISK_ZERO;
		return result;
	}

	/** Method created for testing purposes.  Do not use it, except for tests!!!
	 *
	 * @return how many elements have been included in the disk stack.
	 */
	protected int size() {
		return iPos;
	}

	/** Return the last disk included in the stack.
	 *
	 * @override GenericStack.top
	 * @return the last disk included in the stack.
	 */
	public Disk top() {
		return _stack[iPos - 1];
	}

	/** A clone of the list of disks included in the stack
	 *
	 * @return the disks included (clone of the list).
	 */
	public Disk[] content() {
		return _stack.clone();
	}
}
