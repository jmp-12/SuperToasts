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
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.github.johnpersano.supertoasts.SuperToast.IconPosition;
import com.github.johnpersano.supertoasts.SuperToast.Type;
import com.github.johnpersano.supertoasts.util.OnDismissListener;

/**
 * SuperActivityToasts are designed to be used inside of Activities. When the
 * Activity is destroyed the SuperActivityToast is destroyed along with it.
 * SuperActivityToasts will not linger to the next screen like standard
 * Toasts/SuperToasts.
 */
public class SuperActivityToast {

	private static final String TAG = "SuperActivityToast";
    private static final String BUNDLE = "superactivitytoast_bundle";

    private static final String ERROR_CONTEXTNULL = "The Context that you passed was null! (SuperActivityToast)";
	private static final String ERROR_CONTEXTNOTACTIVITY = "The Context that you passed was not an Activity! (SuperActivityToast)";

	private Context mContext;
	private LayoutInflater mLayoutInflater;
	private ViewGroup mViewGroup;
	private View mToastView;
	private View mDividerView;
	private TextView mMessageTextView;
	private Button mToastButton;
	private LinearLayout mRootLayout;
	private ProgressBar mProgressBar;
	private int mSdkVersion = android.os.Build.VERSION.SDK_INT;
	private int mDuration = SuperToast.Duration.SHORT;
	private boolean mIsIndeterminate;
	private Animation mShowAnimation = getFadeInAnimation();
	private Animation mDismissAnimation = getFadeOutAnimation();
	private OnDismissListener mOnDismissListener;

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

    /**
	 * Instantiates a new SuperActivityToast.
	 * <br>
	 * @param context
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

				throw new IllegalArgumentException(ERROR_CONTEXTNOTACTIVITY);

			}

		} else {

			throw new IllegalArgumentException(ERROR_CONTEXTNULL);

		}

	}

	/**
	 * Instantiates a new SuperActivityToast with a Type.
	 * <br>
	 * @param context
	 * @param type
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

                    mType = Type.BUTTON;

                    try {

                        mButtonOnClickListener = (OnClickListener) mContext;

                    } catch (ClassCastException e) {

                        Log.d(TAG, e.toString());

                    }

                    if(mButtonOnClickListener != null) {

                        mToastButton.setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {

                                mButtonOnClickListener.onClick(v);

                            }
                        });

                    }

				} else if (type == Type.PROGRESS) {

					mToastView = mLayoutInflater.inflate(
							R.layout.superactivitytoast_progresscircle, mViewGroup, false);

					mProgressBar = (ProgressBar) mToastView
							.findViewById(R.id.progressBar);

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

				throw new IllegalArgumentException(ERROR_CONTEXTNOTACTIVITY);

			}

		} else {

			throw new IllegalArgumentException(ERROR_CONTEXTNULL);

		}

	}


	/** Shows the SuperActivityToast. */
	public void show() {

		ManagerSuperActivityToast.getInstance().add(this);

	}

    public Type getType() {

        return mType;

    }

	/**
	 * Sets the message text of the SuperActivityToast.
	 * <br>
	 * @param text
	 */
	public void setText(CharSequence text) {

		mMessageTextView.setText(text);

	}

    /**
     * Gets the message text of the SuperActivityToast.
     */
    public CharSequence getText() {

        return mMessageTextView.getText();

    }

	/**
	 * Sets the message text color of the SuperActivityToast.
	 * <br>
	 * @param textColor
	 */
	public void setTextColor(int textColor) {

		mMessageTextView.setTextColor(textColor);

	}

    /**
     * Gets the message text color of the SuperActivityToast.
     */
    public int getTextColor() {

        return mMessageTextView.getCurrentTextColor();

    }

	/**
	 * Sets the text size of the SuperActivityToast. 
	 * This method will automatically convert the integer 
	 * parameter to scaled pixels.
	 * <br>
	 * @param textSize	
	 */
	public void setTextSize(int textSize) {

		mMessageTextView.setTextSize(textSize);

	}

