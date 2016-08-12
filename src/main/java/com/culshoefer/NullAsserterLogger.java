package com.culshoefer;

/**
 * @author Christoph Ulshoefer <christophsulshoefer@gmail.com> 12/08/16.
 */
public class NullAsserterLogger {
    public static void assertNotNull(Object a, String name) {
        assert a != null : name + " is null";

    }
}
