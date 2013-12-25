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


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.*;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.*;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.github.johnpersano.supertoasts.SuperToast.IconPosition;
import com.github.johnpersano.supertoasts.SuperToast.Type;
import com.github.johnpersano.supertoasts.util.SwipeDismissListener;

/**
 * SuperCardToasts are designed to be used inside of Activities. SuperCardToasts
 * are designed to be displayed at the top of an Activity to display non-essential messages. 
 */

public class SuperCardToast
{

    private static final String TAG  = "(SuperCardToast)";
    private static final String BUNDLE = "supercardtoast_bundle";

    private static final String ERROR_CONTEXTNOTACTIVITY  = "Context must be an instance of Activity";
    private static final String ERROR_CONTAINERNULL = "You must have a LinearLayout with the id of card_container in your layout!";
    private static final String ERROR_VIEWCONTAINERNULL = "Either the View or Container was null when trying to dismiss. Did you create and " +
            "show a SuperCardToast before trying to dismiss it?";
    private static final String ERROR_PREHONEYCOMB  = "Swipe to dismiss was enabled but the SDK version is pre-Honeycomb";

    private final int mSdkVersion = android.os.Build.VERSION.SDK_INT;

    private Context mContext;
    private ViewGroup mViewGroup;
    private Handler mHandler;
    private View mToastView;
    private LayoutInflater mLayoutInflater;
    private TextView mMessageTextView;
    private ProgressBar mProgressBar;
    private Button mToastButton;
    private View mDividerView;
    private LinearLayout mRootLayout;
    private int mDuration = (SuperToast.Duration.LONG);
    private boolean isIndeterminate;
    //private OnDismissListener mOnDismissListener;

    // Values below are used only for the call onRestoreState()
    private Drawable mIconDrawable;
    private int mIconResouce;
    private IconPosition mIconPosition;
    private OnClickListener mOnClickListener;
    private int mBackgroundResouce;
    private Drawable mBackgroundDrawable;
    private boolean isTouchDismissable;
    private OnClickListener mButtonOnClickListener;
    private Drawable mButtonDrawable;
    private int mButtonResource;
    private int mButtonDividerResource;
    private Drawable mButtonDividerDrawable;
    private boolean isProgressIndeterminate;
    private Type mType;
    private boolean isSwipeDismissable;


    /**
     * Instantiates a new SuperCardToast.
     * <br>
     * @param context
     */
    public SuperCardToast(Context context)
    {

        if(context instanceof Activity) {

            this.mContext = context;

            final Activity activity = (Activity) context;

            mLayoutInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if(activity.findViewById(R.id.card_container) != null) {

                mViewGroup = (LinearLayout)
                        activity.findViewById(R.id.card_container);

                mToastView = mLayoutInflater
                        .inflate(R.layout.supercardtoast, mViewGroup, false);

                mMessageTextView = (TextView)
                        mToastView.findViewById(R.id.message_textView);

                mRootLayout = (LinearLayout)
                        mToastView.findViewById(R.id.root_layout);

            } else {

                throw new IllegalArgumentException(ERROR_CONTAINERNULL);

            }

        } else {

            throw new IllegalArgumentException(ERROR_CONTEXTNOTACTIVITY);

        }

    }

