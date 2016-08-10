package com.culshofer.fuzzytesting.src;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.math.BigInteger;

/**
 * @author Christoph Ulshoefer <christophsulshoefer@gmail.com> 06/08/16.
 */
public class IntegerIsCongruentModulo extends TypeSafeMatcher<Integer> {
    private final Integer value;
    private final Integer modulo;

    public IntegerIsCongruentModulo(Integer value, Integer modulo) {
        this.value = value;
        this.modulo = modulo;
    }

    @Override
    public boolean matchesSafely(Integer candidate) {
        return (value - candidate) % modulo == 0;
    }

    @Override
    public void describeMismatchSafely(Integer candidate, Description mismatchDescription) {
        mismatchDescription.appendValue(candidate)
                .appendText(" is not equivalent to ")
                .appendValue(value)
                .appendText(" modulo ")
                .appendValue(modulo);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("An integer that is equivalent to ")
                .appendValue(value)
                .appendText(" modulo ")
                .appendValue(modulo);
    }

    //TODO document
    public static Matcher<Integer> isCongruentModulo(Integer candidate, Integer modulo) {
        return new IntegerIsCongruentModulo(candidate, modulo);
    }
}
