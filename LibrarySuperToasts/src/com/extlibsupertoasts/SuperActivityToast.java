package com.extlibsupertoasts;


import com.extlibsupertoasts.utilities.SuperToastConstants;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Handler;
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
import android.widget.TextView;

@SuppressLint("NewApi")
@SuppressWarnings("deprecation")

public class SuperActivityToast
{
	
	private static final String ERROR_CONTEXTNULL= "The Context that you passed was null! (SuperActivityToast)";
	private static final String ERROR_CONTEXTNOTACTIVITY= "The Context that you passed was not an Activity! (SuperActivityToast)";

	
	private Context mContext;
	private LayoutInflater mLayoutInflater;
	private ViewGroup mViewGroup;
	private View toastView;
	private TextView messageTextView;
	private Handler mHandler;
	private int sdkVersion = android.os.Build.VERSION.SDK_INT;;
	
		
	private CharSequence textCharSequence;
	private int textColor = Color.WHITE;
	private int backgroundResource = SuperToastConstants.BACKGROUND_BLACK;
	private Drawable backgroundDrawable;
	private Typeface typeface = Typeface.DEFAULT;
	private int duration = SuperToastConstants.DURATION_SHORT;
	private float textSize = SuperToastConstants.TEXTSIZE_SMALL;
	private boolean isIndeterminate;
	private OnClickListener mOnClickListener;
	private Animation showAnimation = getFadeInAnimation();
	private Animation dismissAnimation = getFadeOutAnimation();
	private boolean touchDismiss;
	private boolean touchImmediateDismiss;


	
	public SuperActivityToast(Context mContext) 
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
				
				toastView = mLayoutInflater
						.inflate(R.layout.supertoast, mViewGroup, false);
				
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
		
