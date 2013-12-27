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

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.github.johnpersano.supertoasts.SuperToast.Animations;
import com.github.johnpersano.supertoasts.SuperToast.IconPosition;
import com.github.johnpersano.supertoasts.SuperToast.Type;
import com.github.johnpersano.supertoasts.util.OnToastButtonClickListenerHolder;
import com.github.johnpersano.supertoasts.util.OnToastDismissListenerHolder;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/**
 * SuperActivityToasts are designed to be used inside of Activities. When the
 * Activity is destroyed the SuperActivityToast is destroyed along with it.
 * SuperActivityToasts will not linger to the next screen like standard
 * Toasts/SuperToasts.
 */
@SuppressWarnings({"UnusedDeclaration", "BooleanMethodIsAlwaysInverted"})
public class SuperActivityToast {

    private static final String TAG = "SuperActivityToast";

    private static final String ERROR_CONTEXTNULL = " - The Context that you passed was null.";
    private static final String ERROR_CONTEXTNOTACTIVITY = " - The Context that you passed was not an Activity!";

    /** Bundle tag with a hex as a string so it can't interfere with other items in bundle */
    private static final String BUNDLE_TAG = "0x532e412e542e";

    /** Fragment tag with a hex as a string so it can't interfere with other fragment tags */
    public static String FRAGMENTRETAINER_ID = "0x532e412e542e462e522e" ;


    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private ViewGroup mViewGroup;
    private View mToastView;
    private View mDividerView;
    private TextView mMessageTextView;
    private Button mToastButton;
    private LinearLayout mRootLayout;
    private ProgressBar mProgressBar;
    private int mDuration = SuperToast.Duration.SHORT;
    private boolean mIsIndeterminate;
    private OnToastDismissListenerHolder mOnDismissListener;
    private Animations mAnimations = Animations.FADE;
    private int mIconResouce;
    private IconPosition mIconPosition;
    private int mBackgroundResouce = SuperToast.Background.TRANSLUCENT_BLACK;
    private boolean isTouchDismissable;
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
     * Instantiates a new SuperActivityToast.
     * <br>
     * @param context should be Activity
     */
    public SuperActivityToast(Context context) {

        if (context != null) {

            if (context instanceof Activity) {

                this.mContext = context;

                mLayoutInflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                final Activity activity = (Activity) context;

                mViewGroup = (ViewGroup) activity
                        .findViewById(android.R.id.content);

                mToastView = mLayoutInflater.inflate(R.layout.supertoast,
                        mViewGroup, false);

                mMessageTextView = (TextView) mToastView
                        .findViewById(R.id.message_textView);

                mRootLayout = (LinearLayout) mToastView
                        .findViewById(R.id.root_layout);

            } else {

                throw new IllegalArgumentException(TAG + ERROR_CONTEXTNOTACTIVITY);

            }

        } else {

            throw new IllegalArgumentException(TAG + ERROR_CONTEXTNULL);

        }

    }

