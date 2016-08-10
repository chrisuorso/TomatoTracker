package com.culshoefer.tomatotracker.pomodorobase;

import java.util.NoSuchElementException;

import javafx.beans.value.ObservableValueBase;

/**
 * @author Christoph Ulshoefer <christophsulshoefer@gmail.com> 05/08/16.
 */
public class PomodoroIntervalStateManager extends ObservableValueBase<PomodoroState> {

    private Integer currentInterval, initialNumIntervalsUntilLongPause;

    private void checkForNegativeNumInts(int intervalsUntilLongBreak) {
        if (intervalsUntilLongBreak < 0) {
            throw new IllegalArgumentException("Cannot set 0 or less intervals");
        }
    }

    private void checkIfSetInitially() {
        if (this.currentInterval == null) {
            throw new NoSuchElementException();
        }
    }

    public void setIntervalsGiven(int intervalsUntilLongBreak) {
        checkForNegativeNumInts(intervalsUntilLongBreak);
        this.initialNumIntervalsUntilLongPause = intervalsUntilLongBreak;
        this.currentInterval = 0;
        fireValueChangedEvent();
    }

    public int getNumIntervalsUntilLongPause() {
        double ret = initialNumIntervalsUntilLongPause - currentInterval / 2.0;
        return (int) Math.ceil(ret);
    }

    @Override
    public PomodoroState getValue() {
        checkIfSetInitially();
        if (initialNumIntervalsUntilLongPause == 0) {
            return PomodoroState.LONGBREAK;
        }
        if (currentInterval % 2 == 0) {
            return PomodoroState.WORK;
        } else if (currentInterval != 2 * initialNumIntervalsUntilLongPause - 1) {
            return PomodoroState.SHORTBREAK;
        } else {
            return PomodoroState.LONGBREAK;
        }
    }

    public PomodoroState nextInterval() {
        checkIfSetInitially();
        if (initialNumIntervalsUntilLongPause == 0) {
            return PomodoroState.LONGBREAK;
        } else {
            this.currentInterval = (currentInterval + 1) % (2 * initialNumIntervalsUntilLongPause);
            fireValueChangedEvent();
            return this.getValue();
        }
    }

}
