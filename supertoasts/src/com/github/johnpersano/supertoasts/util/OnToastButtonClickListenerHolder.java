package com.github.johnpersano.supertoasts.util;

import android.view.View;
import android.view.View.OnClickListener;

public class OnToastButtonClickListenerHolder implements OnClickListener {

    private String mTag;
    private OnClickListener mOnClickListener;

    public OnToastButtonClickListenerHolder(String tag, OnClickListener onClickListener) {

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