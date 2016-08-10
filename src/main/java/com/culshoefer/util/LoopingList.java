package com.culshoefer.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.NoSuchElementException;

/**
 * @author Christoph Ulshoefer <christophsulshoefer@gmail.com> 02/08/16. TODO add JavaDoc for all
 *         methods TODO test for illegal states/illegal arguments
 */
public class LoopingList<E> implements PositionListIterator<E> {

    private ArrayList<E> items;
    private int currentPosition = -1;

    public LoopingList() {
        this.items = new ArrayList<>();
    }

    public LoopingList(Collection<E> c) {
        this.items = new ArrayList<>(c);
        if (!this.isEmpty())
            this.currentPosition = 0;
    }

    private void checkIfEmpty() {
        if (this.isEmpty()) {
            throw new NoSuchElementException("Not enough elements in list");
        }
    }

    private int getSmallestPostiveIndex(int index) {
        while (index < 0) {
            index += this.items.size();
        }
        return index % this.items.size();
    }

    public int getSize() {
        return this.items.size();
    }

    @Override
    public void add(E e) {
        if (currentPosition != -1) {
            this.items.add(this.currentPosition + 1, e);
        } else {
            this.currentPosition = 0;
            this.items.add(e);
        }
    }

    /**
     * @param index Index at which to insert item. Only positive indices are allowed
     * @param e     The element to be inserted at the index. All elements with bigger index will get
     *              shifted up by one
     */
    public void add(int index, E e) {
        this.items.add(this.getSmallestPostiveIndex(index), e);
    }

    /**
     * Deviates from the standard interpretation of set() in ListIterator
     * We always replace the element at the current rather than the last returned by prev/next
     *
     * @param e The element to override the curent position with
     */
    @Override
    public void set(E e) {
        this.items.set(this.currentPosition, e);
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isEmpty() {
        return this.getSize() <= 0;
    }

    @Override
    public E current() {
        this.checkIfEmpty();
        return this.items.get(this.currentPosition);
    }

    @Override
    public int currentIndex() {
        return this.currentPosition;
    }

    @Override
    public int nextIndex() {
        return this.getSmallestPostiveIndex(currentPosition + 1);
    }

    @Override
    public int previousIndex() {
        return this.getSmallestPostiveIndex(this.currentPosition - 1);
    }

    @Override
    public E next() {
        E thisItem = this.peekNext();
        this.toNext();
        return thisItem;
    }

    @Override
    public E peekNext() {
        this.checkIfEmpty();
        return this.items.get(this.nextIndex());
    }

    @Override
    public E previous() {
        E prevItem = this.peekPrevious();
        this.toPrevious();
        return prevItem;
    }

    @Override
    public E peekPrevious() {
        this.checkIfEmpty();
        return this.items.get(this.previousIndex());
    }

    @Override
    public boolean hasNext() {
        return !this.isEmpty();
    }

    @Override
    public boolean hasPrevious() {
        return !this.isEmpty();
    }

    @Override
    public void toNext() {
        this.currentPosition = this.nextIndex();
    }

    @Override
    public void toPrevious() {
        this.currentPosition = this.previousIndex();
    }

    public void offset(int offsetBy) {
        this.currentPosition = this.getSmallestPostiveIndex(this.currentPosition + offsetBy);
    }
}
