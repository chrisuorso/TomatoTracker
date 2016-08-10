package com.culshofer.fuzzytesting.test;

import org.hamcrest.Matcher;
import org.junit.Test;

import static org.testinfected.hamcrest.AbstractMatcherTest.*;
import static com.culshofer.fuzzytesting.src.IntegerIsCongruentModulo.isCongruentModulo;
import static org.junit.Assert.*;

/**
 * @author Christoph Ulshoefer <christophsulshoefer@gmail.com> 06/08/16.
 */
public class IntegerIsCongruentModuloTest {
    private final Matcher<Integer> matcher = isCongruentModulo(5, 3);

    protected Matcher<?> createMatcher() {
        Integer tautological = 3;
        return isCongruentModulo(tautological, tautological);
    }

    @Test
    public void evaluatesToTrueIfArgumentIsCongruentToFirstIntModuloSecondInt() {
        assertTrue(matcher.matches(2));
        assertTrue(matcher.matches(-10));
        assertTrue(matcher.matches(5));
        assertTrue(matcher.matches(14));

        assertDoesNotMatch("Not in equivalence class", matcher, 3);
        assertMismatchDescription("<3> is not equivalent to <5> modulo <3>", matcher, 3);
        assertDoesNotMatch("Not in equivalence class", matcher, 13);
        assertMismatchDescription("<13> is not equivalent to <5> modulo <3>", matcher, 13);
        assertDoesNotMatch("Not in equivalence class", matcher, -11);
        assertMismatchDescription("<-11> is not equivalent to <5> modulo <3>", matcher, -11);
        assertDoesNotMatch("Not in equivalence class", matcher, -3);
        assertMismatchDescription("<-3> is not equivalent to <5> modulo <3>", matcher, -3);
    }

    @Test
    public void test_is_self_describing() {
        assertDescription("An integer that is equivalent to <5> modulo <3>", matcher);
    }
}