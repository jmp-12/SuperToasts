package com.extlibsupertoasts;


import com.extlibsupertoasts.styles.SuperButtonToastStyle;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SuperButtonToast
{	
	   
	public static final SuperButtonToastStyle STYLE_EDITDARK = new SuperButtonToastStyle
			(com.extlibsupertoasts.R.drawable.icon_dark_edit, "EDIT", (Color.WHITE), (Color.WHITE), 
					(com.extlibsupertoasts.R.drawable.background_black), (Color.WHITE));
	
	public static final SuperButtonToastStyle STYLE_EXITDARK = new SuperButtonToastStyle
			(com.extlibsupertoasts.R.drawable.icon_dark_exit, "EXIT", (Color.WHITE), (Color.WHITE), 
					(com.extlibsupertoasts.R.drawable.background_black), (Color.WHITE));
	
	public static final SuperButtonToastStyle STYLE_INFODARK = new SuperButtonToastStyle
			(com.extlibsupertoasts.R.drawable.icon_dark_info, "INFO", (Color.WHITE), (Color.WHITE), 
					(com.extlibsupertoasts.R.drawable.background_black), (Color.WHITE));
	
	public static final SuperButtonToastStyle STYLE_REDODARK = new SuperButtonToastStyle
			(com.extlibsupertoasts.R.drawable.icon_dark_redo, "REDO", (Color.WHITE), (Color.WHITE), 
					(com.extlibsupertoasts.R.drawable.background_black), (Color.WHITE));
	
	public static final SuperButtonToastStyle STYLE_SAVEDARK = new SuperButtonToastStyle
			(com.extlibsupertoasts.R.drawable.icon_dark_save, "SAVE", (Color.WHITE), (Color.WHITE), 
					(com.extlibsupertoasts.R.drawable.background_black), (Color.WHITE));
	
	public static final SuperButtonToastStyle STYLE_SHAREDARK = new SuperButtonToastStyle
			(com.extlibsupertoasts.R.drawable.icon_dark_share, "SHARE", (Color.WHITE), (Color.WHITE), 
					(com.extlibsupertoasts.R.drawable.background_black), (Color.WHITE));
	
	public static final SuperButtonToastStyle STYLE_UNDODARK = new SuperButtonToastStyle
			(com.extlibsupertoasts.R.drawable.icon_dark_undo, "UNDO", (Color.WHITE), (Color.WHITE), 
					(com.extlibsupertoasts.R.drawable.background_black), (Color.WHITE));
	
	
	public static final SuperButtonToastStyle STYLE_EDITLIGHT = new SuperButtonToastStyle
			(com.extlibsupertoasts.R.drawable.icon_light_edit, "EDIT", (Color.DKGRAY), (Color.DKGRAY), 
					(com.extlibsupertoasts.R.drawable.background_white), (Color.DKGRAY));
	
	public static final SuperButtonToastStyle STYLE_EXITLIGHT = new SuperButtonToastStyle
			(com.extlibsupertoasts.R.drawable.icon_light_exit, "EXIT", (Color.DKGRAY), (Color.DKGRAY), 
					(com.extlibsupertoasts.R.drawable.background_white), (Color.DKGRAY));
	
	public static final SuperButtonToastStyle STYLE_INFOLIGHT = new SuperButtonToastStyle
			(com.extlibsupertoasts.R.drawable.icon_light_info, "INFO", (Color.DKGRAY), (Color.DKGRAY), 
					(com.extlibsupertoasts.R.drawable.background_white), (Color.DKGRAY));
	
	public static final SuperButtonToastStyle STYLE_REDOLIGHT = new SuperButtonToastStyle
			(com.extlibsupertoasts.R.drawable.icon_light_redo, "REDO", (Color.DKGRAY), (Color.DKGRAY), 
					(com.extlibsupertoasts.R.drawable.background_white), (Color.DKGRAY));
	
	public static final SuperButtonToastStyle STYLE_SAVELIGHT = new SuperButtonToastStyle
			(com.extlibsupertoasts.R.drawable.icon_light_save, "SAVE", (Color.DKGRAY), (Color.DKGRAY), 
					(com.extlibsupertoasts.R.drawable.background_white), (Color.DKGRAY));
	
	public static final SuperButtonToastStyle STYLE_SHARELIGHT = new SuperButtonToastStyle
			(com.extlibsupertoasts.R.drawable.icon_light_share, "SHARE", (Color.DKGRAY), (Color.DKGRAY), 
					(com.extlibsupertoasts.R.drawable.background_white), (Color.DKGRAY));
	
	public static final SuperButtonToastStyle STYLE_UNDOLIGHT = new SuperButtonToastStyle
			(com.extlibsupertoasts.R.drawable.icon_light_undo, "UNDO", (Color.DKGRAY), (Color.DKGRAY), 
					(com.extlibsupertoasts.R.drawable.background_white), (Color.DKGRAY));
	
	
	public static final int BACKGROUND_BLACK = (com.extlibsupertoasts.R.drawable.background_black);
	public static final int BACKGROUND_WHITE = (com.extlibsupertoasts.R.drawable.background_white);
	public static final int BACKGROUND_ORANGE = (com.extlibsupertoasts.R.drawable.background_orange);
	public static final int BACKGROUND_PURPLE = (com.extlibsupertoasts.R.drawable.background_purple);
	public static final int BACKGROUND_RED = (com.extlibsupertoasts.R.drawable.background_red);
	public static final int BACKGROUND_HOLOBLUE = (com.extlibsupertoasts.R.drawable.background_holoblue);

	
	public static final int BUTTON_DARK_EDIT = (com.extlibsupertoasts.R.drawable.icon_dark_edit);
	public static final int BUTTON_DARK_EXIT = (com.extlibsupertoasts.R.drawable.icon_dark_exit);	
	public static final int BUTTON_DARK_INFO = (com.extlibsupertoasts.R.drawable.icon_dark_info);
	public static final int BUTTON_DARK_REDO = (com.extlibsupertoasts.R.drawable.icon_dark_redo);
	public static final int BUTTON_DARK_SAVE = (com.extlibsupertoasts.R.drawable.icon_dark_save);
	public static final int BUTTON_DARK_SHARE = (com.extlibsupertoasts.R.drawable.icon_dark_share);
	public static final int BUTTON_DARK_UNDO = (com.extlibsupertoasts.R.drawable.icon_dark_undo);

	public static final int BUTTON_LIGHT_EDIT = (com.extlibsupertoasts.R.drawable.icon_light_edit);
	public static final int BUTTON_LIGHT_EXIT = (com.extlibsupertoasts.R.drawable.icon_light_exit);
	public static final int BUTTON_LIGHT_INFO = (com.extlibsupertoasts.R.drawable.icon_light_info);
	public static final int BUTTON_LIGHT_REDO = (com.extlibsupertoasts.R.drawable.icon_light_redo);
	public static final int BUTTON_LIGHT_SAVE = (com.extlibsupertoasts.R.drawable.icon_light_save);
	public static final int BUTTON_LIGHT_SHARE = (com.extlibsupertoasts.R.drawable.icon_light_share);
	public static final int BUTTON_LIGHT_UNDO = (com.extlibsupertoasts.R.drawable.icon_light_undo);
	
	
	public static final int DURATION_SHORT = (3000);
	public static final int DURATION_MEDIUM = (4500);
	public static final int DURATION_LONG = (6500);

private int ff;

	private SuperButtonToastStyle mSuperButtonToastStyle;
	
	private Context mContext;
	private View mView;
	private ViewGroup mViewGroup;
	
	
	private Handler mHandler = new Handler();;

	private SuperButtonToastCallback mSuperButtonToastCallback;
	
	private OnClickListener mOnClickListener;

	private LinearLayout mRootLayout;
	private int backgroundResource = (R.drawable.background_white);
	private Drawable backgroundDrawable;

	
	private View undodividerView;
	private int dividerColor = (R.color.black);
	private Drawable dividerDrawable;

	
	private int durationInteger = 5000;
	private boolean setIndeterminate;

	
	private Animation animationIn = getFadeInAnimation();
	private Animation animationOut = getFadeOutAnimation();
	
	private TextView mTextView;
	private CharSequence messageText = "Default text.";
	private int messageTextColor = Color.BLACK;
	private float messageTextSize;
	private Typeface messageTextTypeface = Typeface.DEFAULT;

	private Button mButton;
	private int buttonimageResource = BACKGROUND_WHITE;
	private Drawable buttonimageDrawable;
	private CharSequence buttonText = "UNDO";
	private int buttonTextColor = Color.BLACK;
	private float buttonTextSize;
	private Typeface buttonTextTypeface = Typeface.DEFAULT_BOLD;

		
	
	public interface SuperButtonToastCallback
	{
		
		public void onSuperButtonToastClick();
		
	}


	
	public SuperButtonToast(final Context mContext) 
	{
		
		if(mContext != null)
		{

			if(!(mContext instanceof Activity))
			{
				
				throw new IllegalArgumentException("You must pass an Activity Context!");
				
			}
			
			else
			{
				
				this.mContext = mContext;
				
			}
			
		}
		
		else
		{
			
			throw new IllegalArgumentException("The Context you passed was null!");
			
		}
		
	}
	
	
	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	public void show()
	{
		
		final LayoutInflater superundoLayoutInflater = (LayoutInflater) 
				mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
        final Activity activity = (Activity) mContext;
        
		final int sdkVersion = android.os.Build.VERSION.SDK_INT;

        
	        if(mViewGroup == null)
	        {
	        	
	        	mViewGroup = (ViewGroup) 
	            		activity.findViewById(android.R.id.content);
	        	
	        }
        			
	    mView = superundoLayoutInflater
				.inflate(R.layout.superbuttontoast, mViewGroup, false);
			
			
	    if(!setIndeterminate)
	    {
	    	
			mHandler.postDelayed(hideUndoToastRunnable, durationInteger);
	    	
	    }
			
		
		mTextView = (TextView) 
				mView.findViewById(R.id.messageTextView);
		
		mTextView.setText(messageText);
		
			if(messageTextSize > 0)
			{
			
				mTextView.setTextSize(messageTextSize);	

			}
		
		mTextView.setTypeface(messageTextTypeface);

		
			if(mSuperButtonToastStyle != null)
			{
				
				mTextView.setTextColor(mSuperButtonToastStyle.messagecolorResource);
	
			}
			
			else
			{

				mTextView.setTextColor(messageTextColor);

			}
		
		
		mButton = (Button) 
				mView.findViewById(R.id.undoButton);
		
			if(buttonTextSize > 0)
			{
			
				mButton.setTextSize(buttonTextSize);	

			}
		
		mButton.setTypeface(buttonTextTypeface);

			if(mSuperButtonToastStyle != null)
			{
					
				mButton.setTextColor(mSuperButtonToastStyle.undocolorResource);
				
				mButton.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources()
						.getDrawable(mSuperButtonToastStyle.undobuttondrawableResource), null, null, null);
				
				mButton.setText(mSuperButtonToastStyle.undoCharSequence);
		
			}
			
			else
			{
	
				mButton.setTextColor(buttonTextColor);
				
					if(buttonimageDrawable != null)
					{
						
						mButton.setCompoundDrawablesWithIntrinsicBounds(buttonimageDrawable, null, null, null);
						
					}
					
					else
					{
						
						mButton.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources()
								.getDrawable(buttonimageResource), null, null, null);
						
					}
				
				mButton.setText(buttonText);

			}
			
			
			if(mOnClickListener == null)
			{
				
				mSuperButtonToastCallback = (SuperButtonToastCallback) mContext;
				
				mButton.setOnClickListener(new View.OnClickListener() 
				{
	
					@Override
					public void onClick(View view)
					{
						
						mSuperButtonToastCallback.onSuperButtonToastClick();
						
						mHandler.removeCallbacks(hideUndoToastRunnable);
												
						dismiss(true);
						
					}
					
		        });
				
			}
			
			else
			{
				
				mButton.setOnClickListener(mOnClickListener);
				
			}
		
		
		mRootLayout = (LinearLayout) 
				mView.findViewById(R.id.rootLinearLayout);
		
			if(mSuperButtonToastStyle != null)
			{
					
				mRootLayout.setBackgroundResource(mSuperButtonToastStyle.toastbackgroundResource);
		
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
			
		
		undodividerView = (View) 
				mView.findViewById(R.id.undodividerView);
		
			if(mSuperButtonToastStyle != null)
			{
				
				undodividerView.setBackgroundColor(mSuperButtonToastStyle.dividerdrawableResource);
				
			}
			
			else
			{
				
				if(dividerDrawable != null)
				{
					
					if(sdkVersion < android.os.Build.VERSION_CODES.JELLY_BEAN) 
					{
												
						undodividerView.setBackgroundDrawable(dividerDrawable);
						
					}
					
					else 
					{
						
						undodividerView.setBackground(dividerDrawable);
					    
					}
				}
				
				else
				{
					
					undodividerView.setBackgroundColor(dividerColor);

				}
				
			}
        
		mViewGroup.addView(mView);
		
		
		mView.startAnimation(animationIn);

	}
	
	
	
	
	/**
	 * <b> public void setOnClickListener(final OnClickListener mOnClickListener) </b>
	 * 
	 * 
	 * <p> This is used to set the OnClickListener for the button in the SuperButtonToast.
	 *     If you do not wish to set an OnClickListener then you must implement the SuperButtonToastCallback
	 *     in your Activity to receive the Button click.</p>
	 * 
	 * 
	 *	 
	 */
	
	public void setOnClickListener(final OnClickListener mOnClickListener)
	{
		
		this.mOnClickListener = mOnClickListener;
		
	}
	
	
	/**
	 * <b> public void setStyle(final SuperButtonToastStyle mSuperButtonToastStyle) </b>
	 * 
	 * 
	 * <p> This is used to set the style of the SuperButtonToast. Please note: 
	 * 	   If you decide to use a default style than you are limited on the other
	 * 	   modifications you may make to the SuperButtonToast because the style 
	 * 	   presents these components. </p>
	 * 
	 * <b> Parameter example: </b>
	 * 
	 * <p> (SuperButtonToast.STYLE_UNDODARK) </p>
	 * 
	 *	 
	 */

	public void setStyle(final SuperButtonToastStyle mSuperButtonToastStyle)
	{
		
		this.mSuperButtonToastStyle = mSuperButtonToastStyle;
		
	}
	
	
	/**
	 * <b> public void setViewGroup(final ViewGroup mViewGroup) </b>
	 * 
	 * 
	 * <p> This is used to set the ViewGroup that the SuperButtonToast will be added to. 
	 *     By default this is set to (android.R.id.content).</p>
	 *	 
	 */

	public void setViewGroup(final ViewGroup mViewGroup)
	{
		
		this.mViewGroup = mViewGroup;
		
	}
	
	
	/**
	 * <b> public void setBackgroundResource(final int backgroundresource) </b>
	 * 
	 * 
	 * <p> This is used to set the background resource of the SuperButtonToast.
	 * 	   Please note: You may also use a custom Drawable as a background via the 
	 *     setBackgroundDrawable() method. </p>
	 * 
	 * <b> Parameter example: </b>
	 * 
	 * <p> (SuperButtonToast.BACKGROUND_STANDARDWHITE) </p>
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
	 * <p> This is used to set the background Drawable of the SuperButtonToast.
	 * 	   Please note: You may also use the resources in this library as a background via the 
	 *     setBackgroundResource() method. </p>
	 * 
	 *	 
	 */
	
	public void setBackgroundDrawable(final Drawable backgroundDrawable)
	{
		
		this.backgroundDrawable = backgroundDrawable;
		
	}
	
	
	/**
	 * <b> public void setButtonImageResource(final int undobuttondrawableResource) </b>
	 * 
	 * 
	 * <p> This is used to set the image resource of the button in the SuperButtonToast.
	 *     Please note: You may also use a custom Drawable as the image background via the 
	 *     setButtonImageDrawable() method.  </p>
	 * 
	 * <b> Parameter example: </b>
	 * 
	 * <p> (SuperUndoToast.BUTTON_DARK_EDIT) </p>
	 * 
	 * 	    
	 * <b> Design guide: </b>
	 * 
	 * <p> The Drawable supplied through this parameter should be nine-patch format. </p>
	 * 
	 *	 
	 */
	
	public void setButtonImageResource(final int undobuttondrawableResource)
	{
		
		this.buttonimageResource = undobuttondrawableResource;
		
	}
	
	
	/**
	 * <b> public void setButtonImageDrawable(final Drawable buttonimageDrawable) </b>
	 * 
	 * 
	 * <p> This is used to set the image Drawable of the button in the SuperButtonToast.
	 * 	   Please note: You may also use the resources in this library as a background via the 
	 *     setButtonImageResource() method. </p>
	 * 
	 *	 
	 */
	
	public void setButtonImageDrawable(final Drawable buttonimageDrawable)
	{
		
		this.buttonimageDrawable = buttonimageDrawable;
		
	}
	
	
	/**
	 * <b> public void setDividerColor(final int dividerColor) </b>
	 * 
	 * 
	 * <p> This is used to set the color of the divider in the SuperButtonToast. </p>
	 * 
	 * <b> Parameter example: </b>
	 * 
	 * <p> (Color.BLACK) </p>
	 * 
	 *	 
	 */
	
	public void setDividerColor(final int dividerColor)
	{
		
		this.dividerColor = dividerColor;
		
	}
	
	
	/**
	 * <b> public void setDividerDrawable(final Drawable dividerDrawable) </b>
	 * 
	 * 
	 * <p> This is used to set the Drawable of the divider in the SuperButtonToast. </p>
	 * 
	 *	 
	 */
	
	public void setDividerDrawable(final Drawable dividerDrawable)
	{
		
		this.dividerDrawable = dividerDrawable;
		
	}
	
	
	/**
	 * <b> public void setDuration(final int durationInteger) </b>
	 * 
	 * 
	 * <p> This is used to set the duration the SuperButtonToast. You
	 *     may use a custom duration for example (3000) for three seconds. </p>
	 * 
	 * <b> Parameter example: </b>
	 * 
	 * <p> (SuperButtonToast.DURATION_SHORT) </p>
	 * 
	 * 
	 * <b> Design guide: </b>
	 * 
	 * <p> The duration should not exceed 10000 milliseconds. </p>
	 * 
	 *	 
	 */
	
	public void setDuration(final int durationInteger)
	{
		
		this.durationInteger = durationInteger;
		
	}
	
	
	/**
	 * <b> public void setIndeterminate(final boolean setIndeterminate) </b>
	 * 
	 * 
	 * <p> This is used to show the SuperButtonToast indefinitely. Please note: 
	 *     You must call dismiss() to dismiss the SuperButtonToast. </p>
	 * 
	 * 
	 * <b> Design guide: </b>
	 * 
	 * <p> There are a very few situations where this should be used. </p>
	 * 
	 *	 
	 */
	
	public void setIndeterminate(final boolean setIndeterminate)
	{
		
		this.setIndeterminate = setIndeterminate;
		
	}
	

	/**
	 * <b> public void setAnimationIn(final Animation animationIn) </b>
	 * 
	 * 
	 * <p> This is used to set the opening animation of the SuperUndoToast. </p>
	 * 
	 * <p> Please note: The animation should not exceed 750 milliseconds under any circumstances
	 *
	 */
	
	public void setAnimationIn(final Animation animationIn)
	{
		
		this.animationIn = animationIn;
		
	}
	
	
	/**
	 * <b> public void setAnimationIn(final Animation animationIn) </b>
	 * 
	 * 
	 * <p> This is used to set the closing animation of the SuperButtonToast. </p>
	 * 
	 * <p> Please note: The animation should not exceed 750 milliseconds under any circumstances
	 * 
	 */
	
	public void setAnimationOut(final Animation animationOut)
	{
		
		this.animationOut = animationOut;
		
	}
	
	
	/**
	 * <b> public void setMessageText(final CharSequence messageText) </b>
	 * 
	 * 
	 * <p> This is used to set the message text of the SuperButtonToast. </p>
	 * 
	 * <b> Parameter example: </b>
	 * 
	 * <p> ("Hello, I am a SuperButtonToast")</p>
	 * 
	 *	 
	 */

	public void setMessageText(final CharSequence messageText)
	{
		
		this.messageText = messageText;
		
		if(mView != null)
		{
				
			mTextView.setText(messageText);
				
		}
		
	}
	
	
	/**
	 * <b> public void setMessageTextColor(final int messageTextColor) </b>
	 * 
	 * 
	 * <p> This is used to set the color of the message text in the SuperButtonToast.		  </p>
	 * 
	 * <b> Parameter example: </b>
	 * 
	 * <p> (Color.BLACK)</p>
	 * 
	 *	 
	 */

	public void setMessageTextColor(final int messageTextColor)
	{
		
		this.messageTextColor = messageTextColor;
		
	}
	
	
	/**
	 * <b> public void setMessageTextSize(final float messageTextSize) </b>
	 * 
	 * 
	 * <p> This is used to set the size of the message text in the SuperButtonToast. </p>
	 * 
	 * <b> Parameter example: </b>
	 * 
	 * <p> (18) </p>
	 * 
	 *	 
	 */

	public void setMessageTextSize(final float messageTextSize)
	{
		
		this.messageTextSize = messageTextSize;
		
	}
	
	
	/**
	 * <b> public void setMessageTypeface(final Typeface messageTextTypeface) </b>
	 * 
	 * 
	 * <p> This is used to set the Typeface of the message text in the SuperButtonToast. </p>
	 * 
	 * <b> Parameter example: </b>
	 * 
	 * <p> (Typeface.DEFAULT)</p>
	 * 
	 *	 
	 */

	public void setMessageTextTypeface(final Typeface messageTextTypeface)
	{
		
		this.messageTextTypeface = messageTextTypeface;
		
	}
	
	
	/**
	 * <b> public void setButtonText(final CharSequence buttonText) </b>
	 * 
	 * 
	 * <p> This is used to set the message of the button text in the SuperButtonToast. </p>
	 * 
	 * <b> Parameter example: </b>
	 * 
	 * <p> ("UNDO")</p>
	 * 
	 * 
	 * <b> Design guide: </b>
	 * 
	 * <p> This message should be in all capital letters and between 3-6 characters in length. </p>
	 *	 
	 */

	public void setButtonText(final CharSequence buttonText)
	{
		
		this.buttonText = buttonText;
		
		if(mView != null)
		{
			
			mButton.setText(buttonText);
			
		}
	
	}
	
	
	/**
	 * <b> public void setButtonTextColor(final int buttonTextColor) </b>
	 * 
	 * 
	 * <p> This is used to set the text color of the Button in the SuperButtonToast. </p>
	 * 
	 * <b> Parameter example: </b>
	 * 
	 * <p> (Color.BLACK)</p>
	 * 
	 * 
	 * <b> Design guide: </b>
	 * 
	 * <p> This color should match the message color and contrast the background of the SuperButtonToast. </p>
	 *	 
	 */

	public void setButtonTextColor(final int buttonTextColor)
	{
		
		this.buttonTextColor = buttonTextColor;
		
	}
	
	
	/**
	 * <b> public void setButtonTextSize(final int buttonTextSize) </b>
	 * 
	 * 
	 * <p> This is used to set the text size of the Button in the SuperButtonToast. </p>
	 * 
	 * <b> Parameter example: </b>
	 * 
	 * <p> (12)</p>
	 * 
	 * 
	 * <b> Design guide: </b>
	 * 
	 * <p> This should not be larger than the size of the message text. </p>
	 *	 
	 */

	public void setButtonTextSize(final float buttonTextSize)
	{
		
		this.buttonTextSize = buttonTextSize;
		
	}
	
	
	/**
	 * <b> public void setButtonTextTypeface(final Typeface buttonTextTypeface) </b>
	 * 
	 * 
	 * <p> This is used to set the Typeface of the Button in the SuperUndoToast. </p>
	 * 
	 * <b> Parameter example: </b>
	 * 
	 * <p> (Typeface.DEFAULT_BOLD)</p>
	 * 
	 * 
	 * <b> Design guide: </b>
	 * 
	 * <p> This is set to bold by default. </p>
	 *	 
	 */

	public void setButtonTextTypeface(final Typeface buttonTextTypeface)
	{
		
		this.buttonTextTypeface = buttonTextTypeface;
		
	}	
	
	
	
	
	/**
	 * <b> public View getSuperButtonToastView() </b>
	 * 
	 * 
	 * <p> This returns the SuperButtonToast view. </p>
     *
	 *
	 */
	

	public View getSuperButtonToastView()
	{
		
		if(mView != null)
		{
			
			return mView;

		}
		
		else
		{
			
			return null;
			
		}
		
	}
	
	
	/**
	 * <b> public Button getButton() </b>
	 * 
	 * 
	 * <p> This returns the SuperButtonToast Button. </p>
     *
	 *
	 */
	
	public Button getButton()
	{
		
		if(mButton != null)
		{
			
			return mButton;

		}
		
		else
		{
			
			return null;
			
		}
		
	}
	
	
	/**
	 * <b> public TextView getMessageTextView() </b>
	 * 
	 * 
	 * <p> This returns the SuperButtonToast message TextView. </p>
     *
	 *
	 */
	
	public TextView getMessageTextView()
	{
		
		if(mTextView != null)
		{
			
			return mTextView;

		}
		
		else
		{
			
			return null;
			
		}
		
	}
	
	
	/**
	 * <b> public LinearLayout getRootLayout() </b>
	 * 
	 * 
	 * <p> This returns the SuperButtonToast root LinearLayout. </p>
     *
	 *
	 */
	
	public LinearLayout getRootLayout()
	{
		
		if(mRootLayout != null)
		{
			
			return mRootLayout;

		}
		
		else
		{
			
			return null;
			
		}
		
	}
	
	
	/**
	 * <b> public View getDivider() </b>
	 * 
	 * 
	 * <p> This returns the SuperButtonToast divider View. </p>
     *
	 *
	 */
	
	public View getDivider()
	{
		
		if(undodividerView != null)
		{
			
			return undodividerView;

		}
		
		else
		{
			
			return null;
			
		}
		
	}
	
	
	
	
	
	
	
	
	/**
	 * <p> This is used to hide and dispose of the SuperButtonToast. </p>
	 *
	 *
	 * <b> Design guide: </b>
	 * 
	 * <p> This should be called when the user clicks the Button in the SuperButtonToast so
	 *     that it's not hanging around after the user has used it. </p>
	 * 	 
	 *	 
	 */

	public void dismiss(final boolean showAnimation)
	{
		
		mHandler.removeCallbacks(hideUndoToastRunnable);

	    if(mView != null && mViewGroup != null && animationOut != null)
	    {

	    	if (showAnimation) 
	    	{
	    			
	       		animationOut.setAnimationListener(new AnimationListener()
				{

					@Override
					public void onAnimationEnd(Animation animation) 
					{

						mViewGroup.removeView(mView);
			        		
						mView = null;                		
			        	
					}

					@Override
					public void onAnimationRepeat(Animation animation) 
					{

						//Do nothing
						
					}

					@Override
					public void onAnimationStart(Animation animation) 
					{
						
						//Do nothing
						
					}
										
				});

	       		mView.startAnimation(animationOut);	
	       			
	       	}
			
			else
			{

				mViewGroup.removeView(mView);
	        		
				mView = null; 
    
			}
	    	
	    }
	    	
	    else
		{
				
			try
			{
					
				mViewGroup.removeView(mView);
	        		
				mView = null; 
					
			}
				
			catch(Exception exception)
			{
					
				Log.e("SuperUndoToast", exception.toString());
					
			}
    
		}
		
	}
	
	

	
	
	private Runnable hideUndoToastRunnable = new Runnable() 
	{
		 
        public void run() 
        {
        	        	
        	dismiss(true);
        	 
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

	

}
