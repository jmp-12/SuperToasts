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

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Parcelable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.github.johnpersano.supertoasts.library.utils.AccessibilityUtils;
import com.github.johnpersano.supertoasts.library.utils.AnimationUtils;
import com.github.johnpersano.supertoasts.library.utils.BackgroundUtils;

/**
 * SuperToasts are designed to improve upon the stock {@link android.widget.Toast} class and 
 * should be used in similar situations. For usage information, check out the 
 * <a href="https://github.com/JohnPersano/Supertoasts/wiki/SuperToast">SuperToast Wiki page</a>.
 */
@SuppressWarnings("ALL")
public class SuperToast {

    /**
     * Listener that calls onDismiss() when a SuperToast or SuperActivityToast is dismissed. 
     * 
     * @see #setOnDismissListener(String, android.os.Parcelable, com.github.johnpersano.supertoasts.library.SuperToast.OnDismissListener)  
     */
    public interface OnDismissListener {

        /**
         * Called when the SuperToast or SuperActivityToast is dismissed.
         *
         * @param view The View that was removed from the ViewGroup
         * @param token A Parcelable token that can hold data across orientation changes
         */
         void onDismiss(View view, Parcelable token);
    }

    private final Context mContext;
    private final View mView;
    private final TextView mTextView;
    private Style mStyle;
    private OnDismissListener mOnDismissListener;

    /**
     * Public constructor for a SuperToast.
     *
     * @param context A valid Context
     */
    public SuperToast(@NonNull Context context) {
        this.mContext = context;
        this.mStyle = new Style();
        this.mStyle.type = Style.TYPE_STANDARD;

        final LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mView = onCreateView(context, layoutInflater, Style.TYPE_STANDARD);
        this.mTextView = (TextView) this.mView.findViewById(R.id.message);
    }

    /**
     * Public constructor for a SuperToast.
     *
     * @param context A valid Context
     * @param style The desired Style             
     */
    public SuperToast(@NonNull Context context, @NonNull Style style) {
        this.mContext = context;
        this.mStyle = style;

        final LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mView = onCreateView(context, layoutInflater, this.mStyle.type);
        this.mTextView = (TextView) this.mView.findViewById(R.id.message);
    }

    /**
     * Protected constructor that is overridden by the SuperActivityToast class.         
     */
    protected SuperToast(@NonNull Context context, @Style.Type int type) {
        this.mContext = context;
        this.mStyle = new Style();
        this.mStyle.type = type;

        final LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mView = onCreateView(context, layoutInflater, type);
        this.mTextView = (TextView) this.mView.findViewById(R.id.message);
    }

    /**
     * Protected constructor that is overridden by the SuperActivityToast class.         
     */
    protected SuperToast(@NonNull Context context,  @NonNull Style style, @Style.Type int type) {
        this.mContext = context;
        this.mStyle = style;
        this.mStyle.type = type;

        final LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mView = onCreateView(context, layoutInflater, type);
        this.mTextView = (TextView) this.mView.findViewById(R.id.message);
    }

    /**
     * Protected constructor that is overridden by the SuperActivityToast class.         
     */
    protected SuperToast(@NonNull Context context,  @NonNull Style style, @Style.Type int type, @IdRes int viewGroupID) {
        this.mContext = context;
        this.mStyle = style;
        this.mStyle.type = type;

        // TYPE_BUTTON styles are the only ones that look different from the styles set by the Style() constructor
        if (type == Style.TYPE_BUTTON) {
            this.mStyle.yOffset = BackgroundUtils.convertToDIP(24);
            this.mStyle.width = FrameLayout.LayoutParams.MATCH_PARENT;
        }

        final LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mView = onCreateView(context, layoutInflater, type);
        this.mTextView = (TextView) this.mView.findViewById(R.id.message);
    }

    /**
     * Protected View that is overridden by the SuperActivityToast class.         
     */
    @SuppressLint("InflateParams")
    protected View onCreateView(Context context, LayoutInflater layoutInflater, int type) {
        return layoutInflater.inflate(R.layout.supertoast, null);
    }

