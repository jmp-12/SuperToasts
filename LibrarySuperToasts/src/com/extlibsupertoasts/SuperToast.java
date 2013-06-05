package com.extlibsupertoasts;


import com.extlibsupertoasts.utilities.SuperToastConstants;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

@SuppressLint("NewApi")
@SuppressWarnings("deprecation")

public class SuperToast
{
	
	private static final String ERROR_CONTEXTNULL= "The Context that you passed was null! (SuperToast)";
	
	
	public static final int ANIMATION_FADE = (android.R.style.Animation_Toast);
	public static final int ANIMATION_FLYIN = (android.R.style.Animation_Translucent);
	public static final int ANIMATION_SCALE = (android.R.style.Animation_Dialog);
	public static final int ANIMATION_POPUP = (android.R.style.Animation_InputMethod);
	
	
	private Context mContext;
	private LayoutInflater mLayoutInflater;
	private WindowManager mWindowManager;
	private View toastView;
	private TextView messageTextView;
	private Handler mHandler;
	private int sdkVersion = android.os.Build.VERSION.SDK_INT;;
	
		
	private CharSequence textCharSequence;
	private int textColor = Color.WHITE;
	private int backgroundResource = SuperToastConstants.BACKGROUND_BLACK;
	private int gravityInteger = Gravity.BOTTOM|Gravity.CENTER;
	private Drawable backgroundDrawable;
	private Typeface typeface = Typeface.DEFAULT;
	private int duration = SuperToastConstants.DURATION_SHORT;
	private int animationStyle = ANIMATION_FADE;
	private int xOffset = 0;
	private int yOffset = 0;
	private float textSize = SuperToastConstants.TEXTSIZE_SMALL;

	
	public SuperToast(Context mContext) 
	{
				
		if(mContext != null)
		{

			this.mContext = mContext;
				
			yOffset = mContext.getResources()
					.getDimensionPixelSize(R.dimen.toast_yoffset);
			
			mLayoutInflater = (LayoutInflater) 
					mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			toastView = mLayoutInflater
					.inflate(R.layout.supertoast, null);
				
			mWindowManager = (WindowManager)
					toastView.getContext().getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
											
		}
			
		else
		{
				
			throw new IllegalArgumentException(ERROR_CONTEXTNULL);
				
		}
		
	}

	
	
