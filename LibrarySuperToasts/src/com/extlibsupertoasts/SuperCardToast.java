package com.extlibsupertoasts;


import com.extlibsupertoasts.styles.SuperCardToastStyle;
import com.extlibsupertoasts.utilities.SuperToastConstants;
import com.extlibsupertoasts.utilities.SwipeDismissListener;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
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



@SuppressLint("NewApi")
@SuppressWarnings("deprecation")
public class SuperCardToast
{
	
	private static final String ERROR_CONTEXTNOTACTIVITY  = "Context must be an instance of Activity (SuperCardToast)";
	private static final String ERROR_CONTAINERNULL = "You must have a LinearLayout with the id of card_container in your layout! (SuperCardToast)";
	private static final String ERROR_TYPENULL = "You cannot supply null as a Type! (SuperCardToast)";

	
	public static final SuperCardToastStyle STYLE_DARKEDIT = new SuperCardToastStyle
			(com.extlibsupertoasts.R.drawable.icon_dark_edit, Color.WHITE, com.extlibsupertoasts.R.drawable.background_black, Color.WHITE);
	
	public static final SuperCardToastStyle STYLE_DARKEXIT = new SuperCardToastStyle
			(com.extlibsupertoasts.R.drawable.icon_dark_exit, Color.WHITE, com.extlibsupertoasts.R.drawable.background_black, Color.WHITE);
	
	public static final SuperCardToastStyle STYLE_DARKINFO = new SuperCardToastStyle
			(com.extlibsupertoasts.R.drawable.icon_dark_info, Color.WHITE, com.extlibsupertoasts.R.drawable.background_black, Color.WHITE);
	
	public static final SuperCardToastStyle STYLE_DARKREDO = new SuperCardToastStyle
			(com.extlibsupertoasts.R.drawable.icon_dark_redo, Color.WHITE, com.extlibsupertoasts.R.drawable.background_black, Color.WHITE);

	public static final SuperCardToastStyle STYLE_DARKSAVE = new SuperCardToastStyle
			(com.extlibsupertoasts.R.drawable.icon_dark_save, Color.WHITE, com.extlibsupertoasts.R.drawable.background_black, Color.WHITE);
	
	public static final SuperCardToastStyle STYLE_DARKSHARE = new SuperCardToastStyle
			(com.extlibsupertoasts.R.drawable.icon_dark_share, Color.WHITE, com.extlibsupertoasts.R.drawable.background_black, Color.WHITE);

	public static final SuperCardToastStyle STYLE_DARKUNDO = new SuperCardToastStyle
			(com.extlibsupertoasts.R.drawable.icon_dark_undo, Color.WHITE, com.extlibsupertoasts.R.drawable.background_black, Color.WHITE);
	
	
	public static final SuperCardToastStyle STYLE_LIGHTEDIT = new SuperCardToastStyle
			(com.extlibsupertoasts.R.drawable.icon_light_edit, Color.BLACK, com.extlibsupertoasts.R.drawable.background_white, Color.BLACK);
	
	public static final SuperCardToastStyle STYLE_LIGHTEXIT = new SuperCardToastStyle
			(com.extlibsupertoasts.R.drawable.icon_light_exit, Color.BLACK, com.extlibsupertoasts.R.drawable.background_white, Color.BLACK);
	
	public static final SuperCardToastStyle STYLE_LIGHTINFO = new SuperCardToastStyle
			(com.extlibsupertoasts.R.drawable.icon_light_info, Color.BLACK, com.extlibsupertoasts.R.drawable.background_white, Color.BLACK);
	
	public static final SuperCardToastStyle STYLE_LIGHTREDO = new SuperCardToastStyle
			(com.extlibsupertoasts.R.drawable.icon_light_redo, Color.BLACK, com.extlibsupertoasts.R.drawable.background_white, Color.BLACK);

	public static final SuperCardToastStyle STYLE_LIGHTSAVE = new SuperCardToastStyle
			(com.extlibsupertoasts.R.drawable.icon_light_save, Color.BLACK, com.extlibsupertoasts.R.drawable.background_white, Color.BLACK);
	
