package com.culshoefer.tomatotracker.views.mainscreen;

import com.culshoefer.tomatotracker.countdowntimer.CountdownTimer;
import com.culshoefer.tomatotracker.pomodorobase.PomodoroTimeManager;
import com.culshoefer.tomatotracker.shaperow.IntegerNumberDisplay;
import com.culshoefer.tomatotracker.pomodorobase.PomodoroIntervalStateManager;
import com.culshoefer.tomatotracker.pomodorobase.PomodoroState;
import com.culshoefer.tomatotracker.views.settings.SettingsView;
import com.culshoefer.tomatotracker.views.timerbuttons.TimerButtonsView;
import com.sun.scenario.Settings;
import com.sun.xml.internal.bind.annotation.OverrideAnnotationOf;

import java.net.URL;
import java.util.ResourceBundle;

import javax.inject.Inject;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import static com.culshoefer.NullAsserterLogger.assertNotNull;


/**
 * @author Christoph Ulshoefer <christophsulshoefer@gmail.com> 28/06/16.
 * TODO play sound when break
 *         starts/ends TODO add keyboard shortcuts for controls TODO show amount of finished
 *         pomodoros today
 * TODO fix change in width on showing pause buttons (make fix width?)
 */
public class TimerPresenter implements Initializable {
    @FXML
    private Label currentTimeLbl;
    @FXML
    private Button settingsButton;
    @FXML
    private GridPane screen;
    @FXML
    private HBox pomodoroButtons;

    @Inject
    private PomodoroIntervalStateManager pim;
    @Inject
    private IntegerNumberDisplay intervalsUntilLongBreakDisplay;
    @Inject
    private TimerButtonsView timerButtonsView;
    @Inject
    private PomodoroTimeManager ptm;

    private String workBg, shortPauseBg, longPauseBg;

    private SettingsView sv;
    private GridPane tp;


    @Override
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        assertNotNull(currentTimeLbl, "fx:id currentTimeLbl");
        assertNotNull(settingsButton, "SettingsButton");
        assertNotNull(pim, "PomodoroIntervalManager");
        //assertNotNull(root, "TimerView");
        assertNotNull(intervalsUntilLongBreakDisplay, "IntegerNumberDisplay");
        workBg = resources.getString("WORK_BACKGROUND");
        shortPauseBg = resources.getString("SHORTPAUSE_BACKGROUND");
        longPauseBg = resources.getString("LONGPAUSE_BACKGROUND");
        this.pim.addListener((ov, oldValue, newValue) -> changeBackgroundToState(newValue));
        this.pim.addListener((observable, oldValue, newValue) -> intervalsUntilLongBreakDisplay.displayNum(pim.getNumIntervalsUntilLongPause()));
        this.ptm.pomodoroTimer.setLabelled(currentTimeLbl);
        Node timBView = timerButtonsView.getView();
        this.pomodoroButtons.getChildren().add(timBView);
    }

    @FXML
    public void openSettings(ActionEvent actionEvent) {
        if(sv == null) {
            createSettingsView();
            createSettingsWindow();
        }
    }

    private void createSettingsWindow() {
        Scene scene = new Scene(tp, 450, 200);
        Stage stage = new Stage();
        stage.setOnCloseRequest(event -> {
            tp = null;
            sv = null;
        });
        stage.setTitle("TomatoTracker Settings");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    private void createSettingsView() {
        tp = new GridPane();
        sv = new SettingsView((f) -> ptm);
        sv.getViewAsync(tp.getChildren()::add);
        ObservableList<String> styles = sv.getView().getStylesheets();
        for(int i = 0; i < styles.size(); i++){
            tp.getStylesheets().add(styles.get(i));
        }
    }

    private void changeBackgroundToState(PomodoroState newState){
        if(newState.equals(PomodoroState.SHORTBREAK)){
            this.setBackgroundToWebColor(shortPauseBg);
        } else {
            if(newState.equals(PomodoroState.LONGBREAK)){
                this.setBackgroundToWebColor(longPauseBg);
            } else {
                this.setBackgroundToWebColor(workBg);
            }
        }
    }


    private void setBackgroundToWebColor(String webColor) {
        this.screen.setStyle("-fx-background-color: " + webColor + ";");
    }
}
