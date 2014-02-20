/*
 * Copyright 2013 Google Inc.
 *
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

package com.github.johnpersano.supertoasts.util;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.view.*;

@SuppressLint("NewApi")
@SuppressWarnings("javadoc")
public class SwipeDismissListener implements View.OnTouchListener {

    // Cached ViewConfiguration and system-wide constant values
    private final int mScaledTouchSlop;
    private final int mMinFlingVelocity;
    private final int mMaxFlingVelocity;
    private final long mAnimationTime;

    // Fixed properties
    private final View mView;
    private final OnDismissCallback mCallback;

    // Transient properties
    private float mActionDownXCoordinate;
    private boolean isSwiping;
    private VelocityTracker mVelocityTracker;
    private float mTranslationX;


    @SuppressWarnings("UnusedParameters")
    public interface OnDismissCallback {

        void onDismiss(View view);

    }


    public SwipeDismissListener(View view, OnDismissCallback callback) {

        final ViewConfiguration mViewConfiguration = ViewConfiguration
                .get(view.getContext());


        mScaledTouchSlop = mViewConfiguration.getScaledTouchSlop();
        mMinFlingVelocity = mViewConfiguration.getScaledMinimumFlingVelocity();
        mMaxFlingVelocity = mViewConfiguration.getScaledMaximumFlingVelocity();
        mAnimationTime = view.getContext().getResources()
                .getInteger(android.R.integer.config_shortAnimTime);

        mView = view;
        mCallback = callback;

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        motionEvent.offsetLocation(mTranslationX, 0);

        int mViewWidth = mView.getWidth();

        switch (motionEvent.getActionMasked()) {


            case MotionEvent.ACTION_DOWN:

                mActionDownXCoordinate = motionEvent.getRawX();
                mVelocityTracker = VelocityTracker.obtain();
                mVelocityTracker.addMovement(motionEvent);
                view.onTouchEvent(motionEvent);

                return true;


            case MotionEvent.ACTION_UP:

                if (mVelocityTracker == null) {

                    break;
                }

                float deltaXActionUp = motionEvent.getRawX() - mActionDownXCoordinate;

                mVelocityTracker.addMovement(motionEvent);

                mVelocityTracker.computeCurrentVelocity(1000);


                float velocityX = Math.abs(mVelocityTracker.getXVelocity());
                float velocityY = Math.abs(mVelocityTracker.getYVelocity());

                boolean dismiss = false;
                boolean dismissRight = false;


                if (Math.abs(deltaXActionUp) > mViewWidth / 2) {

                    dismiss = true;
                    dismissRight = deltaXActionUp > 0;

                } else if (mMinFlingVelocity <= velocityX && velocityX <= mMaxFlingVelocity && velocityY < velocityX) {

                    dismiss = true;
                    dismissRight = mVelocityTracker.getXVelocity() > 0;

                }


                if (dismiss) {

                    mView.animate()
                            .translationX(dismissRight ? mViewWidth : -mViewWidth)
                            .alpha(0)
                            .setDuration(mAnimationTime)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {

                                    performDismiss();

                                }
                            });

                } else {

                    // User has cancelled action
                    mView.animate()
                            .translationX(0)
                            .alpha(1)
                            .setDuration(mAnimationTime)
                            .setListener(null);

                }

                mVelocityTracker.recycle();
                mTranslationX = 0;
                mActionDownXCoordinate = 0;
                isSwiping = false;

                break;


            case MotionEvent.ACTION_MOVE:

                if (mVelocityTracker == null) {

                    break;

                }

                mVelocityTracker.addMovement(motionEvent);

                float deltaXActionMove = motionEvent.getRawX() - mActionDownXCoordinate;

                if (Math.abs(deltaXActionMove) > mScaledTouchSlop) {

                    isSwiping = true;
                    mView.getParent().requestDisallowInterceptTouchEvent(true);

                    // Cancel listview's touch
                    MotionEvent cancelEvent = MotionEvent.obtain(motionEvent);
                    cancelEvent.setAction(MotionEvent.ACTION_CANCEL |
                            (motionEvent.getActionIndex() << MotionEvent.ACTION_POINTER_INDEX_SHIFT));
                    mView.onTouchEvent(cancelEvent);

                    cancelEvent.recycle();

                }

                if (isSwiping) {

                    mTranslationX = deltaXActionMove;
                    mView.setTranslationX(deltaXActionMove);
                    mView.setAlpha(Math.max(0f, Math.min(1f,
                            1f - 2f * Math.abs(deltaXActionMove) / mViewWidth)));

                    return true;

                }

                break;

        }

        return false;
    }

    private void performDismiss() {

        final ViewGroup.LayoutParams lp = mView.getLayoutParams();
        final int originalHeight = mView.getHeight();

        ValueAnimator animator = ValueAnimator.ofInt(originalHeight, 1)
                .setDuration(mAnimationTime);

        animator.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {

                mCallback.onDismiss(mView);

            }

        });

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {

                lp.height = (Integer) valueAnimator.getAnimatedValue();
                mView.setLayoutParams(lp);

            }

        });

        animator.start();

    }

}