    /**
     * Modify various attributes of the SuperToast before being shown.
     */
    @SuppressLint("NewApi")
    @SuppressWarnings("deprecation")
    protected void onPrepareShow() {
        final int sdkVersion = android.os.Build.VERSION.SDK_INT;

        // Modify various attributes of the message TextView
        this.mTextView.setText(this.mStyle.message);
        this.mTextView.setTypeface(this.mTextView.getTypeface(), this.mStyle.messageTypefaceStyle);
        this.mTextView.setTextColor(this.mStyle.messageTextColor);
        this.mTextView.setTextSize(this.mStyle.messageTextSize);
        if (this.mStyle.messageIconResource > 0) {
            if (this.mStyle.messageIconPosition == Style.ICONPOSITION_LEFT) {
                this.mTextView.setCompoundDrawablesWithIntrinsicBounds(
                        this.mStyle.messageIconResource, 0, 0, 0);
            } else if (this.mStyle.messageIconPosition == Style.ICONPOSITION_TOP) {
                this.mTextView.setCompoundDrawablesWithIntrinsicBounds(
                        0, this.mStyle.messageIconResource, 0, 0);
            } else if (this.mStyle.messageIconPosition == Style.ICONPOSITION_RIGHT) {
                this.mTextView.setCompoundDrawablesWithIntrinsicBounds(
                        0, 0, this.mStyle.messageIconResource, 0);
            } else if (this.mStyle.messageIconPosition == Style.ICONPOSITION_BOTTOM) {
                this.mTextView.setCompoundDrawablesWithIntrinsicBounds(
                        0, 0, 0, this.mStyle.messageIconResource);
            }
        }

        // Handle depreciated API for setting the background
        if (sdkVersion >= Build.VERSION_CODES.JELLY_BEAN) {
            this.mView.setBackground(BackgroundUtils.getBackground(this.mStyle, this.mStyle.color));
            // Give Lollipop devices a nice shadow (does not work with transparent backgrounds)
            if (sdkVersion >= Build.VERSION_CODES.LOLLIPOP) mView.setElevation(10f);
        } else this.mView.setBackgroundDrawable(BackgroundUtils.getBackground(this.mStyle, this.mStyle.color));

        // Make adjustments that are specific to Lollipop frames
        if (this.mStyle.frame == Style.FRAME_LOLLIPOP) {
            this.mTextView.setGravity(Gravity.START);

            // We are on a big screen device, show the SuperToast on the bottom left with padding
            if ((this.mContext.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK)
                    >= Configuration.SCREENLAYOUT_SIZE_LARGE) {
                this.mStyle.xOffset = BackgroundUtils.convertToDIP(12);
                this.mStyle.yOffset = BackgroundUtils.convertToDIP(12);
                this.mStyle.width = BackgroundUtils.convertToDIP(288);
                this.mStyle.gravity = Gravity.BOTTOM | Gravity.START;

                // Simple background shape with rounded corners
                final GradientDrawable gradientDrawable = new GradientDrawable();
                gradientDrawable.setCornerRadius(BackgroundUtils.convertToDIP(2));
                gradientDrawable.setColor(this.mStyle.color);

                // Handle depreciated API for setting the background
                if (sdkVersion >= Build.VERSION_CODES.JELLY_BEAN) this.mView.setBackground(gradientDrawable);
                else this.mView.setBackgroundDrawable(gradientDrawable);

            // We are NOT on a big screen device, show the SuperToast on the bottom with NO padding
            } else {
                this.mStyle.yOffset = 0;
                this.mStyle.width = FrameLayout.LayoutParams.MATCH_PARENT;
            }
            
            // Set the priority color of the Lollipop frame if any
            if (this.mStyle.priorityColor != 0) {
                mView.findViewById(R.id.border).setVisibility(View.VISIBLE);
                mView.findViewById(R.id.border).setBackgroundColor(this.mStyle.priorityColor);
            }
        }
        // Used for PriorityQueue comparisons
        this.getStyle().timestamp = System.currentTimeMillis();
    }

    /**
     * Sets the text of the main TextView.
     *  
     * @param message The message to be displayed
     * @return The current SuperToast instance
     */
    public SuperToast setText(String message) {
        this.mStyle.message = message;
        return this;
    }

