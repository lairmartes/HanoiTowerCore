/*
 * Criado em 20/08/2005
 */
package com.gmail.lairmartes.datastructure.stack;

/** This interface has been designed for stack implementation standardization.
 * @author Lair Martes Junior RA 200105514
 */
public interface GenericStack<T, E> {
	/** Removes the last element that have been put on the list and return this element to the caller.
	 * @return the element on the top of the list
	 */
	T pop();

	/** Includes an element type E in the stack, returning the same element if the stack allows the element or a
	 *  null element if the stack didn't accept the element.
	 *  A stack can be refuse the element inclusion if the stack has reached its capacity (see reset method).
	 *
	 * @param cC The element being included in the stack.
	 * @return the element included or null object if the stack refused the operation (see null object pattern).
	 */
	T push(E cC);

    /** Defines how many elements can be included in the stack.
     *
     * @param capacity quantity of elements that can be included in the stack.
     */
	void reset(int capacity);

    /**
     * Returns the last element included in the stack.  Unlike pop() method, it doesn't remove any element from stack.
     *
     * @return the last element included in the stack
     */
	T top();

    /** Returns an array containing all elements included in the stack.
     *
     * @return an array with the elements included in the stack.
     */
	T[] content();
}
