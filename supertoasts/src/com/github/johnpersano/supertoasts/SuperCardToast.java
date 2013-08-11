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


import com.github.johnpersano.supertoasts.R;
import com.github.johnpersano.supertoasts.SuperToast.IconPosition;
import com.github.johnpersano.supertoasts.SuperToast.Type;
import com.github.johnpersano.supertoasts.util.OnDismissListener;
import com.github.johnpersano.supertoasts.util.SwipeDismissListener;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * SuperCardToasts are designed to be used inside of Activities. SuperCardToasts
 * are designed to be displayed at the top of an Activity to display non-essential messages. 
 */

public class SuperCardToast
{
	
	private static final String TAG  = "(SuperCardToast)";

	private static final String ERROR_CONTEXTNOTACTIVITY  = "Context must be an instance of Activity";
	private static final String ERROR_CONTAINERNULL = "You must have a LinearLayout with the id of card_container in your layout!";
	private static final String ERROR_VIEWCONTAINERNULL = "Either the View or Container was null when trying to dismiss. Did you create and " +
			"show a SuperCardToast before trying to dismiss it?";
    private static final String ERROR_PREHONEYCOMB  = "Swipe to dismiss was enabled but the SDK version is pre-Honeycomb";

	private Context mContext;
	private ViewGroup mViewGroup;
	private int mSdkVersion = android.os.Build.VERSION.SDK_INT;
	private Handler mHandler;
	private View mToastView;
	private LayoutInflater mLayoutInflater;
	private TextView mMessageTextView; 
	private ProgressBar mProgressBar;
	private Button mToastButton;
	private View mDividerView;
	private LinearLayout mRootLayout;	
	private int mDuration = (SuperToast.DURATION_LONG);
	private boolean isIndeterminate;
	private OnDismissListener mOnDismissListener;
    private OnClickListener mOnClickListener;


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
				
