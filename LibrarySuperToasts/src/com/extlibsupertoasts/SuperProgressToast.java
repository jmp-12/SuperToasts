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

package com.extlibsupertoasts;


import com.extlibsupertoasts.utilities.SuperToastConstants;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Handler;
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
import android.view.animation.Animation.AnimationListener;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * SuperProgressToasts are designed to be used inside of Activities. When the
 * Activity is destroyed the SuperProgressToast is destroyed along with it.
 * SuperProgressToasts will not linger to the next screen like standard
 * Toasts/SuperToasts.
 * 
 */
@SuppressLint("NewApi")
@SuppressWarnings("deprecation")
public class SuperProgressToast
{
	
	private static final String TAG = "SuperProgressToast";

	private static final String ERROR_CONTEXTNULL= "The Context that you passed was null! (SuperProgressToast)";
	private static final String ERROR_CONTEXTNOTACTIVITY= "The Context that you passed was not an Activity! (SuperProgressToast)";
	private static final String ERROR_TYPENULL = "You cannot supply null as a Type! (SuperProgressToast)";
	private static final String ERROR_VIEWORCONTAINERNULL = "Either the View or Container was null when trying to dismiss. "
				+ "Did you create and show a SuperProgressToast before trying to dismiss it?";
	
	private Context mContext;
	private LayoutInflater mLayoutInflater;
	private ViewGroup mViewGroup;
	private View toastView;
	private TextView messageTextView;
	private int sdkVersion = android.os.Build.VERSION.SDK_INT;
	private ProgressBar mProgressBar;
		
	private CharSequence textCharSequence;
	private int textColor = Color.WHITE;
	private int backgroundResource = SuperToastConstants.BACKGROUND_BLACK;
	private Drawable backgroundDrawable;
	private Typeface typeface = Typeface.DEFAULT;
	private float textSize = SuperToastConstants.TEXTSIZE_SMALL;
	private boolean isIndeterminate;
	private OnClickListener mOnClickListener;
	private Animation showAnimation = getFadeInAnimation();
	private Animation dismissAnimation = getFadeOutAnimation();
	private boolean touchDismiss;
	private boolean touchImmediateDismiss;
	
	/**
	 * This is used to specify the style of the ProgressBar
	 * in the SuperProgressToast.
	 * 
	 */
	public enum ProgressStyle {
		
		/**
		 * Circle style ProgressBar.
		 */
		CIRCLE,
		
		/**
		 * Horizontal style ProgressBar.
		 */
		HORIZONTAL;
		
	}
		
	/**
	 * Instantiates a new SuperProgressToast. You <b>MUST</b> pass an Activity
	 * as a Context.
	 * 
	 * <br>
	 * 
	 * @param mContext
	 * 
	 * <br>
	 * This must be an Activity Context.
	 * <br>
	 * 
	 */
	public SuperProgressToast(Context mContext) 
	{

		if (mContext != null) {

			if (mContext instanceof Activity) {

				this.mContext = mContext;

				mLayoutInflater = (LayoutInflater) mContext
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

				final Activity mActivity = (Activity) mContext;

				mViewGroup = (ViewGroup) mActivity
						.findViewById(android.R.id.content);

				toastView = mLayoutInflater.inflate(
						R.layout.supercircleprogresstoast, mViewGroup, false);

			} else {

				throw new IllegalArgumentException(ERROR_CONTEXTNOTACTIVITY);

			}

		}

		else {

			throw new IllegalArgumentException(ERROR_CONTEXTNULL);

		}
		
	}

