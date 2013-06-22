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


import com.extlibsupertoasts.styles.SuperButtonToastStyle;
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



/**
 * SuperButtonToasts are designed to be used inside of Activities. When the
 * Activity is destroyed the SuperButtonToast is destroyed along with it.
 * SuperButtonToasts will not linger to the next screen like standard
 * Toasts/SuperToasts.
 * 
 * <br>
 * 
 * <p>
 * <b> Design guide: </b>
 * </p>
 * 
 * <p>
 * SuperButtonToasts are designed to be displayed after a potentially
 * dangerous event such as deleting an email as seen in the official
 * Gmail application. 
 * </p>
 * 
 */
@SuppressLint("NewApi")
@SuppressWarnings("deprecation")
public class SuperButtonToast
{	
	
	private static final String TAG = "SuperButtonToast";

	private static final String ERROR_CONTEXTNULL = "The Context that you passed was null! (SuperButtonToast)";
	private static final String ERROR_CONTEXTNOTACTIVITY = "The Context that you passed was not an Activity! (SuperButtonToast)";
	private static final String ERROR_ACTIVITYNOINTERFACE = "You must either set an OnClickListener or the calling Activity must implement the " +
			"SuperButtonToastCallback. (SuperButtonToast)";
	
	private static final String WARNING_CALLBACKANDONCLICK = "Since an OnClickListener was provided the SuperButtonToastCallback was ignored.";

	/**
	 * This style implements a edit icon with a dark theme background.
	 */
	public static final SuperButtonToastStyle STYLE_EDITDARK = new SuperButtonToastStyle
			(com.extlibsupertoasts.R.drawable.icon_dark_edit, "EDIT", (Color.WHITE), (Color.WHITE), 
					(com.extlibsupertoasts.R.drawable.background_greytranslucent), (Color.WHITE));
	
	/**
	 * This style implements a exit icon with a dark theme background.
	 */
	public static final SuperButtonToastStyle STYLE_EXITDARK = new SuperButtonToastStyle
			(com.extlibsupertoasts.R.drawable.icon_dark_exit, "EXIT", (Color.WHITE), (Color.WHITE), 
					(com.extlibsupertoasts.R.drawable.background_greytranslucent), (Color.WHITE));
	
	/**
	 * This style implements a information icon with a dark theme background.
	 */
	public static final SuperButtonToastStyle STYLE_INFODARK = new SuperButtonToastStyle
			(com.extlibsupertoasts.R.drawable.icon_dark_info, "INFO", (Color.WHITE), (Color.WHITE), 
					(com.extlibsupertoasts.R.drawable.background_greytranslucent), (Color.WHITE));
	
	/**
	 * This style implements a redo icon with a dark theme background.
	 */
	public static final SuperButtonToastStyle STYLE_REDODARK = new SuperButtonToastStyle
			(com.extlibsupertoasts.R.drawable.icon_dark_redo, "REDO", (Color.WHITE), (Color.WHITE), 
					(com.extlibsupertoasts.R.drawable.background_greytranslucent), (Color.WHITE));
	
	/**
	 * This style implements a save icon with a dark theme background.
	 */
	public static final SuperButtonToastStyle STYLE_SAVEDARK = new SuperButtonToastStyle
			(com.extlibsupertoasts.R.drawable.icon_dark_save, "SAVE", (Color.WHITE), (Color.WHITE), 
					(com.extlibsupertoasts.R.drawable.background_greytranslucent), (Color.WHITE));
	
	/**
	 * This style implements a share icon with a dark theme background.
	 */
	public static final SuperButtonToastStyle STYLE_SHAREDARK = new SuperButtonToastStyle
			(com.extlibsupertoasts.R.drawable.icon_dark_share, "SHARE", (Color.WHITE), (Color.WHITE), 
					(com.extlibsupertoasts.R.drawable.background_greytranslucent), (Color.WHITE));
	
