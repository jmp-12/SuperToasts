/**
 *  Copyright 2014 John Persano
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
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.*;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * Manages the life of a SuperActivityToast. Initial code derived from the Crouton library.
 */
class ManagerSuperActivityToast extends Handler {

    @SuppressWarnings("UnusedDeclaration")
    private static final String TAG = "ManagerSuperActivityToast";

    /* Potential messages for the handler to send **/
    private static final class Messages {

        /* Hexadecimal numbers that represent acronyms for the operation. **/
        private static final int DISPLAY = 0x44534154;
        private static final int REMOVE = 0x52534154;

    }

    private static ManagerSuperActivityToast mManagerSuperActivityToast;

    private final LinkedList<SuperActivityToast> mList;

    /* Private method to create a new list if the manager is being initialized */
    private ManagerSuperActivityToast() {

        mList = new LinkedList<SuperActivityToast>();

    }

    /**
     * Singleton method to ensure all SuperActivityToasts are passed through the same manager.
     */
    protected static synchronized ManagerSuperActivityToast getInstance() {

        if (mManagerSuperActivityToast != null) {

            return mManagerSuperActivityToast;

        } else {

            mManagerSuperActivityToast = new ManagerSuperActivityToast();

            return mManagerSuperActivityToast;

        }

    }

    /**
     * Add a SuperActivityToast to the list. Will show immediately if no other SuperActivityToasts
     * are in the list.
     */
    void add(SuperActivityToast superActivityToast) {

        mList.add(superActivityToast);

        this.showNextSuperToast();

    }

    /**
     * Shows the next SuperActivityToast in the list. Called by add() and when the dismiss animation
     * of a previously showing SuperActivityToast ends.
     */
    private void showNextSuperToast() {

        final SuperActivityToast superActivityToast = mList.peek();

        if (mList.isEmpty() || superActivityToast.getActivity() == null) {

            return;

        }

        if (!superActivityToast.isShowing()) {

            final Message message = obtainMessage(Messages.DISPLAY);
            message.obj = superActivityToast;
            sendMessage(message);

        }

    }


    @Override
    public void handleMessage(Message message) {

        final SuperActivityToast superActivityToast = (SuperActivityToast)
                message.obj;

        switch (message.what) {

            case Messages.DISPLAY:

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

    /**
     * Displays a SuperActivityToast.
     */
    private void displaySuperToast(SuperActivityToast superActivityToast) {

        /* If this SuperActivityToast is somehow already showing do nothing */
        if(superActivityToast.isShowing()) {

            return;

        }

        final ViewGroup viewGroup = superActivityToast.getViewGroup();

        final View toastView = superActivityToast.getView();

        if(viewGroup != null) {

            try {

                viewGroup.addView(toastView);

                if(!superActivityToast.getShowImmediate()) {

                    toastView.startAnimation(getShowAnimation(superActivityToast));

                }

            } catch(IllegalStateException e) {

                this.cancelAllSuperActivityToastsForActivity(superActivityToast.getActivity());

            }

        }

        /* Dismiss the SuperActivityToast at the set duration time unless indeterminate */
        if(!superActivityToast.isIndeterminate()) {

            Message message = obtainMessage(Messages.REMOVE);
            message.obj = superActivityToast;
            sendMessageDelayed(message, superActivityToast.getDuration() +
                    getShowAnimation(superActivityToast).getDuration());

        }

    }

    /**
     *  Hide and remove the SuperActivityToast
     */
    void removeSuperToast(final SuperActivityToast superActivityToast) {

        /* If SuperActivityToast has been dismissed before it shows, do not attempt to show it */
        if(!superActivityToast.isShowing()) {

            mList.remove(superActivityToast);

            return;

        }

        /* If being called somewhere else get rid of delayed remove message */
        removeMessages(Messages.REMOVE, superActivityToast);

        final ViewGroup viewGroup = superActivityToast.getViewGroup();

        final View toastView = superActivityToast.getView();

        if (viewGroup != null) {

            Animation animation = getDismissAnimation(superActivityToast);

            animation.setAnimationListener(new Animation.AnimationListener() {

                @Override
                public void onAnimationStart(Animation animation) {

                    /* Do nothing */

                }

                @Override
                public void onAnimationEnd(Animation animation) {

                    if(superActivityToast.getOnDismissWrapper() != null){

                        superActivityToast.getOnDismissWrapper().onDismiss(superActivityToast.getView());

                    }

                    /* Show the SuperActivityToast next in the list if any exist */
                    ManagerSuperActivityToast.this.showNextSuperToast();

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                    /* Do nothing */

                }
            });

            toastView.startAnimation(animation);

            viewGroup.removeView(toastView);

            mList.poll();

        }

    }

    /**
     * Removes all SuperActivityToasts and clears the list
     */
    void cancelAllSuperActivityToasts() {

        removeMessages(Messages.DISPLAY);
        removeMessages(Messages.REMOVE);

        for (SuperActivityToast superActivityToast : mList) {

            if (superActivityToast.isShowing()) {

                superActivityToast.getViewGroup().removeView(
                        superActivityToast.getView());

                superActivityToast.getViewGroup().invalidate();

            }

        }

        mList.clear();

    }

    /**
     * Removes all SuperActivityToasts and clears the list for a specific activity
     */
    void cancelAllSuperActivityToastsForActivity(Activity activity) {

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

                removeMessages(Messages.DISPLAY, superActivityToast);
                removeMessages(Messages.REMOVE, superActivityToast);

                superActivityToastIterator.remove();

            }

        }

    }

