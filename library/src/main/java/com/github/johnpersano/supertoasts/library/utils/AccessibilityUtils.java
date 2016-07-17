/*
 * Copyright 2013-2016 John Persano
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.johnpersano.supertoasts.library.utils;

import android.content.Context;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;

/**
 * Utility class that is used to send an {@link AccessibilityEvent}.
 */
public class AccessibilityUtils {

    /**
     * Try to send an {@link AccessibilityEvent}
     * for a {@link View}.
     *
     * @param view The View that will dispatch the AccessibilityEvent
     * @return true if the AccessibilityEvent was dispatched
     */
    @SuppressWarnings("UnusedReturnValue")
    public static boolean sendAccessibilityEvent(View view) {
        final AccessibilityManager accessibilityManager = (AccessibilityManager)
                view.getContext().getSystemService(Context.ACCESSIBILITY_SERVICE);

        if (!accessibilityManager.isEnabled()) return false;

        final AccessibilityEvent accessibilityEvent = AccessibilityEvent
                .obtain(AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED);
        accessibilityEvent.setClassName(view.getClass().getName());
        accessibilityEvent.setPackageName(view.getContext().getPackageName());

        view.dispatchPopulateAccessibilityEvent(accessibilityEvent);
        accessibilityManager.sendAccessibilityEvent(accessibilityEvent);

        return true;
    }
}