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


import com.extlibsupertoasts.styles.SuperCardToastStyle;
import com.extlibsupertoasts.utilities.OnDismissListener;
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


/**
 * SuperCardToasts are designed to be used inside of Activities. SuperCardToasts
 * are designed to be displayed at the top of an Activity to display non-essential
 * non-intrusive messages. 
 * 
 */
@SuppressLint("NewApi")
@SuppressWarnings("deprecation")
public class SuperCardToast
{
	
	private static final String TAG  = "(SuperCardToast)";

	private static final String ERROR_CONTEXTNOTACTIVITY  = "Context must be an instance of Activity (SuperCardToast)";
	private static final String ERROR_CONTAINERNULL = "You must have a LinearLayout with the id of card_container in your layout! (SuperCardToast)";
	private static final String ERROR_TYPENULL = "You cannot supply null as a Type! (SuperCardToast)";
	private static final String ERROR_NOCLICKLISTENER = "There was no OnClickListener set to the Button. Please call setButtonOnClickListener().";
	private static final String ERROR_VIEWCONTAINERNULL = "Either the View or Container was null when trying to dismiss. Did you create and " +
			"show a SuperCardToast before trying to dismiss it?";


	/**
	 * This style implements a edit icon with a dark theme background.
	 */
	public static final SuperCardToastStyle STYLE_EDITDARK = new SuperCardToastStyle
			(com.extlibsupertoasts.R.drawable.icon_dark_edit, "EDIT", (Color.WHITE), (Color.WHITE), 
					(com.extlibsupertoasts.R.drawable.background_greytranslucent), (Color.WHITE));
	
	/**
	 * This style implements a exit icon with a dark theme background.
	 */
	public static final SuperCardToastStyle STYLE_EXITDARK = new SuperCardToastStyle
			(com.extlibsupertoasts.R.drawable.icon_dark_exit, "EXIT", (Color.WHITE), (Color.WHITE), 
					(com.extlibsupertoasts.R.drawable.background_greytranslucent), (Color.WHITE));
	
	/**
	 * This style implements a information icon with a dark theme background.
	 */
	public static final SuperCardToastStyle STYLE_INFODARK = new SuperCardToastStyle
			(com.extlibsupertoasts.R.drawable.icon_dark_info, "INFO", (Color.WHITE), (Color.WHITE), 
					(com.extlibsupertoasts.R.drawable.background_greytranslucent), (Color.WHITE));
	
	/**
	 * This style implements a redo icon with a dark theme background.
	 */
	public static final SuperCardToastStyle STYLE_REDODARK = new SuperCardToastStyle
			(com.extlibsupertoasts.R.drawable.icon_dark_redo, "REDO", (Color.WHITE), (Color.WHITE), 
					(com.extlibsupertoasts.R.drawable.background_greytranslucent), (Color.WHITE));
	
	/**
	 * This style implements a save icon with a dark theme background.
	 */
	public static final SuperCardToastStyle STYLE_SAVEDARK = new SuperCardToastStyle
			(com.extlibsupertoasts.R.drawable.icon_dark_save, "SAVE", (Color.WHITE), (Color.WHITE), 
					(com.extlibsupertoasts.R.drawable.background_greytranslucent), (Color.WHITE));
	
	/**
	 * This style implements a share icon with a dark theme background.
	 */
	public static final SuperCardToastStyle STYLE_SHAREDARK = new SuperCardToastStyle
			(com.extlibsupertoasts.R.drawable.icon_dark_share, "SHARE", (Color.WHITE), (Color.WHITE), 
					(com.extlibsupertoasts.R.drawable.background_greytranslucent), (Color.WHITE));
	
	/**
	 * This style implements a undo icon with a dark theme background.
	 */
	public static final SuperCardToastStyle STYLE_UNDODARK = new SuperCardToastStyle
			(com.extlibsupertoasts.R.drawable.icon_dark_undo, "UNDO", (Color.WHITE), (Color.WHITE), 
					(com.extlibsupertoasts.R.drawable.background_greytranslucent), (Color.WHITE));
	