    /**
     * Used in SuperActivityToast saveState().
     */
    LinkedList<SuperActivityToast> getList(){

        return mList;

    }

    /**
     * Returns an animation based on the {@link com.github.johnpersano.supertoasts.SuperToast.Animations} enums
     */
    private Animation getShowAnimation(SuperActivityToast superActivityToast) {

        if (superActivityToast.getAnimations() == SuperToast.Animations.FLYIN) {

            TranslateAnimation translateAnimation = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 0.75f, Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);

            AlphaAnimation alphaAnimation = new AlphaAnimation(0f, 1f);

            AnimationSet animationSet = new AnimationSet(true);
            animationSet.addAnimation(translateAnimation);
            animationSet.addAnimation(alphaAnimation);
            animationSet.setInterpolator(new DecelerateInterpolator());
            animationSet.setDuration(250);

            return animationSet;

        } else if (superActivityToast.getAnimations() == SuperToast.Animations.SCALE) {

            ScaleAnimation scaleAnimation = new ScaleAnimation(0.9f, 1.0f, 0.9f, 1.0f,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

            AlphaAnimation alphaAnimation = new AlphaAnimation(0f, 1f);

            AnimationSet animationSet = new AnimationSet(true);
            animationSet.addAnimation(scaleAnimation);
            animationSet.addAnimation(alphaAnimation);
            animationSet.setInterpolator(new DecelerateInterpolator());
            animationSet.setDuration(250);

            return animationSet;

        } else if (superActivityToast.getAnimations() == SuperToast.Animations.POPUP) {

            TranslateAnimation translateAnimation = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.1f, Animation.RELATIVE_TO_SELF, 0.0f);

            AlphaAnimation alphaAnimation = new AlphaAnimation(0f, 1f);

            AnimationSet animationSet = new AnimationSet(true);
            animationSet.addAnimation(translateAnimation);
            animationSet.addAnimation(alphaAnimation);
            animationSet.setInterpolator(new DecelerateInterpolator());
            animationSet.setDuration(250);

            return animationSet;

        } else {

            Animation animation= new AlphaAnimation(0f, 1f);
            animation.setDuration(500);
            animation.setInterpolator(new DecelerateInterpolator());

            return animation;

        }

    }

    /**
     *  Returns an animation based on the {@link com.github.johnpersano.supertoasts.SuperToast.Animations} enums
     */
    private Animation getDismissAnimation(SuperActivityToast superActivityToast) {

        if (superActivityToast.getAnimations() == SuperToast.Animations.FLYIN) {

            TranslateAnimation translateAnimation = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, .75f,
                    Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);

            AlphaAnimation alphaAnimation = new AlphaAnimation(1f, 0f);

            AnimationSet animationSet = new AnimationSet(true);
            animationSet.addAnimation(translateAnimation);
            animationSet.addAnimation(alphaAnimation);
            animationSet.setInterpolator(new AccelerateInterpolator());
            animationSet.setDuration(250);

            return animationSet;

        } else if (superActivityToast.getAnimations() == SuperToast.Animations.SCALE) {

            ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 0.9f, 1.0f, 0.9f,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

            AlphaAnimation alphaAnimation = new AlphaAnimation(1f, 0f);

            AnimationSet animationSet = new AnimationSet(true);
            animationSet.addAnimation(scaleAnimation);
            animationSet.addAnimation(alphaAnimation);
            animationSet.setInterpolator(new DecelerateInterpolator());
            animationSet.setDuration(250);

            return animationSet;

        } else if (superActivityToast.getAnimations() == SuperToast.Animations.POPUP) {

            TranslateAnimation translateAnimation = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.1f);

            AlphaAnimation alphaAnimation = new AlphaAnimation(1f, 0f);

            AnimationSet animationSet = new AnimationSet(true);
            animationSet.addAnimation(translateAnimation);
            animationSet.addAnimation(alphaAnimation);
            animationSet.setInterpolator(new DecelerateInterpolator());
            animationSet.setDuration(250);

            return animationSet;

        } else {

            AlphaAnimation alphaAnimation = new AlphaAnimation(1f, 0f);
            alphaAnimation.setDuration(500);
            alphaAnimation.setInterpolator(new AccelerateInterpolator());

            return alphaAnimation;

        }

    }

}