    /**
     * Sets the text size of the SuperActivityToast.
     * <br>
     * @param textSize
     */
    public void setTextSizeFloat(float textSize) {

        mMessageTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);

    }

    /**
     * Gets the text size of the SuperActivityToast.
     */
    public float getTextSize() {

        return mMessageTextView.getTextSize();

    }

	/**
	 * Sets the duration of the SuperActivityToast.
	 * <br>
	 * @param duration
	 */
	public void setDuration(int duration) {

        this.mDuration = duration;

	}

    /**
     * Gets the duration of the SuperActivityToast.
     */
    public int getDuration() {

        return this.mDuration;

    }

	/**
	 * Sets an indeterminate duration of the SuperActivityToast.
	 * <br>
	 * @param isIndeterminate
	 */
	public void setIndeterminate(boolean isIndeterminate) {

		this.mIsIndeterminate = isIndeterminate;

	}

    /**
     * Gets an indeterminate duration of the SuperActivityToast.
     */
    public boolean isIndeterminate() {

        return this.mIsIndeterminate;

    }

	/**
	 * Sets an icon Drawable to the SuperActivityToast with
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
     * Gets the icon Drawable of the SuperActivityToast
     */
    public Drawable getIconDrawable() {

        return this.mIconDrawable;

    }

    /**
     * Gets the icon position of the SuperActivityToast
     */
    public IconPosition getIconPosition() {

        return this.mIconPosition;

    }

	/**
	 * Sets an icon resource to the SuperActivityToast 
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
     * Gets the Drawable resource of the SuperActivityToast
     */
    public int getIconResource() {

        return this.mIconResouce;

    }

	/**
	 * Sets an OnClickListener to the SuperActivityToast root View.
	 * <br>
	 * @param onClickListener
	 */
	public void setOnClickListener(OnClickListener onClickListener) {

        this.mOnClickListener = onClickListener;

		mToastView.setOnClickListener(onClickListener);

	}

    /**
     * Gets the onClickListener of the SuperActivityToast
     */
    public OnClickListener getOnClickListener() {

        return this.mOnClickListener;

    }

	/**
	 * Sets the background resource of the SuperActivityToast.
	 * <br>
	 * @param backgroundResource
	 * 
	 */
	public void setBackgroundResource(int backgroundResource) {

        this.mBackgroundResouce = backgroundResource;

		mRootLayout.setBackgroundResource(backgroundResource);

	}

    /**
     * Gets the background resource of the SuperActivityToast
     */
    public int getBackgroundResource() {

        return this.mBackgroundResouce;

    }

	/**
	 * Sets the background Drawable of the SuperActivityToast.
	 * <br>
	 * @param backgroundDrawable
	 * 
	 */
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	public void setBackgroundDrawable(Drawable backgroundDrawable) {

        this.mBackgroundDrawable = backgroundDrawable;

		if (mSdkVersion < android.os.Build.VERSION_CODES.JELLY_BEAN) {

			mRootLayout.setBackgroundDrawable(backgroundDrawable);

		}

		else {

			mRootLayout.setBackground(backgroundDrawable);

		}

	}

    /**
     * Gets the background Drawable of the SuperActivityToast
     */
    public Drawable getBackgroundDrawable() {

        return this.mBackgroundDrawable;

    }

	/**
	 * Sets the Typeface of the SuperActivityToast TextView.
	 * <br>
	 * @param typeface
	 */
	public void setTypeface(Typeface typeface) {

		mMessageTextView.setTypeface(typeface);

	}

    /**
     * Gets the Typeface of the SuperActivityToast TextView.
     */
    public Typeface getTypeface() {

        return mMessageTextView.getTypeface();

    }

	/**
	 * Sets the show animation of the SuperActivityToast.
	 * <br>
	 * @param showAnimation
	 */
	public void setShowAnimation(Animation showAnimation) {

		this.mShowAnimation = showAnimation;

	}

    /**
     * Gets the show animation of the SuperActivityToast.
     */
    public Animation getShowAnimation() {

        return this.mShowAnimation;

    }

	/**
	 * Sets the dismiss animation of the SuperActivityToast.
	 * <br>
	 * @param dismissAnimation
	 */
	public void setDismissAnimation(Animation dismissAnimation) {

		this.mDismissAnimation = dismissAnimation;

	}

    /**
     * Gets the dismiss animation of the SuperActivityToast.
     */
    public Animation getDismissAnimation() {

        return this.mDismissAnimation;

    }

	/**
	 * Sets a private OnTouchListener to the SuperActivityToast
	 * that will dismiss it when touched.
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
     * Checks is SuperActivityToast is touch dismissable
     */
    public boolean isTouchDismissable() {

        return this.isTouchDismissable;

    }

	/**
	 * Sets an OnDismissListener defined in this library
	 * to the SuperActivityToast.
	 * <br>
	 * @param onDismissListener
	 */
	public void setOnDismissListener(OnDismissListener onDismissListener) {

		this.mOnDismissListener = onDismissListener;

	}

    /**
     * Returns the OnDismissListener of the SuperActivityToast
     */
    public OnDismissListener getOnDismissListener() {

        return this.mOnDismissListener;

    }

	/** Dismisses the SuperActivityToast. */
	public void dismiss() {

		ManagerSuperActivityToast.getInstance().removeSuperToast(this);

	}


	/**
	 * Sets an OnClickListener to the Button of a BUTTON Type
	 * SuperActivityToast. 
	 * <br>
	 * @param onClickListener
	 */
	public void setButtonOnClickListener(OnClickListener onClickListener) {

		if (mToastButton != null) {

			mToastButton.setOnClickListener(onClickListener);

		}

	}

    /**
     * Returns an OnClickListener to the Button of a BUTTON Type
     */
    public OnClickListener getButtonOnClickListener() {

        return mButtonOnClickListener;

    }

	/**
	 * Sets the background resource of the Button in 
	 * a BUTTON Type SuperActivityToast. 
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
     * a BUTTON Type SuperActivityToast.
     */
    public int getButtonResource() {

        return this.mButtonResource;

    }

	/**
	 * Sets the background Drawable of the Button in 
	 * a BUTTON Type SuperActivityToast. 
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
     * a BUTTON Type SuperActivityToast.
     */
    public Drawable getButtonDrawable() {

        return this.mButtonDrawable;

    }

	/**
	 * Sets the background resource of the Button divider in 
	 * a BUTTON Type SuperActivityToast. 
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
     * a BUTTON Type SuperActivityToast.
     */
    public int getButtonDividerResource() {

        return this.mButtonDividerResource;

    }

	/**
	 * Sets the background Drawable of the Button divider in 
	 * a BUTTON Type SuperActivityToast. 
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
     * a BUTTON Type SuperActivityToast.
     */
    public Drawable getButtonDividerDrawable() {

        return this.mButtonDividerDrawable;

    }

	/**
	 * Sets the text of the Button in 
	 * a BUTTON Type SuperActivityToast. 
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
     * a BUTTON Type SuperActivityToast.
     */
    public CharSequence getButtonText() {

        return mToastButton.getText();

    }

	/**
	 * Sets the text color of the Button in 
	 * a BUTTON Type SuperActivityToast. 
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
     * a BUTTON Type SuperActivityToast.
     */
    public int getButtonTextColor() {

        return mToastButton.getCurrentTextColor();

    }

	/**
	 * Sets the text Typeface of the Button in 
	 * a BUTTON Type SuperActivityToast. 
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
     * a BUTTON Type SuperActivityToast.
     */
    public Typeface getButtonTextTypeface() {

        return mToastButton.getTypeface();

    }

	/**
	 * Sets the text size of the Button in 
	 * a BUTTON Type SuperActivityToast. This
	 * method will automatically convert the integer 
	 * parameter into scaled pixels.
	 * <br>
	 * @param buttonTextSize
	 */
	public void setButtonTextSize(int buttonTextSize) {

		if (mToastButton != null) {

            mMessageTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, buttonTextSize);

		}

	}

    /**
     * Sets the text size of the Button in
     * a BUTTON Type SuperActivityToast
     * <br>
     * @param buttonTextSize
     */
    public void setButtonTextSizeFloat(float buttonTextSize) {

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
	 * a PROGRESS_HORIZONTAL Type SuperActivityToast.
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
     * a PROGRESS_HORIZONTAL Type SuperActivityToast.
     */
    public int getProgress() {

        return mProgressBar.getProgress();

    }
	
	/**
	 * Sets an indeterminate value to the ProgressBar of a PROGRESS Type
	 * SuperActivityToast. 
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
	 * @param context
	 * @param textCharSequence
	 * @param durationInteger
	 * @return SuperActivityToast
	 */
	public static SuperActivityToast createDarkSuperActivityToast(
			Context context, CharSequence textCharSequence, int durationInteger) {

		SuperActivityToast superActivityToast = new SuperActivityToast(context);
		superActivityToast.setText(textCharSequence);
		superActivityToast.setDuration(durationInteger);

		return superActivityToast;

	}

	/**
	 * Returns a light theme SuperActivityToast.
	 * <br>
	 * @param context
	 * @param textCharSequence
	 * @param durationInteger
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

	/** Dismisses and removes all showing/pending SuperActivityToasts. */
	public static void cancelAllSuperActivityToasts() {

		ManagerSuperActivityToast.getInstance().clearQueue();

	}
	
	/** 
	 * Dismisses and removes all showing/pending SuperActivityToasts 
	 * for a specific Activity. 
	 * <br>
	 * @param activity
	 */
	public static void clearSuperActivityToastsForActivity(Activity activity) {

		ManagerSuperActivityToast.getInstance()
				.clearSuperActivityToastsForActivity(activity);

	}

    /**
     * Saves pending/shown SuperActivityToasts to a bundle.
     * <br>
     * @param bundle
     */
    public static void onSaveState(Bundle bundle) {

        SuperActivityToast[] list = new SuperActivityToast[ManagerSuperActivityToast
                .getInstance().getList().size()];

        // Convert LinkedList to SuperActivityToast[] to be saved in Bundle
        for(int i=0; i < ManagerSuperActivityToast.getInstance().getList().size(); i++) {

            list[i] = ManagerSuperActivityToast.getInstance().getList().get(i);

        }

        bundle.putSerializable(BUNDLE, list);

    }


    public static void onRestoreState(Bundle bundle, Activity activity) {

        if(bundle == null) {

            return;
        }

        SuperActivityToast.cancelAllSuperActivityToasts();

        SuperActivityToast[] savedArray = (SuperActivityToast[]) bundle.getSerializable(BUNDLE);
        Log.d(TAG, String.valueOf(savedArray.length));

        for (SuperActivityToast oldSuperActivityToast : savedArray) {

            SuperActivityToast newSuperActivityToast;

            if (oldSuperActivityToast.getType() == Type.BUTTON) {

                newSuperActivityToast = new SuperActivityToast(activity, Type.BUTTON);
                newSuperActivityToast.setButtonOnClickListener(oldSuperActivityToast.getButtonOnClickListener());
                newSuperActivityToast.setButtonText(oldSuperActivityToast.getButtonText());
                newSuperActivityToast.setButtonTextSizeFloat(oldSuperActivityToast.getButtonTextSize());
                newSuperActivityToast.setButtonTextColor(oldSuperActivityToast.getButtonTextColor());
                newSuperActivityToast.setButtonTextTypeface(oldSuperActivityToast.getButtonTextTypeface());

                if (oldSuperActivityToast.getButtonDrawable() != null) {

                    newSuperActivityToast.setButtonDrawable(oldSuperActivityToast.getButtonDrawable());

                } else if (oldSuperActivityToast.getButtonResource() != 0) {

                    newSuperActivityToast.setButtonResource(oldSuperActivityToast.getButtonResource());

                }

                if (oldSuperActivityToast.getButtonDividerDrawable() != null) {

                    newSuperActivityToast.setButtonDividerDrawable(oldSuperActivityToast.getButtonDividerDrawable());

                } else if (oldSuperActivityToast.getButtonDividerResource() != 0) {

                    newSuperActivityToast.setButtonDividerResource(oldSuperActivityToast.getButtonDividerResource());

                }


            } else if(oldSuperActivityToast.getType() == Type.PROGRESS) {

                newSuperActivityToast = new SuperActivityToast(activity, Type.PROGRESS);
                newSuperActivityToast.setProgressIndeterminate(oldSuperActivityToast.getProgressIndeterminate());
                newSuperActivityToast.setProgress(oldSuperActivityToast.getProgress());

            } else if(oldSuperActivityToast.getType() == Type.PROGRESS_HORIZONTAL) {

                newSuperActivityToast = new SuperActivityToast(activity, Type.PROGRESS_HORIZONTAL);
                newSuperActivityToast.setProgressIndeterminate(oldSuperActivityToast.getProgressIndeterminate());
                newSuperActivityToast.setProgress(oldSuperActivityToast.getProgress());

            }  else {

               newSuperActivityToast = new SuperActivityToast(activity);

           }

            newSuperActivityToast.setText(oldSuperActivityToast.getText());
            newSuperActivityToast.setTextColor(oldSuperActivityToast.getTextColor());
            newSuperActivityToast.setTextSizeFloat(oldSuperActivityToast.getTextSize());
            newSuperActivityToast.setDuration(oldSuperActivityToast.getDuration());
            newSuperActivityToast.setIndeterminate(oldSuperActivityToast.isIndeterminate());

            if(oldSuperActivityToast.getIconDrawable() != null && oldSuperActivityToast.getIconPosition() != null) {

                newSuperActivityToast.setIconDrawable(oldSuperActivityToast.getIconDrawable(), oldSuperActivityToast.getIconPosition());

            } else if(oldSuperActivityToast.getIconResource() != 0 && oldSuperActivityToast.getIconPosition() != null) {

                newSuperActivityToast.setIconResource(oldSuperActivityToast.getIconResource(), oldSuperActivityToast.getIconPosition());

            }

            newSuperActivityToast.setOnClickListener(oldSuperActivityToast.getOnClickListener());

            if(oldSuperActivityToast.getBackgroundDrawable() != null) {

                newSuperActivityToast.setBackgroundDrawable(oldSuperActivityToast.getBackgroundDrawable());

            } else {

                newSuperActivityToast.setBackgroundResource(oldSuperActivityToast.getBackgroundResource());

            }

            newSuperActivityToast.setTypeface(oldSuperActivityToast.getTypeface());
            newSuperActivityToast.setShowAnimation(oldSuperActivityToast.getShowAnimation());
            newSuperActivityToast.setDismissAnimation(oldSuperActivityToast.getDismissAnimation());
            newSuperActivityToast.setTouchToDismiss(oldSuperActivityToast.isTouchDismissable());
            newSuperActivityToast.setOnDismissListener(oldSuperActivityToast.getOnDismissListener());
            newSuperActivityToast.show();

        }
    }

	
	private Animation getFadeInAnimation() {

		AlphaAnimation alphaAnimation = new AlphaAnimation(0f, 1f);
		alphaAnimation.setDuration(500);
		alphaAnimation.setInterpolator(new AccelerateInterpolator());

		return alphaAnimation;

	}

	private Animation getFadeOutAnimation() {

		AlphaAnimation alphaAnimation = new AlphaAnimation(1f, 0f);
		alphaAnimation.setDuration(500);
		alphaAnimation.setInterpolator(new AccelerateInterpolator());

		return alphaAnimation;

	}

	private OnTouchListener mTouchDismissListener = new OnTouchListener() {

		int timesTouched;

		@Override
		public boolean onTouch(View view, MotionEvent event) {

			/**
			 * Hack to prevent the user from repeatedly
			 * touching the SuperProgressToast causing erratic behavior
			 */
			if (timesTouched == 0) {

				dismiss();

			}

			timesTouched++;

			return false;

		}

	};

}
