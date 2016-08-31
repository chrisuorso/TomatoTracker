package com.culshoefer.tomatotracker.pomodorobase;

import com.culshoefer.tomatotracker.countdowntimer.CountdownTimer;
import com.culshoefer.tomatotracker.countdowntimer.TimerState;

import java.util.Map;

/**
 * @author Christoph Ulshoefer <christophsulshoefer@gmail.com> 11/08/16.
 */
public class PomodoroTimeManager {
    private Map<PomodoroState, Integer> intervalTimes;
    private Integer intervalsUntilLongBreak;
    private Integer breakExtension;
    private PomodoroIntervalStateManager pim;
    public CountdownTimer pomodoroTimer;//TODO make private

    public PomodoroTimeManager(Map<PomodoroState, Integer> intervalTimes, Integer intervalsUntilLongBreak,
                               Integer breakExtension, PomodoroIntervalStateManager pim, CountdownTimer pomodoroTimer) {
        this.setIntervalTimes(intervalTimes);
        this.setIntervalsUntilLongBreak(intervalsUntilLongBreak);
        this.setBreakExtension(breakExtension);
        this.setPomodoroIntervalStateManager(pim);
        this.setPomodoroTimer(pomodoroTimer);
        this.init();
    }

    private void init() {
        assert intervalsUntilLongBreak != null : "IntervalsUntilLongBreak not injected properly";
        assert breakExtension != null : "BreakExtensionTime not injected properly";
        assert pim != null : "PomodoroIntervalManager not injected properly";
        assert pomodoroTimer != null : "CountdownTimer not injected properly";
        installListeners();
        this.pomodoroTimer.setInitialTime(intervalTimes.get(PomodoroState.WORK));
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

    public Integer getTimeForInterval(PomodoroState pomodoroState) {
        return intervalTimes.get(pomodoroState);
    }

    public int getBreakExtension() {
        return this.breakExtension;
    }

    public void setInitialTimeForState(PomodoroState pomState, int initialTime) {
        this.intervalTimes.put(pomState, initialTime);
        if(pomState.equals(PomodoroState.WORK) && pomodoroTimer.isCurrentState(TimerState.STOPPED)) {
            pomodoroTimer.setInitialTime(initialTime);
            pomodoroTimer.setCurrentTime(initialTime);
        }
    }

    public void setIntervalsUntilLongBreak(int newVal) throws IllegalArgumentException {
        if(newVal < 0) {
            throw new IllegalArgumentException("Cannot specify negative intervals");
        } else {
            this.intervalsUntilLongBreak = newVal;
        }
    }

    public Integer getIntervalsUntilLongBreak() {
        return intervalsUntilLongBreak;
    }

    public void setIntervalTimes(Map<PomodoroState, Integer> intervalTimes) {
        this.intervalTimes = intervalTimes;
    }

    public void setBreakExtension(Integer breakExtension) {
        this.breakExtension = breakExtension;
    }

    /**
     * TODO make this more generic
     * @param pim
     */
    public void setPomodoroIntervalStateManager(PomodoroIntervalStateManager pim) {
        this.pim = pim;
    }

    public void setPomodoroTimer(CountdownTimer pomodoroTimer) {
        this.pomodoroTimer = pomodoroTimer;
    }
}
