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

package com.github.johnpersano.supertoasts.library;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.github.johnpersano.supertoasts.library.utils.BackgroundUtils;
import com.github.johnpersano.supertoasts.library.utils.ListenerUtils;

import java.util.ArrayList;

/**
 * SuperActivityToasts resemble stock {@link android.widget.Toast}s but are added
 * to an Activity's ViewGroup. SuperActivityToasts can receive touch events and
 * be shown indefinitely. For usage information, check out the
 * <a href="https://github.com/JohnPersano/Supertoasts/wiki/SuperActivityToast">SuperActivityToast Wiki page</a>.
 */
@SuppressWarnings({"UnusedDeclaration", "RedundantCast", "UnusedReturnValue"})
public class SuperActivityToast extends SuperToast {

    // Bundle tag with a hex as a string so it's highly unlikely to interfere with other keys in the bundle
    private static final String BUNDLE_KEY = "0x532e412e542e";

    /**
     * Listener that calls onClick() when a TYPE_BUTTON SuperActivityToast receives a Button press event.
     *
     * @see #setOnButtonClickListener(String, android.os.Parcelable,
     * com.github.johnpersano.supertoasts.library.SuperActivityToast.OnButtonClickListener)
     */
    public interface OnButtonClickListener {

        /**
         * Called when a TYPE_BUTTON SuperActivityToast's Button is pressed.
         *
         * @param view The View that was clicked
         * @param token A Parcelable token that can hold data across orientation changes
         */
        void onClick(View view, Parcelable token);
    }

    private Context mContext;
    private View mView;
    private ViewGroup mViewGroup;
    private ProgressBar mProgressBar;
    private ImageView mImg;
    private Style mStyle;
    private OnButtonClickListener mOnButtonClickListener;
    private boolean mFromOrientationChange;

    /**
     * Public constructor for a SuperActivityToast.
     *
     * @param context An Activity Context
     */
    public SuperActivityToast(@NonNull Context context) {
        super(context);

        if (!(context instanceof Activity)) {
            throw new IllegalArgumentException("SuperActivityToast Context must be an Activity.");
        }

        this.mContext = context;
        this.mStyle = this.getStyle(); // Style is created in the super(context) call

        // Set the default ViewGroup as the Activity's content
        this.mViewGroup = (ViewGroup) ((Activity) context).findViewById(android.R.id.content);
    }

    /**
     * Public constructor for a SuperActivityToast.
     *
     * @param context An Activity Context
     * @param style The desired Style
     */
    public SuperActivityToast(@NonNull Context context, @NonNull Style style) {
        super(context, style);

        if (!(context instanceof Activity)) {
            throw new IllegalArgumentException("SuperActivityToast Context must be an Activity.");
        }

        this.mContext = context;
        this.mStyle = style;

        // Set the default ViewGroup as the Activity's content
        this.mViewGroup = (ViewGroup) ((Activity) context).findViewById(android.R.id.content);
    }

    /**
     * Public constructor for a SuperActivityToast.
     *
     * @param context An Activity Context
     * @param type The desired SuperActivityToast type
     */
    public SuperActivityToast(@NonNull Context context, @Style.Type int type) {
        super(context, type);

        if (!(context instanceof Activity)) {
            throw new IllegalArgumentException("SuperActivityToast Context must be an Activity.");
        }

        this.mContext = context;
        this.mStyle = this.getStyle(); // Style is created in the super(context) call

        // Set the default ViewGroup as the Activity's content
        this.mViewGroup = (ViewGroup) ((Activity) context).findViewById(android.R.id.content);
    }

    /**
     * Public constructor for a SuperActivityToast.
     *
     * @param context An Activity Context
     * @param style The desired Style
     * @param type The desired SuperActivityToast type
     */
    public SuperActivityToast(@NonNull Context context, @NonNull Style style, @Style.Type int type) {
        super(context, style, type);

        if (!(context instanceof Activity)) {
            throw new IllegalArgumentException("SuperActivityToast Context must be an Activity.");
        }

        this.mContext = context;
        this.mStyle = this.getStyle(); // Style is created in the super(context) call

        // Set the default ViewGroup as the Activity's content
        this.mViewGroup = (ViewGroup) ((Activity) context).findViewById(android.R.id.content);
    }

