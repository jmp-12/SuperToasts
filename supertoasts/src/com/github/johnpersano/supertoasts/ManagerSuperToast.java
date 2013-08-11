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

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;


import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;

/** Manages the life of a SuperActivityToast. Copied from the Crouton library. */
public class ManagerSuperToast extends Handler {

    private static final String TAG = "ManagerSuperToast";

    private static final class Messages {

        /** Hexadecimal numbers that represent acronyms for the operation. **/
		private static final int DISPLAY_SUPERTOAST = 0x445354;
		private static final int ADD_SUPERTOAST = 0x415354;
		private static final int REMOVE_SUPERTOAST = 0x525354;

		private Messages() {

			// Do nothing

		}

	}

	private static ManagerSuperToast mManagerSuperToast;

	private Queue<SuperToast> mQueue;

	private ManagerSuperToast() {

		mQueue = new LinkedBlockingQueue<SuperToast>();

	}

	protected static synchronized ManagerSuperToast getInstance() {

		if (mManagerSuperToast != null) {

			return mManagerSuperToast;

		} else {

			mManagerSuperToast = new ManagerSuperToast();

			return mManagerSuperToast;

		}

	}
	

	protected void add(SuperToast superToast) {

		mQueue.add(superToast);
		this.showNextSuperToast();

	}

	
	private void showNextSuperToast() {

		if (mQueue.isEmpty()) {

			return;

		}

		final SuperToast superToast = mQueue.peek();

		if (!superToast.isShowing()) {

			sendMessage(superToast, Messages.ADD_SUPERTOAST);

		} else {

			sendMessageDelayed(superToast,
					Messages.DISPLAY_SUPERTOAST,
					getDuration(superToast));

		}

	}

	private void sendMessage(SuperToast superToast,
			final int messageId) {

		final Message message = obtainMessage(messageId);
		message.obj = superToast;
		sendMessage(message);

	}

	private void sendMessageDelayed(SuperToast superToast,
			final int messageId, final long delay) {

		Message message = obtainMessage(messageId);
		message.obj = superToast;
		sendMessageDelayed(message, delay);

	}
	
	private long getDuration(SuperToast superToast) {

		long duration = superToast.getDuration();
		duration += 1000;
		
		return duration;

	}

	@Override
	public void handleMessage(Message message) {

		final SuperToast superToast = (SuperToast) 
				message.obj;

		switch (message.what) {

			case Messages.DISPLAY_SUPERTOAST:
	
				showNextSuperToast();
	
				break;
	
			case Messages.ADD_SUPERTOAST:
	
				displaySuperToast(superToast);
	
				break;
	
			case Messages.REMOVE_SUPERTOAST:
	
				removeSuperToast(superToast);
	
				break;
	
			default: {
	
				super.handleMessage(message);
	
				break;

			}

		}

	}

	private void displaySuperToast(SuperToast superToast) {

		if (superToast.isShowing()) {

			return;

		}

		final WindowManager windowManager = superToast
				.getWindowManager();

		final View toastView = superToast.getView();
		
		final WindowManager.LayoutParams params = superToast
				.getWindowManagerParams();
		
		if(windowManager != null) {
			
			windowManager.addView(toastView, params);

		}
			
		sendMessageDelayed(superToast, Messages.REMOVE_SUPERTOAST,
				superToast.getDuration() + 500);
			
	}
	
	protected void removeSuperToast(SuperToast superToast) {
		
		final WindowManager windowManager = superToast
				.getWindowManager();

		final View toastView = superToast.getView();

		if (windowManager != null) {

			mQueue.poll();

			windowManager.removeView(toastView);

			sendMessageDelayed(superToast,
					Messages.DISPLAY_SUPERTOAST, 500);
			
			if(superToast.getOnDismissListener() != null) {
				
				superToast.getOnDismissListener().onDismiss();
				
			}

		}

	}
	
	protected void clearQueue() {

		removeAllMessages();

		if (mQueue != null) {

			for (SuperToast superToast : mQueue) {

				if (superToast.isShowing()) {

					superToast.getWindowManager().removeView(
							superToast.getView());

				}

			}

			mQueue.clear();

		}

	}
	
	private void removeAllMessages() {

		removeMessages(Messages.ADD_SUPERTOAST);
		removeMessages(Messages.DISPLAY_SUPERTOAST);
		removeMessages(Messages.REMOVE_SUPERTOAST);

	}

}
