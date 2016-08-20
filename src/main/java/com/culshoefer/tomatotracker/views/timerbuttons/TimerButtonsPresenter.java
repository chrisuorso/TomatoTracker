package com.culshoefer.tomatotracker.views.timerbuttons;

import com.culshoefer.tomatotracker.SwitchButton;
import com.culshoefer.tomatotracker.countdowntimer.CountdownTimer;
import com.culshoefer.tomatotracker.countdowntimer.TimerState;
import com.culshoefer.tomatotracker.pomodorobase.PomodoroIntervalStateManager;
import com.culshoefer.tomatotracker.pomodorobase.PomodoroState;
import com.culshoefer.tomatotracker.pomodorobase.PomodoroTimeManager;

import java.net.URL;
import java.util.ResourceBundle;

import javax.inject.Inject;
import javafx.fxml.FXML;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import static com.culshoefer.NullAsserterLogger.assertNotNull;

/**
 * @author Christoph Ulshoefer <christophsulshoefer@gmail.com> 11/08/16.
 * TODO add this to main screen?
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

    @Inject
    private CountdownTimer pomodoroTimer;
    @Inject
    private PomodoroIntervalStateManager pim;
    @Inject
    private PomodoroTimeManager ptm;

    @Override
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        assertNotNull(stopButton, "StopButton");
        assertNotNull(skipButton, "SkipButton");
        assertNotNull(extendButton, "ExtendButton");
        assertNotNull(playPauseButton, "PlayPauseButton");
        assertNotNull(pomodoroTimer, "PomodoroTimer");
        assertNotNull(pim, "PomodoroIntervalStateManager");
        assertNotNull(ptm, "PomodoroTimeManager");
        this.hideBreakButtons();
        this.installListeners();
    }

    private void installListeners() {
        this.pomodoroTimer.addListener((ov, oldValue, newValue) -> {
            if (newValue.equals(TimerState.RUNNING)) {
                showPauseButton();
            } else {
                showPlayButton();
            }
        });
        this.pim.addListener((observable, oldValue, newValue) -> {
            if(newValue.equals(PomodoroState.WORK)) {
                this.hideBreakButtons();
            } else {
                this.showBreakButtons();
            }
        });
    }

    @FXML
    public void skipBreak(ActionEvent actionEvent) {
        pomodoroTimer.stop();
        pomodoroTimer.toggle();
    }

    @FXML
    public void extendBreak(ActionEvent actionEvent) {
        int breakExtension = ptm.getBreakExtension();
        pomodoroTimer.offsetCurrentTime(breakExtension);
    }

    @FXML
    public void stopTimer(ActionEvent actionEvent) {
        pomodoroTimer.stop();
    }

    @FXML
    public void toggleTimer(ActionEvent actionEvent) {
        pomodoroTimer.toggle();
        System.out.println("Toggling timer");
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