    /**
     * Public constructor for a SuperActivityToast.
     *
     * @param context An Activity Context
     * @param style The desired Style
     * @param type The desired SuperActivityToast type
     * @param viewGroupId The id of the ViewGroup to attach the SuperActivityToast to
     */
    public SuperActivityToast(@NonNull Context context, @NonNull Style style,
                              @Style.Type int type, @IdRes int viewGroupId) {
        super(context, style, type, viewGroupId);

        if (!(context instanceof Activity)) {
            throw new IllegalArgumentException("SuperActivityToast Context must be an Activity.");
        }

        this.mContext = context;
        this.mStyle = this.getStyle(); // Style is created in the super(context) call

        // Try to find the ViewGroup id in the layout
        this.mViewGroup = (ViewGroup) ((Activity) context).findViewById(viewGroupId);
        if (this.mViewGroup == null) {
            Log.e(getClass().getName(), "Could not find a ViewGroup with id " + String.valueOf(viewGroupId));
            this.mViewGroup = (ViewGroup) ((Activity) context).findViewById(android.R.id.content);
        }
    }

    /**
     * Handle the inflation of the appropriate View according to the desired type.
     *
     * @param context An Activity Context
     * @param layoutInflater The LayoutInflater created from the Context
     * @param type The desired SuperActivityToast type
     *
     * @return The SuperActivityToast View
     */
    @Override
    protected View onCreateView(@NonNull Context context, LayoutInflater layoutInflater,
                                @Style.Type int type) {

        if(!(context instanceof Activity)) {
            throw new IllegalArgumentException("SuperActivityToast Context must be an Activity.");
        }

        // Inflate the appropriate View for the type, do not return for each case since mView must be set
        switch (type) {
            case Style.TYPE_STANDARD:
                this.mView = layoutInflater.inflate(R.layout.supertoast, (ViewGroup)
                        ((Activity) context)
                        .findViewById(android.R.id.content), false);
                break;

            case Style.TYPE_BUTTON:
                this.mView = layoutInflater.inflate(R.layout.supertoast_button, (ViewGroup)
                        ((Activity) context)
                        .findViewById(android.R.id.content), false);
                break;

            case Style.TYPE_PROGRESS_CIRCLE:
                this.mView = layoutInflater.inflate(R.layout.supertoast_progress_circle,
                        (ViewGroup) ((Activity) context)
                        .findViewById(android.R.id.content), false);
                this.mProgressBar = (ProgressBar) this.mView.findViewById(R.id.progress_bar);
                break;

            case Style.TYPE_PROGRESS_BAR:
                this.mView = layoutInflater.inflate(R.layout.supertoast_progress_bar,
                        (ViewGroup) ((Activity) context)
                        .findViewById(android.R.id.content), false);
                this.mProgressBar = (ProgressBar) this.mView.findViewById(R.id.progress_bar);
                break;

            case Style.TYPE_IMG:
                this.mView = layoutInflater.inflate(R.layout.supertoast_img,
                        (ViewGroup)((Activity) context)
                                .findViewById(android.R.id.content),
                        false);
                this.mImg = (ImageView) this.mView.findViewById(R.id.img);
                break;

            default:
                // Type received was erroneous so inflate the standard SuperToast layout
                this.mView = layoutInflater.inflate(R.layout.supertoast,
                        (ViewGroup) ((Activity) context)
                        .findViewById(android.R.id.content), false);
                break;
        }

        return this.mView;
    }

    /**
     * Sets the {@link com.github.johnpersano.supertoasts.library.SuperToast.OnDismissListener}
     * of the SuperActivityToast. The listener will be triggered when the
     * SuperActivityToast is dismissed.
     *
     * @param tag A unique tag for this listener
     * @param token A Parcelable token to hold data across orientation changes
     * @param onDismissListener The desired OnDismissListener
     *
     * @return The current SuperActivityToast instance
     *
     * @see #setOnDismissListener(String,
     * com.github.johnpersano.supertoasts.library.SuperToast.OnDismissListener)
     */
    @Override
    public SuperToast setOnDismissListener(String tag, Parcelable token,
                                           @NonNull OnDismissListener onDismissListener) {
        return super.setOnDismissListener(tag, token, onDismissListener);
    }

    /**
     * Sets the {@link com.github.johnpersano.supertoasts.library.SuperToast.OnDismissListener}
     * of the SuperActivityToast. The listener will be triggered when the
     * SuperActivityToast is dismissed.
     *
     * @param tag A unique tag for this listener
     * @param onDismissListener The desired OnDismissListener
     *
     * @return The current SuperActivityToast instance
     *
     * @see #setOnDismissListener(String, android.os.Parcelable,
     * com.github.johnpersano.supertoasts.library.SuperToast.OnDismissListener)
     */
    @Override
    public SuperToast setOnDismissListener(String tag,
                                           @NonNull OnDismissListener onDismissListener) {
        return super.setOnDismissListener(tag, onDismissListener);
    }

