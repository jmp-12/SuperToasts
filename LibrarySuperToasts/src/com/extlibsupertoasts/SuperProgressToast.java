package com.extlibsupertoasts;


import com.extlibsupertoasts.utilities.SuperToastConstants;

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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

@SuppressLint("NewApi")
@SuppressWarnings("deprecation")

public class SuperProgressToast
{
	
	private static final String ERROR_CONTEXTNULL= "The Context that you passed was null! (SuperProgressToast)";
	private static final String ERROR_CONTEXTNOTACTIVITY= "The Context that you passed was not an Activity! (SuperProgressToast)";
	
	
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

		
	
	public SuperProgressToast(Context mContext, boolean isHorizontal) 
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
				
				if(isHorizontal)
				{
					
					toastView = mLayoutInflater
							.inflate(R.layout.superhorizontalprogresstoast, mViewGroup, false);
				}
				
				else
				{
					
					
					toastView = mLayoutInflater
							.inflate(R.layout.supercircleprogresstoast, mViewGroup, false);
				}
				
			}
			
			else
			{
				
				throw new IllegalArgumentException(ERROR_CONTEXTNOTACTIVITY);

			}
				
		}
			
		else
		{
				
			throw new IllegalArgumentException(ERROR_CONTEXTNULL);
				
		}
		
	}

	
	
	public void show()
	{
		
		mProgressBar = (ProgressBar)
				toastView.findViewById(R.id.progressBar);
		
		
		if(isIndeterminate)
		{

			mProgressBar.setIndeterminate(true);
			
		}
		
		
		if(mOnClickListener != null)
		{
				
			toastView.setOnClickListener(mOnClickListener);
				
		}
		
		if(touchDismiss || touchImmediateDismiss)
		{
			
			if(touchDismiss)
			{
				
				toastView.setOnTouchListener(mTouchDismissListener);
				
			}
			
			else if(touchImmediateDismiss)
			{
				
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
		
			
		mViewGroup.addView(toastView);

		toastView.startAnimation(showAnimation);		    
		
	}
	
	
	
	/**
	 * <b><i> public void setText(CharSequence textCharSequence) </i></b>
	 * 
	 * <p> This is used to set the message text of the SuperProgressToast. </p>
	 * 
	 * 
	 * <b> Parameter example: </b>
	 * 	 
	 * <p> ("Hello, I am a SuperProgressToast!") </p>
	 * 
	 * 
	 * <b> Important note: </b>
	 * 
	 * <p> This method can should be called after each progress update. </p>
	 *     
	 *     
     * <b> Design guide: </b>
     *
	 * <p> SuperProgressToasts are designed to mimic ProgressDialogs but without
	 *     the UI blocking behavior of ProgressDialogs. Generally you should 
	 *     display progress in the ActionBar. </p>
	 * 
	 */
	public void setText(CharSequence textCharSequence)
	{

		this.textCharSequence = textCharSequence;
		
		if(messageTextView != null)
		{
			
			messageTextView.setText(textCharSequence);
			
		}
		
	}
	
	
	/**
	 * <b><i> public void setProgress(int progress) </i></b>
	 * 
	 * <p> This is used to set the progress integer of the SuperProgressToast. </p>
	 * 
	 * 
	 * <b> Parameter example: </b>
	 * 	 
	 * <p> (45) </p>
	 * 
	 * 
	 * <b> Important note: </b>
	 * 
	 * <p> This method should be used with a horizontal SuperProgressToast to display visual
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


	
	/**
	 * <b><i> public void setTextColor(int textColor) </i></b>
	 * 
	 * <p> This is used to set the message text color of the SuperProgressToast. </p>
	 * 
	 * 
	 * <b> Parameter example: </b>
	 * 	 
	 * <p> (Color.CYAN) </p>
	 * 
	 * 
	 * <b> Design guide: </b>
     *
	 * <p> The text color you select should contrast the background color. 
	 *     Generally Color.WHITE and Color.BLACK should be the only two 
	 *     text colors used. </p>
	 *	 
	 */
	public void setTextColor(int textColor)
	{

		this.textColor = textColor;
		
		if(messageTextView != null)
		{
			
			messageTextView.setTextColor(textColor);
			
		}
	}

	
	
	/**
	 * <b><i> public void setIndeterminate(boolean isIndeterminate) </i></b>
	 * 
	 * 
	 * <p> This is used to set an indeterminate value to the SuperProgressToast.
	 *     This behavior is similar to indeterminate ProgressDialogs. </p>
	 * 
	 * 
	 * <b> Parameter example: </b>
	 * 	 
	 * <p> (true) </p>
	 * 
	 * 
	 * <b> Design guide: </b>
	 * 
	 * <p> This function should be used when the background operation is relatively short. </p>
	 * 
	 */
	public void setIndeterminate(boolean isIndeterminate)
	{

		this.isIndeterminate = isIndeterminate;
		
	}
	
	
	/**
	 * <b><i> public void setOnClickListener(OnClickListener mOnClickListener) </i></b>
	 * 
	 * 
	 * <p> This is used to attach an OnClickListener to the SuperProgressToast. </p>
	 * 
	 * 
	 * <b> Parameter example: </b>
	 * 	 
	 * <p> (mOnClickListener) </p>
	 * 
	 * 
	 * <b> Design guide: </b>
	 * 
	 * <p> This function should contain {@link #dismiss()}. </p>
	 * 
	 */
	public void setOnClickListener(OnClickListener mOnClickListener)
	{

		this.mOnClickListener = mOnClickListener;
		
	}
	
	
	/**
	 * <b><i> setBackgroundResource(int backgroundID) </i></b>
	 * 
	 * <p> This is used to set the background resource of the SuperProgressToast. </p>
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
	 * <p> This is used to set the background Drawable of the SuperProgressToast.
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
	 * <p> This is used to set the text size of the SuperProgressToast message. </p>
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
	 * <p> This is used to set the Typeface of the SuperProgressToast text.	  </p>
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
	 * <p> (mSuperProgressToast.loadRobotoTypeface(SuperProgressToast.FONT_ROBOTO_THIN);
	 *
	 */
	public void setTypeface(Typeface typeface)
	{
		
		this.typeface = typeface;
		
	}
		
	
	/**
	 * <b><i> public void setShowAnimation(Animation showAnimation) </i></b>
	 * 
	 * <p> This is used to set the opening Animation of the SuperProgressToast. </p>
     *
     *
     * <b> Design guide: </b>
	 * 
	 * <p> The Animation you supply here should be simple and not exceed 500 milliseconds.</p>
	 * 
	 */
	public void setShowAnimation(Animation showAnimation)
	{
		
		this.showAnimation = showAnimation;
		
	}
	
	
	/**
	 * <b><i> public void setDismissAnimation(Animation dismissAnimation) </i></b>
	 * 
	 * <p> This is used to set the dismiss Animation of the SuperProgressToast.
	 *     
	 *     
	 * <b> Design guide: </b>
	 * 
	 * <p> The Animation you supply here should be simple and not exceed 500 milliseconds.</p>
     * 
	 */
	public void setDismissAnimation(Animation dismissAnimation)
	{
		
		this.dismissAnimation = dismissAnimation;
		
	}
	
	
	/**
	 * <b><i> public void setTouchToDismiss(boolean touchDismiss) </i></b>
	 * 
	 * <p> This is used to set a private OnTouchListener to the SuperProgressToast
	 *     which will call {@link #dismiss()} if the user touches the SuperProgressToast.

	 *      
	 * <b> Design guide: </b>
	 * 
	 * <p> Using this method can be a good idea for long running SuperProgressToasts. </p>
     * 
	 */
	public void setTouchToDismiss(boolean touchDismiss)
	{
		
		this.touchDismiss = touchDismiss;
		
	}
	
	
	/**
	 * <b><i> public void setTouchToImmediateDismiss(boolean touchImmediateDismiss) </i></b>
	 * 
	 * <p> This is used to set a private OnTouchListener to the SuperProgressToast
	 *     which will call {@link #dismissImmediately()} if the user touches the SuperProgressToast.

	 *      
	 * <b> Design guide: </b>
	 * 
	 * <p> Using this method can be a good idea for long running SuperProgressToasts. </p>
     * 
	 */
	public void setTouchToImmediateDismiss(boolean touchImmediateDismiss)
	{
		
		this.touchImmediateDismiss = touchImmediateDismiss;
		
	}
	
	
	/**
	 * <b><i> public void dismiss() </i></b>
     *
	 * <p> This is used to hide and dispose of the SuperProgressToast. </p>
	 *
	 *
	 * <b> Design guide: </b>
	 * 
	 * <p> Treat your SuperProgressToast like a Dialog, dismiss it when it is no longer
	 *     relevant. </p>
	 *	 
	 */
	public void dismiss()
	{
		
        if(toastView != null && mViewGroup != null) 
        {
        		
        	toastView.startAnimation(dismissAnimation);

        	mViewGroup.removeView(toastView);
        		
            toastView = null;                		

        }
   		
	}
	
	
	/**
	 * 
	 * <b><i> public void dismissImmediately() </i></b>
	 * 
	 * <p> This is used to hide and dispose of the SuperProgressToast.
	 *     immediately (without showing an exit Animation). </p>
	 *	 
	 */
	public void dismissImmediately()
	{
		
        if(toastView != null && mViewGroup != null) 
        {
        		
        	mViewGroup.removeView(toastView);
        		
            toastView = null;                		

        }
   		
	}
	
	
	//Quick Navigation: Getter methods. 
	
	
	/**
	 * <b><i> public TextView getTextView() </i></b>
	 * 
	 * <p> This is used to get the TextView that displays the SuperProgressToast text. </p>
	 * 
	 * 
	 * <b> Returns: </b>
	 * 	 
	 * <p> TextView </p>
	 * 
	 * 
	 * <b> Default value: </b>
	 * 	 
	 * <p> null </p>
	 *	 
	 */
	public TextView getTextView()
	{
		
		return messageTextView;

	}
	
	
	/**
	 * <b><i> public View getView() </i></b>
	 * 
	 * <p> This is used to get the SuperProgressToast View.	  </p>
	 * 
	 * 
	 * <b> Returns: </b>
	 * 	 
	 * <p> View </p>
	 * 
	 * 
	 * <b> Default value: </b>
	 * 	 
	 * <p> null </p>
	 *	 
	 */
	public View getView()
	{
		
		return toastView;

	}

	
	/**
	 * <b><i> public View getProgressBar() </i></b>
	 * 
	 * <p> This is used to get the SuperProgressToast ProgressBar.	  </p>
	 * 
	 * 
	 * <b> Returns: </b>
	 * 	 
	 * <p> ProgressBar </p>
	 * 
	 * 
	 * <b> Default value: </b>
	 * 	 
	 * <p> null </p>
	 *	 
	 */
	public View getProgressBar()
	{
		
		return mProgressBar;

	}
	
	
	//Quick Navigation: Utility methods

	
	/**
	 * <b><i> public Typeface loadRobotoTypeface(String typeface) </i></b>
	 * 
	 * <p> This is used to load a Roboto Typeface. You <b><i>MUST</i></b>
	 *     put the desired font file in the assets folder of your project.
	 *     The Roboto fonts are included in this library as a zip file. Do not modify the
	 *     names of these fonts. </p>
	 * 
	 * 
	 * <b> Returns: </b>
	 * 	 
	 * <p> Typeface </p>
	 * 
	 * 
	 * <b> Parameter example: </b>
     *	 
     * <p> (SuperProgressToast.FONT_ROBOTO_THIN) </p>
	 * 
	 * 
	 * <b> Default value: </b>
	 * 	 
	 * <p> null </p>
	 * 
	 */
	public Typeface loadRobotoTypeface(String typefaceString)
	{
		
		return Typeface.createFromAsset(mContext.getAssets(), typefaceString);

	}
	
	
	/**
	 * <b><i> public boolean isShowing() </i></b>
	 * 
	 * <p> This returns true if the SuperProgressToast is currently
	 *     visible to the user. </p>
	 * 
	 * 
	 * <b> Returns: </b>
	 * 	 
	 * <p> boolean </p>
	 * 
	 * 
	 * <b> Default value: </b>
	 * 	 
	 * <p> false </p>
	 * 
	 */
	public boolean isShowing()
	{
		
		if(toastView != null)
		{
			
			return toastView.isShown();
			
		}
		
		else
		{
			
			return false;
			
		}
				
	}
	

	//Quick Navigation: Private methods.
    
	
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
	
	private OnTouchListener mTouchDismissListener = new OnTouchListener()
	{

		@Override
		public boolean onTouch(View view, MotionEvent event) 
		{
			
			dismiss();
			
			return false;
			
		}
		
	};
	
	private OnTouchListener mTouchImmediateDismissListener = new OnTouchListener()
	{

		@Override
		public boolean onTouch(View view, MotionEvent event) 
		{
			
			dismissImmediately();
			
			return false;
			
		}
		
	};
    
}



