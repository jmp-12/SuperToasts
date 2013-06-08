package com.extlibsupertoasts;


import com.extlibsupertoasts.styles.SuperCardToastStyle;
import com.extlibsupertoasts.utilities.SuperToastConstants;
import com.extlibsupertoasts.utilities.SwipeDismissListener;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
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
	
	private static final String ERROR_CONTEXTNOTACTIVITY= "Context must be an instance of Activity (SuperCardToast)";
	private static final String ERROR_CONTAINERNULL= "You must have a LinearLayout with the id of card_container in your layout! (SuperCardToast)";

	
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
	
	
	private CharSequence textCharSequence;
	private boolean touchDismiss;
	private boolean enableSwipeDismiss;
	private Drawable backgroundDrawable;
	private int backgroundResource = (SuperToastConstants.BACKGROUND_BLACK);
	private Typeface typeface = Typeface.DEFAULT;
	private int dividerColor = (Color.WHITE);
	private Drawable dividerDrawable;
	private int textColor = Color.WHITE;
	private boolean isIndeterminate;
	private boolean isProgressIndeterminate;
	private int duration = SuperToastConstants.DURATION_LONG;
	private float textSize = SuperToastConstants.TEXTSIZE_SMALL;

	
	
	private SuperCardToastStyle mSuperCardToastStyle;

	
	
	public SuperCardToast(Context mContext) 
	{		
		
		if(mContext instanceof Activity)
		{
								
			this.mContext = mContext;
			
			final Activity mActivity = (Activity) mContext;
			
			mLayoutInflater = (LayoutInflater) 
					mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			if(mActivity.findViewById(R.id.card_container) != null)
			{
				
				mContainer = (LinearLayout)
						mActivity.findViewById(R.id.card_container);
				
			}
			
			else
			{
					
				throw new IllegalArgumentException(ERROR_CONTAINERNULL);
					
			}
							
		}
			
		else
		{
				
			throw new IllegalArgumentException(ERROR_CONTEXTNOTACTIVITY);
				
		}

	}

	
	
	/**
	 * <b><i> public void createButtonCard(String messageText, OnClickListener mOnClickListener) </i></b>
	 * 
	 * <p> This is used to create a SuperCardToast with a clickable button. </p>
	 * 
	 * 
	 * <b> Important note: </b>
	 * 
	 * <p> You may only use one create method per object. If you would like
	 *     to show more than one SuperCardToast than you must create multiple objects. </p>
	 *     
	 *     
     * <b> Design guide: </b>
     *
	 * <p> The SuperCardToast with a button is meant to be used in instances where
	 *     a SuperButtonToast would be necessary. Although you can place the layout container </p>
	 *     anywhere in your Activity's layout, it is recommended that you put the container at the very top of
	 *     the layout.
	 * 
	 */
	public void showButtonCard(OnClickListener mOnClickListener)
	{
        			
		toastView = mLayoutInflater
	    		.inflate(R.layout.supercardtoast_button, mContainer, false);
		
			if(touchDismiss)
			{
				
				toastView.setOnTouchListener(mTouchDismissListener);
				
			}
	    
			if(sdkVersion > android.os.Build.VERSION_CODES.HONEYCOMB_MR1 && enableSwipeDismiss) 
			{
				
		        final SwipeDismissListener touchListener = new SwipeDismissListener(toastView, new SwipeDismissListener.OnDismissCallback() 
		        {
							
		        	@Override
					public void onDismiss(View view) 
					{
	
						mContainer.removeView(toastView);
															
					}
		        	
				});
	
		        toastView.setOnTouchListener(touchListener);
				
			}
			
			
			if(!isIndeterminate)
			{
				
				mHandler = new Handler();
				mHandler.postDelayed(mHideRunnable, duration);
												
			}
			
		
	    mTextView = (TextView) 
	    		toastView.findViewById(R.id.messageTextView);
	    
	    	if(mSuperCardToastStyle != null)
	    	{
	    		
	    		mTextView.setTextColor(mSuperCardToastStyle.messagecolorResource);
	    		
	    	}
	    	
	    	else
	    	{
	    		
	        	mTextView.setTextColor(textColor);
	    		
	    	}
	    	
		mTextView.setText(textCharSequence);
		
		
		mButton = (Button) 
				toastView.findViewById(R.id.actionButton);
		
			if(mSuperCardToastStyle != null)
			{
				
				mButton.setCompoundDrawablesWithIntrinsicBounds(null, null, mContext.getResources()
						.getDrawable(mSuperCardToastStyle.buttondrawableResource), null);
				
			}
		
		mButton.setOnClickListener(mOnClickListener);

	   
	    mRootLayout = (LinearLayout)
			   toastView.findViewById(R.id.root_layout);
	   
			if(mSuperCardToastStyle != null)
			{
					
				mRootLayout.setBackgroundResource(mSuperCardToastStyle.backgroundResource);
		
			}
			
			else
			{
				
				if(backgroundDrawable != null)
				{
					
					if(sdkVersion < android.os.Build.VERSION_CODES.JELLY_BEAN) 
					{
														
						mRootLayout.setBackgroundDrawable(backgroundDrawable);
							
					}
						
					else 
					{
							
						mRootLayout.setBackground(backgroundDrawable);
						    
					}
	
				}
				
				else
				{
					
					mRootLayout.setBackgroundResource(backgroundResource);
	
				}
				
			}
			
			
		mDivider = (View) 
				toastView.findViewById(R.id.dividerView);
		
			if(mSuperCardToastStyle != null)
			{
				
				mDivider.setBackgroundColor(mSuperCardToastStyle.dividerdrawableResource);
				
			}
			
			else
			{
				
				if(dividerDrawable != null)
				{
					
					if(sdkVersion < android.os.Build.VERSION_CODES.JELLY_BEAN) 
					{
												
						mDivider.setBackgroundDrawable(dividerDrawable);
						
					}
					
					else 
					{
						
						mDivider.setBackground(dividerDrawable);
					    
					}
					
				}
				
				else
				{
					
					mDivider.setBackgroundColor(dividerColor);

				}
				
			}
			
		mContainer.setVisibility(View.VISIBLE);

		mContainer.addView(toastView);

		toastView.startAnimation(getCardAnimation());
	
	}

	
	
	/**
	 * <b><i> public void createProgressCard(String messageText, boolean isIndeterminate, boolean isHorizontal)</i></b>
	 * 
	 * <p> This is used to create a SuperCardToast with a ProgressBar. </p>
	 * 
	 * 
	 * <b> Important note: </b>
	 * 
	 * <p> You may only use one create method per object. If you would like
	 *     to show more than one SuperCardToast than you must create multiple objects. </p>
	 *     
	 *     
     * <b> Design guide: </b>
     *
	 * <p> The SuperCardToast with a ProgressBar is meant to be used in instances where
	 *     a SuperProgressToast would be necessary. Although you can place the layout container </p>
	 *     anywhere in your Activity's layout, it is recommended that you put the container at the very top of
	 *     the layout.
	 * 
	 */
	public void showProgressCard(boolean isIndeterminate, boolean isHorizontal)
	{
		
			if(touchDismiss)
			{
				
				toastView.setOnTouchListener(mTouchDismissListener);
				
			}
		
		
			if(isHorizontal)
			{
				
				toastView = mLayoutInflater
			    		.inflate(R.layout.supercardtoast_progresshorizontal, mContainer, false);
				
			}
			
			else
			{
				
				toastView = mLayoutInflater
			    		.inflate(R.layout.supercardtoast_progresscircle, mContainer, false);
			}
		
	    
			if(sdkVersion > android.os.Build.VERSION_CODES.HONEYCOMB_MR1 && enableSwipeDismiss) 
			{
				
		        final SwipeDismissListener touchListener = new SwipeDismissListener(toastView, new SwipeDismissListener.OnDismissCallback() 
		        {
							
		        	@Override
					public void onDismiss(View view) 
					{
	
						mContainer.removeView(toastView);
															
					}
		        	
				});
	
		        toastView.setOnTouchListener(touchListener);
				
			}
			
		
	    mTextView = (TextView) 
	    		toastView.findViewById(R.id.messageTextView);
	    
	    	if(mSuperCardToastStyle != null)
	    	{
	    		
	    		mTextView.setTextColor(mSuperCardToastStyle.messagecolorResource);
	    		
	    	}
	    	
		mTextView.setText(textCharSequence);
		
		
		mProgressBar = (ProgressBar) 
				toastView.findViewById(R.id.progressBar);
		
		mProgressBar.setIndeterminate(isIndeterminate);
	   
		
	    mRootLayout = (LinearLayout)
	    		toastView.findViewById(R.id.root_layout);
	   
			if(mSuperCardToastStyle != null)
			{
					
				mRootLayout.setBackgroundResource(mSuperCardToastStyle.backgroundResource);
		
			}
			
			else
			{
				
				if(backgroundDrawable != null)
				{
					
					if(sdkVersion < android.os.Build.VERSION_CODES.JELLY_BEAN) 
					{
														
						mRootLayout.setBackgroundDrawable(backgroundDrawable);
							
					}
						
					else 
					{
							
						mRootLayout.setBackground(backgroundDrawable);
						    
					}
	
				}
				
				else
				{
					
					mRootLayout.setBackgroundResource(backgroundResource);
	
				}
				
			}
			
		mContainer.setVisibility(View.VISIBLE);

		mContainer.addView(toastView);

		toastView.startAnimation(getCardAnimation());		
			
	}
	
	
	
	/**
	 * <b><i> public void showToastCard(String messageText) </i></b>
	 * 
	 * <p> This is used to create a standard SuperCardToast. </p>
	 * 
	 * 
	 * <b> Important note: </b>
	 * 
	 * <p> You may only use one create method per object. If you would like
	 *     to show more than one SuperCardToast than you must create multiple objects. </p>
	 *     
	 *     
     * <b> Design guide: </b>
     *
	 * <p> The standard SuperCardToast is meant to be used in instances where
	 *     a SuperToast would be necessary. Although you can place the layout container </p>
	 *     anywhere in your Activity's layout, it is recommended that you put the container 
	 *     at the very top of the layout.
	 * 
	 */
	public void showToastCard()
	{
		
		toastView = mLayoutInflater
	    		.inflate(R.layout.supercardtoast_toast, mContainer, false);
		
			if(touchDismiss)
			{
				
				toastView.setOnTouchListener(mTouchDismissListener);
				
			}
		
	    
			if(sdkVersion > android.os.Build.VERSION_CODES.HONEYCOMB_MR1 && enableSwipeDismiss) 
			{
				
		        final SwipeDismissListener touchListener = new SwipeDismissListener(toastView, new SwipeDismissListener.OnDismissCallback() 
		        {
							
		        	@Override
					public void onDismiss(View view) 
					{
	
						mContainer.removeView(toastView);
															
					}
		        	
				});
	
		        toastView.setOnTouchListener(touchListener);
				
			}
			

			if(!isIndeterminate)
			{
				
				mHandler = new Handler();
				mHandler.postDelayed(mHideRunnable, duration);
								
			}
			
	
	    mTextView = (TextView) 
	    		toastView.findViewById(R.id.messageTextView);
	    
	    	if(mSuperCardToastStyle != null)
	    	{
	    		
	    		mTextView.setTextColor(mSuperCardToastStyle.messagecolorResource);
	    		
	    	}
	    	
	    	else
	    	{
	    		
	        	mTextView.setTextColor(textColor);
	    		
	    	}
	    	
		mTextView.setText(textCharSequence);
		
		
	    mRootLayout = (LinearLayout)
	    		toastView.findViewById(R.id.root_layout);
	   
			if(mSuperCardToastStyle != null)
			{
					
				mRootLayout.setBackgroundResource(mSuperCardToastStyle.backgroundResource);
		
			}
			
			else
			{
				
				if(backgroundDrawable != null)
				{
					
					if(sdkVersion < android.os.Build.VERSION_CODES.JELLY_BEAN) 
					{
														
						mRootLayout.setBackgroundDrawable(backgroundDrawable);
							
					}
						
					else 
					{
							
						mRootLayout.setBackground(backgroundDrawable);
						    
					}
	
				}
				
				else
				{
					
					mRootLayout.setBackgroundResource(backgroundResource);
	
				}
				
			}
			
		mContainer.setVisibility(View.VISIBLE);

		mContainer.addView(toastView);

		toastView.startAnimation(getCardAnimation());
		
	}
	
	
	//Quick Navigation: Setter methods
	
	
	/**
	 * <b><i> public void setText(CharSequence textCharSequence) </i></b>
	 * 
	 * <p> This is used to set the message text of the SuperActivityToast. </p>
	 * 
	 * 
	 * <b> Parameter example: </b>
	 * 	 
	 * <p> ("Hello, I am a SuperActivityToast!") </p>
	 * 
	 * 
	 * <b> Important note: </b>
	 * 
	 * <p> This method can be called again while the SuperActivityToast is showing to 
	 *     modify the existing message. If your application might show two SuperActivityToasts
	 *     at one time you should try to reuse the same SuperActivityToast by calling this method and
	 *     {@link #resetDuration(int)}. </p>
	 * 
	 */
	public void setText(CharSequence textCharSequence)
	{

		this.textCharSequence = textCharSequence;
		
		if(mTextView != null)
		{
			
			mTextView.setText(textCharSequence);
			
		}
		
	}
	
	
	/**
	 * <b><i> public void setStyle(SuperCardToastStyle mSuperCardToastStyle) </i></b>
	 * 
	 * <p> This is used to set the style of the SuperCardToast. </p>
	 * 
	 * 
	 * <b> Parameter example: </b>
	 * 
	 * <p> (SuperCardToast.STYLE_UNDODARK) </p>
	 *	 
	 */
	
	public void setStyle(SuperCardToastStyle mSuperCardToastStyle)
	{
		
		this.mSuperCardToastStyle = mSuperCardToastStyle;
		
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
	public void setDuration(int duration)
	{
		
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
	public void setIndeterminate(boolean isIndeterminate)
	{

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
	 * <p> This method is not compatible </p>
     * 
	 */
	public void setTouchToDismiss(boolean touchDismiss)
	{
		
		this.touchDismiss = touchDismiss;
		
	}
	
	
	/**
	 * <b><i> setBackgroundResource(int backgroundID) </i></b>
	 * 
	 * <p> This is used to set the background resource of the SuperCardToast. </p>
	 * 
	 * 
	 * <b> Parameter example: </b>
	 * 	 
	 * <p> (SuperToastConstants.BACKGROUND_STANDARDBLACK) </p>
	 * 
	 * 
	 * <b> Design guide: </b>
	 * 
	 * <p> If you choose not to use a background defined in this library
	 *     make sure your background is a nine-patch Drawable. </p>
	 *	 
	 */
	public void setBackgroundResource(int backgroundResource)
	{
		
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
	public void setBackgroundDrawable(Drawable backgroundDrawable)
	{
		
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
	public void setTextSize(int textSizeInt)
	{

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
	public void setTypeface(Typeface typeface)
	{
		
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
		
		mHandler.removeCallbacks(mHideRunnable);

		
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
	
	
	private Runnable mHideRunnable = new Runnable() 
	{
		 
        public void run() 
        {
        	        	
        	dismiss();
        	 
        }
        
    };
    
    
	private OnTouchListener mTouchDismissListener = new OnTouchListener()
	{

		@Override
		public boolean onTouch(View view, MotionEvent event) 
		{
			
			dismiss();
			
			return false;
			
		}
		
	};

	

	
	
	
}