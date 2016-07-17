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

import com.github.johnpersano.supertoasts.library.SuperActivityToast;
import com.github.johnpersano.supertoasts.library.SuperToast;

import java.util.HashMap;

/**
 * Utility class used to hold references to various listeners for orientation change recreation.
 */
@SuppressWarnings("UnusedDeclaration")
public class ListenerUtils {

    private final HashMap<String, SuperToast.OnDismissListener>
            mOnDismissListenerHashMap = new HashMap<>();
    private final HashMap<String, SuperActivityToast.OnButtonClickListener>
            mOnButtonClickListenerHashMap = new HashMap<>();

    /**
     * Returns a new instance of the {@link ListenerUtils}
     * class.
     *
     * @return {@link ListenerUtils}
     */
    public static ListenerUtils newInstance() {
        return new ListenerUtils();
    }

    /**
     * Add either an {@link SuperToast.OnDismissListener}
     * or a {@link SuperActivityToast.OnButtonClickListener}
     * to a stored HashMap along with a String tag. This is used to reattach listeners to a
     * {@link SuperActivityToast} when recreated from an
     * orientation change.
     *
     * @param tag A unique tag for the listener
     * @param onDismissListener The listener to be reattached
     * @return The current instance of the {@link ListenerUtils}
     */
    public ListenerUtils putListener(String tag, SuperToast.OnDismissListener onDismissListener) {
        this.mOnDismissListenerHashMap.put(tag, onDismissListener);
        return this;
    }

    /**
     * Add either an {@link SuperToast.OnDismissListener}
     * or a {@link SuperActivityToast.OnButtonClickListener}
     * to a stored HashMap along with a String tag. This is used to reattach listeners to a
     * {@link SuperActivityToast} when recreated from an
     * orientation change.
     *
     * @param tag A unique tag for the listener
     * @param onButtonClickListener The listener to be reattached
     * @return The current instance of the {@link ListenerUtils}
     */
    public ListenerUtils putListener(String tag, SuperActivityToast
            .OnButtonClickListener onButtonClickListener) {
        this.mOnButtonClickListenerHashMap.put(tag, onButtonClickListener);
        return this;
    }

    /**
     * Returns the {@link SuperToast.OnDismissListener} HashMap.
     *
     * @return The current HashMap<String, SuperToast.OnDismissListener>
     */
    public HashMap<String, SuperToast.OnDismissListener> getOnDismissListenerHashMap() {
        return this.mOnDismissListenerHashMap;
    }

    /**
     * Returns the {@link SuperActivityToast.OnButtonClickListener} HashMap.
     *
     * @return The current HashMap<String, SuperActivityToast.OnButtonClickListener>
     */
    public HashMap<String, SuperActivityToast
            .OnButtonClickListener> getOnButtonClickListenerHashMap() {
        return this.mOnButtonClickListenerHashMap;
    }
}