    /**
     * Instantiates a new SuperActivityToast with a Type.
     * <br>
     * @param context should be Activity
     * @param type choose from SuperToast.Type
     * <br>
     */
    public SuperActivityToast(Context context, Type type) {

        if (context != null) {

            if (context instanceof Activity) {

                this.mContext = context;

                mLayoutInflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                final Activity mActivity = (Activity) context;

                mViewGroup = (ViewGroup) mActivity
                        .findViewById(android.R.id.content);

                if (type == Type.STANDARD) {

                    mToastView = mLayoutInflater.inflate(
                            R.layout.superactivitytoast, mViewGroup, false);

                } else if (type == Type.BUTTON) {

                    mToastView = mLayoutInflater.inflate(
                            R.layout.superactivitytoast_button, mViewGroup,
                            false);

                    mToastButton = (Button) mToastView
                            .findViewById(R.id.button);

                    mDividerView = mToastView
                            .findViewById(R.id.divider);

                    mToastButton.setOnTouchListener(mTouchDismissListener);

                    mType = Type.BUTTON;

                } else if (type == Type.PROGRESS) {

                    mToastView = mLayoutInflater.inflate(
                            R.layout.superactivitytoast_progresscircle, mViewGroup, false);

                    mProgressBar = (ProgressBar) mToastView
                            .findViewById(R.id.progressBar);

                    mProgressBar.setMax(100);

                    mType = Type.PROGRESS;


                } else if (type == Type.PROGRESS_HORIZONTAL) {

                    mToastView = mLayoutInflater.inflate(
                            R.layout.superactivitytoast_progresshorizontal,
                            mViewGroup, false);

                    mProgressBar = (ProgressBar) mToastView
                            .findViewById(R.id.progressBar);

                    mType = Type.PROGRESS_HORIZONTAL;

                }

                mMessageTextView = (TextView) mToastView
                        .findViewById(R.id.message_textView);

                mRootLayout = (LinearLayout) mToastView
                        .findViewById(R.id.root_layout);

            } else {

                throw new IllegalArgumentException(TAG + ERROR_CONTEXTNOTACTIVITY);

            }

        } else {

            throw new IllegalArgumentException(TAG + ERROR_CONTEXTNULL);

        }

    }


    /** Shows the SuperActivityToast. */
    public void show() {

        ManagerSuperActivityToast.getInstance().add(this);

    }

    /**
     * Returns the Type of SuperActivityToast.
     */
    public Type getType() {

        return mType;

    }

    /**
     * Sets the message text of the SuperActivityToast.
     * <br>
     * @param text The message text
     */
    public void setText(CharSequence text) {

        mMessageTextView.setText(text);

    }

    /**
     * Returns the message text of the SuperActivityToast.
     */
    public CharSequence getText() {

        return mMessageTextView.getText();

    }

    /**
     * Sets the message typeface of the SuperActivityToast.
     * <br>
     * @param typeface Use a Typeface constants
     */
    public void setTypeface(int typeface) {

        mTypeface = typeface;

        mMessageTextView.setTypeface(mMessageTextView.getTypeface(), typeface);

    }

    /**
     * Returns the message typeface of the SuperActivityToast.
     */
    public int getTypeface() {

        return mTypeface;

    }

    /**
     * Sets the message text color of the SuperActivityToast.
     * <br>
     * @param textColor The message text color
     */
    public void setTextColor(int textColor) {

        mMessageTextView.setTextColor(textColor);

    }

    /**
     * Returns the message text color of the SuperActivityToast.
     */
    public int getTextColor() {

        return mMessageTextView.getCurrentTextColor();

    }

    /**
     * Sets the text size of the SuperActivityToast.
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
     * Returns the text size of the SuperActivityToast.
     */
    public float getTextSize() {

        return mMessageTextView.getTextSize();

    }

    /**
     * Sets the duration of the SuperActivityToast.
     * <br>
     * @param duration Use SuperToast.Duration constants
     */
    public void setDuration(int duration) {

        this.mDuration = duration;

    }

    /**
     * Returns the duration of the SuperActivityToast.
     */
    public int getDuration() {

        return this.mDuration;

    }

    /**
     * Sets an indeterminate duration of the SuperActivityToast.
     * <br>
     * @param isIndeterminate If true will show until dismissed
     */
    public void setIndeterminate(boolean isIndeterminate) {

        this.mIsIndeterminate = isIndeterminate;

    }

    /**
     * Returns an indeterminate duration of the SuperActivityToast.
     */
    public boolean isIndeterminate() {

        return this.mIsIndeterminate;

    }


    /**
     * Sets an icon resource to the SuperActivityToast
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
     * Returns the icon position of the SuperActivityToast
     */
    public IconPosition getIconPosition() {

        return this.mIconPosition;

    }

    /**
     * Returns the drawable resource of the SuperActivityToast
     */
    public int getIconResource() {

        return this.mIconResouce;

    }

