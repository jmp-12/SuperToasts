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
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.*;
import android.util.Log;
import android.util.TypedValue;
import android.view.*;
import android.view.View.OnTouchListener;
import android.view.animation.*;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.github.johnpersano.supertoasts.SuperToast.Animations;
import com.github.johnpersano.supertoasts.SuperToast.IconPosition;
import com.github.johnpersano.supertoasts.SuperToast.Type;
import com.github.johnpersano.supertoasts.util.OnToastButtonClickListenerHolder;
import com.github.johnpersano.supertoasts.util.OnToastDismissListenerHolder;
import com.github.johnpersano.supertoasts.util.SwipeDismissListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/**
 * SuperCardToasts are designed to be used inside of activities. SuperCardToasts
 * are designed to be displayed at the top of an activity to display non-essential messages.
 */
@SuppressWarnings("UnusedDeclaration")
public class SuperCardToast {

    private static final String TAG = "SuperCardToast";

    private static final String ERROR_CONTEXTNOTACTIVITY  = "Context must be an instance of Activity";
    private static final String ERROR_CONTAINERNULL = "You must have a LinearLayout with the id of card_container in your layout!";
    private static final String ERROR_VIEWCONTAINERNULL = "Either the View or Container was null when trying to dismiss. Did you create and " +
            "show a SuperCardToast before trying to dismiss it?";

    private static final String WARNING_PREHONEYCOMB  = "Swipe to dismiss was enabled but the SDK version is pre-Honeycomb";

    /** Bundle tag with a hex as a string so it can't interfere with other items in bundle */
    private static final String BUNDLE_TAG = "0x532e432e542e";

    /** Fragment tag with a hex as a string so it can't interfere with other fragment tags */
    public static String FRAGMENTRETAINER_ID = "0x532e432e542e462e522e" ;


    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private ViewGroup mViewGroup;
    private View mToastView;
    private View mDividerView;
    private Handler mHandler;
    private TextView mMessageTextView;
    private Button mToastButton;
    private LinearLayout mRootLayout;
    private ProgressBar mProgressBar;
    private int mDuration = SuperToast.Duration.SHORT;
    private boolean isIndeterminate;
    private OnToastDismissListenerHolder mOnDismissListener;
    private Animations mAnimations = Animations.FADE;
    private int mIconResouce;
    private IconPosition mIconPosition;
    private int mBackgroundResouce = SuperToast.Background.TRANSLUCENT_BLACK;
    private boolean isTouchDismissable;
    private boolean isSwipeDismissable;
    private int mButtonResource = SuperToast.Icon.Dark.UNDO;
    private int mButtonDividerResource = (R.color.light_gray);
    private boolean isProgressIndeterminate;
    private Type mType = Type.STANDARD;
    private boolean showImmediate;
    private String mClickListenerTag;
    private int mTypeface = Typeface.NORMAL;
    private int mButtonTypeface = Typeface.BOLD;
    private String mDismissListenerTag;

