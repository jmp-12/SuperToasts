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
import android.view.View.OnClickListener;

/**
 *  Class that holds a reference to OnClickListeners set to button type SuperActivityToasts/SuperCardToasts.
 *  This is used for restoring listeners on orientation changes.
 */
public class OnClickListenerWrapper implements OnClickListener {

    private final String mTag;
    private final OnClickListener mOnClickListener;

    public OnClickListenerWrapper(String tag, OnClickListener onClickListener) {

        this.mTag = tag;
        this.mOnClickListener = onClickListener;

    }

    public String getTag() {

        return mTag;

    }


    @Override
    public void onClick(View view) {

        mOnClickListener.onClick(view);

    }

}