	/**
	 * Instantiates a new SuperProgressToast. You <b>MUST</b> pass an Activity
	 * as a Context.
	 * 
	 * <br>
	 * 
	 * @param mContext
	 * 
	 * <br>
	 * This must be an Activity Context.
	 * <br>
	 * @param mProgressStyle
	 * <br>
	 * Example: (SuperProgressToast.ProgressStyle.HORIZONTAL)
	 * <br>
	 * 
	 */
	public SuperProgressToast(Context mContext, ProgressStyle mProgressStyle) 
	{
				
		if(mContext != null)
		{
			
			
			if(mContext instanceof Activity)
			{
				
				this.mContext = mContext;
				
				mLayoutInflater = (LayoutInflater) 
						mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			    
				final Activity mActivity = (Activity) mContext;
				
				mViewGroup = (ViewGroup) 
						mActivity.findViewById(android.R.id.content);
				
				if(mProgressStyle != null)
				{
					
					if(mProgressStyle == ProgressStyle.CIRCLE) {

						toastView = mLayoutInflater
								.inflate(R.layout.supercircleprogresstoast, mViewGroup, false);
						
					} else if(mProgressStyle == ProgressStyle.HORIZONTAL) {
						
						toastView = mLayoutInflater
								.inflate(R.layout.superhorizontalprogresstoast, mViewGroup, false);
						
					}
					
				} else { 
					
					throw new IllegalArgumentException(ERROR_TYPENULL);
					
				}

				
			} else {

				throw new IllegalArgumentException(ERROR_CONTEXTNOTACTIVITY);

			}

		} else {

			throw new IllegalArgumentException(ERROR_CONTEXTNULL);

		}
		
	}
	
	/**
	 * This is used to show the SuperProgressToast. You should
	 * do all of your modifications to the SuperProgressToast before calling
	 * this method. 
	 */
	public void show()
	{
		
		mProgressBar = (ProgressBar)
				toastView.findViewById(R.id.progressBar);
		
		
		if (isIndeterminate) {

			mProgressBar.setIndeterminate(true);

		}
		
		
		if (mOnClickListener != null) {

			toastView.setOnClickListener(mOnClickListener);

		}

		if (touchDismiss || touchImmediateDismiss) {

			if (touchDismiss) {

				toastView.setOnTouchListener(mTouchDismissListener);

			} else if (touchImmediateDismiss) {

				toastView.setOnTouchListener(mTouchImmediateDismissListener);

			}

		}

		messageTextView = (TextView) 
				toastView.findViewById(R.id.messageTextView);
		
		messageTextView.setText(textCharSequence);
		messageTextView.setTypeface(typeface);
		messageTextView.setTextColor(textColor);
		messageTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);


		final LinearLayout mRootLayout = (LinearLayout) 
				toastView.findViewById(R.id.toast_rootlayout);
		
		if (backgroundDrawable != null) {

			if (sdkVersion < android.os.Build.VERSION_CODES.JELLY_BEAN) {

				mRootLayout.setBackgroundDrawable(backgroundDrawable);

			} else {

				mRootLayout.setBackground(backgroundDrawable);

			}

		} else {

			mRootLayout.setBackgroundResource(backgroundResource);

		}
		
			
		mViewGroup.addView(toastView);

