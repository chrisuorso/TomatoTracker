package com.culshoefer.tomatotracker.views.mainscreen;

import com.culshoefer.tomatotracker.shaperow.IntegerNumberDisplay;
import com.culshoefer.tomatotracker.views.settings.SettingsPresenter;
import com.culshoefer.tomatotracker.pomodorobase.PomodoroIntervalStateManager;
import com.culshoefer.tomatotracker.pomodorobase.PomodoroState;

import java.net.URL;
import java.util.ResourceBundle;

import javax.inject.Inject;
import javafx.fxml.FXML;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import static com.culshoefer.NullAsserterLogger.assertNotNull;


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
    private IntegerNumberDisplay intervalsUntilLongBreakDisplay;

    private String workBg, shortPauseBg, longPauseBg;

    @Override
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        assertNotNull(currentTimeLbl, "fx:id currentTimeLbl");
        assertNotNull(settingsButton, "SettingsButton");
        assertNotNull(pim, "PomodoroIntervalManager");
        //assertNotNull(root, "TimerView");
        assertNotNull(intervalsUntilLongBreakDisplay, "IntegerNumberDisplay");
        this.pim = new PomodoroIntervalStateManager();
        workBg = resources.getString("WORK_BACKGROUND");
        shortPauseBg = resources.getString("SHORTPAUSE_BACKGROUND");
        longPauseBg = resources.getString("LONGPAUSE_BACKGROUND");
        this.pim.addListener((ov, oldValue, newValue) -> changeBackgroundToState(newValue));
        this.pim.addListener((observable, oldValue, newValue) -> intervalsUntilLongBreakDisplay.displayNum(pim.getNumIntervalsUntilLongPause()));

        //TODO re-add the change-at-break-switch
    }

    @FXML
    public void openSettings(ActionEvent actionEvent) {
        System.out.println("Opening settings...");
        Parent root;
        try {
            URL res = getClass().getClassLoader().getResource("com/culshoefer/tomatotracker/views/settings/settingsview.fxml");
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
        //this.root.setStyle("-fx-background-color: " + webColor + ";");
    }
}
