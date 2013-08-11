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

import android.util.Log;

import com.github.johnpersano.supertoasts.R;
import com.github.johnpersano.supertoasts.SuperToast.IconPosition;
import com.github.johnpersano.supertoasts.SuperToast.Type;
import com.github.johnpersano.supertoasts.util.OnDismissListener;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
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

/**
 * SuperActivityToasts are designed to be used inside of Activities. When the
 * Activity is destroyed the SuperActivityToast is destroyed along with it.
 * SuperActivityToasts will not linger to the next screen like standard
 * Toasts/SuperToasts.
 */
public class SuperActivityToast {

	private static final String TAG = "SuperActivityToast";

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
	private int mDuration = SuperToast.DURATION_SHORT;
	private boolean mIsIndeterminate;
	private Animation mShowAnimation = getFadeInAnimation();
	private Animation mDismissAnimation = getFadeOutAnimation();
	private OnDismissListener mOnDismissListener;
    private OnClickListener mOnClickListener;

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

                    try {

                        mOnClickListener = (OnClickListener) mContext;

                    }  catch (ClassCastException e) {

                        Log.d(TAG, e.toString());

                    }


                    if(mOnClickListener != null) {

                        mToastButton.setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {

                                mOnClickListener.onClick(v);

                            }
                        });

                    }

				} else if (type == Type.PROGRESS) {

					mToastView = mLayoutInflater.inflate(
							R.layout.superactivitytoast_progresscircle, mViewGroup, false);

					mProgressBar = (ProgressBar) mToastView
							.findViewById(R.id.progressBar);

				} else if (type == Type.PROGRESS_HORIZONTAL) {

					mToastView = mLayoutInflater.inflate(
							R.layout.superactivitytoast_progresshorizontal,
							mViewGroup, false);

					mProgressBar = (ProgressBar) mToastView
							.findViewById(R.id.progressBar);

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

	/**
	 * Sets the message text of the SuperActivityToast.
	 * <br>
	 * @param text
	 */
	public void setText(CharSequence text) {

		mMessageTextView.setText(text);

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
	 * Sets the text size of the SuperActivityToast. 
	 * This method will automatically convert the integer 
	 * parameter to scaled pixels.
	 * <br>
	 * @param textSize	
	 */
	public void setTextSize(int textSize) {

		mMessageTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);

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
	 * Sets an indeterminate duration of the SuperActivityToast.
	 * <br>
	 * @param isIndeterminate
	 */
	public void setIndeterminate(boolean isIndeterminate) {

		this.mIsIndeterminate = isIndeterminate;

	}

	/**
	 * Sets an icon Drawable to the SuperActivityToast with
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
	 * Sets an icon resource to the SuperActivityToast 
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
	 * Sets an OnClickListener to the SuperActivityToast root View.
	 * <br>
	 * @param onClickListener
	 */
	public void setOnClickListener(OnClickListener onClickListener) {

		mToastView.setOnClickListener(onClickListener);

	}

	/**
	 * Sets the background resource of the SuperActivityToast.
	 * <br>
	 * @param backgroundResource
	 * 
	 */
	public void setBackgroundResource(int backgroundResource) {

		mRootLayout.setBackgroundResource(backgroundResource);

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

		if (mSdkVersion < android.os.Build.VERSION_CODES.JELLY_BEAN) {

			mRootLayout.setBackgroundDrawable(backgroundDrawable);

		}

		else {

			mRootLayout.setBackground(backgroundDrawable);

		}

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
	 * Sets the show animation of the SuperActivityToast.
	 * <br>
	 * @param showAnimation
	 */
	public void setShowAnimation(Animation showAnimation) {

		this.mShowAnimation = showAnimation;

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
	 * Sets a private OnTouchListener to the SuperActivityToast
	 * that will dismiss it when touched.
	 * <br>
	 * @param touchDismiss
	 */
	public void setTouchToDismiss(boolean touchDismiss) {

		if (touchDismiss) {

			mToastView.setOnTouchListener(mTouchDismissListener);

		} else {

			mToastView.setOnTouchListener(null);

		}

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
	 * Sets the background resource of the Button in 
	 * a BUTTON Type SuperActivityToast. 
	 * <br>
	 * @param buttonResource
	 */
	public void setButtonResource(int buttonResource) {

		if (mToastButton != null) {

			mToastButton.setCompoundDrawablesWithIntrinsicBounds(mContext
					.getResources().getDrawable(buttonResource), null, null, null);

		}

	}

	/**
	 * Sets the background Drawable of the Button in 
	 * a BUTTON Type SuperActivityToast. 
	 * <br>
	 * @param buttonDrawable
	 */
	public void setButtonDrawable(Drawable buttonDrawable) {

		if (mToastButton != null) {

			mToastButton.setCompoundDrawablesWithIntrinsicBounds(buttonDrawable,
					null, null, null);

		}

	}

	/**
	 * Sets the background resource of the Button divider in 
	 * a BUTTON Type SuperActivityToast. 
	 * <br>
	 * @param dividerResource
	 */
	public void setButtonDividerResource(int dividerResource) {

		if (mDividerView != null) {

			mDividerView.setBackgroundResource(dividerResource);

		}

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

		if (mSdkVersion < android.os.Build.VERSION_CODES.JELLY_BEAN) {

			mDividerView.setBackgroundDrawable(dividerDrawable);

		} else {

			mDividerView.setBackground(dividerDrawable);

		}

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
	 * Sets the text size of the Button in 
	 * a BUTTON Type SuperActivityToast. This
	 * method will automatically convert the integer 
	 * parameter into scaled pixels.
	 * <br>
	 * @param buttonTextSize
	 */
	public void setButtonTextSize(int buttonTextSize) {

		if (mToastButton != null) {

			mToastButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, buttonTextSize);

		}

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
	 * Sets an indeterminate value to the ProgressBar of a PROGRESS Type
	 * SuperActivityToast. 
	 * <br>
	 * @param isIndeterminate
	 */
	public void setProgressIndeterminate(boolean isIndeterminate) {
		
		if(mProgressBar != null) {
			
			mProgressBar.setIndeterminate(isIndeterminate);
			
		}

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

		if (mToastView != null) {

			return mToastView.isShown();

		}

		else {

			return false;

		}

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
	 * Returns the set duration of the SuperActivityToast.
	 * <br>
	 * @return long <br>
	 */
	public long getDuration() {

		return mDuration;

	}

	/**
	 * Returns the show Animation of the SuperActivityToast.
	 * <br>
	 * @return Animation <br>
	 */
	public Animation getShowAnimation() {

		return mShowAnimation;

	}

	/**
	 * Returns the dismiss Animation of the SuperActivityToast.
	 * <br>
	 * @return Animation <br>
	 */
	public Animation getDismissAnimation() {

		return mDismissAnimation;

	}

	/**
	 * Returns true if the SuperActivityToast is indeterminate.
	 * <br>
	 * @return boolean <br>
	 */
	public boolean getIsIndeterminate() {

		return mIsIndeterminate;

	}

	/**
	 * Returns the OnDismissListener of the SuperActivityToast.
	 * <br>
	 * @return OnDismissListener <br>
	 */
	public OnDismissListener getOnDismissListener() {

		return mOnDismissListener;

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
		superActivityToast.setBackgroundResource(SuperToast.BACKGROUND_WHITE);
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
