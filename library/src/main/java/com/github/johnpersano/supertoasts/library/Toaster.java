/*
 * Copyright 2013-2016 John Persano
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.johnpersano.supertoasts.library;

import android.animation.Animator;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.WindowManager;

import com.github.johnpersano.supertoasts.library.utils.AnimationUtils;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * The Toaster class handles the show and hide function of a
 * {@link com.github.johnpersano.supertoasts.library.SuperToast} and
 * {@link com.github.johnpersano.supertoasts.library.SuperActivityToast}.
 * This class cannot (and should not) be used directly.
 */
class Toaster extends Handler {

    private static final String ERROR_SAT_VIEWGROUP_NULL = "The SuperActivityToast's ViewGroup " +
            "was null, could not show.";
    private static final String ERROR_ST_WINDOWMANAGER_NULL = "The SuperToast's WindowManager " +
            "was null when trying to remove the SuperToast.";

    // Potential messages for the handler to send
    private static final class Messages {
        // Hexadecimal numbers that represent acronyms for the operation
        private static final int DISPLAY_SUPERTOAST = 0x445354;
        private static final int SHOW_NEXT = 0x415354;
        private static final int REMOVE_SUPERTOAST = 0x525354;
    }

    /**
     * Comparator used for ordering {@link SuperToast}s in their queue.
     */
    private class SuperToastComparator implements Comparator<SuperToast> {

        @Override
        public int compare(SuperToast x, SuperToast y) {
            // Do not shuffle around any showing SuperToasts
            if (x.isShowing()) return -1;

            if (x.getStyle().priorityLevel < y.getStyle().priorityLevel) return -1;
            else if (x.getStyle().priorityLevel > y.getStyle().priorityLevel) return 1;

            // PriorityQueue uses a heap, we want to maintain insertion order
            else return x.getStyle().timestamp <= y.getStyle().timestamp ? -1 : 1;
        }
    }

    /**
     * Gets the current instance of the Toaster. If there is no extant instance,
     * a new one will be created.
     *
     * @return The current Toaster instance
     */
    static synchronized Toaster getInstance() {
        if (mToaster != null) return mToaster;
        else {
            mToaster = new Toaster();
            return mToaster;
        }
    }

    private static Toaster mToaster;
    private final PriorityQueue<SuperToast> superToastPriorityQueue;

    // Create a new PriorityQueue when the Toaster class is first initialized
    private Toaster() {
        superToastPriorityQueue = new PriorityQueue<>(10, new SuperToastComparator());
    }

    /**
     * Adds a SuperToast or SuperActivityToast to the current queue.
     *
     * @param superToast The SuperToast or SuperActivityToast to be shown
     */
    void add(SuperToast superToast) {
        // Add SuperToast to queue and try to show it
        superToastPriorityQueue.add(superToast);
        this.showNextSuperToast();
    }

    /**
     * Show the next SuperToast in the current queue. If a SuperToast is currently showing,
     * do nothing. The currently showing SuperToast will call this method when it dismisses.
     */
    private void showNextSuperToast() {
        // Do nothing if the queue is empty
        if (superToastPriorityQueue.isEmpty()) return;

        // Get next SuperToast in the queue
        final SuperToast superToast = superToastPriorityQueue.peek();
        if (!superToast.isShowing()) {
            final Message message = obtainMessage(Messages.DISPLAY_SUPERTOAST);
            message.obj = superToast;
            sendMessage(message);
        }
    }

    /**
     * Send a message at a later time. This is used to dismiss a SuperToast.
     */
    private void sendDelayedMessage(SuperToast superToast, int messageId, long delay) {
        Message message = obtainMessage(messageId);
        message.obj = superToast;
        sendMessageDelayed(message, delay);
    }

    @Override
    public void handleMessage(Message message) {
        final SuperToast superToast = (SuperToast) message.obj;
        switch (message.what) {
            case Messages.SHOW_NEXT:
                showNextSuperToast();
                break;
            case Messages.DISPLAY_SUPERTOAST:
                displaySuperToast(superToast);
                break;
            case Messages.REMOVE_SUPERTOAST:
                removeSuperToast(superToast);
                break;
            default:
                super.handleMessage(message);
                break;
        }
    }