				if(type == Type.STANDARD) {
					
					mToastView = mLayoutInflater
				    		.inflate(R.layout.supercardtoast, mViewGroup, false);
					
				} else if(type == Type.BUTTON) {
					
					mToastView = mLayoutInflater
				    		.inflate(R.layout.supercardtoast_button, mViewGroup, false);
					
					mToastButton = (Button) 
							mToastView.findViewById(R.id.button);
					
					mDividerView = mToastView.findViewById(R.id.divider);

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

		if(!isIndeterminate) {
				
			mHandler = new Handler();
			mHandler.postDelayed(mHideRunnable, mDuration);
												
		}
			
		mViewGroup.addView(mToastView);
		
		final Animation mAnimation = getCardAnimation();
		
		mAnimation.setAnimationListener(new AnimationListener() {

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

		mToastView.startAnimation(mAnimation);
			
	}	

	/**
	 * Sets the message text of the SuperCardToast.
	 * <br>
	 * @param text
	 */
	public void setText(CharSequence text) {

		if (mMessageTextView != null) {

			mMessageTextView.setText(text);

		}

	}

	/**
	 * Sets the message text color of the SuperCardToast.
	 * <br>
	 * @param textColor
	 */
	public void setTextColor(int textColor) {

		if (mMessageTextView != null) {

			mMessageTextView.setTextColor(textColor);

		}

	}

	/**
	 * Sets the text size of the SuperCardToast. 
	 * This method will automatically convert the integer 
	 * parameter to scaled pixels.
	 * <br>
	 * @param textSize	
	 */
	public void setTextSize(int textSize) {

		mMessageTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);

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
	 * Sets an indeterminate duration of the SuperCardToast.
	 * <br>
	 * @param isIndeterminate
	 */
	public void setIndeterminate(boolean isIndeterminate) {

		this.isIndeterminate = isIndeterminate;

	}
	
	/**
	 * Sets an icon Drawable to the SuperCardToast with
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
	 * Sets an icon resource to the SuperCardToast 
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
	 * Sets an OnClickListener to the SuperCardToast root View.
	 * <br>
	 * @param onClickListener
	 */
	public void setOnClickListener(OnClickListener onClickListener) {

		mToastView.setOnClickListener(onClickListener);

	}

	/**
	 * Sets the background resource of the SuperCardToast.
	 * <br>
	 * @param backgroundResource
	 * 
	 */
	public void setBackgroundResource(int backgroundResource) {

		mRootLayout.setBackgroundResource(backgroundResource);

	}

	/**
	 * Sets the background Drawable of the SuperCardToast.
	 * <br>
	 * @param backgroundDrawable
	 * 
	 */
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	public void setBackgroundDrawable(Drawable backgroundDrawable) {

		if (mSdkVersion < android.os.Build.VERSION_CODES.JELLY_BEAN) {

			mRootLayout.setBackgroundDrawable(backgroundDrawable);

		} else {

			mRootLayout.setBackground(backgroundDrawable);

		}

	}

	/**
	 * Sets the Typeface of the SuperCardToast TextView.
	 * <br>
	 * @param typeface
	 */
	public void setTypeface(Typeface typeface) {

		mMessageTextView.setTypeface(typeface);

	}
	

	/**
	 * Sets a private OnTouchListener to the SuperCardToast
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
	 * to the SuperCardToast.
	 * <br>
	 * @param onDismissListener
	 */
	public void setOnDismissListener(OnDismissListener onDismissListener) {

		this.mOnDismissListener = onDismissListener;

	}

	/**
	 * Sets an SwipeDismissListener defined in this library
	 * to the SuperCardToast.
	 * <br>
	 * @param swipeDismiss
	 */
	public void setSwipeToDismiss(boolean swipeDismiss) {
		
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
	
	/** Dismisses the SuperCardToast with Animation. */
	public void dismiss() {

		dismissWithAnimation();

	}
	
	/** Dismisses the SuperCardToast without Animation. */
	public void dismissImmediately() {
		
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
		
		if(mOnDismissListener != null) {
			
			mOnDismissListener.onDismiss();
			
		}

	}

	
	/**
	 * Sets an OnClickListener to the Button of a BUTTON Type
	 * SuperCardToast. 
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
	 * a BUTTON Type SuperCardToast. 
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
	 * a BUTTON Type SuperCardToast. 
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
	 * a BUTTON Type SuperCardToast. 
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
	 * a BUTTON Type SuperCardToast. 
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
	 * Sets the text size of the Button in 
	 * a BUTTON Type SuperCardToast. This
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
	 * Sets an indeterminate value to the ProgressBar of a PROGRESS Type
	 * SuperCardToast. 
	 * <br>
	 * @param isIndeterminate
	 */
	public void setProgressIndeterminate(boolean isIndeterminate) {
		
		if(mProgressBar != null) {
			
			mProgressBar.setIndeterminate(isIndeterminate);
			
		}

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

		if (mToastView != null) {

			return mToastView.isShown();

		}

		else {

			return false;

		}

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
	 * Returns the set duration of the SuperCardToast.
	 * <br>
	 * @return long <br>
	 */
	public long getDuration() {

		return mDuration;

	}

	/**
	 * Returns true if the SuperCardToast is indeterminate.
	 * <br>
	 * @return boolean <br>
	 */
	public boolean getIsIndeterminate() {

		return isIndeterminate;

	}

	/**
	 * Returns the OnDismissListener of the SuperCardToast.
	 * <br>
	 * @return OnDismissListener <br>
	 */
	public OnDismissListener getOnDismissListener() {

		return mOnDismissListener;

	}

	/**
	 * Returns the ViewGroup that the SuperCardToast is attached to.
	 * <br>
	 * @return ViewGroup <br>
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
    private void dismissWithAnimation() {
				
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
	
	private Runnable mHideRunnable = new Runnable() {

		public void run() {

			dismiss();

		}

	};
    
	private Runnable mHideImmediateRunnable = new Runnable() {

		public void run() {

			dismissImmediately();

		}

	};


    private Runnable mHideWithAnimationRunnable = new Runnable() {

        public void run() {

            dismissWithLayoutAnimation();

        }

    };
    
	private Runnable mInvalidateRunnable = new Runnable() {

		public void run() {

			if(mViewGroup != null) {

				mViewGroup.invalidate();

			}

		}

	};
    
	private OnTouchListener mTouchDismissListener = new OnTouchListener() {

		int timesTouched;

		@Override
		public boolean onTouch(View view, MotionEvent event) {

			/**
			 * This is a little hack to prevent the user from repeatedly
			 * touching the SuperCardToast causing erratic behavior
			 **/
			if (timesTouched == 0) {

				dismiss();

			}

			timesTouched++;

			return false;

		}

	};

}