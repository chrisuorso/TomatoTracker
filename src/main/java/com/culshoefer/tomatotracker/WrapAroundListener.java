package com.culshoefer.tomatotracker;

/**
 * @author Christoph Ulshoefer <christophsulshoefer@gmail.com> 09/07/16. true if upper wrap, false
 *         if wrapped around lower boundary
 */
public interface WrapAroundListener {
    void wrappedAround(boolean isUpperWrap);
}