    /**
     * Instantiates a new SuperCardToast with a Type.
     * <br>
     * @param context
     * @param type
     */
    public SuperCardToast(Context context, Type type)
    {

        if(context instanceof Activity) {

            this.mContext = context;

            final Activity activity = (Activity) context;

            mLayoutInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (activity.findViewById(R.id.card_container) != null) {

                mViewGroup = (LinearLayout) activity
                        .findViewById(R.id.card_container);

                if(type == Type.BUTTON) {

                    mType = Type.BUTTON;

                    mToastView = mLayoutInflater
                            .inflate(R.layout.supercardtoast_button, mViewGroup, false);

                    mToastButton = (Button)
                            mToastView.findViewById(R.id.button);

                    mDividerView = mToastView.findViewById(R.id.divider);

                    mToastButton.setOnTouchListener(mTouchDismissListener);

                } else if(type == Type.PROGRESS) {

                    mType = Type.PROGRESS;

                    mToastView = mLayoutInflater
                            .inflate(R.layout.supercardtoast_progresscircle, mViewGroup, false);

                    mProgressBar = (ProgressBar)
                            mToastView.findViewById(R.id.progressBar);

                } else if(type == Type.PROGRESS_HORIZONTAL) {

                    mType = Type.PROGRESS_HORIZONTAL;

                    mToastView = mLayoutInflater
                            .inflate(R.layout.supercardtoast_progresshorizontal, mViewGroup, false);

                    mProgressBar = (ProgressBar)
                            mToastView.findViewById(R.id.progressBar);

                } else {

                    mType = Type.STANDARD;

                    mToastView = mLayoutInflater
                            .inflate(R.layout.supercardtoast, mViewGroup, false);

                }

                mMessageTextView = (TextView)
                        mToastView.findViewById(R.id.message_textView);

                mRootLayout = (LinearLayout)
                        mToastView.findViewById(R.id.root_layout);

            } else {

                throw new IllegalArgumentException(ERROR_CONTAINERNULL);

            }

        } else {

            throw new IllegalArgumentException(ERROR_CONTEXTNOTACTIVITY);

        }

    }


