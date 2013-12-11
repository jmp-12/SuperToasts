/**
 *  Copyright 2013 John Persano
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


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.github.johnpersano.supertoasts.util.OnDismissListener;

/**
 * SuperToasts are designed to replace stock Android Toasts.
 * If you need to display a SuperToast inside of an Activity
 * please see the class SuperActivityToast.
 *
 */
public class SuperToast
{

    private static final String ERROR_CONTEXTNULL= "The Context that you passed was null! (SuperToast)";


    /** Different backgrounds for SuperToasts. */
    public static class Background {

        public static final int BLACK = (R.drawable.background_black);
        public static final int BLUE = (R.drawable.background_blue);
        public static final int GRAY = (R.drawable.background_grey);
        public static final int GREEN = (R.drawable.background_green);
        public static final int ORANGE = (R.drawable.background_orange);
        public static final int PURPLE = (R.drawable.background_purple);
        public static final int RED = (R.drawable.background_red);
        public static final int WHITE = (R.drawable.background_white);

        public static final int TRANSLUCENT_BLACK= (R.drawable.background_blacktranslucent);
        public static final int TRANSLUCENT_BLUE = (R.drawable.background_bluetranslucent);
        public static final int TRANSLUCENT_GREEN = (R.drawable.background_greentranslucent);
        public static final int TRANSLUCENT_GRAY = (R.drawable.background_greytranslucent);
        public static final int TRANSLUCENT_PURPLE = (R.drawable.background_purpletranslucent);
        public static final int TRANSLUCENT_RED = (R.drawable.background_redtranslucent);
        public static final int TRANSLUCENT_WHITE = (R.drawable.background_whitetranslucent);
        public static final int TRANSLUCENT_ORANGE = (R.drawable.background_orangetranslucent);

    }

    /** Animations that can only be used in the SuperToast class. */
    public static class Animation {

        public static final int FADE = (android.R.style.Animation_Toast);
        public static final int FLYIN = (android.R.style.Animation_Translucent);
        public static final int SCALE = (android.R.style.Animation_Dialog);
        public static final int POPUP = (android.R.style.Animation_InputMethod);

    }

    /** Different icons for SuperToasts. */
    public static class Icon {


        /** Icons for SuperToasts with a dark background. */
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

        /** Icons for SuperToasts with a light background. */
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

    /** Preset durations for SuperToasts. */
    public static class Duration {

        public static final int VERY_SHORT = (1500);
        public static final int SHORT = (2000);
        public static final int MEDIUM = (2750);
        public static final int LONG = (3500);
        public static final int EXTRA_LONG = (4500);

    }


    /** Preset textsizes for SuperToasts. */
    public static class TextSize {

        public static final int SMALL = (14);
        public static final int MEDIUM = (18);
        public static final int LARGE = (22);

    }

    /** Specifies the type of SuperToast. */
    public enum Type {

        /**  Standard Toast type used for displaying messages. */
        STANDARD,

        /** Progress type used for showing progress. */
        PROGRESS,

        /** Progress type used for showing progress. */
        PROGRESS_HORIZONTAL,

        /** Button type used for user interaction. */
        BUTTON

    }

    /** Specifies the position of a supplied icon. */
    public enum IconPosition {

        /** Set the icon to the left of the text. */
        LEFT,

        /** Set the icon to the right of the text. */
        RIGHT,

        /** Set the icon on top of the text. */
        TOP,

        /** Set the icon on the bottom of the text. */
        BOTTOM

    }

    private Context mContext;
    private WindowManager mWindowManager;
    private LinearLayout mRootLayout;
    private WindowManager.LayoutParams mWindowManagerParams;
    private View mToastView;
    private TextView mMessageTextView;
    private int mSdkVersion = android.os.Build.VERSION.SDK_INT;
    private int mGravity = Gravity.BOTTOM| Gravity.CENTER;
    private int mDuration = Duration.SHORT;
    private int mAnimation = Animation.FADE;
    private int mXOffset = 0;
    private int mYOffset = 0;
    private OnDismissListener mOnDismissListener;

    /**
     * Instantiates a new SuperToast.
     * <br>
     * @param context
     */
    public SuperToast(Context context) {

        if (context != null) {

            this.mContext = context;

            mYOffset = context.getResources().getDimensionPixelSize(
                    R.dimen.toast_yoffset);

            LayoutInflater layoutInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            mToastView = layoutInflater.inflate(R.layout.supertoast,
                    null);

            mWindowManager = (WindowManager) mToastView.getContext()
                    .getApplicationContext().getSystemService(Context.WINDOW_SERVICE);

            mRootLayout = (LinearLayout)
                    mToastView.findViewById(R.id.root_layout);

            mMessageTextView = (TextView)
                    mToastView.findViewById(R.id.message_textView);

        } else {

            throw new IllegalArgumentException(ERROR_CONTEXTNULL);

        }

    }

