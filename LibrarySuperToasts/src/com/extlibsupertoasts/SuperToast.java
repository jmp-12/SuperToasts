package com.extlibsupertoasts;

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

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;



import android.view.*;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.TypedValue;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.extlibsupertoasts.utilities.OnDismissListener;
import com.extlibsupertoasts.utilities.SuperToastConstants;


/**
 * SuperToasts are designed to replace stock Android Toasts.
 * If you need to display a SuperToast inside of an Activity
 * please see the class SuperActivityToast.
 *
 */
@SuppressLint("NewApi")
@SuppressWarnings("deprecation")
public class SuperToast
{

    private static final String ERROR_CONTEXTNULL= "The Context that you passed was null! (SuperToast)";

    /**
     * This Animation resembles the stock Toast Animation.
     */
    public static final int ANIMATION_FADE = (android.R.style.Animation_Toast);

    /**
     * This Animation makes the SuperToast fly in from the right and disappear to the right.
     */
    public static final int ANIMATION_FLYIN = (android.R.style.Animation_Translucent);

    /**
     * This Animation makes the SuperToast scale in from 0% of its size to 100% and disappear
     * by scaling the SuperToast from 100% of its size to 0%.
     */
    public static final int ANIMATION_SCALE = (android.R.style.Animation_Dialog);

    /**
     * This Animation makes the SuperToast pop-up from the bottom of the screen and disappear
     * to the bottom of the screen.
     */
    public static final int ANIMATION_POPUP = (android.R.style.Animation_InputMethod);

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private WindowManager mWindowManager;
    private View toastView;
    private TextView messageTextView;
    private Handler mHandler;
    private int sdkVersion = android.os.Build.VERSION.SDK_INT;;

    private CharSequence textCharSequence;
    private int textColor = Color.WHITE;
    private int backgroundResource = SuperToastConstants.BACKGROUND_BLACK;
    private int gravityInteger = Gravity.BOTTOM|Gravity.CENTER;
    private Drawable backgroundDrawable;
    private Typeface typeface = Typeface.DEFAULT;
    private int duration = SuperToastConstants.DURATION_SHORT;
    private int animationStyle = ANIMATION_FADE;
    private int xOffset = 0;
    private int yOffset = 0;
    private int paddingLeft = 0;
    private int paddingTop = 0;
    private int paddingRight = 0;
    private int paddingBottom = 0;
    private float outsideVerticalPadding = 0;
    private float outsideHorizontalPadding = 0;
    private float textSize = SuperToastConstants.TEXTSIZE_SMALL;
    private IconPosition mIconPosition = IconPosition.LEFT;
    private Drawable iconDrawable;
    private int iconResource;
    private OnDismissListener mOnDismissListener;

    /**
     * This is used to specify the position of a supplied icon in the
     * SuperActivityToast.
     *
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
        BOTTOM;

    }

    /**
     * Instantiates a new SuperToast.
     *
     * <br>
     *
     * @param mContext
     *
     */
    public SuperToast(Context mContext) {

        if (mContext != null) {

            this.mContext = mContext;

            yOffset = mContext.getResources().getDimensionPixelSize(
                    R.dimen.toast_yoffset);

            mLayoutInflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            toastView = mLayoutInflater.inflate(R.layout.supertoast, null);

            mWindowManager = (WindowManager) toastView.getContext()
                    .getApplicationContext()
                    .getSystemService(Context.WINDOW_SERVICE);

        } else {

            throw new IllegalArgumentException(ERROR_CONTEXTNULL);

        }

    }


    /**
     * This is used to show the SuperToast. You should
     * do all of your modifications to the SuperToast before calling
     * this method.
     */
    public void show()
    {

        mHandler = new Handler();
        mHandler.postDelayed(hideToastRunnable, duration);

        messageTextView = (TextView)
                toastView.findViewById(R.id.messageTextView);

        messageTextView.setText(textCharSequence);
        messageTextView.setTypeface(typeface);
        messageTextView.setTextColor(textColor);
        messageTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);


        final FrameLayout mRootLayout = (FrameLayout)
                toastView.findViewById(R.id.root);