	public static final SuperCardToastStyle STYLE_LIGHTSHARE = new SuperCardToastStyle
			(com.extlibsupertoasts.R.drawable.icon_light_share, Color.BLACK, com.extlibsupertoasts.R.drawable.background_white, Color.BLACK);

	public static final SuperCardToastStyle STYLE_LIGHTUNDO = new SuperCardToastStyle
			(com.extlibsupertoasts.R.drawable.icon_light_undo, Color.BLACK, com.extlibsupertoasts.R.drawable.background_white, Color.BLACK);
	

	private Context mContext;
	private LinearLayout mContainer;
	private int sdkVersion = android.os.Build.VERSION.SDK_INT;
	private Handler mHandler;
	private View toastView;
	private LayoutInflater mLayoutInflater;
	private TextView mTextView; 
	private ProgressBar mProgressBar;
	private Button mButton;
	private View mDivider;
	private LinearLayout mRootLayout;
	private Type mType = Type.STANDARD;
	private ProgressStyle mProgressStyle = ProgressStyle.CIRCLE;
	
	
	private CharSequence textCharSequence;
	private boolean touchDismiss;
	private boolean swipeDismiss;
	private int backgroundResource = (SuperToastConstants.BACKGROUND_BLACK);
	private int dividerColor = (Color.WHITE);
	private int textColor = (Color.WHITE);
	private int buttonResource = (SuperToastConstants.BUTTON_DARK_UNDO);
	private int duration = (SuperToastConstants.DURATION_LONG);
	private Typeface typeface = (Typeface.DEFAULT);
	private Drawable backgroundDrawable;
	private Drawable dividerDrawable;
	private Drawable buttonDrawable;
	private boolean isIndeterminate;
	private float textSize = (SuperToastConstants.TEXTSIZE_SMALL);
	private float buttonTextSize = (SuperToastConstants.TEXTSIZE_MEDIUM);
	private OnClickListener mOnClickListener;
	private OnClickListener mButtonOnClickListener;
	private boolean isProgressIndeterminate;
	
		
	
	public enum Type {
		STANDARD, PROGRESS, BUTTON;
	}

	public enum ProgressStyle {
		CIRCLE, HORIZONTAL;
	}
	
	
	public SuperCardToast(Context mContext) 
	{		
		
		if(mContext instanceof Activity) {
								
			this.mContext = mContext;
			
			final Activity mActivity = (Activity) mContext;
			
			mLayoutInflater = (LayoutInflater) 
					mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			if(mActivity.findViewById(R.id.card_container) != null)
			{
				
				mContainer = (LinearLayout)
						mActivity.findViewById(R.id.card_container);
				
			} else {

				throw new IllegalArgumentException(ERROR_CONTAINERNULL);

			}
							
		} else {

			throw new IllegalArgumentException(ERROR_CONTEXTNOTACTIVITY);

		}

	}
	
	
	public SuperCardToast(Context mContext, Type mType) 
	{		
		
		if (mContext instanceof Activity) {

			this.mContext = mContext;

			final Activity mActivity = (Activity) mContext;

			mLayoutInflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			if (mActivity.findViewById(R.id.card_container) != null) {

				mContainer = (LinearLayout) mActivity
						.findViewById(R.id.card_container);

			} else {

				throw new IllegalArgumentException(ERROR_CONTAINERNULL);

			}

		} else {

			throw new IllegalArgumentException(ERROR_CONTEXTNOTACTIVITY);

		}

		if (mType != null) {

			this.mType = mType;

		} else {

			throw new IllegalArgumentException(ERROR_TYPENULL);

		}

	}

	
	