		if(!isIndeterminate)
		{
			
			mHandler = new Handler();
			mHandler.postDelayed(hideToastRunnable, duration);
			
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
	 *     
     * <b> Design guide: </b>
     *
	 * <p> Toasts/SuperActivityToasts are designed to display short non-essential messages
	 *     such as "Message sent!" after the user sends a SMS. Generally these messages
	 *     should rarely display more than one line of text. </p>
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
	 * <b><i> public void setTextColor(int textColor) </i></b>
	 * 
	 * <p> This is used to set the message text color of the SuperActivityToast. </p>
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
	 * <b><i> public void setDuration(int duration) </i></b>
	 * 
	 * <p> This is used to set the duration of the SuperActivityToast. </p>
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
	 * <b><i> public void resetDuration(int newDuration) </i></b>
	 * 
	 * <p> This is used to reset the duration of the SuperActivityToast 
	 *     while the SuperActivityToast is still showing. </p>
	 * 
	 * 
	 * <b> Parameter example: </b>
	 * 	 
	 * <p> (SuperToastConstants.DURATION_SHORT) </p>
	 * 
	 *	 
	 * <b> Design guide: </b>
     *
	 * <p> This method should be used in collaboration with {@link #setText(CharSequence)} 
	 *     to reuse the same SuperActivityToast when two or more messages are necessary. </p>
	 *     
	 */
	public void resetDuration(int newDuration)
	{
		
		if(mHandler != null)
		{
			
			mHandler.removeCallbacks(hideToastRunnable);
			mHandler = null;

		}
		
		mHandler = new Handler();
		mHandler.postDelayed(hideToastRunnable, newDuration);
		
	}
	
	
	/**
	 * <b><i> public void setIndeterminate(boolean isIndeterminate) </i></b>
	 * 
	 * 
	 * <p> This is used to set an indeterminate value to the SuperActivityToast.
	 *     This will force the SuperActivityToast to ignore any duration set and 
	 *     {@link #dismiss()} must be called to get rid of the SuperActivityToast. </p>
	 * 
	 * 
	 * <b> Parameter example: </b>
	 * 	 
	 * <p> (true) </p>
	 * 
	 * 
	 * <b> Design guide: </b>
	 * 
	 * <p> This function should only be used in very rare cases. </p>
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
	 * <p> This is used to attach an OnClickListener to the SuperActivityToast. </p>
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
	 * <p> This is used to set the background resource of the SuperActivityToast. </p>
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
	 * <p> This is used to set the background Drawable of the SuperActivityToast.
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
	 * <p> This is used to set the text size of the SuperActivityToast message. </p>
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
	 * <p> This is used to set the Typeface of the SuperActivityToast text.	  </p>
	 * 
	 * 
	 * <b> Important note: </b>
	 * 
	 * <p> This library comes with all of the variations of the 
	 *     Roboto font in a zip file. To use the fonts see 
	 *     {@link #loadRobotoTypeface(String)}.
	 * 
	 * 
	 * <b> Parameter example: </b>
	 * 	 
	 * <p> (Typeface.DEFAULT) </p>
	 * 
	 * <b> OR </b>
	 * 
	 * <p> (mSuperActivityToast.loadRobotoTypeface(SuperActivityToast.FONT_ROBOTO_THIN);
	 *
	 */
	public void setTypeface(Typeface typeface)
	{
		
		this.typeface = typeface;
		
	}
		
	
	/**
	 * <b><i> public void setShowAnimation(Animation showAnimation) </i></b>
	 * 
	 * <p> This is used to set the opening Animation of the SuperActivityToast. </p>
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
	 * <p> This is used to set the dismiss Animation of the SuperActivityToast.
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
	 * <p> This is used to set a private OnTouchListener to the SuperActivityToast
	 *     which will call {@link #dismiss()} if the user touches the SuperActivityToast.

	 *      
	 * <b> Design guide: </b>
	 * 
	 * <p> Using this method can be a good idea for long running SuperActivityToasts. </p>
     * 
	 */
	public void setTouchToDismiss(boolean touchDismiss)
	{
		
		this.touchDismiss = touchDismiss;
		
	}
	
	
	/**
	 * <b><i> public void setTouchToImmediateDismiss(boolean touchImmediateDismiss) </i></b>
	 * 
	 * <p> This is used to set a private OnTouchListener to the SuperActivityToast
	 *     which will call {@link #dismissImmediately()} if the user touches the SuperActivityToast.

	 *      
	 * <b> Design guide: </b>
	 * 
	 * <p> Using this method can be a good idea for long running SuperActivityToasts. </p>
     * 
	 */
	public void setTouchToImmediateDismiss(boolean touchImmediateDismiss)
	{
		
		this.touchImmediateDismiss = touchImmediateDismiss;
		
	}
	
	/**
	 * <b><i> public void dismiss() </i></b>
     *
	 * <p> This is used to hide and dispose of the SuperActivityToast. </p>
	 *
	 *
	 * <b> Design guide: </b>
	 * 
	 * <p> Treat your SuperActivityToast like a Dialog, dismiss it when it is no longer
	 *     relevant. </p>
	 *	 
	 */
	public void dismiss()
	{
		
		if(mHandler != null)
		{
			
			mHandler.removeCallbacks(hideToastRunnable);
			
			mHandler = null;

		}
		
		
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
	 * <p> This is used to hide and dispose of the SuperActivityToast.
	 *     immediately (without showing an exit Animation). </p>
	 *	 
	 */
	public void dismissImmediately()
	{
		
		if(mHandler != null)
		{
			
			mHandler.removeCallbacks(hideToastRunnable);
			
			mHandler = null;

		}
		
		
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
	 * <p> This is used to get the TextView that displays the SuperActivityToast text. </p>
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
	 * <p> This is used to get the SuperActivityToast View.	  </p>
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

	
	//Quick Navigation: Utility methods

	
	/**
	 * <b><i> public Typeface loadRobotoTypeface(String typeface) </i></b>
	 * 
	 * <p> This is used to load a Roboto Typeface. You <b><i>MUST</i></b>
	 *     put the desired font file in the assets folder of your project.
	 *     The link to download the Roboto fonts is included in this library as a text file. Do not modify the
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
     * <p> (SuperToastConstants.FONT_ROBOTO_THIN) </p>
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
	 * <p> This returns true if the SuperActivityToast is currently
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
	

	private Runnable hideToastRunnable = new Runnable() 
	{
		 
        public void run() 
        {
        	        	
        	dismiss();
	 
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
    
    
    //Quick Navigation: Static methods.
	
	
	/**
	 * <b><i> public static SuperActivityToast createDarkSuperActivityToast(Context context, CharSequence textCharSequence, int durationInteger) </i></b>
	 * 
	 * <p> Creates a dark theme SuperActivityToast. Don't forget to call {@link #show()}. </p>
	 *	 
	 */
    public static SuperActivityToast createDarkSuperActivityToast(final Context context, final CharSequence textCharSequence, final int durationInteger)
    {
    	
    	final SuperActivityToast mSuperToast = new SuperActivityToast(context);
    	mSuperToast.setText(textCharSequence);
    	mSuperToast.setDuration(durationInteger);
    	
		return mSuperToast;
    	   
    }
    
    
	/**
	 * <b><i> public static SuperActivityToast createLightSuperActivityToast(Context context, CharSequence textCharSequence, int durationInteger) </i></b>
	 * 
	 * <p> Creates a light theme SuperActivityToast. Don't forget to call {@link #show()}. </p>
	 *	 
	 */
    public static SuperActivityToast createLightSuperActivityToast(final Context context, final CharSequence textCharSequence, final int durationInteger)
    {
    	
    	final SuperActivityToast mSuperToast = new SuperActivityToast(context);
    	mSuperToast.setText(textCharSequence);
    	mSuperToast.setDuration(durationInteger);
    	mSuperToast.setBackgroundResource(SuperToastConstants.BACKGROUND_WHITE);
    	mSuperToast.setTextColor(Color.BLACK);

		return mSuperToast;
    	    	
    }
    
    
	/**
	 * <b><i> public static SuperActivityToast createDarkSuperActivityToast(Context context, CharSequence textCharSequence, int durationInteger) </i></b>
	 * 
	 * <p> Creates a dark theme SuperActivityToast with an option for an OnClickListener. Don't forget to call {@link #show()}. </p>
	 *	 
	 */   
    public static SuperActivityToast createDarkSuperActivityToast(Context context, CharSequence textCharSequence, int durationInteger, 
    		OnClickListener mOnClickListener)
    {
    	
    	final SuperActivityToast mSuperToast = new SuperActivityToast(context);
    	mSuperToast.setText(textCharSequence);
    	mSuperToast.setDuration(durationInteger);
    	mSuperToast.setOnClickListener(mOnClickListener);
    	
		return mSuperToast;
    	   
    }
    
    
	/**
	 * <b><i> public static SuperActivityToast createLightSuperActivityToast(Context context, CharSequence textCharSequence, int durationInteger) </i></b>
	 * 
	 * <p> Creates a light theme SuperActivityToast with an option for an OnClickListener. Don't forget to call {@link #show()}. </p>
	 *	 
	 */   
    public static SuperActivityToast createLightSuperActivityToast(Context context, CharSequence textCharSequence, int durationInteger,
    		OnClickListener mOnClickListener)
    {
    	
    	final SuperActivityToast mSuperToast = new SuperActivityToast(context);
    	mSuperToast.setText(textCharSequence);
    	mSuperToast.setDuration(durationInteger);
    	mSuperToast.setBackgroundResource(SuperToastConstants.BACKGROUND_WHITE);
    	mSuperToast.setTextColor(Color.BLACK);
    	mSuperToast.setOnClickListener(mOnClickListener);

		return mSuperToast;
    	    	
    }
    
}



