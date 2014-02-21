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

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Parcelable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.github.johnpersano.supertoasts.util.Style;

/**
 * SuperToasts are designed to replace stock Android Toasts.
 * If you need to display a SuperToast inside of an Activity
 * please see {@link SuperActivityToast}.
 */
@SuppressWarnings("UnusedDeclaration")
public class SuperToast {

    private static final String TAG = "SuperToast";

    private static final String ERROR_CONTEXTNULL = " - You cannot use a null context.";
    private static final String ERROR_DURATIONTOOLONG = " - You should NEVER specify a duration greater than " +
            "four and a half seconds for a SuperToast.";

    /**
     * Custom OnClickListener to be used with SuperActivityToasts/SuperCardToasts. Note that
     * SuperActivityToasts/SuperCardToasts must use this with an
     * {@link com.github.johnpersano.supertoasts.util.OnClickWrapper}
     */
    public interface OnClickListener {

        public void onClick(View view, Parcelable token);

    }

    /**
     * Custom OnDismissListener to be used with any type of SuperToasts. Note that
     * SuperActivityToasts/SuperCardToasts must use this with an
     * {@link com.github.johnpersano.supertoasts.util.OnDismissWrapper}
     */
    public interface OnDismissListener {

        public void onDismiss(View view);

    }

    /**
     * Backgrounds for all types of SuperToasts.
     */
    public static class Background {

        public static final int BLACK = Style.getBackground(Style.BLACK);
        public static final int BLUE = Style.getBackground(Style.BLUE);
        public static final int GRAY = Style.getBackground(Style.GRAY);
        public static final int GREEN = Style.getBackground(Style.GREEN);
        public static final int ORANGE = Style.getBackground(Style.ORANGE);
        public static final int PURPLE = Style.getBackground(Style.PURPLE);
        public static final int RED = Style.getBackground(Style.RED);
        public static final int WHITE = Style.getBackground(Style.WHITE);

    }

    /**
     * Animations for all types of SuperToasts.
     */
    public enum Animations {

        FADE,
        FLYIN,
        SCALE,
        POPUP

    }

    /**
     * Icons for all types of SuperToasts.
     */
    public static class Icon {

        /**
         * Icons for all types of SuperToasts with a dark background.
         */
        public static class Dark {

            public static final int EDIT = (R.drawable.icon_dark_edit);
            public static final int EXIT = (R.drawable.icon_dark_exit);
            public static final int INFO = (R.drawable.icon_dark_info);
            public static final int REDO = (R.drawable.icon_dark_redo);
            public static final int REFRESH = (R.drawable.icon_dark_refresh);
            public static final int SAVE = (R.drawable.icon_dark_save);
            public static final int SHARE = (R.drawable.icon_dark_share);
            public static final int UNDO = (R.drawable.icon_dark_undo);

        }

        /**
         * Icons for all types of SuperToasts with a light background.
         */
        public static class Light {

            public static final int EDIT = (R.drawable.icon_light_edit);
            public static final int EXIT = (R.drawable.icon_light_exit);
            public static final int INFO = (R.drawable.icon_light_info);
            public static final int REDO = (R.drawable.icon_light_redo);
            public static final int REFRESH = (R.drawable.icon_light_refresh);
            public static final int SAVE = (R.drawable.icon_light_save);
            public static final int SHARE = (R.drawable.icon_light_share);
            public static final int UNDO = (R.drawable.icon_light_undo);

        }

    }

    /**
     * Durations for all types of SuperToasts.
     */
    public static class Duration {

        public static final int VERY_SHORT = (1500);
        public static final int SHORT = (2000);
        public static final int MEDIUM = (2750);
        public static final int LONG = (3500);
        public static final int EXTRA_LONG = (4500);

    }

    /**
     * Text sizes for all types of SuperToasts.
     */
    public static class TextSize {

        public static final int EXTRA_SMALL = (12);
        public static final int SMALL = (14);
        public static final int MEDIUM = (16);
        public static final int LARGE = (18);

    }

    /**
     * Types for SuperActivityToasts and SuperCardToasts.
     */
    public enum Type {

        /**
         * Standard type used for displaying messages.
         */
        STANDARD,

        /**
         * Progress type used for showing progress.
         */
        PROGRESS,

        /**
         * Progress type used for showing progress.
         */
        PROGRESS_HORIZONTAL,

        /**
         * Button type used for receiving click actions.
         */
        BUTTON

    }

    /**
     * Positions for icons used in all types of SuperToasts.
     */
    public enum IconPosition {

        /**
         * Set the icon to the left of the text.
         */
        LEFT,