    /**
     * Returns the dismiss tag of the SuperActivityToast.
     *
     * @return The dismiss tag
     */
    @Override
    public String getDismissTag() {
        return super.getDismissTag();
    }

    /**
     * Returns the dismiss Parcelable token of the SuperActivityToast.
     *
     * @return The dismiss Parcelable token
     */
    @Override
    public Parcelable getDismissToken() {
        return super.getDismissToken();
    }

    /**
     * Protected method used by the Toaster to know when not to use the
     * show animation.
     *
     * @return  The current SuperActivityToast instance
     */
    protected SuperActivityToast fromOrientationChange() {
        this.mFromOrientationChange = true;
        return this;
    }

    /**
     * Protected method used by the Toaster to know when not to use the
     * show animation.
     *
     * @return true if coming from orientation change
     */
    protected boolean isFromOrientationChange() {
        return this.mFromOrientationChange;
    }

    /**
     * Set the SuperActivityToast to show indeterminately. This will ignore any
     * duration set by the
     * {@link com.github.johnpersano.supertoasts.library.SuperToast#setDuration(int)}
     * method. This will also enable touch to dismiss.
     *
     * @param indeterminate true if SuperActivityToast should be isIndeterminate
     * @return The current SuperActivityToast instance
     */
    public SuperActivityToast setIndeterminate(boolean indeterminate) {
        this.mStyle.isIndeterminate = indeterminate;

        // Make sure the SuperActivityToast can be dismissed
        this.mStyle.touchToDismiss = true;
        return this;
    }

    /**
     * Returns true if the SuperActivityToast is isIndeterminate.
     *
     * @return true if isIndeterminate
     */
    public boolean isIndeterminate() {
        return this.mStyle.isIndeterminate;
    }

    /**
     * Set a private OnTouchListener to the SuperActivityToast which will dismiss
     * it if any part is touched.
     *
     * @param touchToDismiss true if should touch to dismiss
     * @return The current SuperActivityToast instance
     */
    public SuperActivityToast setTouchToDismiss(boolean touchToDismiss) {
        this.mStyle.touchToDismiss = touchToDismiss;
        return this;
    }

    /**
     * Returns true if the SuperActivityToast is touch dismissible.
     *
     * @return true if touch dismissible
     */
    public boolean isTouchDismissible() {
        return this.mStyle.touchToDismiss;
    }

    /**
     * Set the text of the Button in a TYPE_BUTTON SuperActivityToast. Generally,
     * this String should not exceed four characters. The String passed as the
     * parameter will be capitalized.
     *
     * @param buttonText The desired Button text
     * @return The current SuperActivityToast instance
     */
    public SuperActivityToast setButtonText(String buttonText) {
        this.mStyle.buttonText = buttonText;
        return this;
    }

    /**
     * Returns the Button text of a TYPE_BUTTON SuperActivityToast.
     *
     * @return The Button text
     */
    public String getButtonText() {
        return this.mStyle.buttonText;
    }

    /**
     * Set the Typeface style of the Button text in a TYPE_BUTTON SuperActivityToast.
     * In most cases, this should be {@link android.graphics.Typeface#BOLD}.
     *
     * @param buttonTypefaceStyle The desired Button text Typeface style
     * @return The current SuperActivityToast instance
     */
    public SuperActivityToast setButtonTypefaceStyle(@Style.TypefaceStyle
                                                     int buttonTypefaceStyle) {
        this.mStyle.buttonTypefaceStyle = buttonTypefaceStyle;
        return this;
    }

    /**
     * Returns the Button text Typeface style of a TYPE_BUTTON SuperActivityToast.
     *
     * @return The Button text Typeface style
     */
    @Style.TypefaceStyle
    public int getButtonTypefaceStyle() {
        return this.mStyle.buttonTypefaceStyle;
    }

    /**
     * Set the color of the Button text in a TYPE_BUTTON SuperActivityToast.
     *
     * @param buttonTextColor The desired Button text color
     * @return The current SuperActivityToast instance
     */
    public SuperActivityToast setButtonTextColor(@ColorInt int buttonTextColor) {
        this.mStyle.buttonTextColor = buttonTextColor;
        return this;
    }