	/**
	 * This style implements a edit icon with a light theme background.
	 */
	public static final SuperCardToastStyle STYLE_EDITLIGHT = new SuperCardToastStyle
			(com.extlibsupertoasts.R.drawable.icon_light_edit, "EDIT", (Color.DKGRAY), (Color.DKGRAY), 
					(com.extlibsupertoasts.R.drawable.background_whitetranslucent), (Color.DKGRAY));
	
	/**
	 * This style implements a exit icon with a light theme background.
	 */
	public static final SuperCardToastStyle STYLE_EXITLIGHT = new SuperCardToastStyle
			(com.extlibsupertoasts.R.drawable.icon_light_exit, "EXIT", (Color.DKGRAY), (Color.DKGRAY), 
					(com.extlibsupertoasts.R.drawable.background_whitetranslucent), (Color.DKGRAY));
	
	/**
	 * This style implements a information icon with a light theme background.
	 */
	public static final SuperCardToastStyle STYLE_INFOLIGHT = new SuperCardToastStyle
			(com.extlibsupertoasts.R.drawable.icon_light_info, "INFO", (Color.DKGRAY), (Color.DKGRAY), 
					(com.extlibsupertoasts.R.drawable.background_whitetranslucent), (Color.DKGRAY));
	
	/**
	 * This style implements a redo icon with a light theme background.
	 */
	public static final SuperCardToastStyle STYLE_REDOLIGHT = new SuperCardToastStyle
			(com.extlibsupertoasts.R.drawable.icon_light_redo, "REDO", (Color.DKGRAY), (Color.DKGRAY), 
					(com.extlibsupertoasts.R.drawable.background_whitetranslucent), (Color.DKGRAY));
	
	/**
	 * This style implements a save icon with a light theme background.
	 */
	public static final SuperCardToastStyle STYLE_SAVELIGHT = new SuperCardToastStyle
			(com.extlibsupertoasts.R.drawable.icon_light_save, "SAVE", (Color.DKGRAY), (Color.DKGRAY), 
					(com.extlibsupertoasts.R.drawable.background_whitetranslucent), (Color.DKGRAY));
	
	/**
	 * This style implements a share icon with a light theme background.
	 */
	public static final SuperCardToastStyle STYLE_SHARELIGHT = new SuperCardToastStyle
			(com.extlibsupertoasts.R.drawable.icon_light_share, "SHARE", (Color.DKGRAY), (Color.DKGRAY), 
					(com.extlibsupertoasts.R.drawable.background_whitetranslucent), (Color.DKGRAY));
	
	/**
	 * This style implements a undo icon with a light theme background.
	 */
	public static final SuperCardToastStyle STYLE_UNDOLIGHT = new SuperCardToastStyle
			(com.extlibsupertoasts.R.drawable.icon_light_undo, "UNDO", (Color.DKGRAY), (Color.DKGRAY), 
					(com.extlibsupertoasts.R.drawable.background_whitetranslucent), (Color.DKGRAY));
	

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
	private CharSequence buttonTextCharSequence;	
	private boolean touchDismiss;
	private boolean touchImmediateDismiss;
	private boolean swipeDismiss;
	private int backgroundResource = (SuperToastConstants.BACKGROUND_BLACK);
	private int dividerResource = (com.extlibsupertoasts.R.color.white);
	private int textColor = (Color.WHITE);
	private int buttonTextColor = (Color.WHITE);
	private int buttonResource = (SuperToastConstants.BUTTON_DARK_UNDO);
	private int duration = (SuperToastConstants.DURATION_LONG);
	private Typeface typeface = (Typeface.DEFAULT);
	private Typeface buttonTextTypeface = (Typeface.DEFAULT_BOLD);
	private Drawable backgroundDrawable;
	private Drawable dividerDrawable;
	private Drawable buttonDrawable;
	private boolean isIndeterminate;
	private float textSize = (SuperToastConstants.TEXTSIZE_SMALL);
	private float buttonTextSize = (SuperToastConstants.TEXTSIZE_MEDIUM);
	private OnClickListener mOnClickListener;
	private OnClickListener mButtonOnClickListener;
	private boolean isProgressIndeterminate;
	private OnDismissListener mOnDismissListener;
	