    /**
     * Returns the text of the main TextView.
     *
     * @return The message text
     */
    public String getText() {
        return this.mStyle.message;
    }

    /**
     * Sets the duration of the SuperToast. The duration cannot exceed 4500ms.
     * If a longer duration is required, use {@link SuperActivityToast}s instead
     * as they can be made indeterminate.
     * @see {@link SuperActivityToast#setIndeterminate(boolean)}
     *
     * @param duration The desired show duration
     * @return The current SuperToast instance
     */
    public SuperToast setDuration(@Style.Duration int duration) {
        // There is no need for a SuperToast with an ultra long duration
        if (duration > Style.DURATION_VERY_LONG) {
            Log.e(getClass().getName(), "SuperToast duration cannot exceed 4500ms.");
            this.mStyle.duration = Style.DURATION_VERY_LONG;
            return this;
        }
        this.mStyle.duration = duration;
        return this;
    }

    /**
     * Returns the duration of the SuperToast.
     *
     * @return The SuperToast duration in milliseconds 
     */
    @Style.Duration
    public int getDuration() {
        return this.mStyle.duration;
    }

    /**
     * Sets the color of the SuperToast. Sample colors
     * can be found in {@link Style} class.
     *
     * @param color The desired color
     * @return The current SuperToast instance
     */
    public SuperToast setColor(@ColorInt int color) {
        this.mStyle.color = color;
        return this;
    }

    /**
     * Returns the color of the SuperToast.
     *
     * @return The color value
     */
    @ColorInt
    public int getColor() {
        return this.mStyle.color;
    }

    /**
     * Sets the priority level of the SuperToast. A SuperToast with a
     * higher priority will be shown before a SuperToast with a lower
     * priority. This will <b>not</b> affect any SuperToasts that are
     * already showing.
     *
     * @param priorityLevel The desired priority level
     * @return The current SuperToast instance
     */
    public SuperToast setPriorityLevel(@Style.PriorityLevel int priorityLevel) {
        this.mStyle.priorityLevel = priorityLevel;
        return this;
    }

    /**
     * Returns the priority level of the SuperToast.
     *
     * @return The SuperToast duration in milliseconds
     */
    @Style.PriorityLevel
    public int getPriorityLevel() {
        return this.mStyle.priorityLevel;
    }

    /**
     * Sets the priority color of the SuperToast. The priority color is a
     * small border line at the top of the SuperToast and is only used with
     * Lollipop frames.
     *
     * @param priorityColor The desired color
     * @return The current SuperToast instance
     */
    public SuperToast setPriorityColor(@ColorInt int priorityColor) {
        this.mStyle.priorityColor = priorityColor;
        return this;
    }

    /**
     * Returns the priority color of the SuperToast.
     *
     * @return The color value
     */
    @ColorInt
    public int getPriorityColor() {
        return this.mStyle.priorityColor;
    }

    /**
     * Sets the Typeface style of the main TextView.
     *
     * @param typefaceStyle The desired Typeface style
     * @return The current SuperToast instance
     */
    public SuperToast setTypefaceStyle(@Style.TypefaceStyle int typefaceStyle) {
        this.mStyle.messageTypefaceStyle = typefaceStyle;
        return this;
    }

    /**
     * Returns the Typeface style of the main TextView.
     *
     * @return The Typeface style
     */
    @Style.TypefaceStyle
    public int getTypefaceStyle() {
        return this.mStyle.messageTypefaceStyle;
    }

    /**
     * Sets the text color of the main TextView.
     *
     * @param textColor The desired text color
     * @return The current SuperToast instance
     */
    public SuperToast setTextColor(@ColorInt int textColor) {
        this.mStyle.messageTextColor = textColor;
        return this;
    }

    /**
     * Returns the text color of the main TextView.
     *
     * @return The text color value
     */
    @ColorInt
    public int getTextColor() {
        return this.mStyle.messageTextColor;
    }

