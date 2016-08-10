package com.culshoefer.tomatotracker;


import com.culshoefer.tomatotracker.countdowntimer.CountdownTimer;
import com.culshoefer.tomatotracker.spinnerfield.IntegerSpinnerAutoCommit;
import com.culshoefer.tomatotracker.spinnerfield.MinuteSecondSpinner;
import com.culshoefer.tomatotracker.spinnerfield.PomodoroTimeInputFields;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

/**
 * @author Christoph Ulshoefer <christophsulshoefer@gmail.com> 18/07/16. TODO backup current
 *         interval times to disk
 */
public class SettingsController implements Initializable {
    @FXML
    private IntegerSpinnerAutoCommit pomodoroWorkMinsSpnr;
    @FXML
    private IntegerSpinnerAutoCommit pomodoroWorkSecsSpnr;
    @FXML
    private IntegerSpinnerAutoCommit pomodoroShortBreakMinsSpnr;
    @FXML
    private IntegerSpinnerAutoCommit pomodoroShortBreakSecsSpnr;
    @FXML
    private IntegerSpinnerAutoCommit pomodoroLongBreakMinsSpnr;
    @FXML
    private IntegerSpinnerAutoCommit pomodoroLongBreakSecsSpnr;
    @FXML
    private IntegerSpinnerAutoCommit pomodoroIntervalsSpnr;
    private PomodoroTimeInputFields timeFields;
    private Stage stage;
    private CountdownTimer bt;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        assert pomodoroWorkMinsSpnr != null : "fx:id\"pomodoroMinsSpnr\" was not injected. check FXML TimerView.fxml";
        assert pomodoroWorkSecsSpnr != null : "fx:id\"pomodoroSecsSpnr\" was not injected. check FXML TimerView.fxml";
        assert pomodoroShortBreakMinsSpnr != null : "fx:id\"pomodoroMinsSpnr\" was not injected. check FXML TimerView.fxml";
        assert pomodoroShortBreakSecsSpnr != null : "fx:id\"pomodoroSecsSpnr\" was not injected. check FXML TimerView.fxml";
        MinuteSecondSpinner workFields = new MinuteSecondSpinner(pomodoroWorkMinsSpnr, pomodoroWorkSecsSpnr);
        MinuteSecondSpinner shortBreakFields = new MinuteSecondSpinner(pomodoroShortBreakMinsSpnr, pomodoroShortBreakSecsSpnr);
        MinuteSecondSpinner longBreakFields = new MinuteSecondSpinner(pomodoroLongBreakMinsSpnr, pomodoroLongBreakSecsSpnr);
        assert workFields != null : "fx:id\"workFields\" was not injected. check FXML TimerView.fxml";
        assert shortBreakFields != null : "fx:id\"breakFields\" was not injected. check FXML " +
                "TimerView.fxml";
        assert longBreakFields != null : "fx:id\"breakFields\" was not injected. check FXML " +
                "TimerView.fxml";
        this.timeFields = new PomodoroTimeInputFields(workFields, shortBreakFields, longBreakFields);
    }

    public void init() {
        this.timeFields.addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observ, Integer oldV, Integer newV) {
                //TODO add PomodoroTimerManager class?
                /*bt.setInitialShortBreakTime(timeFields.getShortBreakTime());
                bt.setInitialLongBreakTime(timeFields.getLongBreakTime());
                bt.setInitialWorkTime(newV);*/
            }
        });
        pomodoroIntervalsSpnr.valueProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> obv, Integer oldvalue, Integer newValue) {
                //bt.setIntervalsUntilLongBreak(newValue);
            }
        });
    }

    public void setSettings(Stage stage, CountdownTimer bt) {
        this.stage = stage;
        this.bt = bt;
        this.init();
    }
}