	public void show()
	{
        			
		if(mType == Type.STANDARD)
		{
			
			toastView = mLayoutInflater
		    		.inflate(R.layout.supercardtoast_toast, mContainer, false);
			
		} else if(mType == Type.BUTTON) {
			
			toastView = mLayoutInflater
		    		.inflate(R.layout.supercardtoast_button, mContainer, false);
			
		} else if(mType == Type.PROGRESS) {
			
			if(mProgressStyle == ProgressStyle.CIRCLE) {

				toastView = mLayoutInflater
			    		.inflate(R.layout.supercardtoast_progresscircle, mContainer, false);
				
			} else if(mProgressStyle == ProgressStyle.HORIZONTAL) {

				toastView = mLayoutInflater
			    		.inflate(R.layout.supercardtoast_progresshorizontal, mContainer, false);
				
			}
			
		}
		
		if(touchDismiss){
				
			toastView.setOnTouchListener(mTouchDismissListener);
				
		}
	    
		if (sdkVersion > android.os.Build.VERSION_CODES.HONEYCOMB_MR1
				&& swipeDismiss) {

			final SwipeDismissListener touchListener = new SwipeDismissListener(
					toastView, new SwipeDismissListener.OnDismissCallback() {

						@Override
						public void onDismiss(View view) {

							mContainer.removeView(toastView);

						}

					});

			toastView.setOnTouchListener(touchListener);

		}
			
			
		if(!isIndeterminate) {
				
			mHandler = new Handler();
			mHandler.postDelayed(mHideRunnable, duration);
												
		}
			
		
	    mTextView = (TextView) 
	    		toastView.findViewById(R.id.messageTextView);	    
	    		
	    mTextView.setTextColor(textColor);
	    			    	
		mTextView.setText(textCharSequence);
		
		mTextView.setTypeface(typeface);
		
		mTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);

		
		if(mType == Type.BUTTON) {
			
			mButton = (Button) 
					toastView.findViewById(R.id.actionButton);
			
			if (buttonDrawable != null) {

				mButton.setCompoundDrawablesWithIntrinsicBounds(null, null, buttonDrawable, null);

			} else {

				mButton.setCompoundDrawablesWithIntrinsicBounds(null, null, mContext.getResources()
						.getDrawable(buttonResource), null);
				
			}
								
			if (mButtonOnClickListener != null) {

				mButton.setOnClickListener(mButtonOnClickListener);

			}

			else {

				Log.e("(SuperCardToast)",
						"There was no OnClickListener set to the Button. Please call setButtonOnClickListener().");
				
			}

			mButton.setTypeface(typeface);
			
			mButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, buttonTextSize);
			
			
			mDivider = (View) 
					toastView.findViewById(R.id.dividerView);
			
