package com.culshoefer.tomatotracker.pomodorobase;

import com.culshoefer.tomatotracker.countdowntimer.CountdownTimer;
import com.culshoefer.tomatotracker.countdowntimer.TimerState;

import java.util.Map;

import javax.inject.Inject;

/**
 * @author Christoph Ulshoefer <christophsulshoefer@gmail.com> 11/08/16.
 */
public class PomodoroTimeManager {
    @Inject
    private Map<PomodoroState, Integer> intervalTimes;
    @Inject
    private Integer intervalsUntilLongBreak;
    @Inject
    private Integer breakExtension;
    @Inject
    private PomodoroIntervalStateManager pim;
    @Inject
    private CountdownTimer pomodoroTimer;

    public PomodoroTimeManager(){
        installListeners();
    }

    private void installListeners() {
        this.pomodoroTimer.addListener((ov, oldValue, newValue) -> {
            if (newValue.equals(TimerState.DONE)) {
                PomodoroState newState = pim.nextInterval();
                pomodoroTimer.setInitialTime(intervalTimes.get(newState));
                pomodoroTimer.toggle();
            }
            if (newValue.equals(TimerState.STOPPED)) {
                pim.setIntervalsGiven(intervalsUntilLongBreak);
                pomodoroTimer.setInitialTime(intervalTimes.get(PomodoroState.WORK));
            }
        });
    }

    public int getBreakExtension() {
        return this.breakExtension;
    }

    public void setInitialTimeForState(PomodoroState pomState, int initialTime) {
        this.intervalTimes.put(pomState, initialTime);
    }

    public void setIntervalsUntilLongBreak(int newVal) throws IllegalArgumentException {
        if(newVal < 0) {
            throw new IllegalArgumentException("Cannot specify negative intervals");
        } else {
            this.intervalsUntilLongBreak = newVal;
        }
    }
}