        if (backgroundDrawable != null) {

            if (sdkVersion < android.os.Build.VERSION_CODES.JELLY_BEAN) {

                mRootLayout.setBackgroundDrawable(backgroundDrawable);

            } else {

                mRootLayout.setBackground(backgroundDrawable);

            }

        } else {

            mRootLayout.setBackgroundResource(backgroundResource);

        }


        if (iconDrawable != null) {

            if (mIconPosition == IconPosition.BOTTOM) {

                messageTextView.setCompoundDrawablesWithIntrinsicBounds(null,
                        null, null, backgroundDrawable);

            } else if (mIconPosition == IconPosition.LEFT) {

                messageTextView.setCompoundDrawablesWithIntrinsicBounds(
                        backgroundDrawable, null, null, null);

            } else if (mIconPosition == IconPosition.RIGHT) {

                messageTextView.setCompoundDrawablesWithIntrinsicBounds(null,
                        null, backgroundDrawable, null);

            } else if (mIconPosition == IconPosition.TOP) {

                messageTextView.setCompoundDrawablesWithIntrinsicBounds(null,
                        backgroundDrawable, null, null);

            }

        } else if (iconResource > 0) {

            if (mIconPosition == IconPosition.BOTTOM) {

                messageTextView.setCompoundDrawablesWithIntrinsicBounds(null,
                        null, null,
                        mContext.getResources().getDrawable(iconResource));

            } else if (mIconPosition == IconPosition.LEFT) {

                messageTextView.setCompoundDrawablesWithIntrinsicBounds(
                        mContext.getResources().getDrawable(iconResource),
                        null, null, null);

            } else if (mIconPosition == IconPosition.RIGHT) {

                messageTextView
                        .setCompoundDrawablesWithIntrinsicBounds(
                                null, null, mContext.getResources().getDrawable(iconResource),
                                null);

            } else if (mIconPosition == IconPosition.TOP) {

                messageTextView.setCompoundDrawablesWithIntrinsicBounds(null,
                        mContext.getResources().getDrawable(iconResource),
                        null, null);

            }

        }

        final WindowManager.LayoutParams params = new WindowManager.LayoutParams();

        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;

        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;

        params.format = PixelFormat.TRANSLUCENT;
        params.windowAnimations = animationStyle;
        params.type = WindowManager.LayoutParams.TYPE_TOAST;
        params.gravity = (gravityInteger);
        params.verticalMargin = outsideVerticalPadding;
        params.horizontalMargin = outsideHorizontalPadding;
        params.x = xOffset;
        params.y = yOffset;