	/**
	 * This style implements a undo icon with a dark theme background.
	 */
	public static final SuperButtonToastStyle STYLE_UNDODARK = new SuperButtonToastStyle
			(com.extlibsupertoasts.R.drawable.icon_dark_undo, "UNDO", (Color.WHITE), (Color.WHITE), 
					(com.extlibsupertoasts.R.drawable.background_greytranslucent), (Color.WHITE));
	
	/**
	 * This style implements a edit icon with a light theme background.
	 */
	public static final SuperButtonToastStyle STYLE_EDITLIGHT = new SuperButtonToastStyle
			(com.extlibsupertoasts.R.drawable.icon_light_edit, "EDIT", (Color.DKGRAY), (Color.DKGRAY), 
					(com.extlibsupertoasts.R.drawable.background_whitetranslucent), (Color.DKGRAY));
	
	/**
	 * This style implements a exit icon with a light theme background.
	 */
	public static final SuperButtonToastStyle STYLE_EXITLIGHT = new SuperButtonToastStyle
			(com.extlibsupertoasts.R.drawable.icon_light_exit, "EXIT", (Color.DKGRAY), (Color.DKGRAY), 
					(com.extlibsupertoasts.R.drawable.background_whitetranslucent), (Color.DKGRAY));
	
	/**
	 * This style implements a information icon with a light theme background.
	 */
	public static final SuperButtonToastStyle STYLE_INFOLIGHT = new SuperButtonToastStyle
			(com.extlibsupertoasts.R.drawable.icon_light_info, "INFO", (Color.DKGRAY), (Color.DKGRAY), 
					(com.extlibsupertoasts.R.drawable.background_whitetranslucent), (Color.DKGRAY));
	
	/**
	 * This style implements a redo icon with a light theme background.
	 */
	public static final SuperButtonToastStyle STYLE_REDOLIGHT = new SuperButtonToastStyle
			(com.extlibsupertoasts.R.drawable.icon_light_redo, "REDO", (Color.DKGRAY), (Color.DKGRAY), 
					(com.extlibsupertoasts.R.drawable.background_whitetranslucent), (Color.DKGRAY));
	
	/**
	 * This style implements a save icon with a light theme background.
	 */
	public static final SuperButtonToastStyle STYLE_SAVELIGHT = new SuperButtonToastStyle
			(com.extlibsupertoasts.R.drawable.icon_light_save, "SAVE", (Color.DKGRAY), (Color.DKGRAY), 
					(com.extlibsupertoasts.R.drawable.background_whitetranslucent), (Color.DKGRAY));
	
	/**
	 * This style implements a share icon with a light theme background.
	 */
	public static final SuperButtonToastStyle STYLE_SHARELIGHT = new SuperButtonToastStyle
			(com.extlibsupertoasts.R.drawable.icon_light_share, "SHARE", (Color.DKGRAY), (Color.DKGRAY), 
					(com.extlibsupertoasts.R.drawable.background_whitetranslucent), (Color.DKGRAY));
	
	/**
	 * This style implements a undo icon with a light theme background.
	 */
	public static final SuperButtonToastStyle STYLE_UNDOLIGHT = new SuperButtonToastStyle
			(com.extlibsupertoasts.R.drawable.icon_light_undo, "UNDO", (Color.DKGRAY), (Color.DKGRAY), 
					(com.extlibsupertoasts.R.drawable.background_whitetranslucent), (Color.DKGRAY));

		
	private Context mContext;
	private View toastView;
	private ViewGroup mViewGroup;
	private SuperButtonToastCallback mSuperButtonToastCallback;
	private LinearLayout mRootLayout;
	private View undodividerView;
	private TextView mTextView;
	private LayoutInflater mLayoutInflater;
	private int sdkVersion = android.os.Build.VERSION.SDK_INT;

	
	private CharSequence messageCharSequence;
	private int messageTextColor = (Color.WHITE);
	private int messageTextSize = (SuperToastConstants.TEXTSIZE_MEDIUM);
	private Typeface messageTypeface = (Typeface.DEFAULT);
	private CharSequence buttonTextCharSequence;
	private int buttonTextColor = (Color.WHITE);
	private int buttonTextSize = (SuperToastConstants.TEXTSIZE_SMALL);
	private Typeface buttonTextTypeface = (Typeface.DEFAULT_BOLD);
	private int backgroundResource = (SuperToastConstants.BACKGROUND_BLACK);
	private Drawable backgroundDrawable;
	private int durationInteger = (SuperToastConstants.DURATION_LONG);
	private Handler mHandler;
	private OnClickListener mOnClickListener;
	private int dividerResource = (com.extlibsupertoasts.R.color.white);
	private Drawable dividerDrawable;
	private boolean setIndeterminate;
	private Animation showAnimation = getFadeInAnimation();
	private Animation dismissAnimation = getFadeOutAnimation();
	private Button mButton;
	private int undoButtonResource = (SuperToastConstants.BUTTON_DARK_UNDO);
	private Drawable buttonimageDrawable;

		
	/**
	 * This is a callback that can be implemented in place of an
	 * OnClickListener.
	 */
	public interface SuperButtonToastCallback
	{
		
