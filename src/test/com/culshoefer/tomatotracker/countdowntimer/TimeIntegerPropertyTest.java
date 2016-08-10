package com.culshoefer.tomatotracker.countdowntimer;

import org.junit.Before;
import org.junit.Test;

import java.util.Random;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * @author Christoph Ulshoefer <christophsulshoefer@gmail.com> 30/07/16.
 */
public class TimeIntegerPropertyTest {

  TimeIntegerProperty tiOneArg;
  TimeIntegerProperty tiTwoArgs;

  @Before
  public void setUp() {
    int secs = Math.abs(getInt());
    this.tiOneArg = new TimeIntegerProperty(secs);
    this.tiTwoArgs = new TimeIntegerProperty(Math.floorDiv(secs, 60), secs % 60);
  }

  @Test
  public void bothConstructorsMakeNoDifference() {
    assertThat(tiOneArg.getValue().toString(), equalTo(tiTwoArgs.getValue().toString()));
  }

  @Test
  public void secondsAreMinTwoDigitsSmallNumber() {
    tiOneArg.set(getPositiveIntWithNumDigits(1));
    String formattedMinutes = tiOneArg.getSecondsTwoDigits();
    assertThat(formattedMinutes.length(), equalTo(2));
  }

  @Test
  public void secondsAreMinTwoDigitsNegativeNumber() {
    tiOneArg.set(- getPositiveIntWithNumDigits(1));
    String formattedMinutes = tiOneArg.getSecondsTwoDigits();
    assertThat(formattedMinutes.length(), equalTo(2));
  }

  @Test
  public void minutesAreMinTwoDigitsSmallNumber() {
    tiOneArg.set(getRandIntWithinBounds(59, 600));
    String formattedMinutes = tiOneArg.getMinutesTwoDigits();
    assertThat(formattedMinutes.length(), equalTo(2));
  }

  @Test
  public void minutesAreMinTwoDigitsNegativeNumber() {
    tiOneArg.set(getRandIntWithinBounds(-59, -600));
    String formattedMinutes = tiOneArg.getMinutesTwoDigits();
    assertThat(formattedMinutes.length(), equalTo(2));
  }

  @Test
  public void formattedMinutesIsCorrectlyFormatted() {
    String mins = tiOneArg.getMinutesTwoDigits();
    String secs = tiOneArg.getSecondsTwoDigits();
    assertThat(tiOneArg.formatMinutes().getValue(), equalTo(mins + " : " + secs));
  }

  private int getInt() {
    return new Random().nextInt();
  }

  private int getRandIntWithinBounds(int tooLow, int tooBig){
    int ret = getInt() % tooBig;
    while(ret <= tooLow) {
      ret = getInt() % tooBig;
    }
    return ret;
  }

  private int getPositiveIntWithNumDigits(int numDigits) {
    int tooSmall = (numDigits - 1) * 10;
    int tooBig = numDigits * 10;
    return getRandIntWithinBounds(tooSmall - 1, tooBig);
  }

}