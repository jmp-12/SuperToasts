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
import android.app.FragmentManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.*;

import java.util.Iterator;
import java.util.LinkedList;

/** Manages the life of a SuperActivityToast. Copied from the Crouton library. */
class ManagerSuperActivityToast extends Handler {

    private static final String TAG = "ManagerSuperActivityToast";

    private static final class Messages {

        /** Hexadecimal numbers that represent acronyms for the operation. **/
        private static final int DISPLAY= 0x44534154;
        private static final int REMOVE= 0x52534154;

    }

    private static ManagerSuperActivityToast mManagerSuperActivityToast;

    private final LinkedList<SuperActivityToast> mList;

    private ManagerSuperActivityToast() {

        mList = new LinkedList<SuperActivityToast>();

    }

    static synchronized ManagerSuperActivityToast getInstance() {

        if (mManagerSuperActivityToast != null) {

            return mManagerSuperActivityToast;

        } else {

            mManagerSuperActivityToast = new ManagerSuperActivityToast();

            return mManagerSuperActivityToast;

        }

    }


    void add(SuperActivityToast superActivityToast) {

        mList.add(superActivityToast);
        this.showNextSuperToast();

    }


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

    private void displaySuperToast(SuperActivityToast superActivityToast) {

        if (superActivityToast.isShowing()) {

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

                Log.e(TAG, e.toString());

                clearSuperActivityToastsForActivity(superActivityToast.getActivity());

            }

        }

        if(!superActivityToast.isIndeterminate()) {

            Message message = obtainMessage(Messages.REMOVE);
            message.obj = superActivityToast;
            sendMessageDelayed(message, superActivityToast.getDuration() +
                    getShowAnimation(superActivityToast).getDuration());


        }

    }

    void removeSuperToast(final SuperActivityToast superActivityToast) {

        final ViewGroup viewGroup = superActivityToast.getViewGroup();

        final View toastView = superActivityToast.getView();

        final Activity activity = superActivityToast.getActivity();

        if (viewGroup != null) {

            Animation animation = getDismissAnimation(superActivityToast);

            animation.setAnimationListener(new Animation.AnimationListener() {

                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {

                    if(superActivityToast.getOnDismissListener() != null){

                        superActivityToast.getOnDismissListener().onDismiss(superActivityToast.getView());

                    }

                    ManagerSuperActivityToast.this.showNextSuperToast();

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            toastView.startAnimation(animation);

            viewGroup.removeView(toastView);

            mList.poll();

        }

    }

    void clearQueue() {

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

    void clearSuperActivityToastsForActivity(Activity activity) {

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

    LinkedList<SuperActivityToast> getList(){


        return mList;

    }


    private Animation getShowAnimation(SuperActivityToast superActivityToast) {

        if (superActivityToast.getAnimation() == SuperToast.Animations.FLYIN) {

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

        } else if (superActivityToast.getAnimation() == SuperToast.Animations.SCALE) {

            ScaleAnimation scaleAnimation = new ScaleAnimation(0.9f, 1.0f, 0.9f, 1.0f,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

            AlphaAnimation alphaAnimation = new AlphaAnimation(0f, 1f);

            AnimationSet animationSet = new AnimationSet(true);
            animationSet.addAnimation(scaleAnimation);
            animationSet.addAnimation(alphaAnimation);
            animationSet.setInterpolator(new DecelerateInterpolator());
            animationSet.setDuration(250);

            return animationSet;

        } else if (superActivityToast.getAnimation() == SuperToast.Animations.POPUP) {

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

    private Animation getDismissAnimation(SuperActivityToast superActivityToast) {

        if (superActivityToast.getAnimation() == SuperToast.Animations.FLYIN) {

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

        } else if (superActivityToast.getAnimation() == SuperToast.Animations.SCALE) {

            ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 0.9f, 1.0f, 0.9f,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

            AlphaAnimation alphaAnimation = new AlphaAnimation(1f, 0f);

            AnimationSet animationSet = new AnimationSet(true);
            animationSet.addAnimation(scaleAnimation);
            animationSet.addAnimation(alphaAnimation);
            animationSet.setInterpolator(new DecelerateInterpolator());
            animationSet.setDuration(250);

            return animationSet;

        } else if (superActivityToast.getAnimation() == SuperToast.Animations.POPUP) {

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
