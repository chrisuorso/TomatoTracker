package com.culshoefer.tomatotracker.countdowntimer;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.ObservableValueBase;
import javafx.scene.control.Labeled;

/**
 * @author Christoph Ulshoefer <christophsulshoefer@gmail.com> 10/08/16.
 */
public class CountdownTimer extends ObservableValueBase<TimerState> {
    private TimeIntegerProperty currentTime;
    private Integer initialTime = 0;
    private TimerState currentState;
    private Labeled binding;
    private ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(4);
    private ScheduledFuture runningTimer;

    public CountdownTimer() {
        verifyThatValidTime(initialTime);
        this.runningTimer = null;
        this.currentState = TimerState.STOPPED;
        this.currentTime = new TimeIntegerProperty();
        this.currentTime.setValue(initialTime);
    }

    private void verifyThatValidTime(Integer time) {
        if (time < 0) {
            throw new IllegalArgumentException("Supplied time smaller than 0");
        }
    }

    private boolean hasBeenStartedAtAll() {
        return this.runningTimer != null;
    }

    private void setCurrentStateTo(TimerState newState) {
        this.currentState = newState;
        fireValueChangedEvent();
    }

    private void tickDown() {
        this.currentTime.setValue(currentTime.get() - 1);
        if (this.currentTime.get() <= 0) {
            runningTimer.cancel(true);
            this.setCurrentStateTo(TimerState.DONE);
        }
    }

    private boolean isUseless() {
        return this.initialTime == 0;
    }

    private void scheduleTimer() {
        if (!this.isUseless()) {
            Runnable timerTask = new Runnable() {
                @Override
                public void run() {
                    final Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            if (isCurrentState(TimerState.RUNNING)) {
                                tickDown();
                            }
                        }
                    };
                    Platform.runLater(runnable);
                }
            };
            runningTimer = scheduledThreadPool.scheduleAtFixedRate(timerTask, 0, 1, TimeUnit.SECONDS);
        } else {
            this.setCurrentStateTo(TimerState.STOPPED);
        }
    }

    private void toggleRunning() {
        if (this.isCurrentState(TimerState.PAUSED)) {
            this.setCurrentStateTo(TimerState.RUNNING);
            fireValueChangedEvent();
        } else {
            if (this.isCurrentState(TimerState.RUNNING)) {
                this.setCurrentStateTo(TimerState.PAUSED);
                fireValueChangedEvent();
            }
        }
    }

    public TimerState getValue() {
        return this.currentState;
    }

    public boolean isCurrentState(TimerState otherState) {
        return this.currentState.equals(otherState);
    }

    public void setInitialTime(int initialTime) {
        this.initialTime = initialTime;
    }

    public void setCurrentTime(int newTime) {
        verifyThatValidTime(newTime);
        this.currentTime.setValue(newTime);
    }

    public Labeled getLabeled() {
        return this.binding;
    }

    public void setLabelled(Labeled label) {
        this.binding = label;
        if(binding != null) {
            this.currentTime.addListener((observ, oldV, newV) -> binding.textProperty().setValue(currentTime.formatMinutes().getValue()));
            binding.textProperty().setValue(currentTime.formatMinutes().getValue());
        }
    }

    public void offsetCurrentTime(int offset) {
        this.setCurrentTime(currentTime.get() + offset);
    }

    /**
     * Automatically Triggered after an interval finished
     */
    private void start() {
        this.setCurrentStateTo(TimerState.RUNNING);
        this.currentTime.setValue(this.initialTime);
        this.scheduleTimer();
        //System.out.println("is work: " + this.isWork());
    }

    public void toggle() {
        if (this.isCurrentState(TimerState.STOPPED) || this.isCurrentState(TimerState.DONE)) {
            this.start();
        } else {
            this.toggleRunning();
        }
    }

    public void stop() {
        this.setCurrentStateTo(TimerState.STOPPED);
        if (this.hasBeenStartedAtAll()) {
            runningTimer.cancel(true);
        }
        this.currentTime.setValue(initialTime);
    }

}
