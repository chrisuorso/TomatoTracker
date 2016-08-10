package com.culshoefer.tomatotracker;

import com.culshoefer.tomatotracker.countdowntimer.CountdownTimer;
import com.culshoefer.tomatotracker.countdowntimer.TimerState;
import com.culshoefer.tomatotracker.pomodorobase.PomodoroIntervalStateManager;
import com.culshoefer.tomatotracker.pomodorobase.PomodoroState;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;


/**
 * @author Christoph Ulshoefer <christophsulshoefer@gmail.com> 28/06/16. TODO play sound when break
 *         starts/ends TODO add keyboard shortcuts for controls TODO show amount of finished
 *         pomodoros today
 */
public class PomodoroTimerController implements Initializable {
    private CountdownTimer pomodoroTimer;
    @FXML
    private Label currentTimeLbl;
    @FXML
    private SwitchButton playPauseButton;
    @FXML
    private Button stopButton;
    @FXML
    private Button skipButton;
    @FXML
    private Button extendButton;
    @FXML
    private Button settingsButton;
    private boolean isSettingsOpen = false;
    private Integer workval = 5;
    private Integer shortBreakval = 4;
    private Integer longBreakval = 3;
    private Integer intervalsUntilLongPause = 4;
    private Integer breakExtension = 30;
    private PomodoroIntervalStateManager pim;
    private Stage stage;
    private final String DARK_GREY = "#2A2A2A";
    private final String DARK_GREEN = "#052400";
    private final String DARK_BLUE = "#313351";

    @Override
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        assert currentTimeLbl != null : "fx:id\"currentTimeLbl\" was not injected. check FXML TimerView.fxml";
        this.pim = new PomodoroIntervalStateManager();
        this.pim.setIntervalsGiven(intervalsUntilLongPause);
        this.pomodoroTimer = new CountdownTimer(this.currentTimeLbl, workval);
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
/*        pomodoroTimer.isWork.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observ, Boolean oldValue, Boolean
                    newValue) {
                if(newValue) {
                    breakEnded();
                } else {
                    breakStarted();
                }
            }});*///TODO re-add the change-at-break-switch
        this.hideBreakButtons();
    }

    @FXML
    public void openSettings(ActionEvent actionEvent) {
        System.out.println("Opening settings...");
        Parent root;
        try {
            URL res = getClass().getClassLoader().getResource("views/SettingsView/SettingsView.fxml");
            assert res != null : "not properly injected fxml";
            FXMLLoader fxmlloader = new FXMLLoader(res);
            root = fxmlloader.load();
            /*(Circle c = new Circle();
            c.setRadius(20);
            c.setFill(Color.rgb(20, 255, 30));
            ShapeRow sr = new ShapeRow(c);
            root.getScene().add(sr);
            */
            Stage stage = new Stage();
            stage.setTitle("Settings");
            stage.setScene(new Scene(root, 400, 200));
            stage.show();
            SettingsController sc = fxmlloader.getController();
            sc.setSettings(stage, pomodoroTimer);
        } catch (Exception e) {
            // TODO do something more useful here (screw you, checked exceptions!)
            e.printStackTrace();
        }
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

    public void nowShortBreak() {
        this.showBreakButtons();
        this.setBackgroundToWebColor(DARK_GREEN);
    }

    public void nowLongBreak() {
        this.showBreakButtons();
        this.setBackgroundToWebColor(DARK_BLUE);
    }

    public void nowWork() {
        this.hideBreakButtons();
        this.setBackgroundToWebColor(DARK_GREY);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private void goToNextState() {
        PomodoroState newState = this.pim.nextInterval();
        //TODO make use of hashmap to simplify this
        if (newState.equals(PomodoroState.WORK)) {
            pomodoroTimer.setInitialTime(this.workval);
            nowWork();
        } else if (newState.equals(PomodoroState.SHORTBREAK)) {
            pomodoroTimer.setInitialTime(this.shortBreakval);
            nowShortBreak();
        } else {
            pomodoroTimer.setInitialTime(this.longBreakval);
            nowLongBreak();
        }
    }

    private void setBackgroundToWebColor(String webColor) {
        this.stage.getScene().getRoot().setStyle("-fx-background-color: " + webColor + ";");
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
