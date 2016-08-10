package com.culshoefer.tomatotracker;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

/**
 * @author Christoph Ulshoefer <christophsulshoefer@gmail.com> 06/07/16.
 */
public class SwitchButton extends Button {
    @FXML
    private SimpleBooleanProperty turnedOn = new SimpleBooleanProperty(true);

    public SwitchButton() {
        boolean turnedOnByDefault = true;
        this.turnedOn.set(turnedOnByDefault);
        setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent aE) {
                turnedOn.set(!turnedOn.get());
            }
        });
    }

    public SimpleBooleanProperty getTurnedOn() {
        return this.turnedOn;
    }
}
