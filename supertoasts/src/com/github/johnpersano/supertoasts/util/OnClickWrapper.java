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

import android.os.Parcelable;
import android.view.View;
import com.github.johnpersano.supertoasts.SuperToast;

/**
 *  Class that holds a reference to an OnClickListener set to button type SuperActivityToasts/SuperCardToasts.
 *  This is used for restoring listeners on orientation changes.
 */
@SuppressWarnings("UnusedParameters")
public class OnClickWrapper implements SuperToast.OnClickListener {

    private final String mTag;
    private final SuperToast.OnClickListener mOnClickListener;
    private Parcelable mToken;

    /**
     *  Creates an OnClickWrapper.
     *
     *  @param tag {@link CharSequence} Must be unique to this listener
     *  @param onClickListener {@link com.github.johnpersano.supertoasts.SuperToast.OnClickListener}
     */
    public OnClickWrapper(String tag, SuperToast.OnClickListener onClickListener) {

        this.mTag = tag;
        this.mOnClickListener = onClickListener;

    }

    /**
     *  Returns the tag associated with this OnClickWrapper. This is used to
     *  reattach {@link com.github.johnpersano.supertoasts.SuperToast.OnClickListener}.
     *
     *  @return {@link String}
     */
    public String getTag() {

        return mTag;

    }

    /**
     *  This is used during SuperActivityToast/SuperCardToast recreation and should
     *  never be called by the developer.
     */
    public void setToken(Parcelable token) {

        this.mToken = token;

    }

    @Override
    public void onClick(View view, Parcelable token) {

        mOnClickListener.onClick(view, mToken);

    }

}