    /**
     * Returns the Button text color of a TYPE_BUTTON SuperActivityToast.
     *
     * @return The Button text color
     */
    @ColorInt
    public int getButtonTextColor() {
        return this.mStyle.buttonTextColor;
    }

    /**
     * Set the size of the Button text in a TYPE_BUTTON SuperActivityToast.
     *
     * @param buttonTextSize The desired Button text size
     * @return The current SuperActivityToast instance
     */
    public SuperActivityToast setButtonTextSize(@Style.TextSize int buttonTextSize) {
        this.mStyle.buttonTextSize = buttonTextSize;
        return this;
    }

    /**
     * Returns the Button text size of a TYPE_BUTTON SuperActivityToast.
     *
     * @return The Button text size
     */
    @Style.TextSize
    public int getButtonTextSize() {
        return this.mStyle.buttonTextSize;
    }

    /**
     * Set the color of the divider between the text and the Button in a TYPE_BUTTON
     * SuperActivityToast.
     *
     * @param buttonDividerColor The desired divider color
     * @return The current SuperActivityToast instance
     */
    public SuperActivityToast setButtonDividerColor(@ColorInt int buttonDividerColor) {
        this.mStyle.buttonDividerColor = buttonDividerColor;
        return this;
    }

    /**
     * Returns the divider color of a TYPE_BUTTON SuperActivityToast.
     *
     * @return The divider color
     */
    @ColorInt
    public int getButtonDividerColor() {
        return this.mStyle.buttonDividerColor;
    }

    /**
     * Set the Button icon resource in a TYPE_BUTTON SuperActivityToast.
     *
     * @param buttonIconResource The desired icon resource
     * @return The current SuperActivityToast instance
     */
    public SuperActivityToast setButtonIconResource(@DrawableRes int buttonIconResource) {
        this.mStyle.buttonIconResource = buttonIconResource;
        return this;
    }

    /**
     * Set the Img icon resource in a TYPE_IMG SuperActivityToast.
     *
     * @param imgResource The desired icon resource
     * @return The current SuperActivityToast instance
     */
    public SuperActivityToast setImgResource(@DrawableRes int imgResource) {
        this.mStyle.imgResource = imgResource;
        return this;
    }

    /**
     * Returns the Button icon resource of a TYPE_BUTTON SuperActivityToast.
     *
     * @return The Button icon resource
     */
    public int getButtonIconResource() {
        return this.mStyle.buttonIconResource;
    }

    /**
     * Sets the
     * {@link com.github.johnpersano.supertoasts.library.SuperActivityToast.OnButtonClickListener}
     * in a TYPE_BUTTON SuperActivityToast. The listener will be triggered
     * when the SuperActivityToast Button is pressed.
     *
     * @param tag A unique tag for this listener
     * @param token A Parcelable token to hold data across orientation changes
     * @param onButtonClickListener The desired OnButtonClickListener
     * @return The current SuperActivityToast instance
     */
    public SuperActivityToast setOnButtonClickListener(@NonNull String tag, Parcelable token,
                                                       @NonNull OnButtonClickListener onButtonClickListener) {
        this.mOnButtonClickListener = onButtonClickListener;
        this.mStyle.buttonTag = tag;
        this.mStyle.buttonToken = token;
        return this;
    }

    /**
     * Returns the button click tag of a TYPE_BUTTON SuperActivityToast.
     *
     * @return The button click tag
     */
    public String getButtonTag() {
        return this.mStyle.buttonTag;
    }

    /**
     * Returns the button click Parcelable token of a TYPE_BUTTON SuperActivityToast.
     *
     * @return The button click Parcelable token
     */
    public Parcelable getButtonToken() {
        return this.mStyle.buttonToken;
    }

    /**
     * Returns the
     * {@link com.github.johnpersano.supertoasts.library.SuperActivityToast.OnButtonClickListener}
     * of a TYPE_BUTTON SuperActivityToast.
     *
     * @return The OnButtonClickListener
     */
    public OnButtonClickListener getOnButtonClickListener() {
        return this.mOnButtonClickListener;
    }

    /**
     * Set the progress of the ProgressBar in a TYPE_PROGRESS_BAR SuperActivityToast.
     * This can be called multiple times after the SuperActivityToast is showing.
     *
     * @param progress The desired progress
     * @return The current SuperActivityToast instance
     */
    public SuperActivityToast setProgress(int progress) {
        if (this.mProgressBar == null) {
            Log.e(getClass().getName(), "Could not set SuperActivityToast " +
                    "progress, are you sure you set the type to TYPE_PROGRESS_CIRCLE " +
                    "or TYPE_PROGRESS_BAR?");
            return this;
        }
        this.mStyle.progress = progress;
        this.mProgressBar.setProgress(progress);
        return this;
    }

