package com.culshoefer.tomatotracker.spinnerfield;

import com.culshoefer.tomatotracker.WrapAroundListener;

import java.util.ArrayList;
import java.util.Iterator;

import javafx.beans.InvalidationListener;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.ObservableValueBase;

/**
 * @author Christoph Ulshoefer <christophsulshoefer@gmail.com> 06/07/16.
 */
public class MinuteSecondSpinner extends ObservableValueBase<Integer> {
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
        minuteSpinner.getValueFactory().setWrapAround(false);
        minuteSpinner.valueProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
                oldMinutesTime.setValue(minutesTime.getValue());
                minutesTime.setValue(newValue);
                fireValueChangedEvent();
            }
        });
        secondsSpinner.valueProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
                oldSecondsTime.setValue(secondsTime.getValue());
                secondsTime.setValue(newValue);
                fireValueChangedEvent();
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

    private Integer getSecondsFromOldSpinners() {
        return oldMinutesTime.getValue() * 60 + oldSecondsTime.getValue();
    }

    private Integer getSecondsFromSpinners() {
        return minutesTime.getValue() * 60 + secondsTime.getValue();
    }
}
