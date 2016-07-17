/*
 * Code derived from StackOverflow user Xiaochao Yang in 2013
 * Link: http://stackoverflow.com/a/15442997/1309401
 */

package com.github.johnpersano.supertoasts.demo.utils;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Used to generate view IDs for RadioButtons.
 */
public class ViewUtils {

    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);

    public static int generateViewId() {
        for (;;) {
            final int result = sNextGeneratedId.get();
            int newValue = result + 1;
            if (newValue > 0x00FFFFFF) newValue = 1;
            if (sNextGeneratedId.compareAndSet(result, newValue)) return result;
        }
    }
}