    /**
     * Sets the text size of the main TextView. This value cannot be below 12.
     *
     * @param textSize The desired text size
     * @return The current SuperToast instance
     */
    public SuperToast setTextSize(@Style.TextSize int textSize) {
        if (textSize < Style.TEXTSIZE_VERY_SMALL) {
            Log.e(getClass().getName(), "SuperToast text size cannot be below 12.");
            this.mStyle.messageTextSize = Style.TEXTSIZE_VERY_SMALL;
            return this;
        } else if (textSize > Style.TEXTSIZE_VERY_LARGE) {
            Log.e(getClass().getName(), "SuperToast text size cannot be above 20.");
            this.mStyle.messageTextSize = Style.TEXTSIZE_VERY_LARGE;
            return this;
        }
        this.mStyle.messageTextSize = textSize;
        return this;
    }

    /**
     * Returns the text size of the main TextView.
     *
     * @return The text size value
     */
    @Style.TextSize
    public int getTextSize() {
        return this.mStyle.messageTextSize;
    }

    /**
     * Sets the icon position and icon resource of the main TextView.
     * The recommended icon position for most applications is
     * {@link Style#ICONPOSITION_LEFT}.
     *
     * @param iconPosition The desired icon position
     * @param iconResource The desired icon resource
     * @return The current SuperToast instance
     * 
     * @see #setIconPosition(int) 
     * @see #setIconResource(int)  
     */
    public SuperToast setIconResource(@Style.IconPosition int iconPosition,
                                      @DrawableRes int iconResource) {
        this.mStyle.messageIconPosition = iconPosition;
        this.mStyle.messageIconResource = iconResource;
        return this;
    }

    /**
     * Sets the icon position of the main TextView.
     * The recommended icon position for most applications is
     * {@link Style#ICONPOSITION_LEFT}.
     *
     * @param iconPosition The desired icon position
     * @return The current SuperToast instance
     *
     * @see #setIconResource(int, int) 
     * @see #setIconResource(int)
     */
    public SuperToast setIconPosition(@Style.IconPosition int iconPosition) {
        this.mStyle.messageIconPosition = iconPosition;
        return this;
    }

    /**
     * Sets the icon resource of the main TextView.
     *
     * @param iconResource The desired icon resource
     * @return The current SuperToast instance
     *
     * @see #setIconResource(int, int)
     * @see #setIconPosition(int)
     */
    public SuperToast setIconResource(@DrawableRes int iconResource) {
        this.mStyle.messageIconResource = iconResource;
        return this;
    }

    /**
     * Returns the icon resource of the main TextView.
     *
     * @return The icon resource value
     */
    @DrawableRes
    public int getIconResource() {
        return this.mStyle.messageIconResource;
    }

    /**
     * Returns the icon position of the main TextView.
     *
     * @return The icon position value
     */
    @Style.IconPosition
    public int getIconPosition() {
        return this.mStyle.messageIconPosition;
    }

    /**
     * Sets the frame of the SuperToast. If this is not set, 
     * an appropriate frame will be chosen based on the device's SDK level. 
     *
     * @param frame The desired frame
     * @return The current SuperToast instance
     */
    public SuperToast setFrame(@Style.Frame int frame) {
        this.mStyle.frame = frame;
        return this;
    }

    /**
     * Returns the frame of the SuperToast.
     *
     * @return The frame value
     */
    @Style.Frame
    public int getFrame() {
        return this.mStyle.frame;
    }

    /**
     * Sets the show and hide animations value of the SuperToast.  
     *
     * @param animations The desired animations value
     * @return The current SuperToast instance
     */
    public SuperToast setAnimations(@Style.Animations int animations) {
        this.mStyle.animations = animations;
        return this;
    }
    
    /**
     * Returns the animations value of the SuperToast.
     *
     * @return The animations value
     */
    public int getAnimations() {
        return this.mStyle.animations;
    }

    /**
     * Sets the layout gravity, x, and y offsets for the SuperToast.
     *
     * @param gravity The desired Gravity
     * @param xOffset The desired x (horizontal) offset
     * @param yOffset The desired y (vertical) offset
     * @return The current SuperToast instance
     * 
     * @see #setGravity(int)  
     */
    public SuperToast setGravity(@Style.GravityStyle int gravity, int xOffset, int yOffset) {
        this.mStyle.gravity = gravity;
        this.mStyle.xOffset = xOffset;
        this.mStyle.yOffset = yOffset;
        return this;
    }