		/**
		 * This callback will be called when the button in the
		 * SuperButtonToast is clicked.
		 * 
		 * <br>
		 * 
		 * <p>
		 * <b> Important note: </b>
		 * </p>
		 * 
		 * <p>
		 * This callback will not be called if an OnClickListener is set
		 * to the SuperButtonToast. 
		 * </p>
		 * 
		 */
		public void onSuperButtonToastClick();
		
	}


	
	/**
	 * Instantiates a new SuperButtonToast. You <b>MUST</b> pass an Activity
	 * as a Context.
	 * 
	 * <br>
	 * 
	 * @param mContext
	 *            This must be an Activity Context.
	 * 
	 */
	public SuperButtonToast(Context mContext) 
	{
		
		if(mContext != null)
		{

			if(mContext instanceof Activity)
			{
				
				this.mContext = mContext;

				mLayoutInflater = (LayoutInflater) mContext
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

				final Activity mActivity = (Activity) mContext;

				mViewGroup = (ViewGroup) mActivity
						.findViewById(android.R.id.content);

				toastView = mLayoutInflater.inflate(R.layout.superbuttontoast,
						mViewGroup, false);	
				
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
	
	
	/**
	 * This is used to show the SuperButtonToast. You should
	 * do all of your modifications to the SuperButtonToast before calling
	 * this method. 
	 */
	public void show()
	{		
			
	    if(!setIndeterminate)
	    {
	    	
	    	mHandler = new Handler();
			mHandler.postDelayed(hideToastRunnable, durationInteger);
	    	
	    }
			
		mTextView = (TextView) 
				toastView.findViewById(R.id.messageTextView);
		
		mTextView.setText(messageCharSequence);
	    mTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, messageTextSize);	
		mTextView.setTypeface(messageTypeface);
		mTextView.setTextColor(messageTextColor);
		
		
		mButton = (Button) 
				toastView.findViewById(R.id.undoButton);
		
		mButton.setText(buttonTextCharSequence);
		mButton.setTextSize(TypedValue.COMPLEX_UNIT_SP,buttonTextSize);	
		mButton.setTypeface(buttonTextTypeface);
		mButton.setTextColor(buttonTextColor);

				
		if (buttonimageDrawable != null) {

			mButton.setCompoundDrawablesWithIntrinsicBounds(
					buttonimageDrawable, null, null, null);

		} else {

			mButton.setCompoundDrawablesWithIntrinsicBounds(mContext
					.getResources().getDrawable(undoButtonResource), null,
					null, null);

		}
				

		if(mOnClickListener == null) {
				
			try {
				
				mSuperButtonToastCallback = (SuperButtonToastCallback) mContext;				
				
			} catch (Exception exception) {
				
				throw new IllegalArgumentException(ERROR_ACTIVITYNOINTERFACE);
				
			}
				
			mButton.setOnClickListener(new View.OnClickListener() 
			{
	
				@Override
				public void onClick(View view){
						
					mSuperButtonToastCallback.onSuperButtonToastClick();
						
				}
					
		    });
				
		} else {
				
			mButton.setOnClickListener(mOnClickListener);
			
			if (mContext instanceof SuperButtonToastCallback) {

				Log.w(TAG, WARNING_CALLBACKANDONCLICK);

			}
				
		}
		
		
		mRootLayout = (LinearLayout) 
				toastView.findViewById(R.id.rootLinearLayout);

		if(backgroundDrawable != null) {
			
			if(sdkVersion < android.os.Build.VERSION_CODES.JELLY_BEAN) {
				
				mRootLayout.setBackgroundDrawable(backgroundDrawable);
								
			} else { 
							
				mRootLayout.setBackground(backgroundDrawable);
							    
			}
			
		} else {
					
			mRootLayout.setBackgroundResource(backgroundResource);

		}
		
		undodividerView = (View) 
				toastView.findViewById(R.id.undodividerView);
		
		if(dividerDrawable != null) {
					
			if(sdkVersion < android.os.Build.VERSION_CODES.JELLY_BEAN) {
												
				undodividerView.setBackgroundDrawable(dividerDrawable);
						
			} else {
						
				undodividerView.setBackground(dividerDrawable);
					    
			}
			
		} else {
					
			undodividerView.setBackgroundResource(dividerResource);

		}
        
		mViewGroup.addView(toastView);
		
		toastView.startAnimation(showAnimation);

	}
	
	
	
	/**
	 * This is used to set the message text of the SuperButtonToast.
	 * <br>
	 * 
	 * <p>
	 * <b> Design guide: </b>
	 * </p>
	 * 
	 * <p>
	 * The text of the SuperButtonToast should be short and to the point.
	 * </p>
	 * 
	 * <br>
	 * 
	 * @param messageCharSequence 
	 * 		
	 * <br>
	 * 
	 */
	public void setMessageText(CharSequence messageCharSequence) {

		this.messageCharSequence = messageCharSequence;

		if (mTextView != null) {

			mTextView.setText(messageCharSequence);

		}

	}
	
	
	/**
	 * This is used to set the message text color of the SuperButtonToast.
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
	 * 
	 * @param messageTextColor 
	 * 
	 * <br>
	 * 
	 * Example: (Color.WHITE)
	 * 	 	
	 * <br>
	 * 
	 */
	public void setMessageTextColor(int messageTextColor) {

		this.messageTextColor = messageTextColor;

	}

	
	/**
	 * This is used to set the text size of the message in the SuperButtonToast.
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
	 * 
	 * @param messageTextSize 
	 * 
	 * <br>
     *
	 * Example: (SuperToastConstants.TEXTSIZE_SMALL)
	 * 		
	 * <br>
	 * 
	 */
	public void setMessageTextSize(int messageTextSize) {
		
		this.messageTextSize = messageTextSize;
		
	}
	
	
	/**
	 * This is used to set an OnClickListener to the Button of the SuperButtonToast.
	 * <br>
	 * 
	 * <p>
	 * <b> Important note: </b>
	 * </p>
	 * 
	 * <p>
	 * An OnClickListener must be set to the Button of the SuperButtonToast or 
	 * the SuperButtonToastCallback must be implemented in the calling Activity.
	 * </p>
	 * 
	 * <br>
	 * 
	 * @param mOnClickListener 
	 * 
	 * <br>
	 * 
	 * The OnClickListener should call {@link #dismiss()}.
	 * 		
	 * <br>
	 * 
	 */
	public void setOnClickListener(OnClickListener mOnClickListener) {

		this.mOnClickListener = mOnClickListener;

	}
	
	
	/**
	 * This is used to set the style of the SuperButtonToast.
	 * 
	 * <br>
	 * 
	 * @param mSuperButtonToastStyle
	 * 
	 * <br>
	 *  
	 * Example: (SuperButtonToast.STYLE_UNDODARK)
	 * 		
	 * <br>
	 * 
	 */
	public void setStyle(SuperButtonToastStyle mSuperButtonToastStyle) {

		this.undoButtonResource = mSuperButtonToastStyle.undoButtonResource;
		this.buttonTextCharSequence = mSuperButtonToastStyle.buttonTextCharSequence;
		this.messageTextColor = mSuperButtonToastStyle.messageTextColor;
		this.buttonTextColor = mSuperButtonToastStyle.buttonTextColor;
		this.backgroundResource = mSuperButtonToastStyle.backgroundResource;
		this.dividerResource = mSuperButtonToastStyle.dividerResource;

	}
	
	
	/**
	 * This is used to set the ViewGroup that the SuperButtonToast is 
	 * attached to. By default the SuperButtonToast is attached to 
	 * (android.R.id.content).
	 * 
	 * <br>
	 * 
	 * @param mViewGroup 
	 * 		
	 * <br>
	 * 
	 */
	public void setViewGroup(ViewGroup mViewGroup) {

		this.mViewGroup = mViewGroup;

	}
	
	/**
	 * This is used to set the background of the SuperButtonToast.
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
	 * 
	 * @param backgroundResource
	 * 
	 * <br>
	 *  
	 * Example: (SuperToastConstants.BACKGROUND_BLACK)
	 * 		
	 * <br>
	 * 
	 */
	public void setBackgroundResource(int backgroundResource) {

		this.backgroundResource = backgroundResource;

	}
	
	
	/**
	 * This is used to set the background of the SuperButtonToast.
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
	 * 
	 * @param backgroundDrawable 
	 * 		
	 * <br>
	 * 
	 */
	public void setBackgroundDrawable(Drawable backgroundDrawable) {

		this.backgroundDrawable = backgroundDrawable;

	}
	
	
	/**
	 * This is used to set the background of the Button in the SuperButtonToast.
	 * 
	 * <br>
	 * 
	 * <p>
	 * <b> Design guide: </b>
	 * </p>
	 * 
	 * <p>
	 * This library comes with backgrounds ready to use in your applications.
	 * </p>
	 * 
	 * <br>
	 * 
	 * @param undoButtonResource
	 * 
	 * <br>
	 *  
	 * Example: (SuperToastConstants.BUTTON_DARK_REDO)
     *
	 * <br>
	 * 
	 */
	public void setButtonResource(int undoButtonResource) {

		this.undoButtonResource = undoButtonResource;

	}
	
	
	/**
	 * This is used to set the background drawable of the Button in the SuperButtonToast.
	 * 
	 * <br>
	 * 
	 * <p>
	 * <b> Design guide: </b>
	 * </p>
	 * 
	 * <p>
	 * This library comes with backgrounds ready to use in your applications.
	 * To use them please see {@link #setButtonResource(int)}.
	 * </p>
	 * 
	 * <br>
	 * 
	 * @param buttonimageDrawable 
     *
	 * <br>
	 * 
	 */
	public void setButtonDrawable(Drawable buttonimageDrawable) {

		this.buttonimageDrawable = buttonimageDrawable;

	}
	
	
	/**
	 * This is used to set the resource of the divider in the SuperButtonToast.
	 * 
	 * <br>
	 * 
	 * <p>
	 * <b> Design guide: </b>
	 * </p>
	 * 
	 * <p>
	 * The resource can also be a color defined via XML. Choose a color that contrasts the
	 * background of the SuperButtonToast. 
	 * </p>
	 * 
	 * <br>
	 * 
	 * @param dividerResource 
	 * 
	 * <br>
	 * 
	 * Example: (R.color.white)
     *
	 * <br>
	 * 
	 */
	public void setDividerResource(int dividerResource) {

		this.dividerResource = dividerResource;

	}
	
	
	/**
	 * This is used to set the Drawable of the divider in the SuperButtonToast.
	 * 
	 * <br>
	 * 
	 * <p>
	 * <b> Important note: </b>
	 * </p>
	 * 
	 * <p>
	 * To use a Color instead see {@link #setDividerResource(int)}. 
	 * </p>
	 * 
	 * <br>
	 * 
	 * @param dividerDrawable 
     *
	 * <br>
	 * 
	 */
	public void setDividerDrawable(Drawable dividerDrawable) {

		this.dividerDrawable = dividerDrawable;

	}
	
	
	/**
	 * This is used to set the duration of the SuperButtonToast.
	 * 
	 * <br>
	 * 
	 * <p>
	 * <b> Important note: </b>
	 * </p>
	 * 
	 * <p>
	 * To mimic the actions of the official Gmail application see 
	 * {@link #setIndeterminate(boolean)}.
	 * </p>
	 * 
	 * <br>
	 * 
	 * @param durationInteger
	 * 
	 * <br>
	 *  
	 * Example: (SuperToastConstants.DURATION_LONG)
     *
	 * <br>
	 * 
	 */
	public void setDuration(int durationInteger) {

		this.durationInteger = durationInteger;

	}
	
	
	/**
	 * This is used to set an indeterminate value to the SuperButtonToast.
	 * 
	 * <br>
	 * 
	 * <p>
	 * <b> Important note: </b>
	 * </p>
	 * 
	 * <p>
	 * {@link #dismiss()} must be called to dismiss the SuperButtonToast.
	 * Setting the SuperButtonToast to indeterminate will force the SuperButtonToast 
	 * to ignore any duration set by {@link #setDuration(int)}.
	 * </p>
	 * 
	 * <br>
	 * 
	 * @param setIndeterminate 
     *
	 * <br>
	 * 
	 */
	public void setIndeterminate(boolean setIndeterminate) {

		this.setIndeterminate = setIndeterminate;

	}
	

	/**
	 * This is used to set the show animation of the SuperButtonToast.
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
	 * 
	 * @param showAnimation 
	 * 		
	 * <br>
	 * 
	 */
	public void setShowAnimation(Animation showAnimation) {

		this.showAnimation = showAnimation;

	}
	
	
	/**
	 * This is used to set the dismiss animation of the SuperButtonToast.
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
	 * 
	 * @param dismissAnimation 
	 * 		
	 * <br>
	 * 
	 */
	public void setDismissAnimation(Animation dismissAnimation) {

		this.dismissAnimation = dismissAnimation;

	}
	
	
	/**
	 * This is used to set the Typeface of the message in the SuperButtonToast.
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
	 * 
	 * @param messageTypeface 
	 * 
	 * <br>
	 * 
	 * Example: (Typeface.DEFAULT) OR (mSuperActivityToast.loadRobotoTypeface(SuperToastConstants.
	 * FONT_ROBOTO_THIN);
	 * 		
	 * <br>
	 * 
	 */
	public void setMessageTextTypeface(final Typeface messageTypeface) {

		this.messageTypeface = messageTypeface;

	}
	
	
	/**
	 * This is used to set the message text of the Button in the SuperButtonToast.
	 * <br>
	 * 
	 * <p>
	 * <b> Design guide: </b>
	 * </p>
	 * 
	 * <p>
	 * The text of the SuperButtonToast should be short, and all capital letters.
	 * </p>
	 * 
	 * <br>
	 * 
	 * @param buttonTextCharSequence 
	 * 		
	 * <br>
	 * 
	 */
	public void setButtonText(CharSequence buttonTextCharSequence) {

		this.buttonTextCharSequence = buttonTextCharSequence;

		if (mButton != null) {

			mButton.setText(buttonTextCharSequence);

		}

	}
	
	
	/**
	 * This is used to set the Button text color of the SuperButtonToast.
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
	 * here. This color should also match the color specified in {@link #setMessageTextColor(int)}.
	 * </p>
	 * 
	 * <br>
	 * 
	 * @param buttonTextColor 
	 * 
	 * <br>
	 * 
	 * Example: (Color.WHITE)
	 * 	 	
	 * <br>
	 * 
	 */
	public void setButtonTextColor(final int buttonTextColor) {

		this.buttonTextColor = buttonTextColor;

	}
	
	
	/**
	 * This is used to set the text size of the Button in the SuperButtonToast.
	 * 
	 * <br>
	 * 
	 * <p>
	 * <b> Design guide: </b>
	 * </p>
	 * 
	 * <p>
	 * Generally the text size should be around 16sp.
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
	 * 
	 * @param buttonTextSize 
	 * 
	 * <br>
	 * 
	 * Example: (SuperToastConstants.TEXTSIZE_MEDIUM)
	 * 		
	 * <br>
	 * 
	 */
	public void setButtonTextSize(int buttonTextSize) {

		this.buttonTextSize = buttonTextSize;

	}
	
	
	/**
	 * This is used to set the Typeface of the Button in the SuperButtonToast.
	 * 
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
	 * 
	 * @param buttonTextTypeface 
	 * 
	 * <br>
	 * 
	 * Example: (Typeface.DEFAULT) OR (mSuperActivityToast.loadRobotoTypeface(SuperToastConstants.
	 * FONT_ROBOTO_THIN);
	 * 
	 * 		
	 * <br>
	 * 
	 */
	public void setButtonTextTypeface(Typeface buttonTextTypeface)
	{
		
		this.buttonTextTypeface = buttonTextTypeface;
		
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
			
			if (mHandler != null) {

				mHandler.removeCallbacks(hideToastRunnable);
				mHandler = null;

			}
			
			if(mButton != null) {
				
				mButton.setEnabled(false);
				
			}

			mViewGroup.removeView(toastView);
			toastView = null;

		} else {

			Log.e("SuperCardToast",
					"Either the View or Container was null when trying to dismiss. "
							+ "Did you create and show a SuperCardToast before trying to dismiss it?");

		}

	}
	
	
	//XXX Get methods.
	
	
	/**
	 * This is used to get the SuperButtonToast View.
	 * 
	 * <br>
	 * 
	 * @return View
	 * 
	 * <br>
	 * 
	 */
	public View getView() {

		return toastView;

	}
	
	
	/**
	 * This is used to get the SuperButtonToast Button.
	 * 
	 * <br>
	 * 
	 * @return Button
	 * 
	 * <br>
	 * 
	 */
	public Button getButton() {

		return mButton;

	}
	
	
	/**
	 * This is used to get the SuperButtonToast TextView.
	 * 
	 * <br>
	 * 
	 * @return TextView
	 * 
	 * <br>
	 * 
	 */
	public TextView getTextView() {

		return mTextView;

	}
	
	
	/**
	 * This is used to get the SuperButtonToast root Layout.
	 * 
	 * <br>
	 * 
	 * @return LinearLayout
	 * 
	 * <br>
	 * 
	 */
	public LinearLayout getRootLayout() {

		return mRootLayout;

	}
	
	
	/**
	 * This is used to get the SuperButtonToast divider.
	 * 
	 * <br>
	 * 
	 * @return View
	 * 
	 * <br>
	 * 
	 */
	public View getDivider() {

		return undodividerView;

	}	
	
	
	/**
	 * This is used to get and load a Roboto font. You <b><i>MUST</i></b> put the
	 * desired font file in the assets folder of your project. The link to
	 * download the Roboto fonts is included in this library as a text file. Do
	 * not modify the names of these fonts.
	 * 
	 * <br>
	 * 
	 * @param typefaceString
	 * 
	 * <br>
	 * 
	 * Example: (SuperToastConstants.FONT_ROBOTO_THIN)
	 * 
	 * <br>
	 * 
	 * @return Typeface
	 * 
	 * <br>
	 * 
	 */
	public Typeface loadRobotoTypeface(String typefaceString) {

		return Typeface.createFromAsset(mContext.getAssets(), typefaceString);

	}

	
	//XXX Private methods.
	
	
	private Runnable hideToastRunnable = new Runnable() {

		public void run() {

			dismiss();

		}
	};
	
	
	private Runnable mHideImmediateRunnable = new Runnable() 
	{
		 
        public void run() 
        {
        	        	
        	dismissImmediately();
        	 
        }
        
    };
    
	
	private void dismissWithAnimation() {
		
		if (mHandler != null) {

			mHandler.removeCallbacks(hideToastRunnable);
			mHandler = null;

		}
		
		if(mButton != null) {
			
			mButton.setEnabled(false);
			
		}
		

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