    /** Shows the SuperToast. */
    public void show()
    {

        mWindowManagerParams = new WindowManager.LayoutParams();

        mWindowManagerParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowManagerParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowManagerParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        mWindowManagerParams.format = PixelFormat.TRANSLUCENT;
        mWindowManagerParams.windowAnimations = mAnimation;
        mWindowManagerParams.type = WindowManager.LayoutParams.TYPE_TOAST;
        mWindowManagerParams.gravity = mGravity;
        mWindowManagerParams.x = mXOffset;
        mWindowManagerParams.y = mYOffset;

        ManagerSuperToast.getInstance().add(this);

    }

    /**
     * Sets the message text of the SuperToast.
     * <br>
     * @param text
     */
    public void setText(CharSequence text) {

        mMessageTextView.setText(text);

    }

    /**
     * Sets the message text color of the SuperToast.
     * <br>
     * @param textColor
     */
    public void setTextColor(int textColor) {

        mMessageTextView.setTextColor(textColor);

    }

    /**
     * Sets the text size of the SuperToast.
     * This method will automatically convert the integer
     * parameter to scaled pixels.
     * <br>
     * @param textSize
     */
    public void setTextSize(int textSize) {

        mMessageTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);

    }

    /**
     * Sets the duration of the SuperToast.
     * <br>
     * @param duration
     */
    public void setDuration(int duration) {

        this.mDuration = duration;

    }

    /**
     * Sets an icon Drawable to the SuperToast with
     * a position.
     * <br>
     * @param iconDrawable
     * @param iconPosition
     */
    public void setIconDrawable(Drawable iconDrawable, IconPosition iconPosition) {

        if (iconPosition == IconPosition.BOTTOM) {

            mMessageTextView.setCompoundDrawablesWithIntrinsicBounds(null, null,
                    null, iconDrawable);

        } else if (iconPosition == IconPosition.LEFT) {

            mMessageTextView.setCompoundDrawablesWithIntrinsicBounds(
                    iconDrawable, null, null, null);

        } else if (iconPosition == IconPosition.RIGHT) {

            mMessageTextView.setCompoundDrawablesWithIntrinsicBounds(null, null,
                    iconDrawable, null);

        } else if (iconPosition == IconPosition.TOP) {

            mMessageTextView.setCompoundDrawablesWithIntrinsicBounds(null,
                    iconDrawable, null, null);

        }

    }

    /**
     * Sets an icon resource to the SuperToast
     * with a position.
     * <br>
     * @param iconResource
     * @param iconPosition
     */
    public void setIconResource(int iconResource, IconPosition iconPosition) {

        if (iconPosition == IconPosition.BOTTOM) {

            mMessageTextView.setCompoundDrawablesWithIntrinsicBounds(null, null,
                    null, mContext.getResources().getDrawable(iconResource));

        } else if (iconPosition == IconPosition.LEFT) {

            mMessageTextView
                    .setCompoundDrawablesWithIntrinsicBounds(mContext
                            .getResources().getDrawable(iconResource), null,
                            null, null);

        } else if (iconPosition == IconPosition.RIGHT) {

            mMessageTextView.setCompoundDrawablesWithIntrinsicBounds(null, null,
                    mContext.getResources().getDrawable(iconResource), null);

        } else if (iconPosition == IconPosition.TOP) {

            mMessageTextView.setCompoundDrawablesWithIntrinsicBounds(null,
                    mContext.getResources().getDrawable(iconResource), null,
                    null);

        }

    }

    /**
     * Sets the Gravity of the SuperToast.
     * <br>
     * @param gravity
     */
    public void setGravity(int gravity) {

        this.mGravity = gravity;

    }

    /**
     * Sets the background resource of the SuperToast.
     * <br>
     * @param backgroundResource
     *
     */
    public void setBackgroundResource(int backgroundResource) {

        mRootLayout.setBackgroundResource(backgroundResource);

    }

    /**
     * Sets the background Drawable of the SuperToast.
     * <br>
     * @param backgroundDrawable
     *
     */
    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    public void setBackgroundDrawable(Drawable backgroundDrawable) {

        if (mSdkVersion < android.os.Build.VERSION_CODES.JELLY_BEAN) {

            mRootLayout.setBackgroundDrawable(backgroundDrawable);

        }

        else {

            mRootLayout.setBackground(backgroundDrawable);

        }

    }

    /**
     * Sets the Typeface of the SuperToast TextView.
     * <br>
     * @param typeface
     */
    public void setTypeface(Typeface typeface) {

        mMessageTextView.setTypeface(typeface);

    }

    /**
     * Sets the Animation of the SuperToast.
     * This is limited to the constants of this class.
     * <br>
     * @param animation
     */
    public void setAnimation(int animation) {

        this.mAnimation = animation;

    }