    /**
     * Sets the layout gravity of the SuperToast.
     *
     * @param gravity The desired Gravity
     * @return The current SuperToast instance
     *
     * @see #setGravity(int, int, int) 
     */
    public SuperToast setGravity(@Style.GravityStyle int gravity) {
        this.mStyle.gravity = gravity;
        return this;
    }

    /**
     * Returns the Gravity value of the SuperToast.
     *
     * @return The Gravity value
     */
    @Style.GravityStyle
    public int getGravity() {
        return this.mStyle.gravity;
    }

    /**
     * Returns the x offset value of the SuperToast.
     *
     * @return The x offset (horizontal)
     */
    public int getXOffset() {
        return this.mStyle.xOffset;
    }

    /**
     * Returns the y offset value of the SuperToast.
     *
     * @return The y offset (vertical)
     */
    public int getYOffset() {
        return this.mStyle.yOffset;
    }

    /**
     * Sets the width of the SuperToast. There are not many usage cases
     * where using this method is necessary. 
     *
     * @param width The desired width
     * @return The current SuperToast instance
     */
    public SuperToast setWidth(int width) {
        this.mStyle.width = width;
        return this;
    }

    /**
     * Returns the desired width of the SuperToast.
     *
     * @return The desired width
     */
    public int getWidth() {
        return this.mStyle.width;
    }

    /**
     * Sets the height of the SuperToast. There are not many usage cases
     * where using this method is necessary. 
     *
     * @param height The desired height
     * @return The current SuperToast instance
     */
    public SuperToast setHeight(int height) {
        this.mStyle.height = height;
        return this;
    }

    /**
     * Returns the desired height of the SuperToast.
     *
     * @return The desired height
     */
    public int getHeight() {
        return this.mStyle.height;
    }

    /**
     * Sets the {@link com.github.johnpersano.supertoasts.library.SuperToast.OnDismissListener} 
     * of the SuperActivityToast. The listener will be triggered when the
     * SuperActivityToast is dismissed.
     *
     * @param tag A unique tag for this listener
     * @param token A Parcelable token to hold data across orientation changes
     * @param onDismissListener The desired OnDismissListener
     * @return The current SuperActivityToast instance
     * 
     * @see #setOnDismissListener(String,
     * com.github.johnpersano.supertoasts.library.SuperToast.OnDismissListener)
     */
    protected SuperToast setOnDismissListener(String tag, Parcelable token,
                                              @NonNull OnDismissListener onDismissListener) {
        this.mOnDismissListener = onDismissListener;
        this.mStyle.dismissTag = tag;
        this.mStyle.dismissToken = token;
        return this;
    }

    /**
     * Sets the {@link com.github.johnpersano.supertoasts.library.SuperToast.OnDismissListener}
     * of the SuperActivityToast. The listener will be triggered when the
     * SuperActivityToast is dismissed.
     *
     * @param tag A unique tag for this listener
     * @param onDismissListener The desired OnDismissListener
     * @return The current SuperActivityToast instance
     *
     * @see #setOnDismissListener(String, android.os.Parcelable,
     * com.github.johnpersano.supertoasts.library.SuperToast.OnDismissListener)
     */
    protected SuperToast setOnDismissListener(String tag,
                                              @NonNull OnDismissListener onDismissListener) {
        this.mOnDismissListener = onDismissListener;
        this.mStyle.dismissTag = tag;
        this.mStyle.dismissToken = null;
        return this;
    }

    /**
     * Sets the {@link com.github.johnpersano.supertoasts.library.SuperToast.OnDismissListener}
     * of the SuperToast. The listener will be triggered when the SuperToast is dismissed.
     *
     * @param onDismissListener The desired OnDismissListener
     * @return The current SuperToast instance
     */
    public SuperToast setOnDismissListener(@NonNull OnDismissListener onDismissListener) {
        this.mOnDismissListener = onDismissListener;
        this.mStyle.dismissTag = "";
        this.mStyle.dismissToken = null;
        return this;
    }

    /**
     * Returns the dismiss tag of the SuperActivityToast.
     *
     * @return The dismiss tag
     */
    protected String getDismissTag() {
        return this.mStyle.dismissTag;
    }

