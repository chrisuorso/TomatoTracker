package com.culshoefer.tomatotracker.views.timerbuttons;

import com.culshoefer.tomatotracker.SwitchButton;
import com.culshoefer.tomatotracker.countdowntimer.TimerState;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

/**
 * @author Christoph Ulshoefer <christophsulshoefer@gmail.com> 11/08/16.
 * TODO add changelistener to pim
 */
public class TimerButtonsPresenter implements Initializable {
    @FXML
    private Button stopButton;
    @FXML
    private Button skipButton;
    @FXML
    private Button extendButton;
    @FXML
    private SwitchButton playPauseButton;

    @Override
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        this.hideBreakButtons();
        //TODO install changelistener to pim to change buttons when in break
        //TODO listen for changes in current timerstate to account for things
    }

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


    @FXML
    public void skipBreak(ActionEvent actionEvent) {
        pomodoroTimer.stop();
        this.goToNextState();
        pomodoroTimer.toggle();
    }

    @FXML
    public void extendBreak(ActionEvent actionEvent) {
        pomodoroTimer.offsetCurrentTime(breakExtension);
    }

    @FXML
    public void stopTimer(ActionEvent actionEvent) {
        pomodoroTimer.stop();
        this.showPlayButton();
        this.pim.setIntervalsGiven(intervalsUntilLongPause);
        this.nowWork();
    }

    @FXML
    public void toggleTimer(ActionEvent actionEvent) {
        System.out.println("toggling");
        pomodoroTimer.toggle();
        if (pomodoroTimer.isCurrentState(TimerState.PAUSED)) {
            this.showPlayButton();
        } else {
            this.showPauseButton();
        }
    }

    private void showPlayButton() {
        playPauseButton.setId("playPauseButton");
    }

    private void showPauseButton() {
        playPauseButton.setId("playPauseButtonPause");
    }

    private void hideBreakButtons() {
        this.extendButton.setVisible(false);
        this.extendButton.setManaged(false);
        this.skipButton.setVisible(false);
        this.skipButton.setManaged(false);
    }

    private void showBreakButtons() {
        this.extendButton.setVisible(true);
        this.extendButton.setManaged(true);
        this.skipButton.setVisible(true);
        this.skipButton.setManaged(true);
    }
}