	public void show()
	{

		mHandler = new Handler();
		mHandler.postDelayed(hideToastRunnable, duration);
		
		
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

        final WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE 
        		       | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE 
        		       | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        
        params.format = PixelFormat.TRANSLUCENT;      
        params.windowAnimations = animationStyle;
        params.type = WindowManager.LayoutParams.TYPE_TOAST;
        params.gravity = (gravityInteger);
        params.x = xOffset;       
        params.y = yOffset;      
        
        mWindowManager.addView(toastView, params);
		
	}

	
	/**
	 * <b><i> public void setText(CharSequence textCharSequence) </i></b>
	 * 
	 * <p> This is used to set the text of the SuperToast message. </p>
	 * 
	 * 
	 * <b> Parameter example: </b>
	 * 	 
	 * <p> ("Hello, I am a SuperToast!") </p>
	 * 
	 * 
	 * <b> Important note: </b>
	 * 
	 * <p> This method can be called again while the SuperToast is showing to 
	 *     modify the existing message. If your application might show two SuperToasts
	 *     at one time you should try to reuse the same SuperToast by calling this method and
	 *     {@link #resetDuration(int)}. </p>
	 *     
	 *     
     * <b> Design guide: </b>
     *
	 * <p> Toasts/SuperToasts are designed to display short non-essential messages
	 *     such as "Message sent!" after the user sends a SMS. Generally these messages
	 *     should rarely take more than one line of text. </p>
	 * 
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
	 * <p> This is used to set the message text color of the SuperToast. </p>
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
	 *     text colors ever used. </p>
	 * 
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
	 * <p> This is used to set the duration of the SuperToast. </p>
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
	 * <p> This is used to reset the duration of the SuperToast 
	 *     while the SuperToast is still showing. </p>
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
	 *     to reuse the same SuperToast when two or more messages are necessary. </p>
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
	 * <b><i> public void setGravity(int gravityInteger) </i></b>
	 * 
	 * <p> This is used to set the Gravity of the SuperToast.  </p>
	 * 
	 * 	 
	 * <b> Parameter example: </b>
	 * 	 
	 * <p> (Gravity.TOP|Gravity.LEFT) </p>
	 * 
	 */
	public void setGravity(int gravityInteger)
	{

		this.gravityInteger = gravityInteger;

	}
	
	
	/**
	 * <b><i> setBackgroundResource(int backgroundID) </i></b>
	 * 
	 * <p> This is used to set the background resource of the SuperToast. </p>
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
	 * <p> This is used to set the background Drawable of the SuperToast.
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
	 * <b><i> public void setTextSize(int textSize) </i></b>
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
	public void setTextSize(int textSize)
	{

		this.textSize = textSize;
		
	}
	
	
	/**
	 * <b><i> public void setTypeface(Typeface mTypeface) </i></b>
	 * 
	 * <p> This is used to set the Typeface of the SuperToast text.	  </p>
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
	 * <p> (mSuperToast.loadRobotoTypeface(SuperToastConstants.FONT_ROBOTO_THIN);
	 *
	 */
	public void setTypeface(Typeface typeface)
	{
		
		this.typeface = typeface;
		
	}
	
	
	/**
	 * <b><i> public void setAnimation(int animationStyle) </i></b>
	 * 
	 * <p> This is used to set the Animation of the SuperToast message. You
	 *     can only use the Animations defined in this class, this is a limitation 
	 *     of Android not this library. </p>
	 * 
	 * 
	 * <b> Parameter example: </b>
     *	 
     * <p> (SuperToast.ANIMATION_FADE) </p>
	 *	 
	 */
	public void setAnimation(int animationStyle)
	{
		
		this.animationStyle = animationStyle;
		
	}
	
	
	/**
	 * <b><i> public void setXYCoordinates(int xCoordinate, int yCoordinate) </i></b>
	 * 
	 * <p> This is used to set the exact coordinates of the SuperToast message. </p>
	 *     
	 *     
	 * <b> Parameter example: </b>
	 * 	 
	 * <p> (50, 50) </p>
	 *	 
	 */
	public void setXYCoordinates(int xOffset, int yOffset)
	{
				
		this.xOffset = xOffset;
		this.yOffset = yOffset;

	}
	
	
	/**
	 * <b><i> public void dismiss() </i></b>
     *
	 * <p> This is used to hide and dispose of the SuperButtonToast. </p>
	 *
	 *
	 * <b> Design guide: </b>
	 * 
	 * <p> Treat your SuperToast like a Dialog, dismiss it when it is no longer
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

        if(toastView != null && mWindowManager != null)
        {

        	mWindowManager.removeView(toastView);
        		
            toastView = null;                		

        }
		
	}
	
	
	//Quick Navigation: Getter methods

	
	/**
	 * <b><i> public TextView getTextView() </i></b>
	 * 
	 * <p> This is used to get the TextView that displays the SuperToast message. </p>
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
	 * <b><i> public int getXOffset() </i></b>
	 * 
	 * <p> This is used to get the X offset value from the SuperToast message. </p>
	 * 
	 * 
	 * <b> Returns: </b>
	 * 	 
	 * <p> int </p>
	 * 
	 * 
	 * <b> Default value: </b>
	 * 	 
	 * <p> 0 </p>
	 * 
	 */
	public int getXOffset()
	{
		
		return this.xOffset;

	}
	
	
	/**
	 * <b><i> public int getYOffset() </i></b>
	 * 
	 * <p> This is used to get the Y offset value from the SuperToast message.	  </p>
	 * 
	 * 
	 * <b> Returns: </b>
	 * 	 
	 * <p> int </p>
	 * 
	 * 
	 * <b> Default value: </b>
	 * 	 
	 * <p> 50 </p>
	 * 
	 */
	public int getYOffset()
	{
		
		return this.yOffset;

	}
	
	
	/**
	 * <b><i> public View getView() </i></b>
	 * 
	 * <p> This is used to get the SuperToast View.	  </p>
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
	 *     The link to download the Roboto fonts is included in this library as a text file. 
	 *     Do not modify the names of these fonts. </p>
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



	private Runnable hideToastRunnable = new Runnable() 
	{
		 
        public void run() 
        {
        	        	
        	dismiss();
	 
        }        
    };
    
    
    //Quick Navigation: Static methods.
	
    
	/**
	 * <b><i> public static SuperToast createDarkSuperToast(Context context, CharSequence textCharSequence, int durationInteger) </i></b>
	 * 
	 * <p> Creates a dark theme SuperToast. Don't forget to call {@link #show()}. </p>
	 *	 
	 */
    public static SuperToast createDarkSuperToast(Context context, CharSequence textCharSequence, int durationInteger)
    {
    	
    	SuperToast mSuperToast = new SuperToast(context);
    	mSuperToast.setText(textCharSequence);
    	mSuperToast.setDuration(durationInteger);
    	
		return mSuperToast;
    	   
    }
    
    
	/**
	 * <b><i> public static SuperToast createLightSuperToast(Context context, CharSequence textCharSequence, int durationInteger) </i></b>
	 * 
	 * <p> Creates a light theme SuperToast. Don't forget to call {@link #show()}. </p>
	 *	 
	 */
    public static SuperToast createLightSuperToast(Context context, CharSequence textCharSequence, int durationInteger)
    {
    	
    	SuperToast mSuperToast = new SuperToast(context);
    	mSuperToast.setText(textCharSequence);
    	mSuperToast.setDuration(durationInteger);
    	mSuperToast.setBackgroundResource(SuperToastConstants.BACKGROUND_WHITE);
    	mSuperToast.setTextColor(Color.BLACK);

		return mSuperToast;
    	    	
    }
    
    
	/**
	 * <b><i> public static SuperToast createDarkSuperToast(Context context, CharSequence textCharSequence, int durationInteger) </i></b>
	 * 
	 * <p> Creates a dark theme SuperToast with an option to choose an Animation. Don't forget to call {@link #show()}. </p>
	 *	 
	 */
    public static SuperToast createDarkSuperToast(Context context, CharSequence textCharSequence, int durationInteger, int animation)
    {
    	
    	SuperToast mSuperToast = new SuperToast(context);
    	mSuperToast.setText(textCharSequence);
    	mSuperToast.setDuration(durationInteger);
    	mSuperToast.setAnimation(animation);

		return mSuperToast;
    	   
    }
    
    
	/**
	 * <b><i> public static SuperToast createLightSuperToast(Context context, CharSequence textCharSequence, int durationInteger) </i></b>
	 * 
	 * <p> Creates a light theme SuperToast with an option to choose an Animation. Don't forget to call {@link #show()}. </p>
	 *	 
	 */
    public static SuperToast createLightSuperToast(Context context, CharSequence textCharSequence, int durationInteger, int animation)
    {
    	
    	SuperToast mSuperToast = new SuperToast(context);
    	mSuperToast.setText(textCharSequence);
    	mSuperToast.setDuration(durationInteger);
    	mSuperToast.setBackgroundResource(SuperToastConstants.BACKGROUND_WHITE);
    	mSuperToast.setTextColor(Color.BLACK);
    	mSuperToast.setAnimation(animation);

		return mSuperToast;
    	    	
    }
    
}



