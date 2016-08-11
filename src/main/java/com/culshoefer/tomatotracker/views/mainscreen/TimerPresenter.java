package com.culshoefer.tomatotracker.views.mainscreen;

import com.culshoefer.tomatotracker.views.settings.SettingsPresenter;
import com.culshoefer.tomatotracker.SwitchButton;
import com.culshoefer.tomatotracker.countdowntimer.CountdownTimer;
import com.culshoefer.tomatotracker.countdowntimer.TimerState;
import com.culshoefer.tomatotracker.pomodorobase.PomodoroIntervalStateManager;
import com.culshoefer.tomatotracker.pomodorobase.PomodoroState;

import java.net.URL;
import java.util.ResourceBundle;

import javax.inject.Inject;

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
public class TimerPresenter implements Initializable {
    @FXML
    private Label currentTimeLbl;
    @FXML
    private Button settingsButton;
    @Inject
    private PomodoroIntervalStateManager pim;
    @Inject
    private Parent root;
    @Inject
    private
    private String workBg, shortPauseBg, longPauseBg;

    @Override
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        assert currentTimeLbl != null : "fx:id\"currentTimeLbl\" was not injected. check FXML Timer.fxml";
        this.pim = new PomodoroIntervalStateManager();
        workBg = resources.getString("WORK_BACKGROUND");
        shortPauseBg = resources.getString("SHORTPAUSE_BACKGROUND");
        longPauseBg = resources.getString("LONGPAUSE_BACKGROUND");
        this.pim.addListener((ov, oldValue, newValue) -> changeBackgroundToState(newValue));
        this.pim.addListener((observable, oldValue, newValue) -> intervalsUntilLongBreak = pim.getNumIntervalsUntilLongPause());

        //TODO re-add the change-at-break-switch
    }

    @FXML
    public void openSettings(ActionEvent actionEvent) {
        System.out.println("Opening settings...");
        Parent root;
        try {
            URL res = getClass().getClassLoader().getResource("com/culshoefer/tomatotracker/views/settings/SettingsView.fxml");
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
            SettingsPresenter sc = fxmlloader.getController();
        } catch (Exception e) {
            // TODO do something more useful here (screw you, checked exceptions!)
            e.printStackTrace();
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
        this.root.setStyle("-fx-background-color: " + webColor + ";");
    }
}