        toastView.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);

        mWindowManager.addView(toastView, params);

    }


    /**
     * This is used to set the message text of the SuperToast.
     *
     * <br>
     *
     * <p>
     * <b> Important note: </b>
     * </p>
     *
     * <p>
     * This method can be called again while the SuperToast is showing
     * to modify the existing message. If your application might show two
     * SuperToasts at one time you should try to reuse the same
     * SuperToast by calling this method and {@link #resetDuration(int)}
     * </p>
     *
     * <br>
     *
     * <p>
     * <b> Design guide: </b>
     * </p>
     *
     * <p>
     * Toasts/SuperToasts are designed to display short non-essential
     * messages such as "Message sent!" after the user sends a SMS. Generally
     * these messages should rarely display more than one line of text.
     * </p>
     *
     * <br>
     * @param textCharSequence
     * <br>
     *
     */
    public void setText(CharSequence textCharSequence) {

        this.textCharSequence = textCharSequence;

        if (messageTextView != null) {

            messageTextView.setText(textCharSequence);

        }

    }


    /**
     * This is used to set the message text color of the SuperToast.
     *
     * <br>
     *
     * <p>
     * <b> Design guide: </b>
     * </p>
     *
     * <p>
     * The text color that you choose should contrast the color of the background.
     * Generally the colors white and black are the only colors that should be used
     * here.
     * </p>
     *
     * <br>
     * @param textColor
     * <br>
     * Example: (Color.WHITE)
     * <br>
     *
     */
    public void setTextColor(int textColor) {

        this.textColor = textColor;

        if (messageTextView != null) {

            messageTextView.setTextColor(textColor);

        }

    }


    /**
     * This is used to set the duration of the SuperToast.
     *
     * <br>
     *
     * <p>
     * <b> Design guide: </b>
     * </p>
     *
     * <p>
     * Generally short durations are preferred.
     * </p>
     *
     * <br>
     * @param duration
     * <br>
     * Example: (SuperToastConstants.DURATION_SHORT)
     * <br>
     *
     */
    public void setDuration(int duration) {

        this.duration = duration;

    }



    /**
     * This is used to reset the duration of the SuperToast
     * while it is showing.
     *
     * <br>
     *
     * <p>
     * <b> Design guide: </b>
     * </p>
     *
     * <p>
     * Instead of having overlapping or sequential messages you
     * should use this method to reuse an already showing SuperToast
     * in instances where two or more messages can be showing at the same time.
     * </p>
     *
     * <br>
     * @param newDuration
     * <br>
     * Example: (SuperToastConstants.DURATION_SHORT)
     * <br>
     *
     */
    public void resetDuration(int newDuration) {

        if (mHandler != null) {

            mHandler.removeCallbacks(hideToastRunnable);
            mHandler = null;

        }

        mHandler = new Handler();
        mHandler.postDelayed(hideToastRunnable, newDuration);

    }


    /**
     * This is used to set an icon Drawable to the SuperToast.
     *
     * <br>
     *
     * <p>
     * <b> Design guide: </b>
     * </p>
     *
     * <p>
     * Use {@link #setIconPosition(com.extlibsupertoasts.SuperToast.IconPosition)} to modify the
     * location of the icon.
     * </p>
     *
     * <br>
     * @param iconDrawable
     * <br>
     *
     */
    public void setIconDrawable(Drawable iconDrawable) {

        this.iconDrawable = iconDrawable;

    }


    /**
     * This is used to set an icon resource to the SuperToast.
     *
     * <br>
     *
     * <p>
     * <b> Design guide: </b>
     * </p>
     *
     * <p>
     * Use {@link #setIconPosition(com.extlibsupertoasts.SuperToast.IconPosition)} to modify the
     * location of the icon.
     * </p>
     *
     * <br>
     * @param iconResource
     * <br>
     *
     */
    public void setIconResource(int iconResource) {

        this.iconResource = iconResource;

    }


    /**
     * This is used to set the position of the icon in the SuperToast.
     *
     * <br>
     *
     * <p>
     * <b> Design guide: </b>
     * </p>
     *
     * <p>
     * It is preferable to display the icon to the left of the text.
     * </p>
     *
     * <br>
     * @param mIconPosition
     * <br>
     * Example: IconPosition.LEFT
     * <br>
     *
     */
    public void setIconPosition(IconPosition mIconPosition) {

        this.mIconPosition = mIconPosition;

    }


    /**
     * This is used to set the gravity of the SuperToast.
     *
     *
     * <br>
     * @param gravityInteger
     * <br>
     * Example: Gravity.LEFT
     * <br>
     *
     */
    public void setGravity(int gravityInteger) {

        this.gravityInteger = gravityInteger;

    }


    /**
     * This is used to set the background of the SuperToast.
     *
     * <br>
     *
     * <p>
     * <b> Design guide: </b>
     * </p>
     *
     * <p>
     * This library comes with backgrounds ready to use in your applications.
     * If you would like to use your own backgrounds please make sure that
     * the background is nine-patch or XML format.
     * </p>
     *
     * <br>
     * @param backgroundResource
     * <br>
     * Example: (SuperToastConstants.BACKGROUND_BLACK)
     * <br>
     *
     */
    public void setBackgroundResource(int backgroundResource) {

        this.backgroundResource = backgroundResource;

    }


    /**
     * This is used to set the background of the SuperToast.
     *
     * <br>
     *
     * <p>
     * <b> Design guide: </b>
     * </p>
     *
     * <p>
     * This library comes with backgrounds ready to use in your applications.
     * If you would like to use them please see {@link #setBackgroundResource(int)}.
     * </p>
     *
     * <br>
     * @param backgroundDrawable
     * <br>
     *
     */
    public void setBackgroundDrawable(Drawable backgroundDrawable) {

        this.backgroundDrawable = backgroundDrawable;

    }


    /**
     * This is used to set the text size of the SuperToast.
     *
     * <br>
     *
     * <p>
     * <b> Design guide: </b>
     * </p>
     *
     * <p>
     * Generally the text size should be around 14sp.
     * </p>
     *
     * <br>
     *
     * <p>
     * <b> Important note: </b>
     * </p>
     *
     * <p>
     * You may specify an integer value as a parameter.
     * This method will automatically convert the integer to
     * scaled pixels.
     * </p>
     *
     * <br>
     * @param textSize
     * <br>
     * Example: (SuperToastConstants.TEXTSIZE_SMALL)
     * <br>
     *
     */
    public void setTextSize(int textSize) {

        this.textSize = textSize;

    }


    /**
     * This is used to set the Typeface of the SuperActivityToast text.
     *
     * <br>
     *
     * <p>
     * <b> Important note: </b>
     * </p>
     *
     * <p>
     * This library comes with a link to download the Roboto font. To use the
     * fonts see {@link #loadRobotoTypeface(String)}.
     * </p>
     *
     * <br>
     * @param typeface
     * <br>
     * 		Example: (Typeface.DEFAULT) OR (mSuperActivityToast.loadRobotoTypeface(SuperToastConstants.
     * FONT_ROBOTO_THIN);	 *
     * <br>
     *
     */
    public void setTypeface(Typeface typeface) {

        this.typeface = typeface;

    }


    /**
     * This is used to set the Animation of the SuperToast.
     *
     * <br>
     *
     * <p>
     * <b> Important note: </b>
     * </p>
     *
     * <p>
     * You can only use the four Animations specified in this class. This is a
     * limit of Android not this library.
     * </p>
     *
     * <br>
     *
     * @param animationStyle
     * <br>
     *            Example: SuperToast.ANIMATION_FADE
     *
     */
    public void setAnimation(int animationStyle) {

        this.animationStyle = animationStyle;

    }


    /**
     * This is used to set the X and Y offsets of the SuperToast.
     *
     * <br>
     *
     * <br>
     * @param xOffset
     * @param yOffset
     * <br>
     *
     */
    public void setXYCoordinates(int xOffset, int yOffset) {

        this.xOffset = xOffset;
        this.yOffset = yOffset;

    }

    /**
     * This is used to set an OnDismissListener to the SuperToast.
     *
     * <br>
     *
     * <p>
     * <b> Important note: </b>
     * </p>
     *
     * <p>
     * Make sure that the OnDismissListener is imported from this library.
     * This method is not compatible with other OnDismissListeners.
     * </p>
     *
     * <br>
     * @param mOnDismissListener
     * <br>
     *
     */
    public void setOnDismissListener(OnDismissListener mOnDismissListener) {

        this.mOnDismissListener = mOnDismissListener;

    }


    /**
     * <b><i> public void dismiss() </i></b>
     *
     * <p> This is used to hide and dispose of the SuperButtonToast. </p>
     *
     *
     * <b> Design guide: </b>
     *
     * <p> Treat your SuperToast like a Dialog, dismiss it when it is no longer
     *     relevant. </p>
     *
     */
    public void dismiss() {

        if (mHandler != null) {

            mHandler.removeCallbacks(hideToastRunnable);
            mHandler = null;

        }

        if (toastView != null && mWindowManager != null) {

            mWindowManager.removeView(toastView);
            toastView = null;

        }

        if(mOnDismissListener != null) {

            mOnDismissListener.onDismiss();

        }

    }


    //XXX: Getter methods


    /**
     * This is used to get the SuperToast message TextView.
     *
     * <br>
     *
     * @return TextView
     *
     * <br>
     *
     */
    public TextView getTextView() {

        return messageTextView;

    }


    /**
     * This is used to get the X offset of the SuperToast VIew.
     *
     * <br>
     *
     * @return int
     *
     * <br>
     *
     */
    public int getXOffset()
    {

        return this.xOffset;

    }


    /**
     * This is used to get the Y offset of the SuperToast VIew.
     *
     * <br>
     *
     * @return int
     *
     * <br>
     *
     */
    public int getYOffset() {

        return this.yOffset;

    }


    /**
     * This is used to get the SuperToast View.
     *
     * <br>
     *
     * @return View
     *
     * <br>
     *
     */
    public View getView() {

        return toastView;

    }


    /**
     * Returns true of the SuperToast is currently visible
     * to the user.
     *
     * <br>
     *
     * @return boolean
     *
     * <br>
     *
     */
    public boolean isShowing() {

        if (toastView != null) {

            return toastView.isShown();

        }

        else {

            return false;

        }

    }

    /**
     * Sets the desired padding for ToastView. Use int.
     *
     * <br>
     *
     * @param paddingLeft
     * @param paddingTop
     * @param paddingRight
     * @param paddingBottom
     *
     * <br>
     *
     */
    public void setPadding(int paddingLeft, int paddingTop, int paddingRight, int paddingBottom) {

        this.paddingLeft = paddingLeft;
        this.paddingTop = paddingTop;
        this.paddingRight = paddingRight;
        this.paddingBottom = paddingBottom;

    }

    /**
     * Return the padding used for ToastView. Inside.
     *
     * <br>
     *
     * @return int[]
     *
     * <br>
     *
     */
    public int[] getPadding() {

        return new int[] { paddingLeft, paddingTop, paddingRight, paddingBottom };

    }

    /**
     * Set the outside vertical padding used for ToastView. Float.
     *
     * <br>
     *
     * @param outsideVerticalPadding
     *
     * <br>
     *
     */
    public void setOutsideVerticalPadding(float outsideVerticalPadding) {

        this.outsideVerticalPadding = outsideVerticalPadding;

    }

    /**
     * Set the outside horizontal padding used for ToastView. Float.
     *
     * <br>
     *
     * @param outsideHorizontalPadding
     *
     * <br>
     *
     */
    public void setOutsideHorizontalPadding(float outsideHorizontalPadding) {

        this.outsideHorizontalPadding = outsideHorizontalPadding;

    }

    /**
     * Return the outside horizontal padding used for ToastView.
     *
     * <br>
     *
     * @return float
     *
     * <br>
     *
     */
    public float getOutsideHorizontalPadding() {

        return outsideHorizontalPadding;

    }

    /**
     * Return the outside vertical padding used for ToastView.
     *
     * <br>
     *
     * @return float
     *
     * <br>
     *
     */
    public float getOutsideVerticalPadding() {

        return outsideVerticalPadding;

    }

    /**
     * This is used to get and load a Roboto font. You <b><i>MUST</i></b> put the
     * desired font file in the assets folder of your project. The link to
     * download the Roboto fonts is included in this library as a text file. Do
     * not modify the names of these fonts.
     *
     * <br>
     * @param typefaceString
     * <br>
     * Example: (SuperToastConstants.FONT_ROBOTO_THIN)
     * <br>
     *
     * @return Typeface
     *
     * <br>
     *
     */
    public Typeface loadRobotoTypeface(String typefaceString) {

        return Typeface.createFromAsset(mContext.getAssets(), typefaceString);

    }


    //XXX: Private methods


    private Runnable hideToastRunnable = new Runnable()
    {

        public void run()
        {

            dismiss();

        }
    };


    //Quick Navigation: Static methods.


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

        SuperToast mSuperToast = new SuperToast(context);
        mSuperToast.setText(textCharSequence);
        mSuperToast.setDuration(durationInteger);

        return mSuperToast;

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

        SuperToast mSuperToast = new SuperToast(context);
        mSuperToast.setText(textCharSequence);
        mSuperToast.setDuration(durationInteger);
        mSuperToast.setBackgroundResource(SuperToastConstants.BACKGROUND_WHITE);
        mSuperToast.setTextColor(Color.BLACK);

        return mSuperToast;

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

        SuperToast mSuperToast = new SuperToast(context);
        mSuperToast.setText(textCharSequence);
        mSuperToast.setDuration(durationInteger);
        mSuperToast.setAnimation(animation);

        return mSuperToast;

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

        SuperToast mSuperToast = new SuperToast(context);
        mSuperToast.setText(textCharSequence);
        mSuperToast.setDuration(durationInteger);
        mSuperToast.setBackgroundResource(SuperToastConstants.BACKGROUND_WHITE);
        mSuperToast.setTextColor(Color.BLACK);
        mSuperToast.setAnimation(animation);

        return mSuperToast;

    }

}