        /**
         * Set the icon to the right of the text.
         */
        RIGHT,

        /**
         * Set the icon on top of the text.
         */
        TOP,

        /**
         * Set the icon on the bottom of the text.
         */
        BOTTOM

    }

    private Animations mAnimations = Animations.FADE;
    private Context mContext;
    private int mGravity = Gravity.BOTTOM | Gravity.CENTER;
    private int mDuration = Duration.SHORT;
    private int mTypefaceStyle;
    private int mBackground;
    private int mXOffset = 0;
    private int mYOffset = 0;
    private LinearLayout mRootLayout;
    private OnDismissListener mOnDismissListener;
    private TextView mMessageTextView;
    private View mToastView;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mWindowManagerParams;

    /**
     * Instantiates a new {@value #TAG}.
     *
     * @param context {@link android.content.Context}
     */
    public SuperToast(Context context) {

        if (context == null) {

            throw new IllegalArgumentException(TAG + ERROR_CONTEXTNULL);

        }

        this.mContext = context;

        mYOffset = context.getResources().getDimensionPixelSize(
                R.dimen.toast_hover);

        final LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mToastView = layoutInflater.inflate(R.layout.supertoast, null);

        mWindowManager = (WindowManager) mToastView.getContext()
                .getApplicationContext().getSystemService(Context.WINDOW_SERVICE);

        mRootLayout = (LinearLayout)
                mToastView.findViewById(R.id.root_layout);

        mMessageTextView = (TextView)
                mToastView.findViewById(R.id.message_textview);

    }

    /**
     * Instantiates a new {@value #TAG} with a specified style.
     *
     * @param context {@link android.content.Context}
     * @param style   {@link com.github.johnpersano.supertoasts.util.Style}
     */
    public SuperToast(Context context, Style style) {

        if (context == null) {

            throw new IllegalArgumentException(TAG + ERROR_CONTEXTNULL);

        }

        this.mContext = context;

        mYOffset = context.getResources().getDimensionPixelSize(
                R.dimen.toast_hover);

        final LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mToastView = layoutInflater.inflate(R.layout.supertoast, null);

        mWindowManager = (WindowManager) mToastView.getContext()
                .getApplicationContext().getSystemService(Context.WINDOW_SERVICE);

        mRootLayout = (LinearLayout)
                mToastView.findViewById(R.id.root_layout);

        mMessageTextView = (TextView)
                mToastView.findViewById(R.id.message_textview);

        this.setStyle(style);

    }

