/**
 *  Copyright 2014 John Persano
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *	you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *	Unless required by applicable law or agreed to in writing, software
 *	distributed under the License is distributed on an "AS IS" BASIS,
 *	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *	See the License for the specific language governing permissions and
 *	limitations under the License.
 *
 */

package com.github.johnpersano.supertoasts.util;

import android.view.View;
import com.github.johnpersano.supertoasts.SuperToast;

/**
 *  Class that holds a reference to OnDismissListeners set to button type SuperActivityToasts/SuperCardToasts.
 *  This is used for restoring listeners on orientation changes.
 */
public class OnDismissWrapper implements SuperToast.OnDismissListener {

    private final String mTag;
    private final SuperToast.OnDismissListener mOnDismissListener;

    /**
     *  Creates an OnClickWrapper.
     *
     *  @param tag {@link CharSequence} Must be unique to this listener
     *  @param onDismissListener {@link com.github.johnpersano.supertoasts.SuperToast.OnDismissListener}
     */
    public OnDismissWrapper(String tag, SuperToast.OnDismissListener onDismissListener) {

        this.mTag = tag;
        this.mOnDismissListener = onDismissListener;

    }

    /**
     *  Returns the tag associated with this OnDismissWrapper. This is used to
     *  reattach {@link com.github.johnpersano.supertoasts.SuperToast.OnDismissListener}s.
     *
     *  @return {@link String}
     */
    public String getTag() {

        return mTag;

    }

    @Override
    public void onDismiss(View view) {

        mOnDismissListener.onDismiss(view);

    }

}