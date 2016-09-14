package com.culshoefer.tomatotracker;

import com.airhacks.afterburner.injection.Injector;
import com.culshoefer.tomatotracker.countdowntimer.CountdownTimer;
import com.culshoefer.tomatotracker.countdowntimer.TimerState;
import com.culshoefer.tomatotracker.pomodorobase.PomodoroIntervalStateManager;
import com.culshoefer.tomatotracker.pomodorobase.PomodoroState;
import com.culshoefer.tomatotracker.pomodorobase.PomodoroTimeManager;
import com.culshoefer.tomatotracker.shaperow.CircleFactory;
import com.culshoefer.tomatotracker.shaperow.ShapeRow;
import com.culshoefer.tomatotracker.views.mainscreen.TimerView;
import com.culshoefer.tomatotracker.views.timerbuttons.TimerButtonsView;

import java.util.HashMap;
import java.util.Map;
import java.util.prefs.*;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

/**
 * TODO extract
 */
public class App extends Application {
    private static final Integer DEFAULT_INTERVALS_UNTIL_LONG_BREAK = 4;
    private static final Integer DEFAULT_WORK_INTERVAL_LENGTH = 1200;
    private static final Integer DEFAULT_SHORT_BREAK_INTERVAL_LENGTH = 300;
    private static final Integer DEFAULT_LONG_BREAK_INTERVAL_LENGTH = 1200;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Map<Object, Object> customProperties = new HashMap<>();

        Preferences prefs = Preferences.userRoot();
        Integer intervalsuntillongbreak = prefs.getInt("INTERVALS_UNTIL_LONG_BREAK", DEFAULT_INTERVALS_UNTIL_LONG_BREAK);
        Integer workintervallength = prefs.getInt("WORK_INTERVAL_LENGTH", DEFAULT_WORK_INTERVAL_LENGTH);
        Integer shortbreakintervallength = prefs.getInt("SHORT_BREAK_INTERVAL_LENGTH", DEFAULT_SHORT_BREAK_INTERVAL_LENGTH);
        Integer longbreakintervallength = prefs.getInt("LONG_BREAK_INTERVAL_LENGTH", DEFAULT_LONG_BREAK_INTERVAL_LENGTH);

        customProperties.put("prefs", prefs);

        Map<PomodoroState, Integer> intervalTimes = new HashMap<>();
        intervalTimes.put(PomodoroState.WORK, workintervallength);//TODO get this from properties file
        intervalTimes.put(PomodoroState.SHORTBREAK, shortbreakintervallength);
        intervalTimes.put(PomodoroState.LONGBREAK, longbreakintervallength);
        customProperties.put("intervalTimes", intervalTimes);

        customProperties.put("intervalsUntilLongBreak", intervalsuntillongbreak);
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

    //TODO extract
    private void setHotkeys(CountdownTimer pomodoroTimer, PomodoroIntervalStateManager pim, PomodoroTimeManager ptm, Scene scene) {
        scene.setOnKeyPressed(keyEvent -> {
            if ((pomodoroTimer.getValue().equals(TimerState.STOPPED) || pomodoroTimer.getValue().equals(TimerState.DONE)) &&
                    keyEvent.getCode().equals(KeyCode.SPACE)) {
                pomodoroTimer.toggle();
                keyEvent.consume();
            } else {
                if((pomodoroTimer.getValue().equals(TimerState.RUNNING) || pomodoroTimer.getValue().equals(TimerState.PAUSED)) &&
                        keyEvent.getCode().equals(KeyCode.S)) {
                    pomodoroTimer.toggle();
                    keyEvent.consume();
                }
            else{
                if (keyEvent.getCode().equals(KeyCode.SPACE) && pomodoroTimer.getValue().equals(TimerState.RUNNING)) {
                    pomodoroTimer.stop();
                    keyEvent.consume();
                } else {
                    if (!pim.getValue().equals(PomodoroState.WORK)) {
                        if (keyEvent.getCode().equals(KeyCode.E)) {
                            pomodoroTimer.offsetCurrentTime(ptm.getBreakExtension());
                            keyEvent.consume();
                        }
                        if (keyEvent.getCode().equals(KeyCode.A)) {
                            pomodoroTimer.stop();
                            pomodoroTimer.toggle();
                            keyEvent.consume();
                        }
                    }
                }
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
