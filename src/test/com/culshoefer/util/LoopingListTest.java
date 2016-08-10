package com.culshoefer.util;


import com.culshoefer.util.LoopingList;

import org.junit.Before;
import org.junit.Test;

import java.util.NoSuchElementException;

import static com.culshofer.fuzzytesting.src.IntegerIsCongruentModulo.isCongruentModulo;
import static com.culshofer.fuzzytesting.src.RandomIntegerGenerator.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Christoph Ulshoefer <christophsulshoefer@gmail.com> 02/08/16.
 * TODO replace hard-coded test with fuzzy tests
 * TODO add test for collection constructor
 */
public class LoopingListTest {
    private LoopingList<Integer> intListI;

    @Before
    public void setUp() throws Exception {
        this.intListI = new LoopingList<Integer>();
    }

    @Test
    public void doesNotHaveNextAtStart() {
        assertThat(intListI.hasNext(), is(false));
    }

    @Test
    public void doesNotHavePreviousAtStart() {
        assertThat(intListI.hasPrevious(), is(false));
    }

    @Test
    public void isEmptyAtStart() {
        assertThat(intListI.isEmpty(), is(true));
    }

    @Test
    public void isNotEmptyAfterAddingElement() {
        this.intListI.add(1);
        assertThat(intListI.isEmpty(), is(false));
    }

    @Test
    public void nextItemIsInsertedAtNextPosition() {
        this.intListI.add(1);
        this.intListI.add(2);
        int r = this.intListI.next();
        this.intListI.add(3);
        assertThat(this.intListI.next(), is(3));
    }

    @Test
    public void afterAddingNextValueIsThatValueImmediately() {
        this.intListI.add(1);
        this.intListI.add(2);
        assertThat(this.intListI.next(), is(2));
    }

    @Test
    public void afterNextingPrevValueIsTheOriginalValue() {
        this.intListI.add(1);
        this.intListI.add(2);
        int currentVal = this.intListI.current();
        int r = this.intListI.next();
        assertThat(this.intListI.current(), is(r));
        assertThat(this.intListI.previous(), is(currentVal));
    }

    @Test
    /**
     * TODO merge this and the next test
     */ public void afterNextingListAdvances() {
        this.intListI.add(1);
        this.intListI.add(2);
        int r = this.intListI.next();
        assertThat(this.intListI.next(), is(1));
    }

    @Test
    public void afterAddingValueListDoesNotAdvance() {
        this.intListI.add(1);// TODO check for list index?
        this.intListI.add(2);
        assertThat(this.intListI.next(), is(2));
    }

    @Test
    public void afterNextingIndexIncrements() {
        this.intListI.add(40);
        this.intListI.add(42);
        int r = this.intListI.next();
        assertThat(this.intListI.currentIndex(), is(1));
    }

    @Test
    public void toNextGoesToNextElement() {
        this.intListI.add(42);
        this.intListI.add(43);
        this.intListI.toNext();
        assertThat(this.intListI.currentIndex(), is(1));
        assertThat(this.intListI.current(), is(43));
    }

    @Test
    public void ifNextingMoreThanThereAreElementsItStartsAtBeginning() {
        int numNums = 0;
        for (int i = 40; i < 50; i++) {
            numNums += 1;
            this.intListI.add(i);
        }
        int isRunningTimes = 0;
        for (int i = 1; i < numNums; i++) {
            this.intListI.next();
            isRunningTimes += 1;
        }
        assertThat(this.intListI.next(), is(40));
    }

    @Test
    public void addingItemAtIndexInsertsItThere() {
        this.intListI.add(1);
        this.intListI.add(2);
        this.intListI.add(3);
        this.intListI.add(1, 4);
        assertThat(this.intListI.next(), is(4));
    }

    @Test
    public void addingItemAtIndexAdvancesItemsAfterIt() {
        this.intListI.add(1);
        this.intListI.add(2);
        this.intListI.add(3);
        this.intListI.add(2, 4);
        this.intListI.next();
        this.intListI.next();
        assertThat(this.intListI.next(), is(2));
    }

    @Test
    public void nextIndexIsBiggerByOneWhenNotAtMaxIndex() {
        int numItemsToAdd = 10;
        for (int i = 0; i < numItemsToAdd; i++) {
            this.intListI.add(i);
        }
        for (int i = 0; i < numItemsToAdd / 2; i++) {
            this.intListI.toNext();
        }
        int currentIndex = this.intListI.currentIndex();
        assertThat(currentIndex + 1, is(this.intListI.nextIndex()));
    }

