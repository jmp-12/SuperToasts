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

package com.github.johnpersano.supertoasts;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.util.TypedValue;
import android.view.*;
import android.view.View.OnTouchListener;
import android.widget.*;
import com.github.johnpersano.supertoasts.SuperToast.Animations;
import com.github.johnpersano.supertoasts.SuperToast.IconPosition;
import com.github.johnpersano.supertoasts.SuperToast.Type;
import com.github.johnpersano.supertoasts.util.OnClickWrapper;
import com.github.johnpersano.supertoasts.util.OnDismissWrapper;
import com.github.johnpersano.supertoasts.util.Style;
import com.github.johnpersano.supertoasts.util.Wrappers;

import java.util.LinkedList;


/**
 * SuperActivityToasts are designed to be used inside of Activities. When the
 * Activity is destroyed the SuperActivityToast is destroyed along with it.
 */
@SuppressWarnings({"UnusedDeclaration", "BooleanMethodIsAlwaysInverted", "ConstantConditions"})
public class SuperActivityToast {

    private static final String TAG = "SuperActivityToast";
    private static final String MANAGER_TAG = "SuperActivityToast Manager";

    private static final String ERROR_ACTIVITYNULL = " - You cannot pass a null Activity as a parameter.";
    private static final String ERROR_NOTBUTTONTYPE = " - is only compatible with BUTTON type SuperActivityToasts.";
    private static final String ERROR_NOTPROGRESSHORIZONTALTYPE = " - is only compatible with PROGRESS_HORIZONTAL type SuperActivityToasts.";
    private static final String ERROR_NOTEITHERPROGRESSTYPE = " - is only compatible with PROGRESS_HORIZONTAL or PROGRESS type SuperActivityToasts.";

    /* Bundle tag with a hex as a string so it can't interfere with other tags in the bundle */
    private static final String BUNDLE_TAG = "0x532e412e542e";

    private Activity mActivity;
    private Animations mAnimations = Animations.FADE;
    private boolean mIsIndeterminate;
    private boolean mIsTouchDismissible;
    private boolean isProgressIndeterminate;
    private boolean showImmediate;
    private Button mButton;
    private IconPosition mIconPosition;
    private int mDuration = SuperToast.Duration.SHORT;
    private int mBackground = Style.getBackground(Style.GRAY);
    private int mButtonIcon = SuperToast.Icon.Dark.UNDO;
    private int mDividerColor = Color.LTGRAY;
    private int mIcon;
    private int mTypefaceStyle = Typeface.NORMAL;
    private int mButtonTypefaceStyle = Typeface.BOLD;
    private LayoutInflater mLayoutInflater;
    private LinearLayout mRootLayout;
    private OnDismissWrapper mOnDismissWrapper;
    private OnClickWrapper mOnClickWrapper;
    private Parcelable mToken;
    private ProgressBar mProgressBar;
    private String mOnClickWrapperTag;
    private String mOnDismissWrapperTag;
    private TextView mMessageTextView;
    private Type mType = Type.STANDARD;
    private View mDividerView;
    private ViewGroup mViewGroup;
    private View mToastView;

    /**
     * Instantiates a new {@value #TAG}.
     *
     * @param activity {@link android.app.Activity}
     */
    public SuperActivityToast(Activity activity) {

        if (activity == null) {

            throw new IllegalArgumentException(TAG + ERROR_ACTIVITYNULL);

        }

        this.mActivity = activity;

        mLayoutInflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mViewGroup = (ViewGroup) activity
                .findViewById(android.R.id.content);

        mToastView = mLayoutInflater.inflate(R.layout.supertoast,
                mViewGroup, false);

        mMessageTextView = (TextView) mToastView
                .findViewById(R.id.message_textview);

        mRootLayout = (LinearLayout) mToastView
                .findViewById(R.id.root_layout);

    }

    /**
     * Instantiates a new {@value #TAG} with a specified style.
     *
     * @param activity {@link android.app.Activity}
     * @param style    {@link com.github.johnpersano.supertoasts.util.Style}
     */
    public SuperActivityToast(Activity activity, Style style) {

        if (activity == null) {

            throw new IllegalArgumentException(TAG + ERROR_ACTIVITYNULL);

        }

        this.mActivity = activity;

        mLayoutInflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mViewGroup = (ViewGroup) activity
                .findViewById(android.R.id.content);

        mToastView = mLayoutInflater.inflate(R.layout.supertoast,
                mViewGroup, false);

        mMessageTextView = (TextView) mToastView
                .findViewById(R.id.message_textview);

        mRootLayout = (LinearLayout) mToastView
                .findViewById(R.id.root_layout);

        this.setStyle(style);

    }

