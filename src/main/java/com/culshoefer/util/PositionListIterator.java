package com.culshoefer.util;

import java.util.ListIterator;

/**
 * @author Christoph Ulshoefer <christophsulshoefer@gmail.com> 03/08/16.
 */
public interface PositionListIterator<E> extends ListIterator<E> {
    E peekNext();

    E peekPrevious();

    E current();

    int currentIndex();

    boolean isEmpty();

    void toNext();

    void toPrevious();
}