    @Test
    public void previousIndexIsSmallerByOneWhenNotAtMaxIndex() {
        int numItemsToAdd = 10;
        for (int i = 0; i < numItemsToAdd; i++) {
            this.intListI.add(i);
        }
        for (int i = 0; i < numItemsToAdd / 2; i++) {
            this.intListI.toNext();
        }
        int currentIndex = this.intListI.currentIndex();
        assertThat(currentIndex - 1, is(this.intListI.previousIndex()));
    }

    @Test
    public void atMaxIndexNextIndexIsZero() {
        int numItemsToAdd = 10;
        for (int i = 0; i < numItemsToAdd; i++) {
            this.intListI.add(i);
        }
        for (int i = 0; i < numItemsToAdd - 1; i++) {
            this.intListI.toNext();
        }
        assertThat(this.intListI.nextIndex(), is(0));
    }

    @Test
    public void atZeroNextIndexIsMax() {
        int numItemsToAdd = 10;
        for (int i = 0; i < numItemsToAdd; i++) {
            this.intListI.add(i);
        }
        assertThat(this.intListI.previousIndex(), is(numItemsToAdd - 1));
    }

    @Test
    public void peekNextAndAdvanceEqualsNext() {
        int numsToTest = 10;
        for (int i = 0; i < numsToTest; i++) {
            this.intListI.add(i);
        }
        for (int i = 0; i < numsToTest; i++) {
            int currentElem = this.intListI.current();
            int nextElem = this.intListI.peekNext();
            int nextElemWithoutPeek = this.intListI.next();
            assertThat(nextElemWithoutPeek, is(nextElem));
            this.intListI.toPrevious();
            assertThat(this.intListI.current(), is(currentElem));
        }
    }

    @Test
    public void peekPreviousAndDecrementEqualsPrevious() {
        int numsToTest = 10;
        for (int i = 0; i < numsToTest; i++) {
            this.intListI.add(i);
        }
        for (int i = 0; i < numsToTest; i++) {
            int currentElem = this.intListI.current();
            int prevElem = this.intListI.peekPrevious();
            int prevElemWithoutPeek = this.intListI.previous();
            assertThat(prevElemWithoutPeek, is(prevElem));
            this.intListI.toNext();
            assertThat(this.intListI.current(), is(currentElem));

        }
    }

    @Test
    public void setOverridesElementAtIndex() {
        for (int i = 0; i < 10; i++) {
            this.intListI.add(getInt());
        }
        for (int i = 0; i < getRandIntWithinBounds(-1, 100); i++) {
            this.intListI.toNext();
        }
        int currentIndex = this.intListI.currentIndex();
        int newElem = getInt();
        this.intListI.set(newElem);
        assertThat(this.intListI.current(), is(newElem));
        assertThat(this.intListI.currentIndex(), is(currentIndex));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void removeIsNotSupported() {
        this.intListI.remove();
    }

    @Test(expected = NoSuchElementException.class)
    public void emptyNextThrowsException() {
        this.intListI.next();
    }

    @Test(expected = NoSuchElementException.class)
    public void emptyPrevThrowsException() {
        this.intListI.previous();
    }

    @Test(expected = NoSuchElementException.class)
    public void emptyCurrentThrowsException() {
        this.intListI.current();
    }

    @Test(expected = NoSuchElementException.class)
    public void emptyPeekNextThrowsException() {
        this.intListI.peekNext();
    }

    @Test(expected = NoSuchElementException.class)
    public void emptyPeekPrevThrowsException() {
        this.intListI.peekPrevious();
    }

    @Test
    public void offsetSkipsByNumElementsSpecified(){
        int numElemsToAdd = getRandIntWithinBounds(0, 50);
        for(int i = 0; i < numElemsToAdd; i++){
            this.intListI.add(getInt());
        }
        int numToPutPositionTo = getRandIntWithinBounds(0, 200);
        for(int i = 0; i < numToPutPositionTo; i++){
            this.intListI.toNext();
        }
        int currentI = this.intListI.currentIndex();
        int toOffsetBy = getInt();
        int expectedIndexEquivalenceClass = currentI + toOffsetBy;
        this.intListI.offset(toOffsetBy);
        int size = this.intListI.getSize();
        assertThat(this.intListI.currentIndex(), isCongruentModulo(expectedIndexEquivalenceClass, size));
    }
}