    /** Shows the SuperCardToast. */
    public void show()
    {

        ManagerSuperCardToast.getInstance().add(this);

        if(!isIndeterminate) {

            mHandler = new Handler();
            mHandler.postDelayed(mHideRunnable, mDuration);

        }

        mViewGroup.addView(mToastView);

        final Animation animation = getCardAnimation();

        animation.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationEnd(Animation arg0) {

                /** Must use Handler to modify ViewGroup in onAnimationEnd() **/
                Handler mHandler = new Handler();
                mHandler.post(mInvalidateRunnable);

            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
                // Do nothing

            }

            @Override
            public void onAnimationStart(Animation arg0) {
                // Do nothing

            }

        });

        mToastView.startAnimation(animation);

    }

    /**
     * Returns the Type of SuperCardToast
     */
    public Type getType() {

        return mType;

    }

    /**
     * Sets the message text of the SuperCardToast.
     * <br>
     * @param charSequence
     */
    public void setText(CharSequence charSequence) {

        mMessageTextView.setText(charSequence);

    }

    /**
     * Returns the message text of the SuperCardToast.
     */
    public CharSequence getText() {

        return mMessageTextView.getText();

    }

    /**
     * Sets the message text color of the SuperCardToast.
     * <br>
     * @param textColor
     */
    public void setTextColor(int textColor) {

        mMessageTextView.setTextColor(textColor);

    }

    /**
     * Returns the message text color of the SuperCardToast.
     */
    public int getTextColor() {

        return mMessageTextView.getCurrentTextColor();

    }

    /**
     * Sets the text size of the SuperCardToast.
     * <br>
     * @param textSize
     */
    public void setTextSize(int textSize) {

        mMessageTextView.setTextSize(textSize);

    }

    /**
     * Sets the text size of the SuperCardToast.
     * <br>
     * @param textSize
     */
    public void setTextSizeFloat(float textSize) {

        mMessageTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);

    }

    /**
     * Returns the text size of the SuperCardToast.
     */
    public float getTextSize() {

        return mMessageTextView.getTextSize();

    }

    /**
     * Sets the duration of the SuperCardToast.
     * <br>
     * @param duration
     */
    public void setDuration(int duration) {

        this.mDuration = duration;

    }

    /**
     * Returns the duration of the SuperCardToast.
     */
    public int getDuration() {

        return this.mDuration;

    }

    /**
     * Sets an indeterminate duration of the SuperCardToast.
     * <br>
     * @param isIndeterminate
     */
    public void setIndeterminate(boolean isIndeterminate) {

        this.isIndeterminate = isIndeterminate;

    }

    /**
     * Returns true if the SuperCardToast is indeterminate.
     */
    public boolean isIndeterminate() {

        return this.isIndeterminate;

    }

    /**
     * Sets an icon Drawable to the SuperCardToast with
     * a position.
     * <br>
     * @param iconDrawable
     * @param iconPosition
     */
    public void setIconDrawable(Drawable iconDrawable, IconPosition iconPosition) {

        this.mIconDrawable = iconDrawable;
        this.mIconPosition = iconPosition;

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
     * Returns the icon Drawable of the SuperCardToast.
     */
    public Drawable getIconDrawable() {

        return this.mIconDrawable;

    }

    /**
     * Returns the icon position of the SuperCardToast.
     */
    public IconPosition getIconPosition() {

        return this.mIconPosition;

    }

    /**
     * Sets an icon resource to the SuperCardToast.
     * with a position.
     * <br>
     * @param iconResource
     * @param iconPosition
     */
    public void setIconResource(int iconResource, IconPosition iconPosition) {

        this.mIconResouce = iconResource;
        this.mIconPosition = iconPosition;

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
     * Returns the icon resource of the SuperCardToast.
     */
    public int getIconResource() {

        return this.mIconResouce;

    }

    /**
     * Sets an OnClickListener to the SuperCardToast root View.
     * <br>
     * @param onClickListener
     */
    public void setOnClickListener(OnClickListener onClickListener) {

        this.mOnClickListener = onClickListener;

        mToastView.setOnClickListener(onClickListener);

    }

    /**
     * Returns the onClickListener of the SuperCardToast root View.
     */
    public OnClickListener getOnClickListener() {

        return this.mOnClickListener;

    }

    /**
     * Sets the background resource of the SuperCardToast.
     * <br>
     * @param backgroundResource
     */
    public void setBackgroundResource(int backgroundResource) {

        this.mBackgroundResouce = backgroundResource;

        mRootLayout.setBackgroundResource(backgroundResource);

    }

    /**
     * Returns the background resource of the SuperCardToast.
     */
    public int getBackgroundResource() {

        return this.mBackgroundResouce;

    }

    /**
     * Sets the background Drawable of the SuperCardToast.
     * <br>
     * @param backgroundDrawable
     */
    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    public void setBackgroundDrawable(Drawable backgroundDrawable) {

        this.mBackgroundDrawable = backgroundDrawable;

        if (mSdkVersion < android.os.Build.VERSION_CODES.JELLY_BEAN) {

            mRootLayout.setBackgroundDrawable(backgroundDrawable);

        } else {

            mRootLayout.setBackground(backgroundDrawable);

        }

    }

    /**
     * Returns the background Drawable of the SuperCardToast.
     */
    public Drawable getBackgroundDrawable() {

        return this.mBackgroundDrawable;

    }

    /**
     * Sets the Typeface of the SuperCardToast message.
     * <br>
     * @param typeface
     */
    public void setTypeface(Typeface typeface) {

        mMessageTextView.setTypeface(typeface);

    }

    /**
     * Returns the Typeface of the SuperCardToast message.
     */
    public Typeface getTypeface() {

        return mMessageTextView.getTypeface();

    }

    /**
     * Sets a private OnTouchListener to the SuperCardToast
     * that will dismiss the SuperCardToast when touched.
     * <br>
     * @param touchDismiss
     */
    public void setTouchToDismiss(boolean touchDismiss) {

        this.isTouchDismissable = touchDismiss;

        if (touchDismiss) {

            mToastView.setOnTouchListener(mTouchDismissListener);

        } else {

            mToastView.setOnTouchListener(null);

        }

    }

    /**
     * Returns true if the SuperCardToast is touch dismissable.
     */
    public boolean isTouchDismissable() {

        return this.isTouchDismissable;

    }

    /**
     * Sets an OnDismissListener to the SuperCardToast.
     * <br>
     * @param onDismissListener
     */
//	public void setOnDismissListener(OnDismissListener onDismissListener) {

//		this.mOnDismissListener = onDismissListener;

//	}

    /**
     * Returns the OnDismissListener of the SuperActivityToast.
     */
//    public OnDismissListener getOnDismissListener() {

//        return this.mOnDismissListener;

//    }

    /**
     * Sets a private OnTouchListener to the SuperCardToast
     * that will dismiss the SuperCardToast when swiped.
     * <br>
     * @param swipeDismiss
     */
    public void setSwipeToDismiss(boolean swipeDismiss) {

        this.isSwipeDismissable = swipeDismiss;

        if(swipeDismiss) {

            if (mSdkVersion > android.os.Build.VERSION_CODES.HONEYCOMB_MR1) {

                final SwipeDismissListener swipeDismissListener = new SwipeDismissListener(
                        mToastView, new SwipeDismissListener.OnDismissCallback() {

                    @Override
                    public void onDismiss(View view) {

                        dismissImmediately();

                    }

                });

                mToastView.setOnTouchListener(swipeDismissListener);

            } else {

                Log.e(TAG, ERROR_PREHONEYCOMB);

            }

        } else {

            mToastView.setOnTouchListener(null);

        }

    }

    /**
     *   Returns true if the SuperCardToast is swipe dismissable.
     */
    public boolean isSwipeDismissable() {

        return isSwipeDismissable;

    }

    /** Dismisses the SuperCardToast with an animation. */
    public void dismiss() {

        ManagerSuperCardToast.getInstance().remove(this);

        dismissWithAnimation();

    }

    /** Dismisses the SuperCardToast without an animation. */
    public void dismissImmediately() {

        ManagerSuperCardToast.getInstance().remove(this);

        if(mHandler != null) {

            mHandler.removeCallbacks(mHideRunnable);
            mHandler.removeCallbacks(mHideWithAnimationRunnable);
            mHandler = null;

        }

        if (mToastView != null && mViewGroup != null) {

            mViewGroup.removeView(mToastView);
            mToastView = null;

        } else {

            Log.e(TAG, ERROR_VIEWCONTAINERNULL);

        }

        //if(mOnDismissListener != null) {

        //	mOnDismissListener.onDismiss();

        //	}

    }


    /**
     * Sets an OnClickListener to the Button of a BUTTON Type
     * SuperCardToast.
     * <br>
     * @param onClickListener
     */
    public void setButtonOnClickListener(OnClickListener onClickListener) {

        this.mButtonOnClickListener = onClickListener;

        if (mToastButton != null) {

            mToastButton.setOnClickListener(onClickListener);

        }

    }

    /**
     * Returns the OnClickListener of the Button in a BUTTON Type
     * SuperCardToast.
     */
    public OnClickListener getButtonOnClickListener() {

        return mButtonOnClickListener;

    }

    /**
     * Sets the background resource of the Button in
     * a BUTTON Type SuperCardToast.
     * <br>
     * @param buttonResource
     */
    public void setButtonResource(int buttonResource) {

        this.mButtonResource = buttonResource;

        if (mToastButton != null) {

            mToastButton.setCompoundDrawablesWithIntrinsicBounds(mContext
                    .getResources().getDrawable(buttonResource), null, null, null);

        }

    }

    /**
     * Returns the background resource of the Button in
     * a BUTTON Type SuperCardToast.
     */
    public int getButtonResource() {

        return this.mButtonResource;

    }

    /**
     * Sets the background Drawable of the Button in
     * a BUTTON Type SuperCardToast.
     * <br>
     * @param buttonDrawable
     */
    public void setButtonDrawable(Drawable buttonDrawable) {

        this.mButtonDrawable = buttonDrawable;

        if (mToastButton != null) {

            mToastButton.setCompoundDrawablesWithIntrinsicBounds(buttonDrawable,
                    null, null, null);

        }

    }

    /**
     * Returns the background Drawable of the Button in
     * a BUTTON Type SuperCardToast.
     */
    public Drawable getButtonDrawable() {

        return this.mButtonDrawable;

    }

    /**
     * Sets the background resource of the Button divider in
     * a BUTTON Type SuperCardToast.
     * <br>
     * @param dividerResource
     */
    public void setButtonDividerResource(int dividerResource) {

        this.mButtonDividerResource = dividerResource;

        if (mDividerView != null) {

            mDividerView.setBackgroundResource(dividerResource);

        }

    }

    /**
     * Returns the background resource of the Button divider in
     * a BUTTON Type SuperCardToast.
     */
    public int getButtonDividerResource() {

        return this.mButtonDividerResource;

    }

    /**
     * Sets the background Drawable of the Button divider in
     * a BUTTON Type SuperCardToast.
     * <br>
     * @param dividerDrawable
     */
    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    public void setButtonDividerDrawable(Drawable dividerDrawable) {

        this.mButtonDividerDrawable = dividerDrawable;

        if (mSdkVersion < android.os.Build.VERSION_CODES.JELLY_BEAN) {

            mDividerView.setBackgroundDrawable(dividerDrawable);

        } else {

            mDividerView.setBackground(dividerDrawable);

        }

    }

    /**
     * Returns the background drawable of the Button divider in
     * a BUTTON Type SuperCardToast.
     */
    public Drawable getButtonDividerDrawable() {

        return this.mButtonDividerDrawable;

    }

    /**
     * Sets the text of the Button in
     * a BUTTON Type SuperCardToast.
     * <br>
     * @param buttonText
     */
    public void setButtonText(CharSequence buttonText) {

        if (mToastButton != null) {

            mToastButton.setText(buttonText);

        }

    }

    /**
     * Returns the text of the Button in
     * a BUTTON Type SuperCardToast.
     */
    public CharSequence getButtonText() {

        return mToastButton.getText();

    }

    /**
     * Sets the text color of the Button in
     * a BUTTON Type SuperCardToast.
     * <br>
     * @param buttonTextColor
     */
    public void setButtonTextColor(int buttonTextColor) {

        if (mToastButton != null) {

            mToastButton.setTextColor(buttonTextColor);

        }

    }

    /**
     * Returns the text color of the Button in
     * a BUTTON Type SuperCardToast.
     */
    public int getButtonTextColor() {

        return mToastButton.getCurrentTextColor();

    }

    /**
     * Sets the text Typeface of the Button in
     * a BUTTON Type SuperCardToast.
     * <br>
     * @param buttonTextTypeface
     */
    public void setButtonTextTypeface(Typeface buttonTextTypeface) {

        if (mToastButton != null) {

            mToastButton.setTypeface(buttonTextTypeface);

        }

    }

    /**
     * Returns the text Typeface of the Button in
     * a BUTTON Type SuperCardToast.
     */
    public Typeface getButtonTextTypeface() {

        return mToastButton.getTypeface();

    }

    /**
     * Sets the text size of the Button in
     * a BUTTON Type SuperCardToast.
     * <br>
     * @param buttonTextSize
     */
    public void setButtonTextSize(int buttonTextSize) {

        if (mToastButton != null) {

            mMessageTextView.setTextSize(buttonTextSize);

        }

    }

    /**
     * Sets the text size of the Button in
     * a BUTTON Type SuperCardToast
     * <br>
     * @param buttonTextSize
     */
    public void setButtonTextSizeFloat(float buttonTextSize) {

        mMessageTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, buttonTextSize);

    }

    /**
     * Returns the text size of the Button in
     * a BUTTON Type SuperCardToast.
     */
    public float getButtonTextSize() {

        return mToastButton.getTextSize();

    }

    /**
     * Sets the progress of the ProgressBar in
     * a PROGRESS_HORIZONTAL Type SuperCardToast.
     * <br>
     * @param progress
     */
    public void setProgress(int progress) {

        if (mProgressBar != null) {

            mProgressBar.setProgress(progress);

        }

    }

    /**
     * Returns the progress of the ProgressBar in
     * a PROGRESS_HORIZONTAL Type SuperCardToast.
     */
    public int getProgress() {

        return mProgressBar.getProgress();

    }

    /**
     * Sets an indeterminate value to the ProgressBar of a PROGRESS Type
     * SuperCardToast.
     * <br>
     * @param isIndeterminate
     */
    public void setProgressIndeterminate(boolean isIndeterminate) {

        this.isProgressIndeterminate = isIndeterminate;

        if(mProgressBar != null) {

            mProgressBar.setIndeterminate(isIndeterminate);

        }

    }

    /**
     * Returns if an indeterminate value to the ProgressBar of a PROGRESS Type
     * SuperCardToast has been set.
     */
    public boolean getProgressIndeterminate() {

        return this.isProgressIndeterminate;

    }

    /**
     * Returns the SuperCardToast TextView.
     */
    public TextView getTextView() {

        return mMessageTextView;

    }

    /**
     * Returns the SuperCardToast View.
     */
    public View getView() {

        return mToastView;

    }

    /**
     * Returns true if the SuperCardToast is showing.
     */
    public boolean isShowing() {

        return mToastView != null && mToastView.isShown();

    }

    /**
     * Returns the calling Activity of the SuperCardToast.
     */
    public Activity getActivity() {

        return (Activity) mContext;

    }

    /**
     * Returns true if the SuperCardToast is indeterminate.
     */
    public boolean getIsIndeterminate() {

        return isIndeterminate;

    }


    /**
     * Returns the ViewGroup that the SuperCardToast is attached to.
     */
    public ViewGroup getViewGroup() {

        return mViewGroup;

    }

    /** Hide the SuperCardToast and animate the Layout. Post Honeycomb only. **/
    @SuppressLint("NewApi")
    private void dismissWithLayoutAnimation() {

        if(mToastView != null) {

            final ViewGroup.LayoutParams layoutParams = mToastView.getLayoutParams();
            final int originalHeight = mToastView.getHeight();

            ValueAnimator animator = ValueAnimator.ofInt(originalHeight, 1)
                    .setDuration(mContext.getResources().getInteger(android.R.integer.config_shortAnimTime));

            animator.addListener(new AnimatorListenerAdapter() {

                @Override
                public void onAnimationEnd(Animator animation) {

                    Handler mHandler = new Handler();
                    mHandler.post(mHideImmediateRunnable);

                }

            });

            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {

                    layoutParams.height = (Integer) valueAnimator.getAnimatedValue();
                    mToastView.setLayoutParams(layoutParams);

                }

            });

            animator.start();

        } else {

            dismissImmediately();

        }

    }

    private Animation getCardAnimation() {

        AnimationSet animationSet = new AnimationSet(false);

        TranslateAnimation translateAnimation = new TranslateAnimation(0f, 0f,
                1f, 0f);
        translateAnimation.setDuration(200);

        animationSet.addAnimation(translateAnimation);

        AlphaAnimation alphaAnimation = new AlphaAnimation(0f, 1f);
        alphaAnimation.setDuration(400);

        animationSet.addAnimation(alphaAnimation);

        RotateAnimation rotationAnimation = new RotateAnimation(15f, 0f, 0f,
                0f);
        rotationAnimation.setDuration(225);

        animationSet.addAnimation(rotationAnimation);

        return animationSet;

    }

    @SuppressLint("NewApi")
    @SuppressWarnings("deprecation")
    protected void dismissWithAnimation() {

        if (mSdkVersion > android.os.Build.VERSION_CODES.HONEYCOMB_MR1) {

            if(mToastView != null) {

                int viewWidth = mToastView.getWidth();

                mToastView.animate().translationX(viewWidth).alpha(0).setDuration(500)
                        .setListener(new AnimatorListenerAdapter() {

                            @Override
                            public void onAnimationEnd(Animator animation) {

                                /** Must use Handler to modify ViewGroup in onAnimationEnd() */
                                Handler mHandler = new Handler();
                                mHandler.post(mHideWithAnimationRunnable);

                            }

                        });

            }

        } else {

            AnimationSet animationSet = new AnimationSet(false);

            final Activity activity = (Activity) mContext;

            Display display = activity.getWindowManager()
                    .getDefaultDisplay();

            int width = display.getWidth();

            TranslateAnimation translateAnimation = new TranslateAnimation(0f, width, 0f, 0f);
            translateAnimation.setDuration(500);
            animationSet.addAnimation(translateAnimation);

            AlphaAnimation alphaAnimation = new AlphaAnimation(1f, 0f);
            alphaAnimation.setDuration(500);
            animationSet.addAnimation(alphaAnimation);

            animationSet.setAnimationListener(new AnimationListener() {

                @Override
                public void onAnimationEnd(Animation animation) {

                    /** Must use Handler to modify ViewGroup in onAnimationEnd() **/
                    Handler handler = new Handler();
                    handler.post(mHideImmediateRunnable);

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                    // Not used

                }

                @Override
                public void onAnimationStart(Animation animation) {

                    // Not used

                }

            });

            if(mToastView != null) {

                mToastView.startAnimation(animationSet);

            }

        }

    }

    private final Runnable mHideRunnable = new Runnable() {

        @Override
        public void run() {

            dismiss();

        }

    };

    private final Runnable mHideImmediateRunnable = new Runnable() {

        @Override
        public void run() {

            dismissImmediately();

        }

    };


    private final Runnable mHideWithAnimationRunnable = new Runnable() {

        @Override
        public void run() {

            dismissWithLayoutAnimation();

        }

    };

    private final Runnable mInvalidateRunnable = new Runnable() {

        @Override
        public void run() {

            if(mViewGroup != null) {

                mViewGroup.invalidate();

            }

        }

    };

    /** Dismisses and removes all showing/pending SuperCardToasts. */
    public static void cancelAllSuperCardToasts() {

        ManagerSuperCardToast.getInstance().clearQueue();

    }

    /**
     * Saves currently showing SuperCardToasts to a bundle.
     * <br>
     * @param bundle
     */
    public static void onSaveState(Bundle bundle) {

        SuperCardToast[] list = new SuperCardToast[ManagerSuperCardToast
                .getInstance().getList().size()];

        // Convert LinkedList to SuperCardToast[] to be saved in Bundle
        for(int i=0; i < ManagerSuperCardToast.getInstance().getList().size(); i++) {

            list[i] = ManagerSuperCardToast.getInstance().getList().get(i);

        }

        bundle.putSerializable(BUNDLE, list);

        ManagerSuperCardToast.getInstance().clear();

    }

    /**
     * Recreates SuperCardToasts that were showing during an orientation change.
     * <br>
     * @param bundle
     * @param activity
     */
    public static void onRestoreState(Bundle bundle, Activity activity) {

        if(bundle == null) {

            return;
        }

        for (SuperCardToast oldSuperCardToast : (SuperCardToast[]) bundle.getSerializable(BUNDLE)) {

            SuperCardToast newSuperCardToast;

            if (oldSuperCardToast.getType() == Type.BUTTON) {

                newSuperCardToast = new SuperCardToast(activity, Type.BUTTON);
                newSuperCardToast.setButtonOnClickListener(oldSuperCardToast.getButtonOnClickListener());
                newSuperCardToast.setButtonText(oldSuperCardToast.getButtonText());
                newSuperCardToast.setButtonTextSizeFloat(oldSuperCardToast.getButtonTextSize());
                newSuperCardToast.setButtonTextColor(oldSuperCardToast.getButtonTextColor());
                newSuperCardToast.setButtonTextTypeface(oldSuperCardToast.getButtonTextTypeface());

                if (oldSuperCardToast.getButtonDrawable() != null) {

                    newSuperCardToast.setButtonDrawable(oldSuperCardToast.getButtonDrawable());

                } else if (oldSuperCardToast.getButtonResource() != 0) {

                    newSuperCardToast.setButtonResource(oldSuperCardToast.getButtonResource());

                }

                if (oldSuperCardToast.getButtonDividerDrawable() != null) {

                    newSuperCardToast.setButtonDividerDrawable(oldSuperCardToast.getButtonDividerDrawable());

                } else if (oldSuperCardToast.getButtonDividerResource() != 0) {

                    newSuperCardToast.setButtonDividerResource(oldSuperCardToast.getButtonDividerResource());

                }

            } else if(oldSuperCardToast.getType() == Type.PROGRESS) {

                newSuperCardToast = new SuperCardToast(activity, Type.PROGRESS);
                newSuperCardToast.setProgressIndeterminate(oldSuperCardToast.getProgressIndeterminate());
                newSuperCardToast.setProgress(oldSuperCardToast.getProgress());

            } else if(oldSuperCardToast.getType() == Type.PROGRESS_HORIZONTAL) {

                newSuperCardToast = new SuperCardToast(activity, Type.PROGRESS_HORIZONTAL);
                newSuperCardToast.setProgressIndeterminate(oldSuperCardToast.getProgressIndeterminate());
                newSuperCardToast.setProgress(oldSuperCardToast.getProgress());

            }  else {

                newSuperCardToast = new SuperCardToast(activity);

            }

            newSuperCardToast.setText(oldSuperCardToast.getText());
            newSuperCardToast.setTextColor(oldSuperCardToast.getTextColor());
            newSuperCardToast.setTextSizeFloat(oldSuperCardToast.getTextSize());
            newSuperCardToast.setDuration(oldSuperCardToast.getDuration());
            newSuperCardToast.setIndeterminate(oldSuperCardToast.isIndeterminate());

            if(oldSuperCardToast.getIconDrawable() != null && oldSuperCardToast.getIconPosition() != null) {

                newSuperCardToast.setIconDrawable(oldSuperCardToast.getIconDrawable(), oldSuperCardToast.getIconPosition());

            } else if(oldSuperCardToast.getIconResource() != 0 && oldSuperCardToast.getIconPosition() != null) {

                newSuperCardToast.setIconResource(oldSuperCardToast.getIconResource(), oldSuperCardToast.getIconPosition());

            }

            newSuperCardToast.setOnClickListener(oldSuperCardToast.getOnClickListener());

            if(oldSuperCardToast.getBackgroundDrawable() != null) {

                newSuperCardToast.setBackgroundDrawable(oldSuperCardToast.getBackgroundDrawable());

            } else {

                newSuperCardToast.setBackgroundResource(oldSuperCardToast.getBackgroundResource());

            }

            newSuperCardToast.setTypeface(oldSuperCardToast.getTypeface());

            if(oldSuperCardToast.isTouchDismissable()) {

                newSuperCardToast.setTouchToDismiss(oldSuperCardToast.isTouchDismissable());

            } else if (oldSuperCardToast.isSwipeDismissable()) {

                newSuperCardToast.setSwipeToDismiss(oldSuperCardToast.isSwipeDismissable());

            }

            //    newSuperCardToast.setOnDismissListener(oldSuperCardToast.getOnDismissListener());
            newSuperCardToast.show();

        }
    }

    private OnTouchListener mTouchDismissListener = new OnTouchListener() {

        int timesTouched;

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {

            /**
             * Hack to prevent the user from repeatedly
             * touching the SuperProgressToast causing erratic behavior
             */
            if (timesTouched == 0) {

                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                    dismiss();

                }

            }

            timesTouched++;

            return false;

        }

    };


}