			if (dividerDrawable != null) {

				if (sdkVersion < android.os.Build.VERSION_CODES.JELLY_BEAN) {

					mDivider.setBackgroundDrawable(dividerDrawable);

				} else {

					mDivider.setBackground(dividerDrawable);

				}

			} else {

				mDivider.setBackgroundColor(dividerColor);

			}
			
		}
		
		if(mType == Type.PROGRESS) {
			
			mProgressBar = (ProgressBar) 
					toastView.findViewById(R.id.progressBar);
			
			mProgressBar.setIndeterminate(isProgressIndeterminate);
			
		}
		
	   
	    mRootLayout = (LinearLayout)
			   toastView.findViewById(R.id.root_layout);
	   
	    
		if(backgroundDrawable != null) {

			if (sdkVersion < android.os.Build.VERSION_CODES.JELLY_BEAN) {

				mRootLayout.setBackgroundDrawable(backgroundDrawable);

			} else {

				mRootLayout.setBackground(backgroundDrawable);

			}

		} else {

			mRootLayout.setBackgroundResource(backgroundResource);

		}

		
		if(mOnClickListener != null) {
			
			mRootLayout.setOnClickListener(mOnClickListener);
			
		}

			
		mContainer.setVisibility(View.VISIBLE);

		mContainer.addView(toastView);

		toastView.startAnimation(getCardAnimation());
	
	}
	
	
	//XXX: General methods.
	
	
	/**
	 * <b><i> public void setText(CharSequence textCharSequence) </i></b>
	 * 
	 * <p> This is used to set the message text of the SuperCardToast. </p>
	 * 
	 * 
	 * <b> Parameter example: </b>
	 * 	 
	 * <p> ("Hello, I am a SuperActivityToast!") </p>
	 * 
	 */
	public void setText(CharSequence textCharSequence) {

		this.textCharSequence = textCharSequence;

		if (mTextView != null) {

			mTextView.setText(textCharSequence);

		}

	}
	
	
	/**
	 * <b><i> public void setStyle(SuperCardToastStyle mSuperCardToastStyle) </i></b>
	 * 
	 * <p> This is used to set the style of the SuperCardToast. </p>
	 * 
	 * 
	 * <b> Important note: </b>
	 * 	 
	 * <p> Although these styles are more geared for the Button Type SuperCardToast
	 *     they can be used for any Type of SuperCardToast. </p>
	 * 
	 * 
	 * <b> Parameter example: </b>
	 * 
	 * <p> (SuperCardToast.STYLE_UNDODARK) </p>
	 *	 
	 */
	public void setStyle(SuperCardToastStyle mSuperCardToastStyle) {

		this.backgroundResource = mSuperCardToastStyle.backgroundResource;
		this.buttonResource = mSuperCardToastStyle.buttondrawableResource;
		this.dividerColor = mSuperCardToastStyle.dividerdrawableResource;
		this.textColor = mSuperCardToastStyle.messagecolorResource;

	}

	
	/**
	 * <b><i> public void setDuration(int duration) </i></b>
	 * 
	 * <p> This is used to set the duration of the SuperCardToast. </p>
	 * 
	 * 
	 * <b> Parameter example: </b>
	 * 	 
	 * <p> (SuperToastConstants.DURATION_SHORT) </p>
	 * 
	 *	 
	 * <b> Design guide: </b>
     *
	 * <p> Although you may pass any millisecond integer value as a parameter in this
	 *     method, the duration constants of the SuperToastConstants class should be used. </p>
	 *     
	 */
	public void setDuration(int duration) {

		this.duration = duration;

	}
	
	
	/**
	 * <b><i> public void setIndeterminate(boolean isIndeterminate) </i></b>
	 * 
	 * <p> This is used to set an indeterminate value to the SuperCardToast.
	 *     This will force the SuperCardToast to ignore any duration set and 
	 *     {@link #dismiss()} must be called to get rid of the SuperCardToast. </p>
	 * 
	 * 
	 * <b> Parameter example: </b>
	 * 	 
	 * <p> (true) </p>
	 * 
	 */
	public void setIndeterminate(boolean isIndeterminate) {

		this.isIndeterminate = isIndeterminate;

	}
	
	
	/**
	 * <b><i> public void setTouchToDismiss(boolean touchDismiss) </i></b>
	 * 
	 * <p> This is used to set a private OnTouchListener to the SuperCardToast
	 *     which will call {@link #dismiss()} if the user touches the SuperCardToast.
     *
	 *      
	 * <b> Important note: </b>
	 * 	 
	 * <p> This method is not compatible with the method {@link #setSwipeToDismiss(boolean)}. </p>
     * 
	 */
	public void setTouchToDismiss(boolean touchDismiss) {

		this.touchDismiss = touchDismiss;

	}
	
	
	/**
	 * <b><i> public void setSwipeToDismiss(boolean touchDismiss) </i></b>
	 * 
	 * <p> This is used to set a private SwipeToDismiss to the SuperCardToast
	 *     which will call {@link #dismiss()} if the user swipes the SuperCardToast
	 *     to the left or the right.
     *
	 *      
	 * <b> Important note: </b>
	 * 	 
	 * <p> This method is not compatible with pre-honeycomb devices. </p>
     * 
	 */
	public void setSwipeToDismiss(boolean swipeDismiss) {

		this.swipeDismiss = swipeDismiss;

	}
	
	
	/**
	 * <b><i> setBackgroundResource(int backgroundID) </i></b>
	 * 
	 * <p> This is used to set the background resource of the SuperCardToast. </p>
	 * 
	 * 
	 * <b> Parameter example: </b>
	 * 	 
	 * <p> (SuperToastConstants.BACKGROUND_BLACK) </p>
	 * 
	 * 
	 * <b> Design guide: </b>
	 * 
	 * <p> If you choose not to use a background defined in this library
	 *     make sure your background is a nine-patch Drawable. </p>
	 *	 
	 */
	public void setBackgroundResource(int backgroundResource) {

		this.backgroundResource = backgroundResource;

	}
	
	
	/**
	 * <b><i> setBackgroundDrawable(Drawable backgroundDrawable) </i></b>
	 * 
	 * <p> This is used to set the background Drawable of the SuperCardToast.
	 *     To use a background defined in this library please see 
	 *     {@link #setBackgroundResource(int)}. </p>
     *  
     *  
	 * <b> Design guide: </b>
	 * 
	 * <p> If you choose not to use a background defined in this library
	 *     make sure your background is a nine-patch Drawable. </p>
	 *	 
	 */	
	public void setBackgroundDrawable(Drawable backgroundDrawable) {

		this.backgroundDrawable = backgroundDrawable;

	}
	
	
	/**
	 * <b><i> public void setTextSize(int textSizeInt) </i></b>
	 * 
	 * <p> This is used to set the text size of the SuperCardToast message. </p>
	 * 
	 * 
	 * <b> Important note: </b>
	 * 
	 * <p> This method will automatically convert the Integer parameter
	 *     into scaled pixels.
	 * 
	 * 
	 * <b> Parameter example: </b>
	 * 	 
	 * <p> (SuperToastConstants.TEXTSIZE_SMALL) </p>
	 * 
	 * <b> OR </b>
	 * 
     * <p> (14) </p>
     *
	 */
	public void setTextSize(int textSizeInt) {

		this.textSize = textSizeInt;

	}
	

	/**
	 * <b><i> public void setTypeface(Typeface mTypeface) </i></b>
	 * 
	 * <p> This is used to set the Typeface of the SuperCardToast text.	  </p>
	 * 
	 * 
	 * <b> Important note: </b>
	 * 
	 * <p> This library comes with a link to download the Roboto font. To use the fonts see 
	 *     {@link #loadRobotoTypeface(String)}.
	 * 
	 * 
	 * <b> Parameter example: </b>
	 * 	 
	 * <p> (Typeface.DEFAULT) </p>
	 * 
	 * <b> OR </b>
	 * 
	 * <p> (mSuperCardToast.loadRobotoTypeface(SuperToastConstants.FONT_ROBOTO_THIN);
	 *
	 */
	public void setTypeface(Typeface typeface) {

		this.typeface = typeface;

	}	
	
	
	/**
	 * <b><i> public void dismiss() </i></b>
     *
	 * <p> This is used to hide and dispose of the SuperCardToast. </p>
	 *
	 *
	 * <b> Design guide: </b>
	 * 
	 * <p> Treat your SuperCardToast like a Dialog, dismiss it when it is no longer
	 *     relevant. </p>
	 *	 
	 */
	public void dismiss()
	{

		if(toastView != null)
		{
			
			dismissWithAnimation();
			
		}
		
		else
		{
			
			Log.e("SuperCardToast", "The View was null when trying to dismiss. " +
					"Did you create and show a SuperCardToast before trying to dismiss it?");
			
		}
		
	}
	
	
	/**
	 * <b><i> public void dismiss(Animation animation) </i></b>
     *
	 * <p> This is used to hide and dispose of the SuperCardToast
	 *     after the supplied Animation ends. </p>
	 *
	 *
	 * <b> Design guide: </b>
	 * 
	 * <p> The Animation supplied should not exceed 500 milliseconds. </p>
	 *	 
	 */
	public void dismiss(Animation animation)
	{

		if(toastView != null && mContainer != null)
		{
			
			animation.setAnimationListener(new AnimationListener()
			{

				@Override
				public void onAnimationEnd(Animation animation) 
				{

					mContainer.removeView(toastView);
					
				}

				@Override
				public void onAnimationRepeat(Animation animation) 
				{

					// Not used
					
				}

				@Override
				public void onAnimationStart(Animation animation) 
				{

					// Not used
					
				}

			});
			
			toastView.startAnimation(animation);			
			
		}
		
		else
		{
			
			Log.e("SuperCardToast", "Either the View or Container was null when trying to dismiss. " +
					"Did you create and show a SuperCardToast before trying to dismiss it?");
			
		}
		
	}
	
	
	/**
	 * <b><i> public void dismissImmediate() </i></b>
     *
	 * <p> This is used to hide and dispose of the SuperCardToast without an Animation. </p>
	 *
	 *
	 * <b> Design guide: </b>
	 * 
	 * <p> Treat your SuperCardToast like a Dialog, dismiss it when it is no longer
	 *     relevant. </p>
	 *	 
	 */ 
	public void dismissImmediate()
	{
		

		if(toastView != null && mContainer != null)
		{
			
			mContainer.removeView(toastView);
			
		}
		
		else
		{
			
			Log.e("SuperCardToast", "Either the View or Container was null when trying to dismiss. " +
					"Did you create and show a SuperCardToast before trying to dismiss it?");
			
		}
		
	}
	
	
	//XXX Progress specific methods.
	
	
	/**
	 * <b><i> public void setProgressStyle(ProgressStyle mProgressStyle </i></b>
	 * 
	 * <p> This is used to set the style of the SuperCardToast ProgressBar. </p>
	 * 
	 * 
	 * <b> Parameter example: </b>
	 * 	 
	 * <p> (ProgressType.HORIZONTAL) </p>
	 *
	 */
	public void setProgressStyle(ProgressStyle mProgressStyle) {

		this.mProgressStyle = mProgressStyle;

	}
	
	
	/**
	 * <b><i> public void setProgressIndeterminate(boolean isProgressIndeterminate) </i></b>
	 * 
	 * <p> This is used to set an indeterminate value to the SuperCardToast ProgressBar. </p>
	 * 
	 * 
	 * <b> Parameter example: </b>
	 * 	 
	 * <p> (true) </p>
	 * 
	 */
	public void setProgressIndeterminate(boolean isProgressIndeterminate) {

		this.isProgressIndeterminate = isProgressIndeterminate;

	}
	
	
	/**
	 * <b><i> public void setProgress(int progress) </i></b>
	 * 
	 * <p> This is used to set the progress of the SuperCardToast ProgressBar. </p>
	 * 
	 * 
	 * <b> Parameter example: </b>
	 * 	 
	 * <p> (45) </p>
	 * 
	 * 
	 * <b> Important note: </b>
	 * 
	 * <p> This method should be used with a horizontal SuperCardToast to display visual
	 *     progress. </p>
	 * 
	 */
	public void setProgress(int progress)
	{

		if(mProgressBar != null)
		{
				
			mProgressBar.setProgress(progress);
				
		}			
			

	}
	
	
	//XXX Button specific methods.
	
	
	/**
	 * <b><i> public void setButtonOnClickListener(OnClickListener mButtonOnClickListener) </i></b>
	 * 
	 * <p> This is used to set the OnClickListener of the SuperCardToast. </p>
	 *
	 */
	public void setOnClickListener(OnClickListener mOnClickListener) {

		this.mOnClickListener = mOnClickListener;

	}
	
	
	/**
	 * <b><i> public void setButtonOnClickListener(OnClickListener mButtonOnClickListener) </i></b>
	 * 
	 * <p> This is used to set the OnClickListener of the SuperCardToast Button. </p>
	 * 
	 * 
	 * <b> Important note: </b>
	 * 
	 * <p> This method should be used in conjunction with Button Type SuperCardToasts.
	 *
	 */
	public void setButtonOnClickListener(OnClickListener mButtonOnClickListener) {

		this.mButtonOnClickListener = mButtonOnClickListener;

	}
	
	/**
	 * <b><i> public void setButtonTextSize(int buttonTextSize) </i></b>
	 * 
	 * <p> This is used to set the text size of the SuperCardToast Button. </p>
	 * 
	 * 
	 * <b> Important note: </b>
	 * 
	 * <p> This method will automatically convert the Integer parameter
	 *     into scaled pixels.
	 * 
	 * 
	 * <b> Parameter example: </b>
	 * 	 
	 * <p> (SuperToastConstants.TEXTSIZE_MEDIUM) </p>
	 * 
	 * <b> OR </b>
	 * 
     * <p> (14) </p>
     *
	 */
	public void setButtonTextSize(int buttonTextSize) {

		this.buttonTextSize = buttonTextSize;

	}


	
	
	private Animation getCardAnimation()
	{
		
		AnimationSet mAnimationSet = new AnimationSet(false);
		
		TranslateAnimation mTranslateAnimation = new TranslateAnimation(0f, 0f, 1f, 0f);
		mTranslateAnimation.setDuration(200);
		
		mAnimationSet.addAnimation(mTranslateAnimation);

		
		AlphaAnimation mAlphaAnimation = new AlphaAnimation(0f, 1f);
		mAlphaAnimation.setDuration(400);
		
		mAnimationSet.addAnimation(mAlphaAnimation);
		

		RotateAnimation mRotationAnimation = new RotateAnimation(15f, 0f, 0f, 0f);
		mRotationAnimation.setDuration(225);
		
		mAnimationSet.addAnimation(mRotationAnimation);

		return mAnimationSet;
		
	}
	
	
	private void dismissWithAnimation()
	{
				
		if(sdkVersion > android.os.Build.VERSION_CODES.HONEYCOMB_MR1)
		{
			
		     int mViewWidth = toastView.getWidth();

		     toastView.animate()
       	 		.translationX(mViewWidth)
       	 		.alpha(0)
       	 		.setDuration(500)
       	 		.setListener(new AnimatorListenerAdapter() 
       	 		{
       	 		
       	 			@Override
       	 			public void onAnimationEnd(Animator animation) 
       	 			{
            	 
       	 				dismissImmediate();
                 
       	 			}
       	 			
       	 		});

		}
		
		else
		{

			AnimationSet mAnimationSet = new AnimationSet(false);
			
			final Activity mActivity = (Activity) mContext;
			
			Display display = mActivity.getWindowManager().getDefaultDisplay(); 

			int width = display.getWidth(); 
			
			TranslateAnimation mTranslateAnimation = new TranslateAnimation(0f, width, 0f, 0f);
			mTranslateAnimation.setDuration(500);
			mAnimationSet.addAnimation(mTranslateAnimation);

			
			AlphaAnimation mAlphaAnimation = new AlphaAnimation(1f, 0f);
			mAlphaAnimation.setDuration(500);
			mAnimationSet.addAnimation(mAlphaAnimation);
			
			
			mAnimationSet.setAnimationListener(new AnimationListener()
			{

				@Override
				public void onAnimationEnd(Animation animation) 
				{

					/** Must use Handler to modify ViewGroup in onAnimationEnd() **/
					Handler mHandler = new Handler();
					mHandler.post(mHideImmediateRunnable);
					
				}

				@Override
				public void onAnimationRepeat(Animation animation) 
				{
					
					// Not used
					
				}

				@Override
				public void onAnimationStart(Animation animation) 
				{

					// Not used
					
				}

			});
			
			toastView.startAnimation(mAnimationSet);
						

		}

	}
	
	
	private Runnable mHideRunnable = new Runnable() 
	{
		 
        public void run() 
        {
        	        	
        	dismiss();
        	 
        }
        
    };
    
    
	private Runnable mHideImmediateRunnable = new Runnable() 
	{
		 
        public void run() 
        {
        	        	
        	dismissImmediate();
        	 
        }
        
    };
    
    
	private OnTouchListener mTouchDismissListener = new OnTouchListener()
	{

		int timesTouched;
		
		@Override
		public boolean onTouch(View view, MotionEvent event) 
		{
			
			/** This is a little hack to prevent the user from repeatedly 
			 *  touching the SuperCardToast causing erratic behavior **/
			if(timesTouched == 0)
			{
				
				dismiss();
				
			}
			
			else
			{
				
				dismissImmediate();
				
			}
			
			timesTouched++;
						
			return false;
			
		}
		
	};

	

	
	
	
}