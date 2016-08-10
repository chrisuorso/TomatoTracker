package com.culshoefer.tomatotracker.spinnerfield;

import java.util.function.UnaryOperator;

import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.StringConverter;

/**
 * @author Christoph Ulshoefer <christophsulshoefer@gmail.com> 14/06/16.
 */
public class SpinnerAutoCommit<T> extends Spinner<T> {

    public SpinnerAutoCommit() {
        super();
        addListenerKeyChange();
    }

    public SpinnerAutoCommit(int min, int max, int initialValue) {
        super(min, max, initialValue);
        addListenerKeyChange();
    }

    public SpinnerAutoCommit(int min, int max, int initialValue, int amountToStepBy) {
        super(min, max, initialValue, amountToStepBy);
        addListenerKeyChange();
    }

    public SpinnerAutoCommit(double min, double max, double initialValue) {
        super(min, max, initialValue);
        addListenerKeyChange();
    }

    public SpinnerAutoCommit(double min, double max, double initialValue, double amountToStepBy) {
        super(min, max, initialValue, amountToStepBy);
        addListenerKeyChange();
    }

    public SpinnerAutoCommit(ObservableList<T> items) {
        super(items);
        addListenerKeyChange();
    }

    public SpinnerAutoCommit(SpinnerValueFactory<T> valueFactory) {
        super(valueFactory);
        addListenerKeyChange();
    }

    private void addListenerKeyChange() {
        TextField tf = getEditor();
        StringProperty textProp = tf.textProperty();

        textProp.addListener((observable, oldValue, newValue) -> {
            commitEditorText(oldValue);
        });
        tf.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.ENTER || keyEvent.getCode() == KeyCode.ACCEPT) {
                    UnaryOperator filter = tf.getTextFormatter().getFilter();
                    String oldValue = textProp.getValueSafe();
                    commitEditorText(oldValue);
                }
            }
        });
    }

    private void commitEditorText(String oldValue) {
        if (!isEditable()) return;
        String text = getEditor().textProperty().getValueSafe();
        SpinnerValueFactory<T> valueFactory = getValueFactory();
        if (valueFactory != null) {
            StringConverter<T> converter = valueFactory.getConverter();
            if (converter != null) {
                try {
                    T value = converter.fromString(text);
                    if (value != null) {
                        valueFactory.setValue(value);
                    }
                } catch (Exception e) {
                    T oldValueConv = converter.fromString(oldValue);
                    valueFactory.setValue(oldValueConv);
                    getEditor().setText(oldValue);
                }
            }
        }
    }

}