    /**
     * Returns the dismiss Parcelable token of the SuperActivityToast.
     *
     * @return The dismiss Parcelable token
     */
    protected Parcelable getDismissToken() {
        return this.mStyle.dismissToken;
    }

    /**
     * Returns the {@link com.github.johnpersano.supertoasts.library.SuperToast.OnDismissListener} 
     * of the SuperToast.
     *
     * @return The OnDismissListener
     */
    public OnDismissListener getOnDismissListener() {
        return this.mOnDismissListener;
    }

    /**
     * Sets the {@link com.github.johnpersano.supertoasts.library.Style} of the SuperToast. 
     *
     * @param style The desired Style
     * @return The current SuperToast instance
     */
    protected SuperToast setStyle(Style style) {
        this.mStyle = style;
        return this;
    }

    /**
     * Returns the {@link com.github.johnpersano.supertoasts.library.Style} of the SuperToast.
     *
     * @return The Style
     */
    public Style getStyle() {
        return this.mStyle;
    }

    /**
     * Returns the Context used with this SuperToast.
     *
     * @return The Context
     */
    public Context getContext() {
        return this.mContext;
    }

    /**
     * Returns the main View of the SuperToast.
     *
     * @return The main View
     */
    public View getView() {
        return this.mView;
    }

    /**
     * Returns true if the SuperToast is showing.
     *
     * @return true if the SuperToast is showing
     */
    public boolean isShowing() {
        return mView != null && mView.isShown();
    }

    /**
     * Protected method used by
     * {@link com.github.johnpersano.supertoasts.library.Toaster#displaySuperToast(SuperToast)}
     */
    protected WindowManager.LayoutParams getWindowManagerParams() {
        final WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.height = this.mStyle.height;
        layoutParams.width = this.mStyle.width;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        layoutParams.format = PixelFormat.TRANSLUCENT;
        layoutParams.windowAnimations = AnimationUtils.getSystemAnimationsResource(mStyle.animations);
        layoutParams.type = WindowManager.LayoutParams.TYPE_TOAST;
        layoutParams.gravity = this.mStyle.gravity;
        layoutParams.x = this.mStyle.xOffset;
        layoutParams.y = this.mStyle.yOffset;
        return layoutParams;
    }

    /**
     * Shows the SuperToast. If any SuperToast is already showing, this SuperToast
     * will be enqueued until the others have finished (depending on priority level).
     */
    public void show() {
        this.onPrepareShow();
        Toaster.getInstance().add(this);
        AccessibilityUtils.sendAccessibilityEvent(this.mView);
    }

    /**
     * Dismissed the SuperToast if it is showing.
     */
    public void dismiss() {
        Toaster.getInstance().removeSuperToast(this);
    }

    /**
     * Dismisses every SuperToast and SuperActivityToast that is showing and
     * cancels everything in the queue.
     */
    public static void cancelAllSuperToasts() {
        Toaster.getInstance().cancelAllSuperToasts();
    }

    /**
     * Returns the number of SuperToasts in the queue.
     *
     * @return The queue size
     */
    public static int getQueueSize() {
        return Toaster.getInstance().getQueue().size();
    }

    /**
     * Creates a simple SuperToast. Don't forget to call {@link SuperToast#show()}.
     *
     * @param context A valid Context
     * @param text The desired text to be shown
     * @param duration The desired duration of the SuperToast
     *                      
     * @return The newly created SuperToast
     */
    public static SuperToast create(@NonNull Context context, @NonNull String text,
                                    @Style.Duration int duration) {
        return new SuperToast(context)
                .setText(text)
                .setDuration(duration);
    }

    /**
     * Creates a simple SuperToast. Don't forget to call {@link SuperToast#show()}.
     *
     * @param context A valid Context
     * @param text The desired text to be shown
     * @param duration The desired duration of the SuperToast
     * @param style The desired Style of the SuperToast
     *
     * @return The newly created SuperToast
     */
    public static SuperToast create(@NonNull Context context, @NonNull String text,
                                    @Style.Duration int duration, @NonNull Style style) {
        return new SuperToast(context, style)
                .setText(text)
                .setDuration(duration);
    }
}