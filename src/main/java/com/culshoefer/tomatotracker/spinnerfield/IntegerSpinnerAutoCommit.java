package com.culshoefer.tomatotracker.spinnerfield;

import com.culshoefer.tomatotracker.WrapAroundListener;

import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

import javafx.beans.NamedArg;
import javafx.beans.property.StringProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.converter.IntegerStringConverter;

/**
 * @author Christoph Ulshoefer <christophsulshoefer@gmail.com> 16/06/16.
 */
public class IntegerSpinnerAutoCommit extends SpinnerAutoCommit<Integer> {
    @FXML
    private int min, max, initialValue;
    private Integer currentValue;
    private List<WrapAroundListener> listeners = new ArrayList<WrapAroundListener>();

    public IntegerSpinnerAutoCommit(@NamedArg("min") int min, @NamedArg("max") int max, @NamedArg("initialValue") int initialValue) {
        super(min, max, initialValue);
        setValues(min, max, initialValue);
        this.setDefaultSettings();
        this.addUpDownArrows();
    }

    public void setValues(int min, int max, int initialValue) {
        this.min = min;
        this.max = max;
        this.initialValue = initialValue;
        this.currentValue = initialValue;
        this.setDefaultSettings();
    }

    private void addUpDownArrows() {
        TextField tf = getEditor();
        tf.setOnKeyPressed(keyEvent -> {
            if(keyEvent.getCode().equals(KeyCode.DOWN) || keyEvent.getCode().equals(KeyCode.KP_DOWN)) {
                decrement();
            } else if (keyEvent.getCode().equals(KeyCode.UP) || keyEvent.getCode().equals(KeyCode.KP_UP)) {
                increment();
            }
        });
    }

    private void setDefaultSettings() {
        this.setDefaultValueFormatter();
        this.setEditable(true);
        SpinnerValueFactory.IntegerSpinnerValueFactory intSpinValFac = new SpinnerValueFactory.IntegerSpinnerValueFactory(
                min, max, initialValue);
        intSpinValFac.setWrapAround(true);
        this.setValueFactory(intSpinValFac);
    }

    private UnaryOperator<TextFormatter.Change> getValidNumberFilter() {
        NumberFormat format = NumberFormat.getIntegerInstance();
        UnaryOperator<TextFormatter.Change> filter = c -> {
            if (c.isContentChange()) {
                ParsePosition parsePos = new ParsePosition(0);
                Number res = format.parse(c.getControlNewText(), parsePos);
                if (c.getControlNewText().length() == 0) {
                    c.setText("0");
                    this.currentValue = 0;
                    return c;
                }
                if (res == null || parsePos.getIndex() == 0 || parsePos.getIndex() < c.getControlNewText().length()
                        || isOutOfBounds(res) || res.intValue() == this.currentValue) {
                    return null;
                }
                checkForWrapAround(res);
                alwaysReplaceValueIfZero(c);
                this.currentValue = res.intValue();
            }
            return c;
        };
        return filter;
    }

    private void alwaysReplaceValueIfZero(TextFormatter.Change c) {
        if (c.getControlText().equals("0")) {
            c.setRange(0, 1);
        }
    }

    private void checkForWrapAround(Number res) {
        if (this.isUpperWrapped(res)) {
            notifyAboutWrapAround(true);
        }
        if (this.isLowerWrapped(res)) {
            notifyAboutWrapAround(false);
        }
    }

    private boolean isLowerWrapped(Number num) {
        return (this.currentValue.equals(min) && num.intValue() == max);
    }

    private boolean isUpperWrapped(Number num) {
        return (this.currentValue.equals(max) && num.intValue() == min);
    }

    private boolean isOutOfBounds(Number num) {
        Integer raw = num.intValue();
        return raw < min || raw > max;
    }

    private TextFormatter<Integer> getFormatter() {
        UnaryOperator<TextFormatter.Change> filter = this.getValidNumberFilter();
        TextFormatter<Integer> numberFormatter = new TextFormatter<Integer>(
                new IntegerStringConverter(), this.initialValue, filter);
        return numberFormatter;
    }

    private void setDefaultValueFormatter() {
        TextFormatter<Integer> textFormatter = this.getFormatter();
        this.getEditor().setTextFormatter(textFormatter);
    }

    public void addListener(WrapAroundListener toAdd) {
        listeners.add(toAdd);
    }

    public void removeListener(WrapAroundListener toRemove) {
        listeners.remove(toRemove);
    }

    public void notifyAboutWrapAround(boolean upperWrapped) {
        for (WrapAroundListener listener : listeners) {
            listener.wrappedAround(upperWrapped);
        }
    }

}