		toastView.startAnimation(showAnimation);		    
		
	}
	
	
	
	/**
	 * This is used to set the message text of the SuperProgressToast.
	 * 
	 * <br>
	 * 
	 * <p>
	 * <b> Important note: </b>
	 * </p>
	 * 
	 * <p>
	 * This method can be called again while the SuperProgressToast is showing
	 * to modify the existing message.
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
	 * This is used to set the progress of the SuperProgressToast.
	 * 
	 * <br>
	 * 
	 * <p>
	 * <b> Important note: </b>
	 * </p>
	 * 
	 * <p>
	 * This method will only work with a horizontal style SuperProgressToast.
	 * </p>
	 * 
	 * <br>
	 * @param progress 
	 * <br>
	 * 
	 */
	public void setProgress(int progress) {

		if (mProgressBar != null) {

			mProgressBar.setProgress(progress);

		}

	}

	
	/**
	 * This is used to set the message text color of the SuperProgressToast.
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
	 * This is used to set an indeterminate progress to the 
	 * ProgressBar in the SuperProgressToast.
	 * 	 
	 * <br>
	 * @param isIndeterminate 
	 * <br>
	 * 
	 */
	public void setIndeterminate(boolean isIndeterminate) {

		this.isIndeterminate = isIndeterminate;

	}
	
	
	/**
	 * This is used to set an OnClickListener to the SuperProgressToast.
	 * 
	 * <br>
	 * 
	 * <p>
	 * <b> Important note: </b>
	 * </p>
	 * 
	 * <p>
	 * This method is not compatible with {@link #setTouchToDismiss(boolean)} or
	 * {@link #setTouchToImmediateDismiss(boolean)}.
	 * </p>
	 * 
	 * <br>
	 * @param mOnClickListener 
	 * <br>
	 * 
	 */
	public void setOnClickListener(OnClickListener mOnClickListener) {

		this.mOnClickListener = mOnClickListener;

	}
	
	
	/**
	 * This is used to set the background of the SuperProgressToast.
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
	 * This is used to set the background of the SuperProgressToast.
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
	 * This is used to set the text size of the SuperProgressToast.
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
	 * This is used to set the Typeface of the SuperProgressToast text.
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
	 * 		Example: (Typeface.DEFAULT) OR (mSuperProgressToast.loadRobotoTypeface(SuperToastConstants.
	 * FONT_ROBOTO_THIN);	 * 		
	 * <br>
	 * 
	 */
	public void setTypeface(Typeface typeface) {

		this.typeface = typeface;

	}
		
	
	/**
	 * This is used to set the show animation of the SuperProgressToast.
	 * 
	 * <br>
	 * 
	 * <p>
	 * <b> Design guide: </b>
	 * </p>
	 * 
	 * <p>
	 * The Animation that you supply here should be simple and not exceed
	 * 500 milliseconds.
	 * </p>
	 * 
	 * <br>
	 * @param showAnimation 
	 * <br>
	 * 
	 */
	public void setShowAnimation(Animation showAnimation) {

		this.showAnimation = showAnimation;

	}
	
	
	/**
	 * This is used to set the dismiss animation of the SuperProgressToast.
	 * 
	 * <br>
	 * 
	 * <p>
	 * <b> Design guide: </b>
	 * </p>
	 * 
	 * <p>
	 * The Animation that you supply here should be simple and not exceed
	 * 500 milliseconds.
	 * </p>
	 * 
	 * <br>
	 * @param dismissAnimation 
	 * <br>
	 * 
	 */
	public void setDismissAnimation(Animation dismissAnimation) {

		this.dismissAnimation = dismissAnimation;

	}
	
	
	/**
	 * This is used to set a private OnTouchListener to the SuperProgressToast
	 * that will dismiss the SuperProgressToast with an Animation.
	 * 
	 * <br>
	 * 
	 * <p>
	 * <b> Design guide: </b>
	 * </p>
	 * 
	 * <p>
	 * This method should be used with long running SuperProgressToasts in case
	 * the SuperProgressToast comes in between application content and the user.
	 * This method is not compatible with {@link #setOnClickListener(OnClickListener)}.
	 * </p>
	 * 
	 * <br>
	 * @param touchDismiss 
	 * <br>
	 * 
	 */
	public void setTouchToDismiss(boolean touchDismiss) {

		this.touchDismiss = touchDismiss;

	}
	
	
	/**
	 * This is used to set a private OnTouchListener to the SuperProgressToast
	 * that will dismiss the SuperProgressToast immediately without an Animation.
	 * 
	 * <br>
	 * 
	 * <p>
	 * <b> Design guide: </b>
	 * </p>
	 * 
	 * <p>
	 * This method should be used with long running SuperProgressToasts in case
	 * the SuperProgressToast comes in between application content and the user.
	 * This method is not compatible with {@link #setOnClickListener(OnClickListener)}.
	 * </p>
	 * 
	 * <br>
	 * @param touchImmediateDismiss 
	 * <br>
	 * 
	 */
	public void setTouchToImmediateDismiss(boolean touchImmediateDismiss) {

		this.touchImmediateDismiss = touchImmediateDismiss;

	}
	
	
	/**
	 * This is used to dismiss the SuperActivityToast.
	 * 
	 * <br>
	 * 
	 */
	public void dismiss() {

		dismissWithAnimation();

	}
	
	
	/**
	 * This is used to dismiss the SuperActivityToast immediately without Animation.
	 * 
	 * <br>
	 * 
	 */
	public void dismissImmediately() {

		if (toastView != null && mViewGroup != null) {

			mViewGroup.removeView(toastView);
			toastView = null;

		} else {

			Log.e(TAG, ERROR_VIEWORCONTAINERNULL);

		}

	}
	
	
	//XXX: Getter methods. 
	
	
	/**
	 * This is used to get the SuperProgressToast message TextView.
	 * 
	 * <br>
	 * @return TextView
	 * <br>
	 * 
	 */
	public TextView getTextView() {

		return messageTextView;

	}
	
	
	/**
	 * This is used to get the SuperProgressToast View.
	 * 
	 * <br>
	 * @return View
	 * <br>
	 * 
	 */
	public View getView() {

		return toastView;

	}

	
	/**
	 * This is used to get the SuperProgressToast ProgressBar.
	 * 
	 * <br>
	 * @return View
	 * <br>
	 * 
	 */
	public View getProgressBar() {

		return mProgressBar;

	}
	
	
	/**
	 * Returns true of the SuperProgressToast is currently visible 
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
	 * @return Typeface
	 * <br>
	 * 
	 */
	public Typeface loadRobotoTypeface(String typefaceString) {

		return Typeface.createFromAsset(mContext.getAssets(), typefaceString);

	}
	

	//XXX: Private methods.
    
	
	private Runnable mHideImmediateRunnable = new Runnable() 
	{
		 
		public void run() {

			dismissImmediately();

		}
        
    };
    
	private Animation getFadeInAnimation()
	{
		
		
		AlphaAnimation mAlphaAnimation = new AlphaAnimation(0f, 1f);
		mAlphaAnimation.setDuration(500);
		mAlphaAnimation.setInterpolator(new AccelerateInterpolator());
		
		return mAlphaAnimation;
		
	}
	
	private Animation getFadeOutAnimation()
	{
		
		
		AlphaAnimation mAlphaAnimation = new AlphaAnimation(1f, 0f);
		mAlphaAnimation.setDuration(500);
		mAlphaAnimation.setInterpolator(new AccelerateInterpolator());
		
		return mAlphaAnimation;
		
	}
	
	private OnTouchListener mTouchDismissListener = new OnTouchListener() {

		int timesTouched;

		@Override
		public boolean onTouch(View view, MotionEvent event) {

			/** This is a little hack to prevent the user from repeatedly 
			 *  touching the SuperProgressToast causing erratic behavior **/
			if (timesTouched == 0) {

				dismiss();

			} 
			
			timesTouched++;
			
			return false;

		}

	};

	private OnTouchListener mTouchImmediateDismissListener = new OnTouchListener() {

		@Override
		public boolean onTouch(View view, MotionEvent event) {

			dismissImmediately();

			return false;

		}

	};

	
	private void dismissWithAnimation() {

		if (dismissAnimation != null) {

			dismissAnimation.setAnimationListener(new AnimationListener() {

				@Override
				public void onAnimationEnd(Animation animation) {

					/** Must use Handler to modify ViewGroup in onAnimationEnd() **/
					Handler mHandler = new Handler();
					mHandler.post(mHideImmediateRunnable);

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

			toastView.startAnimation(dismissAnimation);

		}

		else {

			Animation mAnimation = getFadeOutAnimation();

			mAnimation.setAnimationListener(new AnimationListener() {

				@Override
				public void onAnimationEnd(Animation animation) {

					/** Must use Handler to modify ViewGroup in onAnimationEnd() **/
					Handler mHandler = new Handler();
					mHandler.post(mHideImmediateRunnable);

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

			toastView.startAnimation(mAnimation);

		}

	}
    
}