    /**
     * Try to show the SuperToast. SuperToasts will be shown using the WindowManager while
     * SuperActivityToasts will be shown using their supplied ViewGroup.
     * @param superToast the SuperToast (or SuperActivityToast) to be shown
     */
    private void displaySuperToast(SuperToast superToast) {

        // Make sure the SuperToast isn't already showing for some reason
        if (superToast.isShowing()) return;

        // If the SuperToast is a SuperActivityToast, show it via the supplied ViewGroup
        if (superToast instanceof SuperActivityToast) {
            if (((SuperActivityToast) superToast).getViewGroup() == null) {
                Log.e(getClass().getName(), ERROR_SAT_VIEWGROUP_NULL);
                return;
            }

            try {
                ((SuperActivityToast) superToast).getViewGroup().addView(superToast.getView());

                // Do not use the show animation on the first SuperToast if from orientation change
                if (!((SuperActivityToast) superToast).isFromOrientationChange()) {
                    AnimationUtils.getShowAnimation((SuperActivityToast) superToast).start();
                }
            } catch (IllegalStateException illegalStateException) {
                Log.e(getClass().getName(), illegalStateException.toString());
            }

            if (!((SuperActivityToast) superToast).isIndeterminate()) {
                // This will remove the SuperToast after the total duration
                sendDelayedMessage(superToast, Messages.REMOVE_SUPERTOAST,
                        superToast.getDuration() + AnimationUtils.SHOW_DURATION);
            }

        // The SuperToast is NOT a SuperActivityToast, show it via the WindowManager
        } else {
            final WindowManager windowManager = (WindowManager) superToast.getContext()
                    .getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
            if (windowManager != null) {
                windowManager.addView(superToast.getView(), superToast.getWindowManagerParams());
            }

            // This will remove the SuperToast after a certain duration
            sendDelayedMessage(superToast, Messages.REMOVE_SUPERTOAST,
                    superToast.getDuration() + AnimationUtils.SHOW_DURATION);
        }
    }

    /**
     * Removes a showing SuperToast. This method will poll the Queue as well as try
     * to show the next SuperToast if one exists in the Queue.
     * @param superToast the SuperToast (or SuperActivityToast) to be removed
     */
    void removeSuperToast(final SuperToast superToast) {
        // If the SuperToast is a SuperActivityToast, remove it from the supplied ViewGroup
        if (superToast instanceof SuperActivityToast) {
            // If SuperActivityToast has already been dismissed, do not attempt to dismiss it again
            if (!superToast.isShowing()) {
                this.superToastPriorityQueue.remove(superToast);
                return;
            }

            final Animator animator = AnimationUtils.getHideAnimation(
                    (SuperActivityToast) superToast);
            animator.addListener(new Animator.AnimatorListener() {

                @Override
                public void onAnimationStart(Animator animation) {
                    // Do nothing
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if (superToast.getOnDismissListener() != null) {
                        superToast.getOnDismissListener().onDismiss(superToast.getView(),
                                superToast.getStyle().dismissToken);
                    }

                    ((SuperActivityToast) superToast).getViewGroup().removeView(superToast.getView());

                    // Show the next SuperToast if any exist
                    Toaster.this.showNextSuperToast();
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    // Do nothing
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                    // Do nothing
                }
            });
            animator.start();

        // If the SuperToast is NOT a SuperActivityToast, remove it from the WindowManager
        } else {
            final WindowManager windowManager = (WindowManager) superToast.getContext()
                    .getSystemService(Context.WINDOW_SERVICE);

            // If the WindowManager is null, the SuperToast will linger indefinitely
            if (windowManager == null) throw new IllegalStateException(ERROR_ST_WINDOWMANAGER_NULL);

            try {
                windowManager.removeView(superToast.getView());
            } catch (IllegalArgumentException illegalArgumentException) {
                Log.e(getClass().getName(), illegalArgumentException.toString());
            }

            if (superToast.getOnDismissListener() != null) {
                superToast.getOnDismissListener().onDismiss(superToast.getView(),
                        superToast.getStyle().dismissToken);
            }

            // Show the next SuperToast in the queue if any exist after the hide duration
            this.sendDelayedMessage(superToast, Messages.SHOW_NEXT, AnimationUtils.HIDE_DURATION);
        }

        superToastPriorityQueue.poll();
    }

    /**
     * Cancels and removes all pending and/or showing SuperToasts and SuperActivityToasts.
     */
    void cancelAllSuperToasts() {
        removeMessages(Messages.SHOW_NEXT);
        removeMessages(Messages.DISPLAY_SUPERTOAST);
        removeMessages(Messages.REMOVE_SUPERTOAST);

        // Iterate through the Queue, polling and removing everything
        for (SuperToast superToast : superToastPriorityQueue) {
            if (superToast instanceof SuperActivityToast) {
                if (superToast.isShowing()) {
                    try{
                        ((SuperActivityToast) superToast).getViewGroup().removeView(superToast.getView());
                        ((SuperActivityToast) superToast).getViewGroup().invalidate();
                    } catch (NullPointerException|IllegalStateException exception) {
                        Log.e(getClass().getName(), exception.toString());
                    }
                }
            } else {
                final WindowManager windowManager = (WindowManager) superToast.getContext()
                        .getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
                if (superToast.isShowing()) {
                    try{
                        windowManager.removeView(superToast.getView());
                    } catch (NullPointerException|IllegalArgumentException exception) {
                        Log.e(getClass().getName(), exception.toString());
                    }
                }
            }
        }
        superToastPriorityQueue.clear();
    }

    /**
     * Returns the {@link PriorityQueue} associated with this Toaster.
     * @return the current PriorityQueue
     */
    public PriorityQueue<SuperToast> getQueue() {
        return this.superToastPriorityQueue;
    }

}