    /**
     * Shows the {@value #TAG}. If another {@value #TAG} is showing than
     * this one will be added to a queue and shown when the previous {@value #TAG}
     * is dismissed.
     */
    public void show() {

        mWindowManagerParams = new WindowManager.LayoutParams();

        mWindowManagerParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowManagerParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowManagerParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        mWindowManagerParams.format = PixelFormat.TRANSLUCENT;
        mWindowManagerParams.windowAnimations = getAnimation();
        mWindowManagerParams.type = WindowManager.LayoutParams.TYPE_TOAST;
        mWindowManagerParams.gravity = mGravity;
        mWindowManagerParams.x = mXOffset;
        mWindowManagerParams.y = mYOffset;

        ManagerSuperToast.getInstance().add(this);

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

        if(duration > Duration.EXTRA_LONG) {

            Log.e(TAG, TAG + ERROR_DURATIONTOOLONG);

            this.mDuration = Duration.EXTRA_LONG;

        } else {

            this.mDuration = duration;

        }

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
     * Sets an icon resource to the {@value #TAG} with a specified position.
     *
     * @param iconResource {@link com.github.johnpersano.supertoasts.SuperToast.Icon}
     * @param iconPosition {@link com.github.johnpersano.supertoasts.SuperToast.IconPosition}
     */
    public void setIcon(int iconResource, IconPosition iconPosition) {

        if (iconPosition == IconPosition.BOTTOM) {

            mMessageTextView.setCompoundDrawablesWithIntrinsicBounds(null, null,
                    null, mContext.getResources().getDrawable(iconResource));

        } else if (iconPosition == IconPosition.LEFT) {

            mMessageTextView.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources()
                    .getDrawable(iconResource), null, null, null);

        } else if (iconPosition == IconPosition.RIGHT) {

            mMessageTextView.setCompoundDrawablesWithIntrinsicBounds(null, null,
                    mContext.getResources().getDrawable(iconResource), null);

        } else if (iconPosition == IconPosition.TOP) {

            mMessageTextView.setCompoundDrawablesWithIntrinsicBounds(null,
                    mContext.getResources().getDrawable(iconResource), null, null);

        }

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
     * Sets the gravity of the {@value #TAG} along with x and y offsets.
     *
     * @param gravity {@link android.view.Gravity} int
     * @param xOffset int
     * @param yOffset int
     */
    public void setGravity(int gravity, int xOffset, int yOffset) {

        this.mGravity = gravity;
        this.mXOffset = xOffset;
        this.mYOffset = yOffset;

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
     * Sets an OnDismissListener defined in this library
     * to the {@value #TAG}. Does not require wrapper.
     *
     * @param onDismissListener {@link com.github.johnpersano.supertoasts.SuperToast.OnDismissListener}
     */
    public void setOnDismissListener(OnDismissListener onDismissListener) {

        this.mOnDismissListener = onDismissListener;

    }

    /**
     * Returns the OnDismissListener set to the {@value #TAG}.
     *
     * @return {@link com.github.johnpersano.supertoasts.SuperToast.OnDismissListener}
     */
    public OnDismissListener getOnDismissListener() {

        return mOnDismissListener;

    }

    /**
     * Dismisses the {@value #TAG}.
     */
    public void dismiss() {

        ManagerSuperToast.getInstance().removeSuperToast(this);

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
     * Returns the window manager that the {@value #TAG} is attached to.
     *
     * @return {@link android.view.WindowManager}
     */
    public WindowManager getWindowManager() {

        return mWindowManager;

    }

    /**
     * Returns the window manager layout params of the {@value #TAG}.
     *
     * @return {@link android.view.WindowManager.LayoutParams}
     */
    public WindowManager.LayoutParams getWindowManagerParams() {

        return mWindowManagerParams;

    }

    /**
     * Private method used to return a specific animation for a animations enum
     */
    private int getAnimation() {

        if (mAnimations == Animations.FLYIN) {

            return android.R.style.Animation_Translucent;

        } else if (mAnimations == Animations.SCALE) {

            return android.R.style.Animation_Dialog;

        } else if (mAnimations == Animations.POPUP) {

            return android.R.style.Animation_InputMethod;

        } else {

            return android.R.style.Animation_Toast;

        }

    }

    /**
     * Private method used to set a default style to the {@value #TAG}
     */
    private void setStyle(Style style) {

        this.setAnimations(style.animations);
        this.setTypefaceStyle(style.typefaceStyle);
        this.setTextColor(style.textColor);
        this.setBackground(style.background);

    }

    /**
     * Returns a standard {@value #TAG}.
     *
     * @param context          {@link android.content.Context}
     * @param textCharSequence {@link CharSequence}
     * @param durationInteger  {@link com.github.johnpersano.supertoasts.SuperToast.Duration}
     *
     * @return {@link SuperToast}
     */
    public static SuperToast create(Context context, CharSequence textCharSequence,
                                              int durationInteger) {

        SuperToast superToast = new SuperToast(context);
        superToast.setText(textCharSequence);
        superToast.setDuration(durationInteger);

        return superToast;

    }

    /**
     * Returns a standard {@value #TAG} with specified animations.
     *
     * @param context          {@link android.content.Context}
     * @param textCharSequence {@link CharSequence}
     * @param durationInteger  {@link com.github.johnpersano.supertoasts.SuperToast.Duration}
     * @param animations       {@link com.github.johnpersano.supertoasts.SuperToast.Animations}
     *
     * @return {@link SuperToast}
     */
    public static SuperToast create(Context context, CharSequence textCharSequence,
                                              int durationInteger, Animations animations) {

        final SuperToast superToast = new SuperToast(context);
        superToast.setText(textCharSequence);
        superToast.setDuration(durationInteger);
        superToast.setAnimations(animations);

        return superToast;

    }

    /**
     * Returns a {@value #TAG} with a specified style.
     *
     * @param context          {@link android.content.Context}
     * @param textCharSequence {@link CharSequence}
     * @param durationInteger  {@link com.github.johnpersano.supertoasts.SuperToast.Duration}
     * @param style            {@link com.github.johnpersano.supertoasts.util.Style}
     *
     * @return SuperCardToast
     */
    public static SuperToast create(Context context, CharSequence textCharSequence, int durationInteger, Style style) {

        final SuperToast superToast = new SuperToast(context);
        superToast.setText(textCharSequence);
        superToast.setDuration(durationInteger);
        superToast.setStyle(style);

        return superToast;

    }

    /**
     * Dismisses and removes all showing/pending {@value #TAG}.
     */
    public static void cancelAllSuperToasts() {

        ManagerSuperToast.getInstance().cancelAllSuperToasts();

    }

}


