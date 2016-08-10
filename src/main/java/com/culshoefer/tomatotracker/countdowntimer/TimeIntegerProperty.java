package com.culshoefer.tomatotracker.countdowntimer;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * @author Christoph Ulshoefer <christophsulshoefer@gmail.com> 14/06/16.
 */
public class TimeIntegerProperty extends SimpleIntegerProperty {

    TimeIntegerProperty() {
        super(Integer.MAX_VALUE);
    }

    TimeIntegerProperty(Integer seconds) {
        super(seconds);
    }

    TimeIntegerProperty(Integer minutes, Integer seconds) {
        super(minutes * 60 + seconds);
    }

    private String getMinTwoDigits(Integer num) {
        Integer absNum = Math.abs(num);
        if (absNum >= 10) {
            return Integer.toString(num);
        } else {
            if (num < 0) {
                return num.toString();
            } else {
                return '0' + num.toString();
            }
        }
    }

    public int getSeconds() {
        return this.get() % 60;
    }

    public int getMinutes() {
        return Math.floorDiv(this.get(), 60);
    }

    public String getSecondsTwoDigits() {
        return getMinTwoDigits(this.getSeconds());
    }

    public String getMinutesTwoDigits() {
        return getMinTwoDigits(this.getMinutes());
    }

    public StringExpression formatMinutes() {
        String strMinutes = this.getMinutesTwoDigits();
        String strSeconds = this.getSecondsTwoDigits();
        return Bindings.concat(strMinutes, " : ", strSeconds);
    }
}
