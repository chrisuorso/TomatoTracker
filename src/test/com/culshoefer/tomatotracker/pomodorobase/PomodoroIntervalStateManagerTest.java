package com.culshoefer.tomatotracker.pomodorobase;

import com.culshoefer.tomatotracker.pomodorobase.PomodoroIntervalStateManager;
import com.culshoefer.tomatotracker.pomodorobase.PomodoroState;

import org.junit.Before;
import org.junit.Test;

import java.util.NoSuchElementException;

import static com.culshofer.fuzzytesting.src.RandomIntegerGenerator.getRandIntWithinBounds;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * @author Christoph Ulshoefer <christophsulshoefer@gmail.com> 06/08/16.
 */
public class PomodoroIntervalStateManagerTest {
    PomodoroIntervalStateManager pim;

    @Before
    public void setUp() throws Exception {
        this.pim = new PomodoroIntervalStateManager();
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwsExceptionIfNegativeIntervalSpecification() {
        this.pim.setIntervalsGiven(-5);
    }

    @Test(expected = NoSuchElementException.class)
    public void throwsExceptionIfGettingIntervalsBeforeSettingThem() {
        PomodoroState c = this.pim.getValue();
    }

    @Test(expected = NoSuchElementException.class)
    public void throwsExceptionIfGettingNextIntervalBeforeSettingThem() {
        PomodoroState c = this.pim.nextInterval();
    }

    @Test
    public void everySecondIntervalIsWork() {
        this.pim.setIntervalsGiven(getRandIntWithinBounds(0, 100));
        assertThat(this.pim.getValue(), is(PomodoroState.WORK));
        for(int i = 0; i < getRandIntWithinBounds(10, 400); i++){
            this.pim.nextInterval();
            assertThat(this.pim.nextInterval(), is(PomodoroState.WORK));
        }
    }

    @Test
    public void ifZeroIntervalsThereIsOnlyBreak() {
        this.pim.setIntervalsGiven(0);
        for(int i = 0; i < 100; i++)
            assertThat(this.pim.nextInterval(), is(PomodoroState.LONGBREAK));
    }

    @Test
    public void ifMoreThanZeroIntervalsThenShortBreaksUntilOneLongBreak() {
        int numInts = getRandIntWithinBounds(0, 100);
        this.pim.setIntervalsGiven(numInts);
        for(int j = 0; j < 5; j++) {
            for (int i = 1; i < numInts; i++) {
                assertThat(this.pim.nextInterval(), is(PomodoroState.SHORTBREAK));
                this.pim.nextInterval(); //which will be work
            }
            assertThat(this.pim.nextInterval(), is(PomodoroState.LONGBREAK));
            this.pim.nextInterval(); //to go back to work
        }
    }

}