package com.culshoefer.tomatotracker.spinnerfield;

import com.culshoefer.tomatotracker.WrapAroundListener;

import java.util.ArrayList;
import java.util.Iterator;

import javafx.beans.InvalidationListener;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 * @author Christoph Ulshoefer <christophsulshoefer@gmail.com> 06/07/16.
 */
public class MinuteSecondSpinner implements ObservableValue<Integer> {
    private ArrayList<ChangeListener<? super Integer>> listeners;
    private ArrayList<InvalidationListener> invlisteners;
    private IntegerSpinnerAutoCommit minuteSpinner;
    private IntegerSpinnerAutoCommit secondsSpinner;
    private IntegerProperty oldMinutesTime;
    private IntegerProperty oldSecondsTime;
    private IntegerProperty minutesTime;
    private IntegerProperty secondsTime;

    public MinuteSecondSpinner(IntegerSpinnerAutoCommit minuteSpinner, IntegerSpinnerAutoCommit secondsSpinner) {
        this.minuteSpinner = minuteSpinner;
        this.secondsSpinner = secondsSpinner;
        this.minutesTime = new SimpleIntegerProperty();
        this.secondsTime = new SimpleIntegerProperty();
        this.oldMinutesTime = new SimpleIntegerProperty();
        this.oldSecondsTime = new SimpleIntegerProperty();
        this.minutesTime.setValue(minuteSpinner.getValue());
        this.secondsTime.setValue(secondsSpinner.getValue());
        this.listeners = new ArrayList<ChangeListener<? super Integer>>();
        this.invlisteners = new ArrayList<InvalidationListener>();
        minuteSpinner.getValueFactory().setWrapAround(false);
        minuteSpinner.valueProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
                oldMinutesTime.setValue(minutesTime.getValue());
                minutesTime.setValue(newValue);
                fireChange(observable);
            }
        });
        secondsSpinner.valueProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
                oldSecondsTime.setValue(secondsTime.getValue());
                secondsTime.setValue(newValue);
                fireChange(observable);
            }
        });
        secondsSpinner.addListener(new WrapAroundListener() {
            @Override
            public void wrappedAround(boolean upperWrapped) {
                if (upperWrapped) {
                    minuteSpinner.increment();
                } else {
                    minuteSpinner.decrement();
                }
            }
        });
    }

    @Override
    public Integer getValue() {
        return this.getSecondsFromSpinners();
    }

    @Override
    public void addListener(ChangeListener<? super Integer> listener) {
        this.listeners.add(listener);
    }

    @Override
    public void removeListener(ChangeListener<? super Integer> listener) {
        this.listeners.remove(listener);
    }

    @Override
    public void addListener(InvalidationListener listener) {
        this.invlisteners.add(listener);
    }

    @Override
    public void removeListener(InvalidationListener listener) {
        this.invlisteners.remove(listener);
    }

    private void fireInvalid() {
        for (InvalidationListener invalidListener : this.invlisteners) {
            invalidListener.invalidated(this);
        }
    }

    private void fireChange(ObservableValue<? extends Integer> ov) {
        Iterator<ChangeListener<? super Integer>> listenerI = this.listeners.iterator();
        while (listenerI.hasNext()) {
            ChangeListener changeL = listenerI.next();
            changeL.changed(this, getSecondsFromOldSpinners(), getSecondsFromSpinners());
        }
    }

    private Integer getSecondsFromOldSpinners() {
        try {
            return oldMinutesTime.getValue() * 60 + oldSecondsTime.getValue();
        } catch (Exception e) {
            this.fireInvalid();
            return null;
        }
    }

    private Integer getSecondsFromSpinners() {
        try {
            return minutesTime.getValue() * 60 + secondsTime.getValue();
        } catch (Exception e) {
            this.fireInvalid();
            return minuteSpinner.getValue() * 60 + secondsSpinner.getValue();
        }
    }
}