    /**
     * Instantiates a new {@value #TAG} with a type.
     *
     * @param activity {@link android.app.Activity}
     * @param type     {@link com.github.johnpersano.supertoasts.SuperToast.Type}
     */
    public SuperActivityToast(Activity activity, Type type) {

        if (activity == null) {

            throw new IllegalArgumentException(TAG + ERROR_ACTIVITYNULL);

        }

        this.mActivity = activity;
        this.mType = type;

        mLayoutInflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mViewGroup = (ViewGroup) activity
                .findViewById(android.R.id.content);

        if (type == Type.STANDARD) {

            mToastView = mLayoutInflater.inflate(
                    R.layout.supertoast, mViewGroup, false);

        } else if (type == Type.BUTTON) {

            mToastView = mLayoutInflater.inflate(
                    R.layout.superactivitytoast_button, mViewGroup, false);

            mButton = (Button) mToastView
                    .findViewById(R.id.button);

            mDividerView = mToastView
                    .findViewById(R.id.divider);

            mButton.setOnClickListener(mButtonListener);

        } else if (type == Type.PROGRESS) {

            mToastView = mLayoutInflater.inflate(R.layout.superactivitytoast_progresscircle,
                    mViewGroup, false);

            mProgressBar = (ProgressBar) mToastView
                    .findViewById(R.id.progress_bar);

        } else if (type == Type.PROGRESS_HORIZONTAL) {

            mToastView = mLayoutInflater.inflate(R.layout.superactivitytoast_progresshorizontal,
                    mViewGroup, false);

            mProgressBar = (ProgressBar) mToastView
                    .findViewById(R.id.progress_bar);

        }

        mMessageTextView = (TextView) mToastView
                .findViewById(R.id.message_textview);

        mRootLayout = (LinearLayout) mToastView
                .findViewById(R.id.root_layout);

    }

    /**
     * Instantiates a new {@value #TAG} with a type and a specified style.
     *
     * @param activity {@link android.app.Activity}
     * @param type     {@link com.github.johnpersano.supertoasts.SuperToast.Type}
     * @param style    {@link com.github.johnpersano.supertoasts.util.Style}
     */
    public SuperActivityToast(Activity activity, Type type, Style style) {

        if (activity == null) {

            throw new IllegalArgumentException(TAG + ERROR_ACTIVITYNULL);

        }

        this.mActivity = activity;
        this.mType = type;

        mLayoutInflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mViewGroup = (ViewGroup) activity
                .findViewById(android.R.id.content);

        if (type == Type.STANDARD) {

            mToastView = mLayoutInflater.inflate(
                    R.layout.supertoast, mViewGroup, false);

        } else if (type == Type.BUTTON) {

            mToastView = mLayoutInflater.inflate(
                    R.layout.superactivitytoast_button, mViewGroup, false);

            mButton = (Button) mToastView
                    .findViewById(R.id.button);

            mDividerView = mToastView
                    .findViewById(R.id.divider);

            mButton.setOnClickListener(mButtonListener);

        } else if (type == Type.PROGRESS) {

            mToastView = mLayoutInflater.inflate(R.layout.superactivitytoast_progresscircle,
                    mViewGroup, false);

            mProgressBar = (ProgressBar) mToastView
                    .findViewById(R.id.progress_bar);

        } else if (type == Type.PROGRESS_HORIZONTAL) {

            mToastView = mLayoutInflater.inflate(R.layout.superactivitytoast_progresshorizontal,
                    mViewGroup, false);

            mProgressBar = (ProgressBar) mToastView
                    .findViewById(R.id.progress_bar);

        }

        mMessageTextView = (TextView) mToastView
                .findViewById(R.id.message_textview);

        mRootLayout = (LinearLayout) mToastView
                .findViewById(R.id.root_layout);

        this.setStyle(style);

    }

    /**
     * Shows the {@value #TAG}. If another {@value #TAG} is showing than
     * this one will be added to a queue and shown when the previous {@value #TAG}
     * is dismissed.
     */
    public void show() {

        ManagerSuperActivityToast.getInstance().add(this);

    }

    /**
     * Returns the type of the {@value #TAG}.
     *
     * @return {@link com.github.johnpersano.supertoasts.SuperToast.Type}
     */
    public Type getType() {

        return mType;

    }

    /**
     * Sets the message text of the {@value #TAG}.
     *
     * @param text {@link CharSequence}
     */
    public void setText(CharSequence text) {

        mMessageTextView.setText(text);

    }

    /**
     * Returns the message text of the {@value #TAG}.
     *
     * @return {@link CharSequence}
     */
    public CharSequence getText() {

        return mMessageTextView.getText();

    }

    /**
     * Sets the message typeface style of the {@value #TAG}.
     *
     * @param typeface {@link android.graphics.Typeface} int
     */
    public void setTypefaceStyle(int typeface) {

        mTypefaceStyle = typeface;

        mMessageTextView.setTypeface(mMessageTextView.getTypeface(), typeface);

    }

    /**
     * Returns the message typeface style of the {@value #TAG}.
     *
     * @return {@link android.graphics.Typeface} int
     */
    public int getTypefaceStyle() {

        return mTypefaceStyle;

    }

    /**
     * Sets the message text color of the {@value #TAG}.
     *
     * @param textColor {@link android.graphics.Color}
     */
    public void setTextColor(int textColor) {

        mMessageTextView.setTextColor(textColor);

    }

    /**
     * Returns the message text color of the {@value #TAG}.
     *
     * @return int
     */
    public int getTextColor() {

        return mMessageTextView.getCurrentTextColor();

    }

    /**
     * Sets the text size of the {@value #TAG} message.
     *
     * @param textSize int
     */
    public void setTextSize(int textSize) {

        mMessageTextView.setTextSize(textSize);

    }

