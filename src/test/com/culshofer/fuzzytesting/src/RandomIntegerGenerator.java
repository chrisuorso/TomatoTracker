package com.culshofer.fuzzytesting.src;

import java.util.Random;

/**
 * Provides a suite of random functions to be used for fuzzy testing
 * @author Christoph Ulshoefer <christophsulshoefer@gmail.com> 06/08/16.
 * TODO make static?
 */
public class RandomIntegerGenerator {

    private static Random getGen() {
        return new Random();
    }

    /**
     * @return an integer greater or equal 0
     */
    public static int getPositiveInt(){
        return Math.abs(getGen().nextInt());
    }

    /**
     *
     * @return an integer smaller or equal 0
     */
    public static int getNegativeInt() {
        return - getPositiveInt();
    }

    public static int getInt() {
        return getGen().nextInt();
    }

    /**
     * Returns an int that fits into boundaries. It continuously generates random numbers modulo the
     * big number until the generated number fits within the specified boundaries
     * @param tooLow The lower boundary to be set. The value given here is just too low,
     *               i.e. the returned value will always be bigger than the specified value
     * @param tooBig The upper boundary to be set.
     *               The returned value will always be smaller than this value
     * @return An integer that fits within the specified boundaries
     * @throws IllegalArgumentException If the upper boundary lies below/is equal
     * with the lower boundary, then this exception is thrown
     */
    public static int getRandIntWithinBounds(int tooLow, int tooBig) throws IllegalArgumentException{
        if(tooBig <= tooLow){
            throw new IllegalArgumentException("No value lies within the specified boundaries");
        }
        int ret = getInt() % tooBig;
        while(ret <= tooLow) {
            ret = getInt() % tooBig;
        }
        return ret;
    }
}