    /**
     * Returns the ProgressBar progress of a TYPE_PROGRESS_BAR SuperActivityToast.
     *
     * @return The progress
     */
    public int getProgress() {
        return this.mStyle.progress;
    }

    /**
     * REQUIRES API 21
     *
     * Set the progress color of the ProgressBar in a TYPE_PROGRESS_BAR SuperActivityToast.
     *
     * @param progressBarColor The desired progress color
     * @return The current SuperActivityToast instance
     */
    public SuperActivityToast setProgressBarColor(@ColorInt int progressBarColor) {
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            Log.w(getClass().getName(), "SuperActivityToast.setProgressBarColor() requires API " +
                    "21 or newer.");
            return this;
        }
        this.mStyle.progressBarColor = progressBarColor;
        return this;
    }

    /**
     * REQUIRES API 21
     *
     * Returns the ProgressBar progress of a TYPE_PROGRESS_BAR SuperActivityToast.
     *
     * @return The progress color
     */
    @ColorInt
    public int getProgressBarColor() {
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            Log.w(getClass().getName(), "SuperActivityToast.getProgressBarColor() requires API " +
                    "21 or newer.");
            return 0;
        }
        return this.mStyle.progressBarColor;
    }

    /**
     * Set the maximum progress of the ProgressBar in a TYPE_PROGRESS_BAR SuperActivityToast.
     *
     * @param progressMax The desired progress maximum
     * @return The current SuperActivityToast instance
     */
    public SuperActivityToast setProgressMax(int progressMax) {
        this.mStyle.progressMax = progressMax;
        return this;
    }

    /**
     * Returns the maximum progress of a TYPE_PROGRESS_BAR SuperActivityToast.
     *
     * @return The maximum progress
     */
    public int getProgressMax() {
        return this.mStyle.progressMax;
    }

    /**
     * Set the ProgressBar to be isIndeterminate in a TYPE_PROGRESS_BAR SuperActivityToast.
     *
     * @param progressIndeterminate true if progress should be isIndeterminate
     * @return The current SuperActivityToast instance
     */
    public SuperActivityToast setProgressIndeterminate(boolean progressIndeterminate) {
        this.mStyle.progressIndeterminate = progressIndeterminate;
        return this;
    }

    /**
     * Returns true if the SuperActivityToast ProgressBar is isIndeterminate.
     *
     * @return true if isIndeterminate.
     */
    public boolean getProgressIndeterminate() {
        return this.mStyle.progressIndeterminate;
    }

    /**
     * Returns the ViewGroup that the SuperActivityToast is being attached to.
     *
     * @return The ViewGroup
     */
    public ViewGroup getViewGroup() {
        return this.mViewGroup;
    }

    /**
     * Returns the SuperActivityToast's type.
     *
     * @return The type
     */
    @Style.Type
    public int getType() {
        return this.mStyle.type;
    }

    /**
     * Modify various attributes of the SuperActivityToast before being shown.
     */
    @Override
    protected void onPrepareShow() {
        super.onPrepareShow(); // This will take care of many modifications

        final FrameLayout.LayoutParams layoutParams = new FrameLayout
                .LayoutParams(this.mStyle.width, this.mStyle.height);

        // Make some type specific tweaks
        switch (this.mStyle.type) {

            case Style.TYPE_STANDARD:
                this.mStyle.width = FrameLayout.LayoutParams.MATCH_PARENT;
                this.mStyle.xOffset = BackgroundUtils.convertToDIP(24);
                this.mStyle.yOffset = BackgroundUtils.convertToDIP(24);
                break;

            case Style.TYPE_BUTTON:
                // If NOT Lollipop frame, give padding on each side
                if (this.mStyle.frame != Style.FRAME_LOLLIPOP) {
                    this.mStyle.width = FrameLayout.LayoutParams.MATCH_PARENT;
                    this.mStyle.xOffset = BackgroundUtils.convertToDIP(24);
                    this.mStyle.yOffset = BackgroundUtils.convertToDIP(24);
                }

                // On a big screen device, show the SuperActivityToast on the bottom left
                if ((this.mContext.getResources().getConfiguration().screenLayout
                        & Configuration.SCREENLAYOUT_SIZE_MASK)
                        >= Configuration.SCREENLAYOUT_SIZE_LARGE) {
                    this.mStyle.width = BackgroundUtils.convertToDIP(568);
                    this.mStyle.gravity = Gravity.BOTTOM | Gravity.START;
                }

                // Set up the Button attributes
                final Button button = (Button) this.mView.findViewById(R.id.button);
                button.setBackgroundResource(BackgroundUtils
                        .getButtonBackgroundResource(this.mStyle.frame));
                button.setText(this.mStyle.buttonText != null ?
                        this.mStyle.buttonText.toUpperCase() : "");
                button.setTypeface(button.getTypeface(), this.mStyle.buttonTypefaceStyle);
                button.setTextColor(this.mStyle.buttonTextColor);
                button.setTextSize(this.mStyle.buttonTextSize);

                if (this.mStyle.frame != Style.FRAME_LOLLIPOP) {
                    this.mView.findViewById(R.id.divider).setBackgroundColor(this
                            .mStyle.buttonDividerColor);

                    // Set an icon resource if desired
                    if(this.mStyle.buttonIconResource > 0) {
                        button.setCompoundDrawablesWithIntrinsicBounds(ResourcesCompat
                                .getDrawable(mContext.getResources(),
                                        this.mStyle.buttonIconResource,
                                        mContext.getTheme()),
                                null, null, null);
                    }
                }

                if (this.mOnButtonClickListener != null) {
                    button.setOnClickListener(new View.OnClickListener() {

                        short clicked = 0;
                        @Override
                        public void onClick(View view) {
                            // Prevent button spamming
                            if (clicked > 0) return;
                            clicked++;

                            mOnButtonClickListener.onClick(view, getButtonToken());
                            SuperActivityToast.this.dismiss();
                        }
                    });
                }
                break;

            case Style.TYPE_PROGRESS_CIRCLE:
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    this.mProgressBar.setIndeterminateTintMode(PorterDuff.Mode.SRC_IN);
                    this.mProgressBar.setIndeterminateTintList(ColorStateList
                            .valueOf(this.mStyle.progressBarColor));
                }
                break;

            case Style.TYPE_PROGRESS_BAR:
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    this.mProgressBar.setIndeterminateTintMode(PorterDuff.Mode.SRC_IN);
                    this.mProgressBar.setIndeterminateTintList(ColorStateList
                            .valueOf(this.mStyle.progressBarColor));
                    this.mProgressBar.setProgressTintMode(PorterDuff.Mode.SRC_IN);
                    this.mProgressBar.setProgressTintList(ColorStateList
                            .valueOf(this.mStyle.progressBarColor));
                }
                this.mProgressBar.setProgress(this.mStyle.progress);
                this.mProgressBar.setMax(this.mStyle.progressMax);
                this.mProgressBar.setIndeterminate(this.mStyle.progressIndeterminate);
                break;

            case Style.TYPE_IMG:
                this.mStyle.width = FrameLayout.LayoutParams.MATCH_PARENT;
                this.mStyle.xOffset = BackgroundUtils.convertToDIP(24);
                this.mStyle.yOffset = BackgroundUtils.convertToDIP(24);

                if(this.mStyle.imgResource > 0) {
                    this.mImg.setImageResource(this.mStyle.imgResource);
                }

                break;
        }

        layoutParams.width = this.mStyle.width;
        layoutParams.height = this.mStyle.height;
        layoutParams.gravity = this.mStyle.gravity;
        layoutParams.bottomMargin = this.mStyle.yOffset;
        layoutParams.topMargin = this.mStyle.yOffset;
        layoutParams.leftMargin = this.mStyle.xOffset;
        layoutParams.rightMargin = this.mStyle.xOffset;

        this.mView.setLayoutParams(layoutParams);

        // Set up touch to dismiss
        if (this.mStyle.touchToDismiss) {
            mView.setOnTouchListener(new View.OnTouchListener() {

                int timesTouched;
                @Override
                public boolean onTouch(View v, MotionEvent motionEvent) {
                    // Prevent repetitive touch events
                    if (timesTouched == 0 && motionEvent.getAction() == MotionEvent.ACTION_DOWN) dismiss();
                    timesTouched++;

                    return false; // Do not consume the event in case a Button listener is set
                }
            });
        } else {
            // Make sure no listener is set
            mView.setOnTouchListener(null);
        }
    }

    /**
     * Saves the state of all SuperToasts that are showing and/or pending.
     *
     * This should be called in the {@link android.app.Activity#onSaveInstanceState(android.os.Bundle)}
     * method of your Activity.
     *
     * @param bundle The Bundle provided in onSaveInstanceState()
     */
    @SuppressWarnings("unchecked")
    public static void onSaveState(Bundle bundle) {
        final ArrayList<Style> styleList = new ArrayList();

        // Create a list of every Style used by a SuperToast in the queue
        for (SuperToast superToast : Toaster.getInstance().getQueue()) {
            if (superToast instanceof SuperActivityToast) {
                superToast.getStyle().isSuperActivityToast = true;
            }
            styleList.add(superToast.getStyle());
        }

        bundle.putParcelableArrayList(BUNDLE_KEY, styleList);

        // Let's avoid any erratic behavior and cancel any showing/pending SuperActivityToasts manually
        Toaster.getInstance().cancelAllSuperToasts();
    }

    /**
     * Restores the state of all SuperToasts that were showing and/or pending.
     *
     * This should be called in the {@link android.app.Activity#onCreate(android.os.Bundle)}
     * method of your Activity.
     *
     * @param context The Activity Context
     * @param bundle The Bundle provided in onCreate()
     */
    public static void onRestoreState(Context context, Bundle bundle) {
        // The Bundle will be null sometimes
        if (bundle == null)  return;

        // Get the List created in onSaveState()
        final ArrayList<Style> styleList = bundle.getParcelableArrayList(BUNDLE_KEY);

        if (styleList == null) {
            Log.e(SuperActivityToast.class.getName(), "Cannot recreate " +
                    "SuperActivityToasts onRestoreState(). Was onSaveState() called?");
            return;
        }

        // Create a flag that knows if the SuperActivityToast is first in the List or not
        boolean firstInList = true;
        for (Style style : styleList) {
            if (!style.isSuperActivityToast) new SuperToast(context, style).show();
            else {
                // This SuperActivityToast was most likely showing before the orientation change so ignore the show animation
                if (firstInList)
                    new SuperActivityToast(context, style).fromOrientationChange().show();
                else new SuperActivityToast(context, style).show();
            }
            firstInList = false;
        }
    }

    /**
     * This should be called in the {@link android.app.Activity#onCreate(android.os.Bundle)}
     * method of your Activity. If you used an
     * {@link com.github.johnpersano.supertoasts.library.SuperToast.OnDismissListener}
     * or
     * {@link com.github.johnpersano.supertoasts.library.SuperActivityToast.OnButtonClickListener}
     * in your SuperActivityToast, use
     * {@link com.github.johnpersano.supertoasts.library.utils.ListenerUtils#newInstance()}
     * along with
     * {@link com.github.johnpersano.supertoasts.library.utils.ListenerUtils
     * #putListener(String, OnButtonClickListener)} and
     * {@link com.github.johnpersano.supertoasts.library.utils.ListenerUtils
     * #putListener(String, OnDismissListener)} to reattach them
     * after orientation change recovery.
     *
     * @param context The Activity Context
     * @param bundle The Bundle provided in onCreate()
     * @param listenerUtils A ListenerUtils object with every listener used added to it
     */
    public static void onRestoreState(Context context, Bundle bundle,
                                      ListenerUtils listenerUtils) {
        if (bundle == null) return; // The Bundle will be null sometimes

        // Get the List created in onSaveState()
        final ArrayList<Style> styleList = bundle.getParcelableArrayList(BUNDLE_KEY);

        if (styleList == null) {
            Log.e(SuperActivityToast.class.getName(), "Cannot recreate SuperActivityToasts onRestoreState(). Was " +
                    "onSaveState() called?");
            return;
        }

        // Create a flag that knows if the SuperActivityToast is first in the List or not
        boolean firstInList = true;
        for (Style style : styleList) {
            if (!style.isSuperActivityToast) new SuperToast(context, style).show();
            else {
                final SuperActivityToast superActivityToast = new SuperActivityToast(context, style);
                // This SuperActivityToast was most likely showing before the orientation change so ignore the show animation
                if (firstInList) superActivityToast.fromOrientationChange();

                final OnDismissListener onDismissListener = listenerUtils
                        .getOnDismissListenerHashMap().get(style.dismissTag);
                final OnButtonClickListener onButtonClickListener = listenerUtils
                        .getOnButtonClickListenerHashMap().get(style.buttonTag);

                // The SuperActivityToast had an OnDismissListener, reattach it
                if(onDismissListener != null) {
                    superActivityToast.setOnDismissListener(style.dismissTag,
                            style.dismissToken, onDismissListener);
                }
                // The SuperActivityToast had an OnButtonClickListener, reattach it
                if(onButtonClickListener != null) {
                    superActivityToast.setOnButtonClickListener(style.buttonTag,
                            style.buttonToken, onButtonClickListener);
                }
                superActivityToast.show();
            }
            firstInList = false;
        }
    }

    /**
     * Creates a simple SuperActivityToast. Don't forget to call {@link SuperToast#show()}.
     *
     * @param context An Activity Context
     * @return The newly created SuperActivityToast
     */
    public static SuperActivityToast create(@NonNull Context context) {
        return (SuperActivityToast) new SuperActivityToast(context);
    }

    /**
     * Creates a simple SuperActivityToast. Don't forget to call {@link SuperToast#show()}.
     *
     * @param context An Activity Context
     * @param style The desired Style of the SuperActivityToast
     * @return The newly created SuperActivityToast
     */
    public static SuperActivityToast create(@NonNull Context context, @NonNull Style style) {
        return (SuperActivityToast) new SuperActivityToast(context, style);
    }

    /**
     * Creates a simple SuperActivityToast. Don't forget to call {@link SuperToast#show()}.
     *
     * @param context An Activity Context
     * @param type The desired type of the SuperActivityToast
     *
     * @return The newly created SuperActivityToast
     */
    public static SuperActivityToast create(@NonNull Context context, @Style.Type int type) {
        return (SuperActivityToast) new SuperActivityToast(context, type);
    }

    /**
     * Creates a simple SuperActivityToast. Don't forget to call {@link SuperToast#show()}.
     *
     * @param context An Activity Context
     * @param style The desired Style of the SuperActivityToast
     * @param type The desired type of the SuperActivityToast
     * @return The newly created SuperActivityToast
     */
    public static SuperActivityToast create(@NonNull Context context, @NonNull Style style,
                                            @Style.Type int type) {
        return (SuperActivityToast) new SuperActivityToast(context, style, type);
    }

    /**
     * Creates a simple SuperActivityToast. Don't forget to call {@link SuperToast#show()}.
     *
     * @param context An Activity Context
     * @param style The desired Style of the SuperActivityToast
     * @param type The desired type of the SuperActivityToast/
     * @param viewGroup the id of a ViewGroup to add the SuperActivityToast to
     * @return The newly created SuperActivityToast
     */
    public static SuperActivityToast create(@NonNull Context context, @NonNull Style style,
                                            @Style.Type int type, @IdRes int viewGroup) {
        return (SuperActivityToast) new SuperActivityToast(context, style, type, viewGroup);
    }

    /**
     * Creates a simple SuperActivityToast. Don't forget to call {@link SuperToast#show()}.
     *
     * @param context An Activity Context
     * @param text The desired text to be shown
     * @param duration The desired duration of the SuperActivityToast
     * @return The newly created SuperActivityToast
     */
    public static SuperActivityToast create(@NonNull Context context, @NonNull String text, @Style.Duration int duration) {
        return (SuperActivityToast) new SuperActivityToast(context)
                .setText(text)
                .setDuration(duration);
    }

    /**
     * Creates a simple SuperActivityToast. Don't forget to call {@link SuperToast#show()}.
     *
     * @param context An Activity Context
     * @param text The desired text to be shown
     * @param duration The desired duration of the SuperActivityToast
     * @param style The desired Style of the SuperToast
     * @return The newly created SuperActivityToast
     */
    public static SuperActivityToast create(@NonNull Context context, @NonNull String text, @Style.Duration int duration,
                                            @NonNull Style style) {
        return (SuperActivityToast)  new SuperActivityToast(context, style)
                .setText(text)
                .setDuration(duration);
    }

    /**
     * Creates a simple SuperActivityToast. Don't forget to call {@link SuperToast#show()}.
     *
     * @param context An Activity Context
     * @param text The desired text to be shown
     * @param duration The desired duration of the SuperActivityToast
     * @param style The desired Style of the SuperToast
     * @param viewGroup The ViewGroup to attach the SuperActivityToast to
     * @return The newly created SuperActivityToast
     */
    public static SuperActivityToast create(@NonNull Context context, @NonNull String text, @Style.Duration int duration,
                                            @NonNull Style style, @IdRes int viewGroup) {
        return (SuperActivityToast)  new SuperActivityToast(context, style, Style.TYPE_STANDARD, viewGroup)
                .setText(text)
                .setDuration(duration);
    }
}