    /**
     * Used by orientation change recreation
     */
    private void setTextSizeFloat(float textSize) {

        mMessageTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);

    }

    /**
     * Returns the text size of the {@value #TAG} message in pixels.
     *
     * @return float
     */
    public float getTextSize() {

        return mMessageTextView.getTextSize();

    }

    /**
     * Sets the duration that the {@value #TAG} will show.
     *
     * @param duration {@link com.github.johnpersano.supertoasts.SuperToast.Duration}
     */
    public void setDuration(int duration) {

        this.mDuration = duration;

    }

    /**
     * Returns the duration of the {@value #TAG}.
     *
     * @return int
     */
    public int getDuration() {

        return this.mDuration;

    }

    /**
     * If true will show the {@value #TAG} for an indeterminate time period and ignore any set duration.
     *
     * @param isIndeterminate boolean
     */
    public void setIndeterminate(boolean isIndeterminate) {

        this.mIsIndeterminate = isIndeterminate;

    }

    /**
     * Returns true if the {@value #TAG} is indeterminate.
     *
     * @return boolean
     */
    public boolean isIndeterminate() {

        return this.mIsIndeterminate;

    }

    /**
     * Sets an icon resource to the {@value #TAG} with a specified position.
     *
     * @param iconResource {@link com.github.johnpersano.supertoasts.SuperToast.Icon}
     * @param iconPosition {@link com.github.johnpersano.supertoasts.SuperToast.IconPosition}
     */
    public void setIcon(int iconResource, IconPosition iconPosition) {

        this.mIcon = iconResource;
        this.mIconPosition = iconPosition;

        if (iconPosition == IconPosition.BOTTOM) {

            mMessageTextView.setCompoundDrawablesWithIntrinsicBounds(null, null,
                    null, mActivity.getResources().getDrawable(iconResource));

        } else if (iconPosition == IconPosition.LEFT) {

            mMessageTextView.setCompoundDrawablesWithIntrinsicBounds(mActivity.getResources()
                    .getDrawable(iconResource), null, null, null);

        } else if (iconPosition == IconPosition.RIGHT) {

            mMessageTextView.setCompoundDrawablesWithIntrinsicBounds(null, null,
                    mActivity.getResources().getDrawable(iconResource), null);

        } else if (iconPosition == IconPosition.TOP) {

            mMessageTextView.setCompoundDrawablesWithIntrinsicBounds(null,
                    mActivity.getResources().getDrawable(iconResource), null, null);

        }

    }

    /**
     * Returns the icon position of the {@value #TAG}.
     *
     * @return {@link com.github.johnpersano.supertoasts.SuperToast.IconPosition}
     */
    public IconPosition getIconPosition() {

        return this.mIconPosition;

    }

    /**
     * Returns the icon resource of the {@value #TAG}.
     *
     * @return int
     */
    public int getIconResource() {

        return this.mIcon;

    }

    /**
     * Sets the background resource of the {@value #TAG}.
     *
     * @param background {@link com.github.johnpersano.supertoasts.SuperToast.Background}
     */
    public void setBackground(int background) {

        this.mBackground = background;

        mRootLayout.setBackgroundResource(background);

    }

    /**
     * Returns the background resource of the {@value #TAG}.
     *
     * @return int
     */
    public int getBackground() {

        return this.mBackground;

    }

    /**
     * Sets the show/hide animations of the {@value #TAG}.
     *
     * @param animations {@link com.github.johnpersano.supertoasts.SuperToast.Animations}
     */
    public void setAnimations(Animations animations) {

        this.mAnimations = animations;

    }

    /**
     * Returns the show/hide animations of the {@value #TAG}.
     *
     * @return {@link com.github.johnpersano.supertoasts.SuperToast.Animations}
     */
    public Animations getAnimations() {

        return this.mAnimations;

    }

    /**
     * If true will show the {@value #TAG} without animation.
     *
     * @param showImmediate boolean
     */
    public void setShowImmediate(boolean showImmediate) {

        this.showImmediate = showImmediate;
    }

    /**
     * Returns true if the {@value #TAG} is set to show without animation.
     *
     * @return boolean
     */
    public boolean getShowImmediate() {

        return this.showImmediate;

    }

    /**
     * If true will dismiss the {@value #TAG} if the user touches it.
     *
     * @param touchDismiss boolean
     */
    public void setTouchToDismiss(boolean touchDismiss) {

        this.mIsTouchDismissible = touchDismiss;

        if (touchDismiss) {

            mToastView.setOnTouchListener(mTouchDismissListener);

        } else {

            mToastView.setOnTouchListener(null);

        }

    }

    /**
     * Returns true if the {@value #TAG} is touch dismissible.
     */
    public boolean isTouchDismissible() {

        return this.mIsTouchDismissible;

    }

    /**
     * Sets an OnDismissWrapper defined in this library
     * to the {@value #TAG}.
     *
     * @param onDismissWrapper {@link com.github.johnpersano.supertoasts.util.OnDismissWrapper}
     */
    public void setOnDismissWrapper(OnDismissWrapper onDismissWrapper) {

        this.mOnDismissWrapper = onDismissWrapper;
        this.mOnDismissWrapperTag = onDismissWrapper.getTag();

    }

    /**
     * Used in {@value #MANAGER_TAG}.
     */
    protected OnDismissWrapper getOnDismissWrapper() {

        return this.mOnDismissWrapper;

    }

    /**
     * Used in orientation change recreation.
     */
    private String getOnDismissWrapperTag() {

        return this.mOnDismissWrapperTag;

    }

    /**
     * Dismisses the {@value #TAG}.
     */
    public void dismiss() {

        ManagerSuperActivityToast.getInstance().removeSuperToast(this);

    }

    /**
     * Sets an OnClickWrapper to the button in a BUTTON
     * {@link com.github.johnpersano.supertoasts.SuperToast.Type} {@value #TAG}.
     *
     * @param onClickWrapper {@link com.github.johnpersano.supertoasts.util.OnClickWrapper}
     */
    public void setOnClickWrapper(OnClickWrapper onClickWrapper) {

        if (mType != Type.BUTTON) {

            Log.e(TAG, "setOnClickListenerWrapper()" + ERROR_NOTBUTTONTYPE);

        }

        this.mOnClickWrapper = onClickWrapper;
        this.mOnClickWrapperTag = onClickWrapper.getTag();

    }

    /**
     * Sets an OnClickWrapper with a parcelable object to the button in a BUTTON
     * {@link com.github.johnpersano.supertoasts.SuperToast.Type} {@value #TAG}.
     *
     * @param onClickWrapper {@link com.github.johnpersano.supertoasts.util.OnClickWrapper}
     * @param token {@link android.os.Parcelable}
     */
    public void setOnClickWrapper(OnClickWrapper onClickWrapper, Parcelable token) {

        if (mType != Type.BUTTON) {

            Log.e(TAG, "setOnClickListenerWrapper()" + ERROR_NOTBUTTONTYPE);

        }

        onClickWrapper.setToken(token);

        this.mToken = token;
        this.mOnClickWrapper = onClickWrapper;
        this.mOnClickWrapperTag = onClickWrapper.getTag();

    }

    /**
     * Used in orientation change recreation.
     */
    private Parcelable getToken(){

        return this.mToken;

    }

    /**
     * Used in orientation change recreation.
     */
    private String getOnClickWrapperTag() {

        return this.mOnClickWrapperTag;

    }

    /**
     * Sets the icon resource of the button in a BUTTON
     * {@link com.github.johnpersano.supertoasts.SuperToast.Type} {@value #TAG}.
     *
     * @param buttonIcon {@link com.github.johnpersano.supertoasts.SuperToast.Icon}
     */
    public void setButtonIcon(int buttonIcon) {

        if (mType != Type.BUTTON) {

            Log.e(TAG, "setButtonIcon()" + ERROR_NOTBUTTONTYPE);

        }

        this.mButtonIcon = buttonIcon;

        if (mButton != null) {

            mButton.setCompoundDrawablesWithIntrinsicBounds(mActivity
                    .getResources().getDrawable(buttonIcon), null, null, null);

        }

    }

    /**
     * Sets the icon resource and text of the button in
     * a BUTTON {@link com.github.johnpersano.supertoasts.SuperToast.Type} {@value #TAG}.
     *
     * @param buttonIcon {@link com.github.johnpersano.supertoasts.SuperToast.Icon}
     * @param buttonText {@link CharSequence}
     */
    public void setButtonIcon(int buttonIcon, CharSequence buttonText) {

        if (mType != Type.BUTTON) {

            Log.w(TAG, "setButtonIcon()" + ERROR_NOTBUTTONTYPE);

        }

        this.mButtonIcon = buttonIcon;

        if (mButton != null) {

            mButton.setCompoundDrawablesWithIntrinsicBounds(mActivity
                    .getResources().getDrawable(buttonIcon), null, null, null);

            mButton.setText(buttonText);

        }

    }

    /**
     * Returns the icon resource of the button in
     * {@link com.github.johnpersano.supertoasts.SuperToast.Type} {@value #TAG}.
     *
     * @return int
     */
    public int getButtonIcon() {

        return this.mButtonIcon;

    }

    /**
     * Sets the divider color of a BUTTON
     * {@link com.github.johnpersano.supertoasts.SuperToast.Type} {@value #TAG}.
     *
     * @param dividerColor int
     */
    public void setDividerColor(int dividerColor) {

        if (mType != Type.BUTTON) {

            Log.e(TAG, "setDivider()" + ERROR_NOTBUTTONTYPE);

        }

        this.mDividerColor = dividerColor;

        if (mDividerView != null) {

            mDividerView.setBackgroundColor(dividerColor);

        }

    }

    /**
     * Returns the divider color of a BUTTON
     * {@link com.github.johnpersano.supertoasts.SuperToast.Type} {@value #TAG}.
     *
     * @return int
     */
    public int getDividerColor() {

        return this.mDividerColor;

    }

    /**
     * Sets the button text of a BUTTON
     * {@link com.github.johnpersano.supertoasts.SuperToast.Type} {@value #TAG}.
     *
     * @param buttonText {@link CharSequence}
     */
    public void setButtonText(CharSequence buttonText) {

        if (mType != Type.BUTTON) {

            Log.e(TAG, "setButtonText()" + ERROR_NOTBUTTONTYPE);

        }

        if (mButton != null) {

            mButton.setText(buttonText);

        }

    }

    /**
     * Returns the button text of a BUTTON
     * {@link com.github.johnpersano.supertoasts.SuperToast.Type} {@value #TAG}.
     *
     * @return {@link CharSequence}
     */
    public CharSequence getButtonText() {

        if(mButton != null) {

            return mButton.getText();

        } else {

            Log.e(TAG, "getButtonText()" + ERROR_NOTBUTTONTYPE);

            return "";

        }

    }

    /**
     * Sets the typeface style of the button in a BUTTON
     * {@link com.github.johnpersano.supertoasts.SuperToast.Type} {@value #TAG}.
     *
     * @param typefaceStyle {@link android.graphics.Typeface}
     */
    public void setButtonTypefaceStyle(int typefaceStyle) {

        if (mType != Type.BUTTON) {

            Log.e(TAG, "setButtonTypefaceStyle()" + ERROR_NOTBUTTONTYPE);

        }

        if (mButton != null) {

            mButtonTypefaceStyle = typefaceStyle;

            mButton.setTypeface(mButton.getTypeface(), typefaceStyle);

        }

    }

    /**
     * Returns the typeface style of the button in a BUTTON
     * {@link com.github.johnpersano.supertoasts.SuperToast.Type} {@value #TAG}.
     *
     * @return int
     */
    public int getButtonTypefaceStyle() {

        return this.mButtonTypefaceStyle;

    }

    /**
     * Sets the button text color of a BUTTON
     * {@link com.github.johnpersano.supertoasts.SuperToast.Type} {@value #TAG}.
     *
     * @param buttonTextColor {@link android.graphics.Color}
     */
    public void setButtonTextColor(int buttonTextColor) {

        if (mType != Type.BUTTON) {

            Log.e(TAG, "setButtonTextColor()" + ERROR_NOTBUTTONTYPE);

        }

        if (mButton != null) {

            mButton.setTextColor(buttonTextColor);

        }

    }

    /**
     * Returns the button text color of a BUTTON
     * {@link com.github.johnpersano.supertoasts.SuperToast.Type} {@value #TAG}.
     *
     * @return int
     */
    public int getButtonTextColor() {

        if(mButton != null) {

            return mButton.getCurrentTextColor();

        } else {

            Log.e(TAG, "getButtonTextColor()" + ERROR_NOTBUTTONTYPE);

            return 0;

        }

    }

    /**
     * Sets the button text size of a BUTTON
     * {@link com.github.johnpersano.supertoasts.SuperToast.Type} {@value #TAG}.
     *
     * @param buttonTextSize int
     */
    public void setButtonTextSize(int buttonTextSize) {

        if (mType != Type.BUTTON) {

            Log.e(TAG, "setButtonTextSize()" + ERROR_NOTBUTTONTYPE);

        }

        if (mButton != null) {

            mButton.setTextSize(buttonTextSize);

        }

    }

    /**
     * Used by orientation change recreation
     */
    private void setButtonTextSizeFloat(float buttonTextSize) {

        mButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, buttonTextSize);

    }

    /**
     * Returns the button text size of a BUTTON
     * {@link com.github.johnpersano.supertoasts.SuperToast.Type} {@value #TAG}.
     *
     * @return float
     */
    public float getButtonTextSize() {

        if(mButton != null) {

            return mButton.getTextSize();

        } else {

            Log.e(TAG, "getButtonTextSize()" + ERROR_NOTBUTTONTYPE);

            return 0.0f;

        }

    }

    /**
     * Sets the progress of the progressbar in a PROGRESS_HORIZONTAL
     * {@link com.github.johnpersano.supertoasts.SuperToast.Type} {@value #TAG}.
     *
     * @param progress int
     */
    public void setProgress(int progress) {

        if (mType != Type.PROGRESS_HORIZONTAL) {

            Log.e(TAG, "setProgress()" + ERROR_NOTPROGRESSHORIZONTALTYPE);

        }

        if (mProgressBar != null) {

            mProgressBar.setProgress(progress);

        }

    }

    /**
     * Returns the progress of the progressbar in a PROGRESS_HORIZONTAL
     * {@link com.github.johnpersano.supertoasts.SuperToast.Type} {@value #TAG}.
     *
     * @return int
     */
    public int getProgress() {

        if(mProgressBar != null) {

            return mProgressBar.getProgress();

        } else {

            Log.e(TAG, "getProgress()" + ERROR_NOTPROGRESSHORIZONTALTYPE);

            return 0;

        }

    }

    /**
     * Sets the maximum value of the progressbar in a PROGRESS_HORIZONTAL
     * {@link com.github.johnpersano.supertoasts.SuperToast.Type} {@value #TAG}.
     *
     * @param maxProgress int
     */
    public void setMaxProgress(int maxProgress) {

        if (mType != Type.PROGRESS_HORIZONTAL) {

            Log.e(TAG, "setMaxProgress()" + ERROR_NOTPROGRESSHORIZONTALTYPE);

        }

        if (mProgressBar != null) {

            mProgressBar.setMax(maxProgress);

        }

    }

    /**
     * Returns the maximum value of the progressbar in a PROGRESS_HORIZONTAL
     * {@link com.github.johnpersano.supertoasts.SuperToast.Type} {@value #TAG}.
     *
     * @return int
     */
    public int getMaxProgress() {

        if(mProgressBar != null) {

            return mProgressBar.getMax();

        } else {

            Log.e(TAG, "getMaxProgress()" + ERROR_NOTPROGRESSHORIZONTALTYPE);

            return 0;

        }

    }

    /**
     * Sets an indeterminate value to the progressbar of a PROGRESS
     * {@link com.github.johnpersano.supertoasts.SuperToast.Type} {@value #TAG}.
     *
     * @param isIndeterminate boolean
     */
    public void setProgressIndeterminate(boolean isIndeterminate) {

        if (mType != Type.PROGRESS_HORIZONTAL && mType != Type.PROGRESS) {

            Log.e(TAG, "setProgressIndeterminate()" + ERROR_NOTEITHERPROGRESSTYPE);

        }

        this.isProgressIndeterminate = isIndeterminate;

        if (mProgressBar != null) {

            mProgressBar.setIndeterminate(isIndeterminate);

        }

    }

    /**
     * Returns an indeterminate value to the progressbar of a PROGRESS
     * {@link com.github.johnpersano.supertoasts.SuperToast.Type} {@value #TAG}.
     *
     * @return boolean
     */
    public boolean getProgressIndeterminate() {

        return this.isProgressIndeterminate;

    }

    /**
     * Returns the {@value #TAG} message textview.
     *
     * @return {@link android.widget.TextView}
     */
    public TextView getTextView() {

        return mMessageTextView;

    }

    /**
     * Returns the {@value #TAG} view.
     *
     * @return {@link android.view.View}
     */
    public View getView() {

        return mToastView;

    }

    /**
     * Returns true if the {@value #TAG} is showing.
     *
     * @return boolean
     */
    public boolean isShowing() {

        return mToastView != null && mToastView.isShown();

    }

    /**
     * Returns the calling activity of the {@value #TAG}.
     *
     * @return {@link android.app.Activity}
     */
    public Activity getActivity() {

        return mActivity;

    }

    /**
     * Returns the viewgroup that the {@value #TAG} is attached to.
     *
     * @return {@link android.view.ViewGroup}
     */
    public ViewGroup getViewGroup() {

        return mViewGroup;

    }

    /**
     * Returns the LinearLayout that the {@value #TAG} is attached to.
     *
     * @return {@link android.widget.LinearLayout}
     */
    private LinearLayout getRootLayout(){

        return mRootLayout;

    }

    /**
     * Private method used to set a default style to the {@value #TAG}
     */
    private void setStyle(Style style) {

        this.setAnimations(style.animations);
        this.setTypefaceStyle(style.typefaceStyle);
        this.setTextColor(style.textColor);
        this.setBackground(style.background);

        if (this.mType == Type.BUTTON) {

            this.setDividerColor(style.dividerColor);
            this.setButtonTextColor(style.buttonTextColor);

        }

    }

    /**
     * Returns a standard {@value #TAG}.
     *
     * @param activity         {@link android.app.Activity}
     * @param textCharSequence {@link CharSequence}
     * @param durationInteger  {@link com.github.johnpersano.supertoasts.SuperToast.Duration}
     *
     * @return {@link SuperActivityToast}
     */
    public static SuperActivityToast create(Activity activity, CharSequence textCharSequence, int durationInteger) {

        final SuperActivityToast superActivityToast = new SuperActivityToast(activity);
        superActivityToast.setText(textCharSequence);
        superActivityToast.setDuration(durationInteger);

        return superActivityToast;

    }

    /**
     * Returns a standard {@value #TAG} with specified animations.
     *
     * @param activity         {@link android.app.Activity}
     * @param textCharSequence {@link CharSequence}
     * @param durationInteger  {@link com.github.johnpersano.supertoasts.SuperToast.Duration}
     * @param animations       {@link com.github.johnpersano.supertoasts.SuperToast.Animations}
     *
     * @return {@link SuperActivityToast}
     */
    public static SuperActivityToast create(Activity activity, CharSequence textCharSequence, int durationInteger, Animations animations) {

        final SuperActivityToast superActivityToast = new SuperActivityToast(activity);
        superActivityToast.setText(textCharSequence);
        superActivityToast.setDuration(durationInteger);
        superActivityToast.setAnimations(animations);

        return superActivityToast;

    }

    /**
     * Returns a {@value #TAG} with a specified style.
     *
     * @param activity         {@link android.app.Activity}
     * @param textCharSequence {@link CharSequence}
     * @param durationInteger  {@link com.github.johnpersano.supertoasts.SuperToast.Duration}
     * @param style            {@link com.github.johnpersano.supertoasts.util.Style}
     *
     * @return {@link SuperActivityToast}
     */
    public static SuperActivityToast create(Activity activity, CharSequence textCharSequence, int durationInteger, Style style) {

        final SuperActivityToast superActivityToast = new SuperActivityToast(activity);
        superActivityToast.setText(textCharSequence);
        superActivityToast.setDuration(durationInteger);
        superActivityToast.setStyle(style);

        return superActivityToast;

    }

    /**
     * Dismisses and removes all pending/showing {@value #TAG}.
     */
    public static void cancelAllSuperActivityToasts() {

        ManagerSuperActivityToast.getInstance().cancelAllSuperActivityToasts();

    }

    /**
     * Dismisses and removes all pending/showing {@value #TAG}
     * for a specific activity.
     *
     * @param activity {@link android.app.Activity}
     */
    public static void clearSuperActivityToastsForActivity(Activity activity) {

        ManagerSuperActivityToast.getInstance()
                .cancelAllSuperActivityToastsForActivity(activity);

    }

    /**
     * Saves pending/showing {@value #TAG} to a bundle.
     *
     * @param bundle {@link android.os.Bundle}
     */
    public static void onSaveState(Bundle bundle) {

        ReferenceHolder[] list = new ReferenceHolder[ManagerSuperActivityToast
                .getInstance().getList().size()];

        LinkedList<SuperActivityToast> lister = ManagerSuperActivityToast
                .getInstance().getList();

        for (int i = 0; i < list.length; i++) {

            list[i] = new ReferenceHolder(lister.get(i));

        }

        bundle.putParcelableArray(BUNDLE_TAG, list);

        SuperActivityToast.cancelAllSuperActivityToasts();

    }

    /**
     * Recreates pending/showing {@value #TAG} from orientation change.
     *
     * @param bundle   {@link android.os.Bundle}
     * @param activity {@link android.app.Activity}
     */
    public static void onRestoreState(Bundle bundle, Activity activity) {

        if (bundle == null) {

            return;
        }

        Parcelable[] savedArray = bundle.getParcelableArray(BUNDLE_TAG);

        int i = 0;

        if (savedArray != null) {

            for (Parcelable parcelable : savedArray) {

                i++;

                new SuperActivityToast(activity, (ReferenceHolder) parcelable, null, i);

            }

        }

    }

    /**
     * Recreates pending/showing {@value #TAG} from orientation change and
     * reattaches any OnClickWrappers/OnDismissWrappers.
     *
     * @param bundle   {@link android.os.Bundle}
     * @param activity {@link android.app.Activity}
     * @param wrappers {@link com.github.johnpersano.supertoasts.util.Wrappers}
     */
    public static void onRestoreState(Bundle bundle, Activity activity, Wrappers wrappers) {

        if (bundle == null) {

            return;
        }

        Parcelable[] savedArray = bundle.getParcelableArray(BUNDLE_TAG);

        int i = 0;

        if (savedArray != null) {

            for (Parcelable parcelable : savedArray) {

                i++;

                new SuperActivityToast(activity, (ReferenceHolder) parcelable, wrappers, i);

            }

        }

    }

    /**
     * Method used to recreate {@value #TAG} after orientation change
     */
    private SuperActivityToast(Activity activity, ReferenceHolder referenceHolder, Wrappers wrappers, int position) {

        SuperActivityToast superActivityToast;

        if (referenceHolder.mType == Type.BUTTON) {

            superActivityToast = new SuperActivityToast(activity, Type.BUTTON);
            superActivityToast.setButtonText(referenceHolder.mButtonText);
            superActivityToast.setButtonTextSizeFloat(referenceHolder.mButtonTextSize);
            superActivityToast.setButtonTextColor(referenceHolder.mButtonTextColor);
            superActivityToast.setButtonIcon(referenceHolder.mButtonIcon);
            superActivityToast.setDividerColor(referenceHolder.mDivider);
            superActivityToast.setButtonTypefaceStyle(referenceHolder.mButtonTypefaceStyle);

            int screenSize = activity.getResources().getConfiguration().screenLayout &
                    Configuration.SCREENLAYOUT_SIZE_MASK;

            /* Changes the size of the BUTTON type SuperActivityToast to mirror Gmail app */
            if(screenSize >= Configuration.SCREENLAYOUT_SIZE_LARGE) {

                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                layoutParams.gravity = Gravity.BOTTOM | Gravity.RIGHT;
                layoutParams.bottomMargin = (int) activity.getResources().getDimension(R.dimen.buttontoast_hover);
                layoutParams.rightMargin = (int) activity.getResources().getDimension(R.dimen.buttontoast_x_padding);
                layoutParams.leftMargin = (int) activity.getResources().getDimension(R.dimen.buttontoast_x_padding);

                superActivityToast.getRootLayout().setLayoutParams(layoutParams);

            }

            /* Reattach any OnClickWrappers by matching tags sent through parcel */
            if (wrappers != null) {

                for (OnClickWrapper onClickWrapper : wrappers.getOnClickWrappers()) {

                    if (onClickWrapper.getTag().equalsIgnoreCase(referenceHolder.mClickListenerTag)) {

                        superActivityToast.setOnClickWrapper(onClickWrapper, referenceHolder.mToken);

                    }

                }
            }

        } else if (referenceHolder.mType == Type.PROGRESS) {

            /* PROGRESS {@value #TAG} should be managed by the developer */

            return;

        } else if (referenceHolder.mType == Type.PROGRESS_HORIZONTAL) {

            /* PROGRESS_HORIZONTAL {@value #TAG} should be managed by the developer */

            return;

        } else {

            superActivityToast = new SuperActivityToast(activity);

        }

        /* Reattach any OnDismissWrappers by matching tags sent through parcel */
        if (wrappers != null) {

            for (OnDismissWrapper onDismissWrapper : wrappers.getOnDismissWrappers()) {

                if (onDismissWrapper.getTag().equalsIgnoreCase(referenceHolder.mDismissListenerTag)) {

                    superActivityToast.setOnDismissWrapper(onDismissWrapper);

                }

            }
        }

        superActivityToast.setAnimations(referenceHolder.mAnimations);
        superActivityToast.setText(referenceHolder.mText);
        superActivityToast.setTypefaceStyle(referenceHolder.mTypefaceStyle);
        superActivityToast.setDuration(referenceHolder.mDuration);
        superActivityToast.setTextColor(referenceHolder.mTextColor);
        superActivityToast.setTextSizeFloat(referenceHolder.mTextSize);
        superActivityToast.setIndeterminate(referenceHolder.mIsIndeterminate);
        superActivityToast.setIcon(referenceHolder.mIcon, referenceHolder.mIconPosition);
        superActivityToast.setBackground(referenceHolder.mBackground);
        superActivityToast.setTouchToDismiss(referenceHolder.mIsTouchDismissible);

        /* Do not use show animation on recreation of {@value #TAG} that was previously showing */
        if (position == 1) {

            superActivityToast.setShowImmediate(true);

        }

        superActivityToast.show();

    }

    /* This OnTouchListener handles the setTouchToDismiss() function */
    private OnTouchListener mTouchDismissListener = new OnTouchListener() {

        int timesTouched;

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {

            /* Hack to prevent repeat touch events causing erratic behavior */
            if (timesTouched == 0) {

                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                    dismiss();

                }

            }

            timesTouched++;

            return false;

        }

    };

    /* This OnClickListener handles the button click event */
    private View.OnClickListener mButtonListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {

            if (mOnClickWrapper != null) {

                mOnClickWrapper.onClick(view, mToken);

            }

            dismiss();

            /* Make sure the button cannot be clicked multiple times */
            mButton.setClickable(false);

        }
    };

    /**
     * Parcelable class that saves all data on orientation change
     */
    private static class ReferenceHolder implements Parcelable {

        Animations mAnimations;
        boolean mIsIndeterminate;
        boolean mIsTouchDismissible;
        float mTextSize;
        float mButtonTextSize;
        IconPosition mIconPosition;
        int mDuration;
        int mTextColor;
        int mIcon;
        int mBackground;
        int mTypefaceStyle;
        int mButtonTextColor;
        int mButtonIcon;
        int mDivider;
        int mButtonTypefaceStyle;
        Parcelable mToken;
        String mText;
        String mButtonText;
        String mClickListenerTag;
        String mDismissListenerTag;
        Type mType;

        public ReferenceHolder(SuperActivityToast superActivityToast) {

            mType = superActivityToast.getType();

            if (mType == Type.BUTTON) {

                mButtonText = superActivityToast.getButtonText().toString();
                mButtonTextSize = superActivityToast.getButtonTextSize();
                mButtonTextColor = superActivityToast.getButtonTextColor();
                mButtonIcon = superActivityToast.getButtonIcon();
                mDivider = superActivityToast.getDividerColor();
                mClickListenerTag = superActivityToast.getOnClickWrapperTag();
                mButtonTypefaceStyle = superActivityToast.getButtonTypefaceStyle();
                mToken = superActivityToast.getToken();

            }

            if (superActivityToast.getIconResource() != 0 && superActivityToast.getIconPosition() != null) {

                mIcon = superActivityToast.getIconResource();
                mIconPosition = superActivityToast.getIconPosition();

            }

            mDismissListenerTag = superActivityToast.getOnDismissWrapperTag();
            mAnimations = superActivityToast.getAnimations();
            mText = superActivityToast.getText().toString();
            mTypefaceStyle = superActivityToast.getTypefaceStyle();
            mDuration = superActivityToast.getDuration();
            mTextColor = superActivityToast.getTextColor();
            mTextSize = superActivityToast.getTextSize();
            mIsIndeterminate = superActivityToast.isIndeterminate();
            mBackground = superActivityToast.getBackground();
            mIsTouchDismissible = superActivityToast.isTouchDismissible();

        }

        public ReferenceHolder(Parcel parcel) {

            mType = Type.values()[parcel.readInt()];

            if (mType == Type.BUTTON) {

                mButtonText = parcel.readString();
                mButtonTextSize = parcel.readFloat();
                mButtonTextColor = parcel.readInt();
                mButtonIcon = parcel.readInt();
                mDivider = parcel.readInt();
                mButtonTypefaceStyle = parcel.readInt();
                mClickListenerTag = parcel.readString();
                mToken = parcel.readParcelable(((Object) this).getClass().getClassLoader());

            }

            boolean hasIcon = parcel.readByte() != 0;

            if (hasIcon) {

                mIcon = parcel.readInt();
                mIconPosition = IconPosition.values()[parcel.readInt()];

            }

            mDismissListenerTag = parcel.readString();
            mAnimations = Animations.values()[parcel.readInt()];
            mText = parcel.readString();
            mTypefaceStyle = parcel.readInt();
            mDuration = parcel.readInt();
            mTextColor = parcel.readInt();
            mTextSize = parcel.readFloat();
            mIsIndeterminate = parcel.readByte() != 0;
            mBackground = parcel.readInt();
            mIsTouchDismissible = parcel.readByte() != 0;

        }


        @Override
        public void writeToParcel(Parcel parcel, int i) {

            parcel.writeInt(mType.ordinal());

            if (mType == Type.BUTTON) {

                parcel.writeString(mButtonText);
                parcel.writeFloat(mButtonTextSize);
                parcel.writeInt(mButtonTextColor);
                parcel.writeInt(mButtonIcon);
                parcel.writeInt(mDivider);
                parcel.writeInt(mButtonTypefaceStyle);
                parcel.writeString(mClickListenerTag);
                parcel.writeParcelable(mToken, 0);

            }

            if (mIcon != 0 && mIconPosition != null) {

                parcel.writeByte((byte) 1);

                parcel.writeInt(mIcon);
                parcel.writeInt(mIconPosition.ordinal());

            } else {

                parcel.writeByte((byte) 0);

            }

            parcel.writeString(mDismissListenerTag);
            parcel.writeInt(mAnimations.ordinal());
            parcel.writeString(mText);
            parcel.writeInt(mTypefaceStyle);
            parcel.writeInt(mDuration);
            parcel.writeInt(mTextColor);
            parcel.writeFloat(mTextSize);
            parcel.writeByte((byte) (mIsIndeterminate ? 1 : 0));
            parcel.writeInt(mBackground);
            parcel.writeByte((byte) (mIsTouchDismissible ? 1 : 0));

        }

        @Override
        public int describeContents() {

            return 0;

        }

        public static final Creator CREATOR = new Creator() {

            public ReferenceHolder createFromParcel(Parcel parcel) {

                return new ReferenceHolder(parcel);

            }

            public ReferenceHolder[] newArray(int size) {

                return new ReferenceHolder[size];

            }

        };

    }

}