    /**
     * Sets the background resource of the SuperActivityToast.
     * <br>
     * @param backgroundResource Use SuperToast.Background constants
     */
    public void setBackgroundResource(int backgroundResource) {

        this.mBackgroundResouce = backgroundResource;

        mRootLayout.setBackgroundResource(backgroundResource);

    }

    /**
     * Returns the background resource of the SuperActivityToast
     */
    public int getBackgroundResource() {

        return this.mBackgroundResouce;

    }


    /**
     * Sets the animation of the SuperActivityToast.
     * <br>
     * @param animations Use SuperToast.Animations
     */
    public void setAnimations(Animations animations) {

        this.mAnimations = animations;

    }

    /**
     * Gets the animation of the SuperActivityToast.
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
     * Sets a private OnTouchListener to the SuperActivityToast
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
     * Returns if SuperActivityToast is touch dismissable
     */
    public boolean isTouchDismissable() {

        return this.isTouchDismissable;

    }

    /**
     * Sets an OnDismissListener defined in this library
     * to the SuperActivityToast.
     * <br>
     * @param onToastDismissListener Use OnToastDismissListenerHolder for orientation change support
     */
    public void setOnToastDismissListener(OnToastDismissListenerHolder onToastDismissListener) {

        this.mOnDismissListener = onToastDismissListener;
        this.mDismissListenerTag = onToastDismissListener.getTag();

        /** On devices > API 11 save listener to retained Fragment */
        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {

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

    /** Used in ManagerSuperActivityToast*/
    protected OnToastDismissListenerHolder getOnDismissListener() {

        return this.mOnDismissListener;

    }

    private String getDismissListenerTag() {

        return mDismissListenerTag;

    }

    /** Dismisses the SuperActivityToast. */
    public void dismiss() {

        ManagerSuperActivityToast.getInstance().removeSuperToast(this);

    }

    /**
     * Sets an OnToastButtonClickListener to the button in a
     * a BUTTON type SuperActivityToast.
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
        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {

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
     * a BUTTON type SuperActivityToast.
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
     * a BUTTON type SuperActivityToast.
     */
    public int getButtonResource() {

        return this.mButtonResource;

    }

    /**
     * Sets the background resource of the Button divider in
     * a BUTTON Type SuperActivityToast.
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
     * a BUTTON Type SuperActivityToast.
     */
    public int getButtonDividerResource() {

        return this.mButtonDividerResource;

    }

    /**
     * Sets the text of the Button in
     * a BUTTON Type SuperActivityToast.
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
     * a BUTTON Type SuperActivityToast.
     */
    public CharSequence getButtonText() {

        return mToastButton.getText();

    }

    /**
     * Sets the typeface of the button in
     * a BUTTON type SuperActivityToast.
     * <br>
     * @param typeface Use Typeface consstants
     */
    public void setButtonTypeface(int typeface) {

        mButtonTypeface = typeface;

        mToastButton.setTypeface(mToastButton.getTypeface(), typeface);

    }

    /**
     * Returns the typeface of the button in
     * a BUTTON type SuperActivityToast.
     */
    public int getButtonTypeface() {

        return mButtonTypeface;

    }

    /**
     * Sets the text color of the Button in
     * a BUTTON type SuperActivityToast.
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
     * a BUTTON Type SuperActivityToast.
     */
    public int getButtonTextColor() {

        return mToastButton.getCurrentTextColor();

    }

    /**
     * Sets the text size of the Button in
     * a BUTTON type SuperActivityToast.
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
     * a BUTTON Type SuperActivityToast.
     */
    public float getButtonTextSize() {

        return mToastButton.getTextSize();

    }

    /**
     * Sets the progress of the ProgressBar in
     * a PROGRESS_HORIZONTAL type SuperActivityToast.
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
     * a PROGRESS_HORIZONTAL type SuperActivityToast.
     */
    public int getProgress() {

        return mProgressBar.getProgress();

    }

    /**
     * Sets an indeterminate value to the ProgressBar of a PROGRESS type
     * SuperActivityToast.
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
     * SuperActivityToast has been set.
     */
    public boolean getProgressIndeterminate() {

        return this.isProgressIndeterminate;

    }


    /**
     * Returns the SuperActivityToast TextView.
     * <br>
     * @return TextView <br>
     */
    public TextView getTextView() {

        return mMessageTextView;

    }

    /**
     * Returns the SuperActivityToast View.
     * <br>
     * @return View <br>
     */
    public View getView() {

        return mToastView;

    }

    /**
     * Returns true if the SuperActivityToast is showing.
     * <br>
     * @return boolean <br>
     */
    public boolean isShowing() {

        return mToastView != null && mToastView.isShown();

    }

    /**
     * Returns the calling Activity of the SuperActivityToast.
     * <br>
     * @return Activity <br>
     */
    public Activity getActivity() {

        return (Activity) mContext;

    }

    /**
     * Returns the ViewGroup that the SuperActivityToast is attached to.
     * <br>
     * @return ViewGroup <br>
     */
    public ViewGroup getViewGroup() {

        return mViewGroup;

    }


    /**
     * Returns a dark theme SuperActivityToast.
     * <br>
     * @param context Should be Activity
     * @param textCharSequence Message text
     * @param durationInteger Should use SuperToast.Duration constants
     * @return SuperActivityToast
     */
    public static SuperActivityToast createSuperActivityToast(
            Context context, CharSequence textCharSequence, int durationInteger) {

        SuperActivityToast superActivityToast = new SuperActivityToast(context);
        superActivityToast.setText(textCharSequence);
        superActivityToast.setDuration(durationInteger);

        return superActivityToast;

    }

    /**
     * Returns a dark theme SuperActivityToast with a specified animation.
     * <br>
     * @param context Should be Activity
     * @param textCharSequence Message text
     * @param durationInteger Should use SuperToast.Duration constants
     * @param animations Should use SuperToast.Animations
     * @return SuperActivityToast
     */
    public static SuperActivityToast createSuperActivityToast(
            Context context, CharSequence textCharSequence, int durationInteger, Animations animations) {

        SuperActivityToast superActivityToast = new SuperActivityToast(context);
        superActivityToast.setText(textCharSequence);
        superActivityToast.setDuration(durationInteger);
        superActivityToast.setAnimations(animations);

        return superActivityToast;

    }

    /**
     * Returns a light theme SuperActivityToast.
     * <br>
     * @param context Should be Activity
     * @param textCharSequence Message text
     * @param durationInteger Should use SuperToast.Duration constants
     * @return SuperActivityToast
     */
    public static SuperActivityToast createLightSuperActivityToast(
            Context context, CharSequence textCharSequence, int durationInteger) {

        SuperActivityToast superActivityToast = new SuperActivityToast(context);
        superActivityToast.setText(textCharSequence);
        superActivityToast.setDuration(durationInteger);
        superActivityToast.setBackgroundResource(SuperToast.Background.WHITE);
        superActivityToast.setTextColor(Color.BLACK);

        return superActivityToast;

    }

    /**
     * Returns a light theme SuperActivityToast with a specified animation.
     * <br>
     * @param context Should be Activity
     * @param textCharSequence Message text
     * @param durationInteger Should use SuperToast.Duration constants
     * @param animations Should use SuperToast.Animations
     * @return SuperActivityToast
     */
    public static SuperActivityToast createLightSuperActivityToast(
            Context context, CharSequence textCharSequence, int durationInteger, Animations animations) {

        SuperActivityToast superActivityToast = new SuperActivityToast(context);
        superActivityToast.setText(textCharSequence);
        superActivityToast.setDuration(durationInteger);
        superActivityToast.setBackgroundResource(SuperToast.Background.WHITE);
        superActivityToast.setTextColor(Color.BLACK);
        superActivityToast.setAnimations(animations);

        return superActivityToast;

    }

    /** Dismisses and removes all showing/pending SuperActivityToasts. */
    public static void cancelAllSuperActivityToasts() {

        ManagerSuperActivityToast.getInstance().clearQueue();

    }

    /**
     * Dismisses and removes all showing/pending SuperActivityToasts
     * for a specific Activity.
     * <br>
     * @param activity that needs to remove showing/pending SuperActivityToasts
     */
    public static void clearSuperActivityToastsForActivity(Activity activity) {

        ManagerSuperActivityToast.getInstance()
                .clearSuperActivityToastsForActivity(activity);

    }

    /**
     * Saves pending/shown SuperActivityToasts to a bundle.
     * <br>
     * @param bundle Use onSaveInstanceState() bundle
     */
    public static void onSaveState(Bundle bundle) {

        Style[] list = new Style[ManagerSuperActivityToast
                .getInstance().getList().size()];

        LinkedList<SuperActivityToast> lister = ManagerSuperActivityToast
                .getInstance().getList();

        for(int i = 0; i < list.length; i++) {

            list[i] = new Style(lister.get(i));

        }

        bundle.putParcelableArray(BUNDLE_TAG, list);

        SuperActivityToast.cancelAllSuperActivityToasts();

    }

    /**
     * Returns and shows pending/shown SuperActivityToasts from orientation change.
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

                new SuperActivityToast(activity, (Style) parcelable, null, null, i);

            }

        }

    }

    /**
     * Returns and shows pending/shown SuperActivityToasts from orientation change and
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

                new SuperActivityToast(activity, (Style) parcelable, onToastButtonClickListeners, null, i);

            }

        }

    }

    /**
     * Returns and shows pending/shown SuperActivityToasts from orientation change and
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

                new SuperActivityToast(activity, (Style) parcelable, onToastButtonClickListeners, onToastDismissListeners, i);

            }

        }

    }

    /**
     * Method used to recreate SuperActivityToasts after orientation change
     */
    private SuperActivityToast(Activity activity, Style style, List<OnToastButtonClickListenerHolder> onToastButtonClickListeners,
                                 List<OnToastDismissListenerHolder> onToastDismissListeners, int position) {

        SuperActivityToast superActivityToast;

        if (style.mType == Type.BUTTON) {

            superActivityToast = new SuperActivityToast(activity, Type.BUTTON);
            superActivityToast.setButtonText(style.mButtonText);
            superActivityToast.setButtonTextSizeFloat(style.mButtonTextSize);
            superActivityToast.setButtonTextColor(style.mButtonTextColor);
            superActivityToast.setButtonResource(style.mButtonResource);
            superActivityToast.setButtonDividerResource(style.mButtonDividerResource);
            superActivityToast.setButtonTypeface(style.mButtonTypeface);

            /** Reattach any OnToastButtonClickListeners via retained Fragment */
            if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {

                FragmentRetainer fragmentRetainer = (FragmentRetainer)
                        activity.getFragmentManager().findFragmentByTag(FRAGMENTRETAINER_ID);

                if (fragmentRetainer != null && fragmentRetainer.getClickListeners() != null) {

                    for (OnToastButtonClickListenerHolder onToastButtonClickListenerHolder : fragmentRetainer.getClickListeners()) {

                        if(onToastButtonClickListenerHolder.getTag().equals(style.mClickListenerTag)) {

                            superActivityToast.setOnToastButtonClickListener(onToastButtonClickListenerHolder);

                        }

                    }

                }

            /** Reattach any OnToastButtonClickListeners via provided List */
            } else {

                if(onToastButtonClickListeners != null) {

                    for (OnToastButtonClickListenerHolder onToastButtonClickListenerHolder : onToastButtonClickListeners) {

                        if(onToastButtonClickListenerHolder.getTag().equalsIgnoreCase(style.mClickListenerTag)) {

                            superActivityToast.setOnToastButtonClickListener(onToastButtonClickListenerHolder);

                        }

                    }
                }

            }

        } else if(style.mType == Type.PROGRESS) {

            /** PROGRESS style SuperActivityToasts should be managed by the developer */

            return;

        } else if(style.mType == Type.PROGRESS_HORIZONTAL) {

            /** PROGRESS_HORIZONTAL style SuperActivityToasts should be managed by the developer */

            return;

        }  else {

            superActivityToast = new SuperActivityToast(activity);

        }

        /** Reattach any OnToastBDismissListeners via retained Fragment */
        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {

            FragmentRetainer fragmentRetainer = (FragmentRetainer)
                    activity.getFragmentManager().findFragmentByTag(FRAGMENTRETAINER_ID);

            if (fragmentRetainer != null && fragmentRetainer.getDismissListeners() != null) {

                for (OnToastDismissListenerHolder onToastDismissListenerHolder : fragmentRetainer.getDismissListeners()) {

                    if(onToastDismissListenerHolder.getTag().equals(style.mDismissListenerTag)) {

                        superActivityToast.setOnToastDismissListener(onToastDismissListenerHolder);

                    }

                }

            }

        /** Reattach any OnToastBDismissListeners via provided List */
        } else {

            if(onToastDismissListeners != null) {

                for (OnToastDismissListenerHolder onToastDismissListenerHolder : onToastDismissListeners) {

                    if(onToastDismissListenerHolder.getTag().equalsIgnoreCase(style.mDismissListenerTag)) {

                        superActivityToast.setOnToastDismissListener(onToastDismissListenerHolder);

                    }

                }
            }

        }

        superActivityToast.setAnimations(style.mAnimations);
        superActivityToast.setText(style.mText);
        superActivityToast.setTypeface(style.mTypeface);
        superActivityToast.setDuration(style.mDuration);
        superActivityToast.setTextColor(style.mTextColor);
        superActivityToast.setTextSizeFloat(style.mTextSize);
        superActivityToast.setIndeterminate(style.isIndeterminate);
        superActivityToast.setIcon(style.mIconResource, style.mIconPosition);
        superActivityToast.setBackgroundResource(style.mBackgroundResource);
        superActivityToast.setTouchToDismiss(style.isTouchDismissable);

        /** Do not use show animation on recreation of SuperActivityToast that was previously showing */
        if(position == 1) {

            superActivityToast.setShowImmediate(true);

        }

        superActivityToast.show();

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


        public Style(SuperActivityToast superActivityToast) {

            mType = superActivityToast.getType();

            if (mType == Type.BUTTON) {

                mButtonText = superActivityToast.getButtonText().toString();
                mButtonTextSize = superActivityToast.getButtonTextSize();
                mButtonTextColor = superActivityToast.getButtonTextColor();
                mButtonResource = superActivityToast.getButtonResource();
                mButtonDividerResource = superActivityToast.getButtonDividerResource();
                mClickListenerTag = superActivityToast.getClickListenerTag();
                mButtonTypeface = superActivityToast.getButtonTypeface();

            }

            if(superActivityToast.getIconResource() != 0 && superActivityToast.getIconPosition() != null) {

                mIconResource = superActivityToast.getIconResource();
                mIconPosition = superActivityToast.getIconPosition();

            }

            mDismissListenerTag = superActivityToast.getDismissListenerTag();
            mAnimations = superActivityToast.getAnimation();
            mText = superActivityToast.getText().toString();
            mTypeface = superActivityToast.getTypeface();
            mDuration = superActivityToast.getDuration();
            mTextColor = superActivityToast.getTextColor();
            mTextSize = superActivityToast.getTextSize();
            isIndeterminate = superActivityToast.isIndeterminate();
            mBackgroundResource = superActivityToast.getBackgroundResource();
            isTouchDismissable = superActivityToast.isTouchDismissable();

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

        }

        @Override
        public int describeContents() {
            return 0;

        }

        public final Parcelable.Creator CREATOR = new Parcelable.Creator() {

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
