package com.github.johnpersano.supertoasts.util;

public class OnToastDismissListenerHolder implements OnToastDismissListener {

    private String mTag;
    private OnToastDismissListener mOnToastDismissListener;

    public OnToastDismissListenerHolder(String tag, OnToastDismissListener onToastDismissListener) {

        this.mTag = tag;
        this.mOnToastDismissListener = onToastDismissListener;

    }

    public String getTag() {

        return mTag;

    }

    @Override
    public void onDismiss() {

        mOnToastDismissListener.onDismiss();

    }

}