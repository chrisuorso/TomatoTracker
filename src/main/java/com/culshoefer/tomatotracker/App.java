package com.culshoefer.tomatotracker;

import com.airhacks.afterburner.injection.Injector;
import com.culshoefer.tomatotracker.countdowntimer.CountdownTimer;
import com.culshoefer.tomatotracker.pomodorobase.PomodoroIntervalStateManager;
import com.culshoefer.tomatotracker.pomodorobase.PomodoroState;
import com.culshoefer.tomatotracker.pomodorobase.PomodoroTimeManager;
import com.culshoefer.tomatotracker.shaperow.CircleFactory;
import com.culshoefer.tomatotracker.shaperow.ShapeRow;
import com.culshoefer.tomatotracker.views.mainscreen.TimerView;
import com.culshoefer.tomatotracker.views.timerbuttons.TimerButtonsView;

import java.util.HashMap;
import java.util.Map;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

/**
 * TODO extract
 */
public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        Map<Object, Object> customProperties = new HashMap<>();

        Map<PomodoroState, Integer> intervalTimes = new HashMap<>();
        intervalTimes.put(PomodoroState.WORK, 1200);//TODO get this from properties file
        intervalTimes.put(PomodoroState.SHORTBREAK, 300);
        intervalTimes.put(PomodoroState.LONGBREAK, 1200);
        customProperties.put("intervalTimes", intervalTimes);

        customProperties.put("intervalsUntilLongBreak", 4);
        customProperties.put("breakExtension", 30);

        CountdownTimer pomodoroTimer = new CountdownTimer();
        pomodoroTimer.setInitialTime(intervalTimes.get(PomodoroState.WORK));
        pomodoroTimer.setCurrentTime(intervalTimes.get(PomodoroState.WORK));
        customProperties.put("pomodoroTimer", pomodoroTimer);

        PomodoroIntervalStateManager pim = new PomodoroIntervalStateManager();
        pim.setIntervalsGiven((Integer)customProperties.get("intervalsUntilLongBreak"));
        customProperties.put("pim", pim);

        CircleFactory cf = new CircleFactory();
        ShapeRow sr = new ShapeRow(cf);
        customProperties.put("intervalsUntilLongBreakDisplay", sr);


        TimerButtonsView timerButtonsView = new TimerButtonsView();
        customProperties.put("timerButtonsView", timerButtonsView);
        PomodoroTimeManager ptm = new PomodoroTimeManager(intervalTimes, (Integer)customProperties.get("intervalsUntilLongBreak"),
                (Integer)customProperties.get("breakExtension"), pim, pomodoroTimer);
        customProperties.put("ptm", ptm);

        Injector.setConfigurationSource(customProperties::get);

        TimerView appView = new TimerView();
        Scene scene = new Scene(appView.getView());
        primaryStage.setTitle("TomatoTracker");
        primaryStage.setScene(scene);
        primaryStage.setOnHidden(e -> Platform.exit());
        primaryStage.setResizable(false);
        primaryStage.show();
        setHotkeys(pomodoroTimer, pim, ptm, scene);
    }

    private void setHotkeys(CountdownTimer pomodoroTimer, PomodoroIntervalStateManager pim, PomodoroTimeManager ptm, Scene scene) {
        scene.setOnKeyPressed(keyEvent -> {
            if(keyEvent.getCode() == KeyCode.SPACE) {
                pomodoroTimer.toggle();
                keyEvent.consume();
            }
            if(keyEvent.getCode() == KeyCode.S) {
                pomodoroTimer.stop();
                keyEvent.consume();
            }
            if(!pim.getValue().equals(PomodoroState.WORK)) {
                if (keyEvent.getCode() == KeyCode.E) {
                    pomodoroTimer.offsetCurrentTime(ptm.getBreakExtension());
                    keyEvent.consume();
                }
                if (keyEvent.getCode() == KeyCode.A) {
                    pomodoroTimer.stop();
                    pomodoroTimer.toggle();
                    keyEvent.consume();
                }
            }
        });
    }

    @Override
    public void stop() throws Exception {
        Injector.forgetAll();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