    /**
     * Instantiates a new SuperCardToast.
     * <br>
     * @param context should be Activity
     */
    public SuperCardToast(Context context) {

        if(context instanceof Activity) {

            this.mContext = context;

            this.mType = Type.STANDARD;

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

                throw new IllegalArgumentException(TAG + ERROR_CONTAINERNULL);

            }

        } else {

            throw new IllegalArgumentException(TAG + ERROR_CONTEXTNOTACTIVITY);

        }

    }

    /**
     * Instantiates a new SuperCardToast with a type.
     * <br>
     * @param context should be Activity
     * @param type choose from SuperToast.Type
     * <br>
     */
    public SuperCardToast(Context context, Type type) {

        if(context instanceof Activity) {

            this.mContext = context;

            final Activity activity = (Activity) context;

            mLayoutInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            this.mType = type;

            if (activity.findViewById(R.id.card_container) != null) {

                mViewGroup = (LinearLayout) activity
                        .findViewById(R.id.card_container);

                if(type == Type.BUTTON) {

                    mToastView = mLayoutInflater
                            .inflate(R.layout.supercardtoast_button, mViewGroup, false);

                    mToastButton = (Button)
                            mToastView.findViewById(R.id.button);

                    mDividerView = mToastView.findViewById(R.id.divider);

                    mToastButton.setOnTouchListener(mTouchDismissListener);

                } else if(type == Type.PROGRESS) {

                    mToastView = mLayoutInflater
                            .inflate(R.layout.supercardtoast_progresscircle, mViewGroup, false);

                    mProgressBar = (ProgressBar)
                            mToastView.findViewById(R.id.progressBar);

                } else if(type == Type.PROGRESS_HORIZONTAL) {

                    mToastView = mLayoutInflater
                            .inflate(R.layout.supercardtoast_progresshorizontal, mViewGroup, false);

                    mProgressBar = (ProgressBar)
                            mToastView.findViewById(R.id.progressBar);

                } else {

                    mToastView = mLayoutInflater
                            .inflate(R.layout.supercardtoast, mViewGroup, false);

                }

                mMessageTextView = (TextView)
                        mToastView.findViewById(R.id.message_textView);

                mRootLayout = (LinearLayout)
                        mToastView.findViewById(R.id.root_layout);

            } else {

                throw new IllegalArgumentException(TAG + ERROR_CONTAINERNULL);

            }

        } else {

            throw new IllegalArgumentException(TAG + ERROR_CONTEXTNOTACTIVITY);

        }

    }


    /** Shows the SuperCardToast. */
    public void show() {

        ManagerSuperCardToast.getInstance().add(this);

        if(!isIndeterminate) {

            mHandler = new Handler();
            mHandler.postDelayed(mHideRunnable, mDuration);

        }

        mViewGroup.addView(mToastView);

        if(!showImmediate) {

            final Animation animation = this.getShowAnimation();

            /** Invalidate the ViewGroup after the show animation completes **/
            animation.setAnimationListener(new Animation.AnimationListener() {

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

    }

    /**
     * Returns the Type of SuperCardToast.
     */
    public Type getType() {

        return mType;

    }

    /**
     * Sets the message text of the SuperCardToast.
     * <br>
     * @param text The message text
     */
    public void setText(CharSequence text) {

        mMessageTextView.setText(text);

    }

    /**
     * Returns the message text of the SuperCardToast.
     */
    public CharSequence getText() {

        return mMessageTextView.getText();

    }

    /**
     * Sets the message typeface of the SuperCardToast.
     * <br>
     * @param typeface Use a Typeface constants
     */
    public void setTypeface(int typeface) {

        mTypeface = typeface;

        mMessageTextView.setTypeface(mMessageTextView.getTypeface(), typeface);

    }

    /**
     * Returns the message typeface of the SuperCardToast.
     */
    public int getTypeface() {

        return mTypeface;

    }

    /**
     * Sets the message text color of the SuperCardToast.
     * <br>
     * @param textColor The message text color
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
     * @param textSize Will automaticly convert to SP
     */
    public void setTextSize(int textSize) {

        mMessageTextView.setTextSize(textSize);

    }

    protected void setTextSizeFloat(float textSize) {

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
     * @param duration Use SuperToast.Duration constants
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
     * @param isIndeterminate If true will show until dismissed
     */
    public void setIndeterminate(boolean isIndeterminate) {

        this.isIndeterminate = isIndeterminate;

    }

    /**
     * Returns an indeterminate duration of the SuperCardToast.
     */
    public boolean isIndeterminate() {

        return this.isIndeterminate;

    }


    /**
     * Sets an icon resource to the SuperCardToast
     * with a position.
     * <br>
     * @param iconResource Use SuperToast.Icon constants
     * @param iconPosition Use SuperToast.IconPosition
     */
    public void setIcon(int iconResource, IconPosition iconPosition) {

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
     * Returns the icon position of the SuperCardToast
     */
    public IconPosition getIconPosition() {

        return this.mIconPosition;

    }

    /**
     * Returns the drawable resource of the SuperCardToast
     */
    public int getIconResource() {

        return this.mIconResouce;

    }

    /**
     * Sets the background resource of the SuperCardToast.
     * <br>
     * @param backgroundResource Use SuperToast.Background constants
     */
    public void setBackgroundResource(int backgroundResource) {

        this.mBackgroundResouce = backgroundResource;

        mRootLayout.setBackgroundResource(backgroundResource);

    }

    /**
     * Returns the background resource of the SuperCardToast
     */
    public int getBackgroundResource() {

        return this.mBackgroundResouce;

    }


    /**
     * Sets the animation of the SuperCardToast.
     * <br>
     * @param animations Use SuperToast.Animations
     */
    public void setAnimations(Animations animations) {

        this.mAnimations = animations;

    }

    /**
     * Gets the animation of the SuperCardToast.
     */
    public Animations getAnimation() {

        return this.mAnimations;

    }

    public void setShowImmediate(boolean showImmediate){

        this.showImmediate = showImmediate;
    }

    public boolean getShowImmediate(){

        return this.showImmediate;

    }

    /**
     * Sets a private OnTouchListener to the SuperCardToast
     * View that will dismiss it when touched.
     * <br>
     * @param touchDismiss If true will dismiss when touched
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
     * Returns if SuperCardToast is touch dismissable
     */
    public boolean isTouchDismissable() {

        return this.isTouchDismissable;

    }

    /**
     * Sets a private OnTouchListener to the SuperCardToast
     * that will dismiss the SuperCardToast when swiped.
     * <br>
     * @param swipeDismiss
     */
    public void setSwipeToDismiss(boolean swipeDismiss) {

        this.isSwipeDismissable = swipeDismiss;

        if(swipeDismiss) {

            if (Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.HONEYCOMB_MR1) {

                final SwipeDismissListener swipeDismissListener = new SwipeDismissListener(
                        mToastView, new SwipeDismissListener.OnDismissCallback() {

                    @Override
                    public void onDismiss(View view) {

                        dismissImmediately();

                    }

                });

                mToastView.setOnTouchListener(swipeDismissListener);

            } else {

                Log.w(TAG, WARNING_PREHONEYCOMB);

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


    /**
     * Sets an OnDismissListener defined in this library
     * to the SuperCardToast.
     * <br>
     * @param onToastDismissListener Use OnToastDismissListenerHolder for orientation change support
     */
    public void setOnToastDismissListener(OnToastDismissListenerHolder onToastDismissListener) {

        this.mOnDismissListener = onToastDismissListener;
        this.mDismissListenerTag = onToastDismissListener.getTag();

        /** On devices > API 11 save listener to retained Fragment */
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {

            final Activity activity = (Activity) mContext;

            FragmentManager fragmentManager = activity.getFragmentManager();

            FragmentRetainer fragmentRetainer = (FragmentRetainer)
                    fragmentManager.findFragmentByTag(FRAGMENTRETAINER_ID);

            if (fragmentRetainer == null) {

                fragmentRetainer = new FragmentRetainer();
                fragmentManager.beginTransaction().add(fragmentRetainer, FRAGMENTRETAINER_ID).commit();

            }

            fragmentRetainer.addDismissListenerToList(onToastDismissListener);

        }


    }

    /** Used in ManagerSuperCardToast*/
    protected OnToastDismissListenerHolder getOnDismissListener() {

        return this.mOnDismissListener;

    }

    private String getDismissListenerTag() {

        return mDismissListenerTag;

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

            if(mOnDismissListener != null) {

                mOnDismissListener.onDismiss(getView());

            }

            mToastView = null;

        } else {

            Log.e(TAG, ERROR_VIEWCONTAINERNULL);

        }


    }


    /** Hide the SuperCardToast and animate the Layout. Post Honeycomb only. **/
    @SuppressLint("NewApi")
    private void dismissWithLayoutAnimation() {

        if(mToastView != null) {

            mToastView.setVisibility(View.INVISIBLE);

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

                    if(mToastView != null) {

                        layoutParams.height = (Integer) valueAnimator.getAnimatedValue();
                        mToastView.setLayoutParams(layoutParams);

                    }

                }

            });

            animator.start();

        } else {

            dismissImmediately();

        }

    }

    @SuppressLint("NewApi")
    @SuppressWarnings("deprecation")
    private void dismissWithAnimation() {

        Animation animation = this.getDismissAnimation();

        animation.setAnimationListener(new Animation.AnimationListener() {

                @Override
                public void onAnimationEnd(Animation animation) {

                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {

                        /** Must use Handler to modify ViewGroup in onAnimationEnd() **/
                        Handler handler = new Handler();
                        handler.post(mHideWithAnimationRunnable);

                    } else {

                        /** Must use Handler to modify ViewGroup in onAnimationEnd() **/
                        Handler handler = new Handler();
                        handler.post(mHideImmediateRunnable);

                    }

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

            if (mToastView != null) {

                mToastView.startAnimation(animation);

            }

    }

    /**
     * Sets an OnToastButtonClickListener to the button in a
     * a BUTTON type SuperCardToast.
     * <br>
     * IMPORTANT: On Devices > API 11 any listener passed will automaticly be saved to a retained Fragment
     * for preservation on orientation changes.
     * <br>
     * @param onToastButtonClickListener Use OnToastButtonClickListenerHolder for orientation change support
     */
    public void setOnToastButtonClickListener(OnToastButtonClickListenerHolder onToastButtonClickListener) {

        mToastButton.setOnClickListener(onToastButtonClickListener);

        this.mClickListenerTag = onToastButtonClickListener.getTag();

        /** On devices > API 11 save listener to retained Fragment */
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {

            final Activity activity = (Activity) mContext;

            FragmentManager fragmentManager = activity.getFragmentManager();

            FragmentRetainer fragmentRetainer = (FragmentRetainer)
                    fragmentManager.findFragmentByTag(FRAGMENTRETAINER_ID);

            if (fragmentRetainer == null) {

                fragmentRetainer = new FragmentRetainer();
                fragmentManager.beginTransaction().add(fragmentRetainer, FRAGMENTRETAINER_ID).commit();

            }

            fragmentRetainer.addClickListenerToList(onToastButtonClickListener);

        }

    }

    private String getClickListenerTag() {

        return mClickListenerTag;

    }

    /**
     * Sets the background resource of the Button in
     * a BUTTON type SuperCardToast.
     * <br>
     * @param buttonResource Use SuperToast.Icon constants
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
     * a BUTTON type SuperCardToast.
     */
    public int getButtonResource() {

        return this.mButtonResource;

    }

    /**
     * Sets the background resource of the Button divider in
     * a BUTTON Type SuperCardToast.
     * <br>
     * @param dividerResource Use color resources to maintain design consistncy
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
     * Sets the text of the Button in
     * a BUTTON Type SuperCardToast.
     * <br>
     * @param buttonText Should be all uppercase and about 4 characters long
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
     * Sets the typeface of the button in
     * a BUTTON type SuperCardToast.
     * <br>
     * @param typeface Use Typeface consstants
     */
    public void setButtonTypeface(int typeface) {

        mButtonTypeface = typeface;

        mToastButton.setTypeface(mToastButton.getTypeface(), typeface);

    }

    /**
     * Returns the typeface of the button in
     * a BUTTON type SuperCardToast.
     */
    public int getButtonTypeface() {

        return mButtonTypeface;

    }

    /**
     * Sets the text color of the Button in
     * a BUTTON type SuperCardToast.
     * <br>
     * @param buttonTextColor Should have alpha of around 175
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
     * Sets the text size of the Button in
     * a BUTTON type SuperCardToast.
     * <br>
     * @param buttonTextSize Will convert to SP
     */
    public void setButtonTextSize(int buttonTextSize) {

        if (mToastButton != null) {

            mMessageTextView.setTextSize(buttonTextSize);

        }

    }

    private void setButtonTextSizeFloat(float buttonTextSize) {

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
     * a PROGRESS_HORIZONTAL type SuperCardToast.
     * <br>
     * @param progress Max default is 100
     */
    public void setProgress(int progress) {

        if (mProgressBar != null) {

            mProgressBar.setProgress(progress);

        }

    }

    /**
     * Returns the progress of the ProgressBar in
     * a PROGRESS_HORIZONTAL type SuperCardToast.
     */
    public int getProgress() {

        return mProgressBar.getProgress();

    }

    /**
     * Sets an indeterminate value to the ProgressBar of a PROGRESS type
     * SuperCardToast.
     * <br>
     * @param isIndeterminate If true will be indeterminate
     */
    public void setProgressIndeterminate(boolean isIndeterminate) {

        this.isProgressIndeterminate = isIndeterminate;

        if(mProgressBar != null) {

            mProgressBar.setIndeterminate(isIndeterminate);

        }

    }

    /**
     * Returns if an indeterminate value to the ProgressBar of a PROGRESS type
     * SuperCardToast has been set.
     */
    public boolean getProgressIndeterminate() {

        return this.isProgressIndeterminate;

    }


    /**
     * Returns the SuperCardToast TextView.
     * <br>
     * @return TextView <br>
     */
    public TextView getTextView() {

        return mMessageTextView;

    }

    /**
     * Returns the SuperCardToast View.
     * <br>
     * @return View <br>
     */
    public View getView() {

        return mToastView;

    }

    /**
     * Returns true if the SuperCardToast is showing.
     * <br>
     * @return boolean <br>
     */
    public boolean isShowing() {

        return mToastView != null && mToastView.isShown();

    }

    /**
     * Returns the calling Activity of the SuperCardToast.
     * <br>
     * @return Activity <br>
     */
    public Activity getActivity() {

        return (Activity) mContext;

    }

    /**
     * Returns the ViewGroup that the SuperCardToast is attached to.
     * <br>
     * @return ViewGroup <br>
     */
    public ViewGroup getViewGroup() {

        return mViewGroup;

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

                mViewGroup.postInvalidate();
                
            }

        }

    };

    private Animation getShowAnimation() {

        if (this.getAnimation() == SuperToast.Animations.FLYIN) {

            TranslateAnimation translateAnimation = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 0.75f, Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);

            AlphaAnimation alphaAnimation = new AlphaAnimation(0f, 1f);

            AnimationSet animationSet = new AnimationSet(true);
            animationSet.addAnimation(translateAnimation);
            animationSet.addAnimation(alphaAnimation);
            animationSet.setInterpolator(new DecelerateInterpolator());
            animationSet.setDuration(250);

            return animationSet;

        } else if (this.getAnimation() == SuperToast.Animations.SCALE) {

            ScaleAnimation scaleAnimation = new ScaleAnimation(0.9f, 1.0f, 0.9f, 1.0f,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

            AlphaAnimation alphaAnimation = new AlphaAnimation(0f, 1f);

            AnimationSet animationSet = new AnimationSet(true);
            animationSet.addAnimation(scaleAnimation);
            animationSet.addAnimation(alphaAnimation);
            animationSet.setInterpolator(new DecelerateInterpolator());
            animationSet.setDuration(250);

            return animationSet;

        } else if (this.getAnimation() == SuperToast.Animations.POPUP) {

            TranslateAnimation translateAnimation = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.1f, Animation.RELATIVE_TO_SELF, 0.0f);

            AlphaAnimation alphaAnimation = new AlphaAnimation(0f, 1f);

            AnimationSet animationSet = new AnimationSet(true);
            animationSet.addAnimation(translateAnimation);
            animationSet.addAnimation(alphaAnimation);
            animationSet.setInterpolator(new DecelerateInterpolator());
            animationSet.setDuration(250);

            return animationSet;

        } else {

            Animation animation= new AlphaAnimation(0f, 1f);
            animation.setDuration(500);
            animation.setInterpolator(new DecelerateInterpolator());

            return animation;

        }


    }

    private Animation getDismissAnimation() {

        if (this.getAnimation() == SuperToast.Animations.FLYIN) {

            TranslateAnimation translateAnimation = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, .75f,
                    Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);

            AlphaAnimation alphaAnimation = new AlphaAnimation(1f, 0f);

            AnimationSet animationSet = new AnimationSet(true);
            animationSet.addAnimation(translateAnimation);
            animationSet.addAnimation(alphaAnimation);
            animationSet.setInterpolator(new AccelerateInterpolator());
            animationSet.setDuration(250);

            return animationSet;

        } else if (this.getAnimation() == SuperToast.Animations.SCALE) {

            ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 0.9f, 1.0f, 0.9f,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

            AlphaAnimation alphaAnimation = new AlphaAnimation(1f, 0f);

            AnimationSet animationSet = new AnimationSet(true);
            animationSet.addAnimation(scaleAnimation);
            animationSet.addAnimation(alphaAnimation);
            animationSet.setInterpolator(new DecelerateInterpolator());
            animationSet.setDuration(250);

            return animationSet;

        } else if (this.getAnimation() == SuperToast.Animations.POPUP) {

            TranslateAnimation translateAnimation = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.1f);

            AlphaAnimation alphaAnimation = new AlphaAnimation(1f, 0f);

            AnimationSet animationSet = new AnimationSet(true);
            animationSet.addAnimation(translateAnimation);
            animationSet.addAnimation(alphaAnimation);
            animationSet.setInterpolator(new DecelerateInterpolator());
            animationSet.setDuration(250);

            return animationSet;

        } else {

            AlphaAnimation alphaAnimation = new AlphaAnimation(1f, 0f);
            alphaAnimation.setDuration(500);
            alphaAnimation.setInterpolator(new AccelerateInterpolator());

            return alphaAnimation;

        }

    }

    /**
     * Returns a dark theme SuperCardToast.
     * <br>
     * IMPORTANT: Activity layout should contain a linear layout
     * with the id card_container
     * <br>
     * @param context Should be Activity
     * @param textCharSequence Message text
     * @param durationInteger Should use SuperToast.Duration constants
     * @return SuperCardToast
     */
    public static SuperCardToast createSuperCardToast(
            Context context, CharSequence textCharSequence, int durationInteger) {

        SuperCardToast superCardToast = new SuperCardToast(context);
        superCardToast.setText(textCharSequence);
        superCardToast.setDuration(durationInteger);

        return superCardToast;

    }

    /**
     * Returns a dark theme SuperCardToast with a supplied animation.
     * <br>
     * IMPORTANT: Activity layout should contain a linear layout
     * with the id card_container
     * <br>
     * @param context Should be Activity
     * @param textCharSequence Message text
     * @param durationInteger Should use SuperToast.Duration constants
     * @param animations Should use SuperToast.Animations
     * @return SuperCardToast
     */
    public static SuperCardToast createSuperCardToast(
            Context context, CharSequence textCharSequence, int durationInteger, Animations animations) {

        SuperCardToast superCardToast = new SuperCardToast(context);
        superCardToast.setText(textCharSequence);
        superCardToast.setDuration(durationInteger);
        superCardToast.setAnimations(animations);

        return superCardToast;

    }

    /**
     * Returns a light theme SuperCardToast.
     * <br>
     * IMPORTANT: Activity layout should contain a linear layout
     * with the id card_container
     * <br>
     * @param context Should be Activity
     * @param textCharSequence Message text
     * @param durationInteger Should use SuperToast.Duration constants
     * @return SuperCardToast
     */
    public static SuperCardToast createLightSuperCardToast(
            Context context, CharSequence textCharSequence, int durationInteger) {

        SuperCardToast SuperCardToast = new SuperCardToast(context);
        SuperCardToast.setText(textCharSequence);
        SuperCardToast.setDuration(durationInteger);
        SuperCardToast.setBackgroundResource(SuperToast.Background.WHITE);
        SuperCardToast.setTextColor(Color.BLACK);

        return SuperCardToast;

    }

    /**
     * Returns a light theme SuperCardToast with a supplied animation.
     * <br>
     * IMPORTANT: Activity layout should contain a linear layout
     * with the id card_container
     * <br>
     * @param context Should be Activity
     * @param textCharSequence Message text
     * @param durationInteger Should use SuperToast.Duration constants
     * @param animations Should use SuperToast.Animations
     * @return SuperCardToast
     */
    public static SuperCardToast createLightSuperCardToast(
            Context context, CharSequence textCharSequence, int durationInteger, Animations animations) {

        SuperCardToast SuperCardToast = new SuperCardToast(context);
        SuperCardToast.setText(textCharSequence);
        SuperCardToast.setDuration(durationInteger);
        SuperCardToast.setBackgroundResource(SuperToast.Background.WHITE);
        SuperCardToast.setTextColor(Color.BLACK);
        SuperCardToast.setAnimations(animations);

        return SuperCardToast;

    }

    /** Dismisses and removes all showing/pending SuperCardToasts. */
    public static void cancelAllSuperCardToasts() {

        ManagerSuperCardToast.getInstance().clearQueue();

    }

    /**
     * Saves pending/shown SuperCardToasts to a bundle.
     * <br>
     * @param bundle Use onSaveInstanceState() bundle
     */
    public static void onSaveState(Bundle bundle) {

        Style[] list = new Style[ManagerSuperCardToast
                .getInstance().getList().size()];

        LinkedList<SuperCardToast> lister = ManagerSuperCardToast
                .getInstance().getList();

        for(int i = 0; i < list.length; i++) {

            list[i] = new Style(lister.get(i));

        }

        bundle.putParcelableArray(BUNDLE_TAG, list);

        SuperCardToast.cancelAllSuperCardToasts();

    }

    /**
     * Returns and shows pending/shown SuperCardToasts from orientation change.
     * <br>
     * IMPORTANT: On devices > API 11 this method will automatically save any OnClickListeners and OnDismissListeners
     * via retained Fragment.
     * <br>
     * @param bundle Use onCreate() bundle
     * @param activity The current activity
     */
    public static void onRestoreState(Bundle bundle, Activity activity) {

        if(bundle == null) {

            return;
        }

        Parcelable[] savedArray = bundle.getParcelableArray(BUNDLE_TAG);

        int i = 0;

        if(savedArray != null) {

            for (Parcelable parcelable : savedArray) {

                i++;

                new SuperCardToast(activity, (Style) parcelable, null, null, i);

            }

        }

    }

    /**
     * Returns and shows pending/shown SuperCardToasts from orientation change and
     * reattaches any OnToastButtonClickListeners.
     * <br>
     * IMPORTANT: Use this method to save OnToastButtonClickListeners on devices with
     * API < 11. Otherwise use onRestoreState(Bundle bundle, Activity activity) as that
     * method uses a retained fragment to save listeners.
     * <br>
     * @param bundle Use onCreate() bundle
     * @param activity The current activity
     * @param onToastButtonClickListeners List of any attached OnToastButtonClickListenerHolder from previous orientation
     */
    public static void onRestoreState(Bundle bundle, Activity activity, List<OnToastButtonClickListenerHolder> onToastButtonClickListeners) {

        if(bundle == null) {

            return;
        }

        Parcelable[] savedArray = bundle.getParcelableArray(BUNDLE_TAG);

        int i = 0;

        if(savedArray != null) {

            for (Parcelable parcelable : savedArray) {

                i++;

                new SuperCardToast(activity, (Style) parcelable, onToastButtonClickListeners, null, i);

            }

        }

    }

    /**
     * Returns and shows pending/shown SuperCardToasts from orientation change and
     * reattaches any OnToastButtonClickListeners and any OnToastDismissListeners.
     * <br>
     * IMPORTANT: Use this method to save OnToastButtonClickListeners and
     * OnToastDismissListenerHolder on devices with API < 11. Otherwise use
     * onRestoreState(Bundle bundle, Activity activity) as that method uses
     * a retained fragment to save listeners.
     * <br>
     * @param bundle Use onCreate() bundle
     * @param activity The current activity
     * @param onToastButtonClickListeners List of any attached OnToastButtonClickListenerHolders from previous orientation
     * @param onToastDismissListeners List of any attached OnToastDismissListenerHolders from previous orientation
     */
    public static void onRestoreState(Bundle bundle, Activity activity, List<OnToastButtonClickListenerHolder> onToastButtonClickListeners,
                                      List<OnToastDismissListenerHolder> onToastDismissListeners) {

        if(bundle == null) {

            return;
        }

        Parcelable[] savedArray = bundle.getParcelableArray(BUNDLE_TAG);

        int i = 0;

        if(savedArray != null) {

            for (Parcelable parcelable : savedArray) {

                i++;

                new SuperCardToast(activity, (Style) parcelable, onToastButtonClickListeners, onToastDismissListeners, i);

            }

        }

    }

    /**
     * Method used to recreate SuperCardToasts after orientation change
     */
    private SuperCardToast(Activity activity, Style style, List<OnToastButtonClickListenerHolder> onToastButtonClickListeners,
                 List<OnToastDismissListenerHolder> onToastDismissListeners, int position) {

        SuperCardToast superCardToast;

        if (style.mType == Type.BUTTON) {

            superCardToast = new SuperCardToast(activity, Type.BUTTON);
            superCardToast.setButtonText(style.mButtonText);
            superCardToast.setButtonTextSizeFloat(style.mButtonTextSize);
            superCardToast.setButtonTextColor(style.mButtonTextColor);
            superCardToast.setButtonResource(style.mButtonResource);
            superCardToast.setButtonDividerResource(style.mButtonDividerResource);
            superCardToast.setButtonTypeface(style.mButtonTypeface);

            /** Reattach any OnToastButtonClickListeners via retained Fragment */
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {

                FragmentRetainer fragmentRetainer = (FragmentRetainer)
                        activity.getFragmentManager().findFragmentByTag(FRAGMENTRETAINER_ID);

                if (fragmentRetainer != null && fragmentRetainer.getClickListeners() != null) {

                    for (OnToastButtonClickListenerHolder onToastButtonClickListenerHolder : fragmentRetainer.getClickListeners()) {

                        if(onToastButtonClickListenerHolder.getTag().equals(style.mClickListenerTag)) {

                            superCardToast.setOnToastButtonClickListener(onToastButtonClickListenerHolder);

                        }

                    }

                }

            /** Reattach any OnToastButtonClickListeners via provided List */
            } else {

                if(onToastButtonClickListeners != null) {

                    for (OnToastButtonClickListenerHolder onToastButtonClickListenerHolder : onToastButtonClickListeners) {

                        if(onToastButtonClickListenerHolder.getTag().equalsIgnoreCase(style.mClickListenerTag)) {

                            superCardToast.setOnToastButtonClickListener(onToastButtonClickListenerHolder);

                        }

                    }
                }

            }

        } else if(style.mType == Type.PROGRESS) {

            /** PROGRESS style SuperCardToasts should be managed by the developer */

            return;

        } else if(style.mType == Type.PROGRESS_HORIZONTAL) {

            /** PROGRESS_HORIZONTAL style SuperCardToasts should be managed by the developer */

            return;

        }  else {

            superCardToast = new SuperCardToast(activity);

        }

        /** Reattach any OnToastBDismissListeners via retained Fragment */
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {

            FragmentRetainer fragmentRetainer = (FragmentRetainer)
                    activity.getFragmentManager().findFragmentByTag(FRAGMENTRETAINER_ID);

            if (fragmentRetainer != null && fragmentRetainer.getDismissListeners() != null) {

                for (OnToastDismissListenerHolder onToastDismissListenerHolder : fragmentRetainer.getDismissListeners()) {

                    if(onToastDismissListenerHolder.getTag().equals(style.mDismissListenerTag)) {

                        superCardToast.setOnToastDismissListener(onToastDismissListenerHolder);

                    }

                }

            }

        /** Reattach any OnToastBDismissListeners via provided List */
        } else {

            if(onToastDismissListeners != null) {

                for (OnToastDismissListenerHolder onToastDismissListenerHolder : onToastDismissListeners) {

                    if(onToastDismissListenerHolder.getTag().equalsIgnoreCase(style.mDismissListenerTag)) {

                        superCardToast.setOnToastDismissListener(onToastDismissListenerHolder);

                    }

                }
            }

        }

        superCardToast.setAnimations(style.mAnimations);
        superCardToast.setText(style.mText);
        superCardToast.setTypeface(style.mTypeface);
        superCardToast.setDuration(style.mDuration);
        superCardToast.setTextColor(style.mTextColor);
        superCardToast.setTextSizeFloat(style.mTextSize);
        superCardToast.setIndeterminate(style.isIndeterminate);
        superCardToast.setIcon(style.mIconResource, style.mIconPosition);
        superCardToast.setBackgroundResource(style.mBackgroundResource);

        /** Must use if else statements here to prevent erratic behavior */
        if(style.isTouchDismissable) {

            superCardToast.setTouchToDismiss(true);

        } else if(style.isSwipeDismissable) {

            superCardToast.setSwipeToDismiss(true);

        }

        superCardToast.setShowImmediate(true);
        superCardToast.show();

    }

    private OnTouchListener mTouchDismissListener = new OnTouchListener() {

        int timesTouched;

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {

            /** Hack to prevent repeat touch events causing erratic behavior */
            if (timesTouched == 0) {

                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                    dismiss();

                }

            }

            timesTouched++;

            return false;

        }

    };


    /**
     * Parcelable class that saves all data on orientation change
     */
    private static class Style implements Parcelable  {

        //STANDARD
        Type mType;
        String mText;
        int mDuration;
        int mTextColor;
        float mTextSize;
        boolean isIndeterminate;
        IconPosition mIconPosition;
        int mIconResource;
        int mBackgroundResource;
        boolean isTouchDismissable;
        boolean isSwipeDismissable;
        Animations mAnimations;
        int mTypeface;
        String mDismissListenerTag;

        //BUTTON type stuff
        String mButtonText;
        float mButtonTextSize;
        int mButtonTextColor;
        int mButtonResource;
        int mButtonDividerResource;
        String mClickListenerTag;
        int mButtonTypeface;


        public Style(SuperCardToast superCardToast) {

            mType = superCardToast.getType();

            if (mType == Type.BUTTON) {

                mButtonText = superCardToast.getButtonText().toString();
                mButtonTextSize = superCardToast.getButtonTextSize();
                mButtonTextColor = superCardToast.getButtonTextColor();
                mButtonResource = superCardToast.getButtonResource();
                mButtonDividerResource = superCardToast.getButtonDividerResource();
                mClickListenerTag = superCardToast.getClickListenerTag();
                mButtonTypeface = superCardToast.getButtonTypeface();

            }

            if(superCardToast.getIconResource() != 0 && superCardToast.getIconPosition() != null) {

                mIconResource = superCardToast.getIconResource();
                mIconPosition = superCardToast.getIconPosition();

            }

            mDismissListenerTag = superCardToast.getDismissListenerTag();
            mAnimations = superCardToast.getAnimation();
            mText = superCardToast.getText().toString();
            mTypeface = superCardToast.getTypeface();
            mDuration = superCardToast.getDuration();
            mTextColor = superCardToast.getTextColor();
            mTextSize = superCardToast.getTextSize();
            isIndeterminate = superCardToast.isIndeterminate();
            mBackgroundResource = superCardToast.getBackgroundResource();
            isTouchDismissable = superCardToast.isTouchDismissable();
            isSwipeDismissable = superCardToast.isSwipeDismissable();

        }

        public Style(Parcel parcel) {

            mType = Type.values()[parcel.readInt()];

            if(mType == Type.BUTTON) {

                mButtonText = parcel.readString();
                mButtonTextSize = parcel.readFloat();
                mButtonTextColor = parcel.readInt();
                mButtonResource = parcel.readInt();
                mButtonDividerResource = parcel.readInt();
                mButtonTypeface = parcel.readInt();
                mClickListenerTag = parcel.readString();

            }

            boolean hasIcon = parcel.readByte() != 0;

            if(hasIcon) {

                mIconResource = parcel.readInt();
                mIconPosition = IconPosition.values()[parcel.readInt()];

            }

            mDismissListenerTag = parcel.readString();
            mAnimations = Animations.values()[parcel.readInt()];
            mText = parcel.readString();
            mTypeface = parcel.readInt();
            mDuration = parcel.readInt();
            mTextColor = parcel.readInt();
            mTextSize = parcel.readFloat();
            isIndeterminate = parcel.readByte() != 0;
            mBackgroundResource = parcel.readInt();
            isTouchDismissable = parcel.readByte() != 0;
            isSwipeDismissable = parcel.readByte() != 0;

        }


        @Override
        public void writeToParcel(Parcel parcel, int i) {

            parcel.writeInt(mType.ordinal());

            if (mType == Type.BUTTON) {

                parcel.writeString(mButtonText);
                parcel.writeFloat(mButtonTextSize);
                parcel.writeInt(mButtonTextColor);
                parcel.writeInt(mButtonResource);
                parcel.writeInt(mButtonDividerResource);
                parcel.writeInt(mButtonTypeface);
                parcel.writeString(mClickListenerTag);

            }

            if(mIconResource != 0 && mIconPosition != null) {

                parcel.writeByte((byte) 1);

                parcel.writeInt(mIconResource);
                parcel.writeInt(mIconPosition.ordinal());

            } else {

                parcel.writeByte((byte) 0);

            }

            parcel.writeString(mDismissListenerTag);
            parcel.writeInt(mAnimations.ordinal());
            parcel.writeString(mText);
            parcel.writeInt(mTypeface);
            parcel.writeInt(mDuration);
            parcel.writeInt(mTextColor);
            parcel.writeFloat(mTextSize);
            parcel.writeByte((byte) (isIndeterminate ? 1 : 0));
            parcel.writeInt(mBackgroundResource);
            parcel.writeByte((byte) (isTouchDismissable ? 1 : 0));
            parcel.writeByte((byte) (isSwipeDismissable ? 1 : 0));

        }

        @Override
        public int describeContents() {
            return 0;

        }

        public final Creator CREATOR = new Creator() {

            public Style createFromParcel(Parcel parcel) {

                return new Style(parcel);

            }

            public Style[] newArray(int size) {

                return new Style[size];

            }

        };

    }


    /**
     * Fragment that saves OnClickListeners and OnDismissListeners on orientation change
     * for devices > API 11
     */
    private class FragmentRetainer extends Fragment {

        List<OnToastButtonClickListenerHolder> mOnToastButtonClickListenerHolderList;
        List<OnToastDismissListenerHolder> mOnToastDismissListenerHolderList;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            setRetainInstance(true);

        }

        private void addClickListenerToList(OnToastButtonClickListenerHolder onToastButtonClickListenerHolder) {

            if(mOnToastButtonClickListenerHolderList == null) {

                mOnToastButtonClickListenerHolderList = new ArrayList<OnToastButtonClickListenerHolder>();

            }

            boolean alreadyContains = false;

            for(OnToastButtonClickListenerHolder savedOnToastButtonClickListenerHolder : mOnToastButtonClickListenerHolderList) {

                if(savedOnToastButtonClickListenerHolder.getTag().equals(onToastButtonClickListenerHolder.getTag())) {

                    alreadyContains = true;

                }

            }

            if(!alreadyContains) {

                mOnToastButtonClickListenerHolderList.add(onToastButtonClickListenerHolder);

            }

        }

        private void addDismissListenerToList(OnToastDismissListenerHolder onToastDismissListenerHolder) {

            if(mOnToastDismissListenerHolderList == null) {

                mOnToastDismissListenerHolderList = new ArrayList<OnToastDismissListenerHolder>();

            }

            boolean alreadyContains = false;

            for(OnToastDismissListenerHolder savedOnToastDismissListenerHolderList : mOnToastDismissListenerHolderList) {

                if(savedOnToastDismissListenerHolderList.getTag().equals(onToastDismissListenerHolder.getTag())) {

                    alreadyContains = true;

                }

            }

            if(!alreadyContains) {

                mOnToastDismissListenerHolderList.add(onToastDismissListenerHolder);

            }

        }

        private List<OnToastButtonClickListenerHolder> getClickListeners() {

            return mOnToastButtonClickListenerHolderList;

        }

        private List<OnToastDismissListenerHolder> getDismissListeners() {

            return mOnToastDismissListenerHolderList;

        }

    }

}
