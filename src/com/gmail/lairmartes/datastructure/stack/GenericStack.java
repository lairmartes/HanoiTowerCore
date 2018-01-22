/*
 * Criado em 20/08/2005
 */
package com.gmail.lairmartes.datastructure.stack;

/**
 * @author Lair Martes Junior RA 200105514
 */
public interface GenericStack<T, E> {
	T pop();
	T push(E cC);
	void reset(int capacity);
	T top();
	T[] content();
}
