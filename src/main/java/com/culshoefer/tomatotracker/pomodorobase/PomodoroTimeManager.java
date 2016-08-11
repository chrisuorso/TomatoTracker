package com.culshoefer.tomatotracker.pomodorobase;

import com.culshoefer.tomatotracker.countdowntimer.TimerState;

import java.util.Map;

import javax.inject.Inject;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 * @author Christoph Ulshoefer <christophsulshoefer@gmail.com> 11/08/16.
 */
public class PomodoroTimeManager {
    private PomodoroIntervalStateManager pim;
    private Map<PomodoroState, Integer> intervalTimes;

    @Inject
    private Integer initialWorkTime;
    @Inject
    private Integer initialShortBreakTime;
    @Inject
    private Integer initialLongBreakTime;
    @Inject
    private Integer intervalsUntilLongBreak;
    @Inject
    private Integer breakExtension;


    initialWorkTime = (Integer)resources.getObject("intervalsUntilLongBreak");
    initialShortBreakTime = (Integer)resources.getObject("initialWorkTime");
    initialLongBreakTime = (Integer)resources.getObject("initialShortBreakTime");
    intervalsUntilLongBreak = (Integer)resources.getObject("initialLongBreakTime");
    breakExtension = (Integer)resources.getObject("breakExtension");

    private void installListeners() {
        this.pomodoroTimer.addListener(new ChangeListener<TimerState>() {
            @Override
            public void changed(ObservableValue<? extends TimerState> ov, TimerState oldValue, TimerState newValue) {
                if (newValue.equals(TimerState.RUNNING)) {
                    showPauseButton();
                } else if (newValue.equals(TimerState.PAUSED)) {
                    showPlayButton();
                } else if (newValue.equals(TimerState.DONE)) {
                    goToNextState();
                    pomodoroTimer.toggle();
                }
            }
        });
        //TODO add changelistener to pim
    }

    private void goToNextState() {
        PomodoroState newState = this.pim.nextInterval();
        //TODO make use of hashmap to simplify this
        if (newState.equals(PomodoroState.WORK)) {
            pomodoroTimer.setInitialTime(this.initialWorkTime);
            nowWork();
        } else if (newState.equals(PomodoroState.SHORTBREAK)) {
            pomodoroTimer.setInitialTime(this.initialShortBreakTime);
            nowShortBreak();
        } else {
            pomodoroTimer.setInitialTime(this.initialLongBreakTime);
            nowLongBreak();
        }
    }
}