	/**
	 * This is used to specify the type of SuperCardToast to 
	 * be used.
	 * 
	 */
	public enum Type {
		
		/**
		 * Standard Toast type used for displaying messages.
		 */
		STANDARD, 
		
		/**
		 * Progress type used for showing progress.
		 */
		PROGRESS, 
		
		/**
		 * Button type used for user interaction.
		 */
		BUTTON;
		
	}

	/**
	 * This is used to specify the style of a progress SuperCardToast.
	 * 
	 */
	public enum ProgressStyle {
		
		/**
		 * Circle style with a rotating indicator.
		 */
		CIRCLE, 
		
		/**
		 * Horizontal style with a line indicator.
		 */
		HORIZONTAL;
		
	}
	
	/**
	 * Instantiates a new SuperCardToast. You <b>MUST</b> pass an Activity
	 * as a Context. The style of this SuperCardToast will be Standard.
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
	
	/**
	 * Instantiates a new SuperActivityToast. You <b>MUST</b> pass an Activity
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
	 * @param mType 
	 * <br>
	 * Example: Type.STANDARD
	 * <br>
	 * 
	 */
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

	
	/**
	 * This is used to show the SuperCardToast. You should
	 * do all of your modifications to the SuperCardToast before calling
	 * this method. 
	 */
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
		
		if (touchDismiss || touchImmediateDismiss) {

			if (touchDismiss) {

				toastView.setOnTouchListener(mTouchDismissListener);

			}

			else if (touchImmediateDismiss) {

				toastView.setOnTouchListener(mTouchImmediateDismissListener);

			}

		} else if (sdkVersion > android.os.Build.VERSION_CODES.HONEYCOMB_MR1
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

				Log.e(TAG, ERROR_NOCLICKLISTENER);
				
			}

			mButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, buttonTextSize);
			mButton.setTextColor(buttonTextColor);
			mButton.setText(buttonTextCharSequence);
			mButton.setTypeface(buttonTextTypeface);

			
			mDivider = (View) 
					toastView.findViewById(R.id.dividerView);
			
			if (dividerDrawable != null) {

				if (sdkVersion < android.os.Build.VERSION_CODES.JELLY_BEAN) {

					mDivider.setBackgroundDrawable(dividerDrawable);

				} else {

					mDivider.setBackground(dividerDrawable);

				}

			} else {

				mDivider.setBackgroundResource(dividerResource);

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

		toastView.startAnimation(mAnimation);
			
	}
	
	
	//XXX: General methods.
	
	
	/**
	 * This is used to set the message text of the SuperCardToast.
	 * 
	 * <br>
	 * 
	 * <p>
	 * <b> Important note: </b>
	 * </p>
	 * 
	 * <p>
	 * This method can be called again while the SuperCardToast is showing
	 * to modify the existing message. If your application might show two
	 * SuperCardToasts at one time you should try to reuse the same
	 * SuperCardToast by calling this method and {@link #resetDuration(int)}
	 * </p>
	 * 
	 * <br>
	 * 
	 * <p>
	 * <b> Design guide: </b>
	 * </p>
	 * 
	 * <p>
	 * Toasts/SuperCardToast are designed to display short non-essential
	 * messages such as "Message sent!" after the user sends a SMS. Generally
	 * these messages should rarely display more than one line of text.
	 * </p>
	 * 
	 * <br>
	 * 
	 * @param textCharSequence 
	 * 		
	 * <br>
	 * 
	 */
	public void setText(CharSequence textCharSequence) {

		this.textCharSequence = textCharSequence;

		if (mTextView != null) {

			mTextView.setText(textCharSequence);

		}

	}
	
	
	/**
	 * This is used to set the message text color of the SuperCardToast.
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

		if (mTextView != null) {

			mTextView.setTextColor(textColor);

		}

	}
	
	
	/**
	 * This is used to set the style of the SuperCardToast.
	 * 
	 * <br>
	 * 
	 * @param mSuperCardToastStyle
	 * <br>
	 * Example: (SuperCardToastStyle.STYLE_UNDODARK)
	 * <br>
	 * 
	 */
	public void setStyle(SuperCardToastStyle mSuperCardToastStyle) {

		this.buttonResource = mSuperCardToastStyle.undoButtonResource;
		this.buttonTextCharSequence = mSuperCardToastStyle.buttonTextCharSequence;
		this.textColor = mSuperCardToastStyle.messageTextColor;
		this.buttonTextColor = mSuperCardToastStyle.buttonTextColor;
		this.backgroundResource = mSuperCardToastStyle.backgroundResource;
		this.dividerResource = mSuperCardToastStyle.dividerResource;

	}

	
	/**
	 * This is used to set the duration of the SuperCardToast.
	 * 
	 * <br>
	 * 
	 * <p>
	 * <b> Design guide: </b>
	 * </p>
	 * 
	 * <p>
	 * Generally short durations are preferred. 
	 * </p>
	 * 
	 * <br>
	 * @param duration
	 * <br>
	 * Example: (SuperToastConstants.DURATION_SHORT)
	 * <br>
	 * 
	 */
	public void setDuration(int duration) {

		this.duration = duration;

	}
	
	
	/**
	 * This is used to reset the duration of the SuperCardToast 
	 * while it is showing.
	 * 
	 * <br>
	 * 
	 * <p>
	 * <b> Design guide: </b>
	 * </p>
	 * 
	 * <p>
	 * Instead of having overlapping or sequential messages you
	 * should use this method to reuse an already showing SuperCardToast
	 * in instances where two or more messages can be showing at the same time.
	 * </p>
	 * 
	 * <br>
	 * @param newDuration 
	 * <br>
	 * Example: (SuperToastConstants.DURATION_SHORT)
	 * <br>
	 * 
	 */
	public void resetDuration(int newDuration) {

		if (mHandler != null) {

			mHandler.removeCallbacks(mHideRunnable);
			mHandler = null;

		}

		mHandler = new Handler();
		mHandler.postDelayed(mHideRunnable, newDuration);

	}

	
	/**
	 * This is used to set an indeterminate duration of the SuperCardToast.
	 * 
	 * <br>
	 * 
	 * <p>
	 * <b> Design guide: </b>
	 * </p>
	 * 
	 * <p>
	 * This should be used in conjunction with the PROGRESS Type SuperCardToast.
	 * Any duration set via {@link #setDuration(int)} will be ignored. 
	 * </p>
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
	 * This is used to set a private OnTouchListener to the SuperCardToast
	 * that will dismiss the SuperCardToast with an Animation.
	 * 
	 * <br>
	 * 
	 * <p>
	 * <b> Design guide: </b>
	 * </p>
	 * 
	 * <p>
	 * This method should be used with long running SuperCardToasts in case
	 * the SuperCardToast comes in between application content and the user.
	 * This method is not compatible with {@link #setOnClickListener(OnClickListener)}.
	 * </p>
	 * 
	 * <br>
	 * 
	 * @param touchDismiss 
	 * 		
	 * <br>
	 * 
	 */
	public void setTouchToDismiss(boolean touchDismiss) {

		this.touchDismiss = touchDismiss;

	}
	
	
	/**
	 * This is used to set a private OnTouchListener to the SuperCardToast
	 * that will dismiss the SuperCardToast immediately without an Animation.
	 * 
	 * <br>
	 * 
	 * <p>
	 * <b> Design guide: </b>
	 * </p>
	 * 
	 * <p>
	 * This method should be used with long running SuperActivityToasts in case
	 * the SuperActivityToast comes in between application content and the user.
	 * This method is not compatible with {@link #setOnClickListener(OnClickListener)}.
	 * </p>
	 * 
	 * <br>
	 * 
	 * @param touchImmediateDismiss 
	 * 		
	 * <br>
	 * 
	 */
	public void setTouchToImmediateDismiss(boolean touchImmediateDismiss) {

		this.touchImmediateDismiss = touchImmediateDismiss;

	}
	
	
	/**
	 * This is used to set a private SwipeDismissListener to the SuperCardToast
	 * that will dismiss the SuperCardToast when the user swipes the SuperCardToast 
	 * left or right.
	 * 
	 * <br>
	 * 
	 * <p>
	 * <b> Design guide: </b>
	 * </p>
	 * 
	 * <p>
	 * This method should be used with long running SuperCardToasts in case
	 * the SuperCardToast comes in between application content and the user.
	 * This method is not compatible with {@link #setTouchToDismiss(boolean)}.
	 * </p>
	 * 
	 * 
	 * <p>
	 * <b> Important note: </b>
	 * </p>
	 * 
	 * <p>
	 * This method does not work on pre-honeycomb devices.
	 * </p>
	 * 
	 * <br>
	 * 
	 * @param swipeDismiss 
	 * 		
	 * <br>
	 * 
	 */
	public void setSwipeToDismiss(boolean swipeDismiss) {

		this.swipeDismiss = swipeDismiss;

	}
	
	/**
	 * This is used to set an OnClickListener to the SuperCardToast.
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
	 * This is used to set the background of the SuperCardToast.
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
	 * This is used to set the background of the SuperCardToast.
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
	 * This is used to set the text size of the SuperCardToast.
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
	 * This is used to set the Typeface of the SuperCardToast text.
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
	 * Example: (Typeface.DEFAULT) OR (mSuperCardToast.loadRobotoTypeface(SuperToastConstants.
	 * FONT_ROBOTO_THIN);	 * 		
	 * <br>
	 * 
	 */
	public void setTypeface(Typeface typeface) {

		this.typeface = typeface;

	}	
	
	
	/**
	 * This is used to set an OnDismissListener to the SuperCardToast.
	 * 
	 * <br>
	 * 
	 * <p>
	 * <b> Important note: </b>
	 * </p>
	 * 
	 * <p>
	 * Make sure that the OnDismissListener is imported from this library.
	 * This method is not compatible with other OnDismissListeners.
	 * </p>
	 * 
	 * <br>
	 * @param mOnDismissListener 
	 * <br>
	 * 
	 */
	public void setOnDismissListener(OnDismissListener mOnDismissListener) {

		this.mOnDismissListener = mOnDismissListener;

	}
	
	
	/**
	 * This is used to dismiss the SuperCardToast.
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
		
		if(mHandler != null) { 
			
			mHandler.removeCallbacks(mHideRunnable);
			mHandler = null;
			
		}

		if (toastView != null && mContainer != null) {

			mContainer.removeView(toastView);
			toastView = null;

		} else {

			Log.e(TAG, ERROR_VIEWCONTAINERNULL);

		}
		
		if(mOnDismissListener != null) {
			
			mOnDismissListener.onDismiss();
			
		}

	}
	
	
	//XXX Progress specific methods.
	
	
	/**
	 * This is used to set the ProgressStyle of a PROGRESS Type SuperCardToast.
	 * 
	 * <br>
	 * @param mProgressStyle 
	 * <br>
	 * Example: (ProgressStyle.CIRCLE)
	 * <br>
	 * 
	 */
	public void setProgressStyle(ProgressStyle mProgressStyle) {

		this.mProgressStyle = mProgressStyle;

	}
	
	
	/**
	 * This is used to set an indeterminate value to the ProgressBar
	 * in a PROGRESS Type SuperCardToast.
	 * 
	 * <br>
	 * @param isProgressIndeterminate 
	 * <br>
	 * 
	 */
	public void setProgressIndeterminate(boolean isProgressIndeterminate) {

		this.isProgressIndeterminate = isProgressIndeterminate;

	}
	
	
	/**
	 * This is used to set the progress of the ProgressBar in a
	 * Progress Type SuperCardToast.
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
	
	
	//XXX Button specific methods.
	
	
	/**
	 * This is used to set the an OnClickListener to the Button in 
	 * a BUTTON Type SuperCardToast.
	 * 
	 * <br>
	 * @param mButtonOnClickListener 
	 * <br>
	 * 
	 */
	public void setButtonOnClickListener(OnClickListener mButtonOnClickListener) {

		this.mButtonOnClickListener = mButtonOnClickListener;

	}
	
	
	/**
	 * This is used to set the text size of the Button in 
	 * a BUTTON Type SuperCardToast.
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
	 * @param buttonTextSize 
	 * <br>
	 * Example: (SuperToastConstants.TEXTSIZE_SMALL)		
	 * <br>
	 * 
	 */
	public void setButtonTextSize(int buttonTextSize) {

		this.buttonTextSize = buttonTextSize;

	}

	/**
	 * This is used to set the Typeface of the Button in 
	 * a BUTTON Type SuperCardToast.
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
	 * Example: (Typeface.DEFAULT) OR (mSuperCardToast.loadRobotoTypeface(SuperToastConstants.
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
	 * This is used to set the background of the Button in 
	 * a BUTTON Type SuperCardToast.
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
	 * @param buttonResource
	 * <br>
	 * Example: (SuperToastConstants.BUTTON_DARK_REDO)
	 * <br>
	 * 
	 */
	public void setButtonResource(int buttonResource) {

		this.buttonResource = buttonResource;

	}
	
	
	/**
	 * This is used to set the background Drawable of the Button in 
	 * a BUTTON Type SuperCardToast.	 
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
	 * @param buttonDrawable 
	 * <br>
	 * 
	 */
	public void setButtonDrawable(Drawable buttonDrawable) {

		this.buttonDrawable = buttonDrawable;

	}
	
	
	/**
	 * This is used to set the resource of the divider of the Button in 
	 * a BUTTON Type SuperCardToast.	 
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
	 * This is used to set the Drawable of the divider of the Button in 
	 * a BUTTON Type SuperCardToast.	 
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
	 * This is used to set the Button text color of the SuperCardToast.
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
	 * here. This color should also match the color specified in {@link #setTextColor(int)}.
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
	 * This is used to set the message text of the Button in the SuperCardToast.
	 * <br>
	 * 
	 * <p>
	 * <b> Design guide: </b>
	 * </p>
	 * 
	 * <p>
	 * The text of the SuperCardToast should be short, and all capital letters.
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

	
	// XXX: Getter methods.

	
	/**
	 * This is used to get the SuperCardToast message TextView.
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
	 * This is used to get the SuperCardToast View.
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
	 * Returns true of the SuperCardToast is currently visible 
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
	 * 
	 * @return Typeface
	 * 
	 * <br>
	 * 
	 */
	public Typeface loadRobotoTypeface(String typefaceString) {

		return Typeface.createFromAsset(mContext.getAssets(), typefaceString);

	}
	
	
	//XXX: Private methods.
	
	
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
            	 
    					/** Must use Handler to modify ViewGroup in onAnimationEnd() **/
    					Handler mHandler = new Handler();
    					mHandler.post(mHideImmediateRunnable);
    					
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
        	        	
        	dismissImmediately();
        	 
        }
        
    };
    
    
	private Runnable mInvalidateRunnable = new Runnable() 
	{
		 
        public void run() 
        {
        	        	
        	if(mContainer != null) {
        		
        		mContainer.invalidate();
        		
        	}
        	 
        }
        
    };
    
	private OnTouchListener mTouchDismissListener = new OnTouchListener() {

		int timesTouched;

		@Override
		public boolean onTouch(View view, MotionEvent event) {

			/** This is a little hack to prevent the user from repeatedly 
			 *  touching the SuperCardToast causing erratic behavior **/
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

}