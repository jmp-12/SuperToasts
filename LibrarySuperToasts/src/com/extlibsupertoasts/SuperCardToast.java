package com.extlibsupertoasts;


import com.extlibsupertoasts.styles.SuperCardToastStyle;
import com.extlibsupertoasts.utilities.SuperToastConstants;
import com.extlibsupertoasts.utilities.SwipeDismissListener;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
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
	
	
	private CharSequence textCharSequence;
	private boolean disableSwipeDismiss;
	private Drawable backgroundDrawable;
	private int backgroundResource = (SuperToastConstants.BACKGROUND_BLACK);
	private int dividerColor = (Color.WHITE);
	private Drawable dividerDrawable;
	private boolean mIsTimed;
	private int textColor = Color.WHITE;
	private int duration = SuperToastConstants.DURATION_LONG;
	private float textSize = SuperToastConstants.TEXTSIZE_SMALL;

	
	
	private SuperCardToastStyle mSuperCardToastStyle;

	
	
	public SuperCardToast(Context mContext) 
	{		
		
		if(mContext instanceof Activity)
		{
								
			this.mContext = mContext;
			
			final Activity mActivity = (Activity) mContext;
			
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


	public void createButtonCard(String messageText, final OnClickListener mOnClickListener)
	{
				
		final LayoutInflater superundoLayoutInflater = (LayoutInflater) 
				mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        			
		toastView = superundoLayoutInflater
	    		.inflate(R.layout.supercardtoast_button, mContainer, false);
	    
			if(sdkVersion > android.os.Build.VERSION_CODES.HONEYCOMB_MR1 && !disableSwipeDismiss) 
			{
				
		        final SwipeDismissListener touchListener = new SwipeDismissListener(toastView,
	                    new SwipeDismissListener.OnDismissCallback() {
							
							@Override
							public void onDismiss(View view) 
							{
	
								mContainer.removeView(toastView);
															
							}
						});
	
		        toastView.setOnTouchListener(touchListener);
				
			}
			
			
			if(mIsTimed && duration > 0)
			{
				
				mHandler = new Handler();
				mHandler.postDelayed(mHideRunnable, duration);
								
			}
			
		
	    final TextView mTextView = (TextView) 
	    		toastView.findViewById(R.id.messageTextView);
	    
	    	if(mSuperCardToastStyle != null)
	    	{
	    		
	    		mTextView.setTextColor(mSuperCardToastStyle.messagecolorResource);
	    		
	    	}
	    	
	    	else
	    	{
	    		
	        	mTextView.setTextColor(textColor);
	    		
	    	}
	    	
		mTextView.setText(messageText);
		
		
		final Button actionButton = (Button) 
				toastView.findViewById(R.id.actionButton);
		
			if(mSuperCardToastStyle != null)
			{
				
				actionButton.setCompoundDrawablesWithIntrinsicBounds(null, null, mContext.getResources()
						.getDrawable(mSuperCardToastStyle.buttondrawableResource), null);
				
			}
		
	   actionButton.setOnClickListener(mOnClickListener);

	   
	   final LinearLayout mRootLayout = (LinearLayout)
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
			
			
		final View dividerView = (View) 
				toastView.findViewById(R.id.dividerView);
		
			if(mSuperCardToastStyle != null)
			{
				
				dividerView.setBackgroundColor(mSuperCardToastStyle.dividerdrawableResource);
				
			}
			
			else
			{
				
				if(dividerDrawable != null)
				{
					
					if(sdkVersion < android.os.Build.VERSION_CODES.JELLY_BEAN) 
					{
												
						dividerView.setBackgroundDrawable(dividerDrawable);
						
					}
					
					else 
					{
						
						dividerView.setBackground(dividerDrawable);
					    
					}
					
				}
				
				else
				{
					
					dividerView.setBackgroundColor(dividerColor);

				}
				
			}			
	
	}
	

	public void createProgressCard(String messageText, final boolean isIndeterminate)
	{
				
		final LayoutInflater superundoLayoutInflater = (LayoutInflater) 
				mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        			
		toastView = superundoLayoutInflater
	    		.inflate(R.layout.supercardtoast_progress, mContainer, false);
	    
			if(sdkVersion > android.os.Build.VERSION_CODES.HONEYCOMB_MR1 && !disableSwipeDismiss) 
			{
				
		        final SwipeDismissListener touchListener = new SwipeDismissListener(toastView,
	                    new SwipeDismissListener.OnDismissCallback() {
							
							@Override
							public void onDismiss(View view) 
							{
	
								mContainer.removeView(toastView);
															
							}
						});
	
		        toastView.setOnTouchListener(touchListener);
				
			}
			
		
	    final TextView mTextView = (TextView) 
	    		toastView.findViewById(R.id.messageTextView);
	    
	    	if(mSuperCardToastStyle != null)
	    	{
	    		
	    		mTextView.setTextColor(mSuperCardToastStyle.messagecolorResource);
	    		
	    	}
	    	
		mTextView.setText(messageText);
		
		
		final ProgressBar progressBar = (ProgressBar) 
				toastView.findViewById(R.id.circleProgressBar);
		
		progressBar.setIndeterminate(isIndeterminate);
	   
		
	    final LinearLayout mRootLayout = (LinearLayout)
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
			
	}
	
	
	public void createToastCard(String messageText)
	{


		final LayoutInflater superundoLayoutInflater = (LayoutInflater) 
				mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		toastView = superundoLayoutInflater
	    		.inflate(R.layout.supercardtoast_toast, mContainer, false);
	    
			if(sdkVersion > android.os.Build.VERSION_CODES.HONEYCOMB_MR1 && !disableSwipeDismiss) 
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
			
		
			if(mIsTimed && duration > 0)
			{
				
				mHandler = new Handler();
				mHandler.postDelayed(mHideRunnable, duration);

			}
			
	
	    final TextView mTextView = (TextView) 
	    		toastView.findViewById(R.id.messageTextView);
	    
	    	if(mSuperCardToastStyle != null)
	    	{
	    		
	    		mTextView.setTextColor(mSuperCardToastStyle.messagecolorResource);
	    		
	    	}
	    	
	    	else
	    	{
	    		
	        	mTextView.setTextColor(textColor);
	    		
	    	}
	    	
		mTextView.setText(messageText);
		
		
	    final LinearLayout mRootLayout = (LinearLayout)
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
			
	
	}
	
	
	//Quick Navigation: Setter methods
	
	
	/**
	 * <b> public void setStyle(final SuperCardToastStyle mSuperCardToastStyle) </b>
	 * 
	 * 
	 * <p> This is used to set the style of the SuperCardToast. Please note: 
	 * 	   If you decide to use a default style than you are limited on the other
	 * 	   modifications you may make to the SuperButtonToast because the style 
	 * 	   already contains these components. </p>
	 * 
	 * <b> Parameter example: </b>
	 * 
	 * <p> (SuperCardToast.STYLE_UNDODARK) </p>
	 * 
	 *	 
	 */
	
	public void setStyle(final SuperCardToastStyle mSuperCardToastStyle)
	{
		
		this.mSuperCardToastStyle = mSuperCardToastStyle;
		
	}
	
	
	/**
	 * <b> public void setBackgroundResource(final int backgroundresource) </b>
	 * 
	 * 
	 * <p> This is used to set the background resource of the SuperCardToast.
	 * 	   Please note: You may also use a custom Drawable as a background via the 
	 *     setBackgroundDrawable() method. </p>
	 * 
	 * <b> Parameter example: </b>
	 * 
	 * <p> (SuperCardToast.BACKGROUND_STANDARDWHITE) </p>
	 * 
	 *	 
	 */
	
	public void setBackgroundResource(final int backgroundResource)
	{
		
		this.backgroundResource = backgroundResource;
		
	}
	
	
	/**
	 * <b> public void setBackgroundDrawable(final Drawable backgroundDrawable) </b>
	 * 
	 * 
	 * <p> This is used to set the background Drawable of the SuperCardToast.
	 * 	   Please note: You may also use the resources in this library as a background via the 
	 *     setBackgroundResource() method. </p>
	 *     
	 * <b> Design guide: </b>
	 * 
	 * <p> The Drawable supplied through this parameter should be nine-patch format. </p>
	 * 
	 *	 
	 */
	
	public void setBackgroundDrawable(final Drawable backgroundDrawable)
	{
		
		this.backgroundDrawable = backgroundDrawable;
		
	}
	
	
	/**
	 * <b> public void setDuration(final int mDuration) </b>
	 * 
	 * 
	 * <p> This is an optional feature used to set a duration the SuperCardToast.
	 *     You may use a custom duration for example (3000) for three seconds. </p>
	 * 
	 * <b> Parameter example: </b>
	 * 
	 * <p> (SuperCardToast.DURATION_SHORT) </p>
	 * 
	 * 
	 * <b> Design guide: </b>
	 * 
	 * <p> The duration should not exceed 10000 milliseconds. </p>
	 * 
	 *	 
	 */
	
	public void setDuration(final int duration)
	{
		
		this.mIsTimed = true;
		this.duration = duration;
		
	}
	
	
	

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

    
	
	public void show()
	{
		
		mContainer.setVisibility(View.VISIBLE);

		mContainer.addView(toastView);

		toastView.startAnimation(getCardAnimation());
		
			if(mIsTimed && duration > 0)
			{
				
				mHandler.postDelayed(mHideRunnable, duration);
				
			}

	}
	
	public void show(final Animation mAnimation)
	{
		
		mContainer.setVisibility(View.VISIBLE);
		
		mContainer.addView(toastView);
		
		toastView.startAnimation(mAnimation);
		
			if(mIsTimed && duration > 0)
			{
				
				mHandler.postDelayed(mHideRunnable, duration);
				
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

	

	
	
	
}