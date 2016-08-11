package com.culshoefer.tomatotracker.spinnerfield;

import java.util.ArrayList;

import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.ObservableValueBase;

/**
 * @author Christoph Ulshoefer <christophsulshoefer@gmail.com> 06/07/16.
 * @deprecated Does too much!
 */
public class PomodoroTimeInputFields extends ObservableValueBase<Integer> {
    private ArrayList<ChangeListener<? super Integer>> listeners;
    private ArrayList<InvalidationListener> invlisteners;
    private MinuteSecondSpinner workSpinner, shortBreakSpinner, longBreakSpinner;
    private Integer workTime, shortBreakTime, longBreakTime;

    public PomodoroTimeInputFields(MinuteSecondSpinner workSpinner, MinuteSecondSpinner shortBreakSpinner,
                                   MinuteSecondSpinner longtBreakTimeSpinner) {
        this.workSpinner = workSpinner;
        this.shortBreakSpinner = shortBreakSpinner;
        this.longBreakSpinner = longtBreakTimeSpinner;
        this.workTime = workSpinner.getValue();
        this.shortBreakTime = shortBreakSpinner.getValue();
        this.longBreakTime = longBreakSpinner.getValue();
        this.workSpinner.addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> obv, Integer oldvalue, Integer newValue) {
                workTime = newValue;
                fireValueChangedEvent();
            }
        });
        this.shortBreakSpinner.addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> obv, Integer oldvalue, Integer newValue) {
                shortBreakTime = newValue;
                fireValueChangedEvent();
            }
        });
        this.longBreakSpinner.addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> obv, Integer oldvalue, Integer newValue) {
                longBreakTime = newValue;
                fireValueChangedEvent();
            }
        });
    }

    public Integer getShortBreakTime() {
        return this.shortBreakTime;
    }

    public Integer getLongBreakTime() {
        return this.longBreakTime;
    }

    @Override
    public Integer getValue() {
        return this.workTime;
    }

}