    /**
     * Sets the X and Y offsets of the SuperToast.
     * <br>
     * @param xOffset
     * @param yOffset
     */
    public void setXYCoordinates(int xOffset, int yOffset) {

        this.mXOffset = xOffset;
        this.mYOffset = yOffset;

    }

    /**
     * Sets an OnDismissListener defined in this library
     * to the SuperToast.
     * <br>
     * @param onDismissListener
     */
    public void setOnDismissListener(OnDismissListener onDismissListener) {

        this.mOnDismissListener = onDismissListener;

    }

    /** Dismisses the SuperToast. */
    public void dismiss() {

        ManagerSuperToast.getInstance().removeSuperToast(this);

    }


    //TODO: Getter methods
    /**
     * Returns the SuperToast TextView.
     * <br>
     * @return TextView <br>
     */
    public TextView getTextView() {

        return mMessageTextView;

    }

    /**
     * Returns the SuperToast View.
     * <br>
     * @return View <br>
     */
    public View getView() {

        return mToastView;

    }

    /**
     * Returns true if the SuperToast is showing.
     * <br>
     * @return boolean <br>
     */
    public boolean isShowing() {

        return mToastView != null && mToastView.isShown();

    }

    /**
     * Returns the set duration of the SuperToast.
     * <br>
     * @return long <br>
     */
    public long getDuration() {

        return mDuration;

    }

    /**
     * Returns the OnDismissListener of the SuperToast.
     * <br>
     * @return OnDismissListener <br>
     */
    public OnDismissListener getOnDismissListener() {

        return mOnDismissListener;

    }

    /**
     * Returns the WindowManager that the SuperToast is attached to.
     * <br>
     * @return ViewGroup <br>
     */
    public WindowManager getWindowManager() {

        return mWindowManager;

    }

    /**
     * Returns the WindowManager.Params of the SuperToast.
     * <br>
     * @return ViewGroup <br>
     */
    public WindowManager.LayoutParams getWindowManagerParams() {

        return mWindowManagerParams;

    }


    //XXX: Static methods.


    /**
     * Creates a dark theme SuperToast. Don't forget to call
     * {@link #show()}.
     *
     * <br>
     *
     * @param context
     *
     * @param textCharSequence
     *
     * @param durationInteger
     *
     * @return SuperToast
     *
     */
    public static SuperToast createDarkSuperToast(Context context,
                                                  CharSequence textCharSequence, int durationInteger) {

        SuperToast superToast = new SuperToast(context);
        superToast.setText(textCharSequence);
        superToast.setDuration(durationInteger);

        return superToast;

    }


    /**
     * Creates a light theme SuperToast. Don't forget to call
     * {@link #show()}.
     *
     * <br>
     *
     * @param context
     *
     * @param textCharSequence
     *
     * @param durationInteger
     *
     * @return SuperToast
     *
     */
    public static SuperToast createLightSuperToast(Context context,
                                                   CharSequence textCharSequence, int durationInteger) {

        SuperToast superToast = new SuperToast(context);
        superToast.setText(textCharSequence);
        superToast.setDuration(durationInteger);
        superToast.setBackgroundResource(Background.WHITE);
        superToast.setTextColor(Color.BLACK);

        return superToast;

    }


    /**
     * Creates a dark theme SuperToast. Don't forget to call
     * {@link #show()}.
     *
     * <br>
     *
     * @param context
     *
     * @param textCharSequence
     *
     * @param durationInteger
     *
     * @param animation
     *
     * @return SuperToast
     *
     */
    public static SuperToast createDarkSuperToast(Context context,
                                                  CharSequence textCharSequence, int durationInteger, int animation) {

        SuperToast superToast = new SuperToast(context);
        superToast.setText(textCharSequence);
        superToast.setDuration(durationInteger);
        superToast.setAnimation(animation);

        return superToast;

    }


    /**
     * Creates a light theme SuperToast. Don't forget to call
     * {@link #show()}.
     *
     * <br>
     *
     * @param context
     *
     * @param textCharSequence
     *
     * @param durationInteger
     *
     * @param animation
     *
     * @return SuperToast
     *
     */
    public static SuperToast createLightSuperToast(Context context,
                                                   CharSequence textCharSequence, int durationInteger, int animation) {

        SuperToast superToast = new SuperToast(context);
        superToast.setText(textCharSequence);
        superToast.setDuration(durationInteger);
        superToast.setBackgroundResource(Background.WHITE);
        superToast.setTextColor(Color.BLACK);
        superToast.setAnimation(animation);

        return superToast;

    }

    /** Dismisses and removes all showing/pending SuperActivityToasts. */
    public static void cancelAllSuperToasts() {

        ManagerSuperToast.getInstance().clearQueue();

    }

}


