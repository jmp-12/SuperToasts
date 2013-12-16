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

package com.github.johnpersano.supertoasts;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

import java.util.Iterator;
import java.util.LinkedList;

/** Manages the life of a SuperActivityToast. Copied from the Crouton library. */
public class ManagerSuperActivityToast extends Handler {
	
	private static final String TAG = "ManagerSuperActivityToast";

	private static final class Messages {

        /** Hexadecimal numbers that represent acronyms for the operation. **/
		private static final int DISPLAY= 0x44534154;
		private static final int ADD = 0x41534154;
		private static final int REMOVE= 0x52534154;

	}

	private static ManagerSuperActivityToast mManagerSuperActivityToast;

	private LinkedList<SuperActivityToast> mList;

	private ManagerSuperActivityToast() {

        mList = new LinkedList<SuperActivityToast>();

	}

	protected static synchronized ManagerSuperActivityToast getInstance() {

		if (mManagerSuperActivityToast != null) {

			return mManagerSuperActivityToast;

		} else {

			mManagerSuperActivityToast = new ManagerSuperActivityToast();

			return mManagerSuperActivityToast;

		}

	}
	

	protected void add(SuperActivityToast superActivityToast) {

        mList.add(superActivityToast);
		this.showNextSuperToast();

	}

	
	private void showNextSuperToast() {

        final SuperActivityToast superActivityToast = mList.peek();

        if (mList.isEmpty() || superActivityToast.getActivity() == null) {

			return;

		}

		if (!superActivityToast.isShowing()) {

			sendMessage(superActivityToast, Messages.ADD);

		} else {

			sendMessageDelayed(superActivityToast,
					Messages.DISPLAY,
					getDuration(superActivityToast));

		}

	}

	
	private void sendMessage(SuperActivityToast superActivityToast,
			final int messageId) {

		final Message message = obtainMessage(messageId);
		message.obj = superActivityToast;
		sendMessage(message);

	}

	private void sendMessageDelayed(SuperActivityToast superActivityToast,
			final int messageId, final long delay) {

		Message message = obtainMessage(messageId);
		message.obj = superActivityToast;
		sendMessageDelayed(message, delay);

	}

	private long getDuration(SuperActivityToast superActivityToast) {

		long duration = superActivityToast.getDuration();
		duration += superActivityToast.getShowAnimation().getDuration();
		duration += superActivityToast.getDismissAnimation().getDuration();

		return duration;

	}

	@Override
	public void handleMessage(Message message) {

		final SuperActivityToast superActivityToast = (SuperActivityToast)
				message.obj;

		switch (message.what) {

			case Messages.DISPLAY:

				showNextSuperToast();

				break;

			case Messages.ADD:

				displaySuperToast(superActivityToast);

				break;

			case Messages.REMOVE:

				removeSuperToast(superActivityToast);

				break;

			default: {

				super.handleMessage(message);

				break;

			}

		}

	}

	private void displaySuperToast(SuperActivityToast superActivityToast) {

		if (superActivityToast.isShowing()) {

			return;

		}

		final ViewGroup viewGroup = superActivityToast.getViewGroup();

		final View toastView = superActivityToast.getView();
		
		//TODO: Temporary fix for orientation change crash.
		if(viewGroup != null) {
			
			try {
				
				viewGroup.addView(toastView);

				toastView.startAnimation(superActivityToast.getShowAnimation());
				
			} catch(IllegalStateException e) {
				
				Log.e(TAG, e.toString());
				
				clearSuperActivityToastsForActivity(superActivityToast.getActivity());

			}

		}

		if(!superActivityToast.isIndeterminate()) {
			
			sendMessageDelayed(superActivityToast, Messages.REMOVE,
					superActivityToast.getDuration() + superActivityToast.getShowAnimation().getDuration());
			
		}

	}

	protected void removeSuperToast(SuperActivityToast superActivityToast) {

        final ViewGroup viewGroup = superActivityToast.getViewGroup();

		final View toastView = superActivityToast.getView();

		if (viewGroup != null) {

            Animation animation = superActivityToast.getDismissAnimation();

            animation.setAnimationListener(new Animation.AnimationListener() {

                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {

                    ManagerSuperActivityToast.this.showNextSuperToast();

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

			toastView.startAnimation(animation);

			viewGroup.removeView(toastView);

            mList.poll();

			sendMessageDelayed(superActivityToast,
                    Messages.DISPLAY, superActivityToast
                    .getDismissAnimation().getDuration());
			
			if(superActivityToast.getOnDismissListener() != null) {
				
				superActivityToast.getOnDismissListener().onDismiss();
				
			}

		}

        removeMessages(Messages.ADD);
        removeMessages(Messages.DISPLAY);
        removeMessages(Messages.REMOVE);

    }

	protected void clearQueue() {

        removeMessages(Messages.ADD);
        removeMessages(Messages.DISPLAY);
        removeMessages(Messages.REMOVE);

		if (mList != null) {

			for (SuperActivityToast superActivityToast : mList) {

				if (superActivityToast.isShowing()) {

					superActivityToast.getViewGroup().removeView(
							superActivityToast.getView());

                    superActivityToast.getViewGroup().invalidate();

                }

			}

            mList.clear();

		}

	}

	protected void clearSuperActivityToastsForActivity(Activity activity) {

		if (mList != null) {

			Iterator<SuperActivityToast> superActivityToastIterator = mList
					.iterator();

			while (superActivityToastIterator.hasNext()) {

				SuperActivityToast superActivityToast = superActivityToastIterator
						.next();

				if ((superActivityToast.getActivity()) != null
						&& superActivityToast.getActivity().equals(activity)) {

					if (superActivityToast.isShowing()) {

						superActivityToast.getViewGroup().removeView(
								superActivityToast.getView());

					}

                    removeMessages(Messages.ADD, superActivityToast);
                    removeMessages(Messages.DISPLAY, superActivityToast);
                    removeMessages(Messages.REMOVE, superActivityToast);

					superActivityToastIterator.remove();

				}

			}

		}

	}

    protected LinkedList<SuperActivityToast> getList() {

        return mList;

    }


}
