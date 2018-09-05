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

package com.github.johnpersano.supertoasts.demo.fragments;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.johnpersano.supertoasts.demo.MainActivity;
import com.github.johnpersano.supertoasts.demo.R;
import com.github.johnpersano.supertoasts.demo.utils.AttributeUtils;
import com.github.johnpersano.supertoasts.library.Style;
import com.github.johnpersano.supertoasts.library.SuperActivityToast;
import com.github.johnpersano.supertoasts.library.SuperToast;
import com.github.johnpersano.supertoasts.library.utils.ListenerUtils;

public class SuperActivityToastFragment extends PagerFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final ActionBar actionBar = ((MainActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) actionBar.setSubtitle("SuperActivityToast");

        /*
         * Restore any showing/pending SuperActivityToasts after orientation change
         * and reattach any listeners.
         */
        SuperActivityToast.onRestoreState(getActivity(), savedInstanceState,
                ListenerUtils.newInstance()
                        .putListener("good_tag_name", onButtonClickListener));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        final FloatingActionButton button = (FloatingActionButton) getActivity()
                .findViewById(R.id.floating_action_button);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            @SuppressWarnings("ResourceType")
            public void onClick(View view) {
                final int containerId = AttributeUtils.getFrame(getActivity()) == Style.FRAME_LOLLIPOP ||
                        AttributeUtils.getType(getActivity()) == Style.TYPE_BUTTON ? R.id.toast_container : 0;

                SuperActivityToast.create(getActivity(), new Style(),
                        AttributeUtils.getType(getActivity()), containerId)
                        .setButtonText("UNDO")
                        .setButtonIconResource(R.drawable.ic_undo)
                        .setImgResource(R.drawable.toast_warning)
                        .setOnButtonClickListener("good_tag_name", null, onButtonClickListener)
                        .setProgressBarColor(Color.WHITE)
                        .setText("SuperActivityToast")
                        .setDuration(AttributeUtils.getDuration(getActivity()))
                        .setFrame(AttributeUtils.getFrame(getActivity()))
                        .setColor(AttributeUtils.getColor(getActivity()))
                        .setAnimations(AttributeUtils.getAnimations(getActivity())).show();
            }
        });

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void addFragments() {
        final Resources resources = getResources();
        this.addFragment(resources.getString(R.string.type_title), AttributeRadioGroupFragment.newInstance(
                resources.getString(R.string.type_title),
                resources.getString(R.string.type_subtitle),
                resources.getString(R.string.type_summary),
                resources.getStringArray(R.array.type_constants)));

        this.addFragment(resources.getString(R.string.duration_title), AttributeRadioGroupFragment.newInstance(
                resources.getString(R.string.duration_title),
                resources.getString(R.string.duration_subtitle),
                resources.getString(R.string.duration_summary),
                resources.getStringArray(R.array.duration_constants)));

        this.addFragment(resources.getString(R.string.frame_title), AttributeRadioGroupFragment.newInstance(
                resources.getString(R.string.frame_title),
                resources.getString(R.string.frame_subtitle),
                resources.getString(R.string.frame_summary),
                resources.getStringArray(R.array.frame_constants)));

        this.addFragment(resources.getString(R.string.animation_title), AttributeRadioGroupFragment.newInstance(
                resources.getString(R.string.animation_title),
                resources.getString(R.string.animation_subtitle),
                resources.getString(R.string.animation_summary),
                resources.getStringArray(R.array.animation_constants)));

        this.addFragment(resources.getString(R.string.color_title), AttributeSpinnerFragment.newInstance(
                resources.getString(R.string.color_title),
                resources.getString(R.string.color_subtitle),
                resources.getString(R.string.color_summary),
                resources.getStringArray(R.array.color_constants)));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        SuperActivityToast.onSaveState(outState);
    }

    private final SuperActivityToast.OnButtonClickListener onButtonClickListener =
            new SuperActivityToast.OnButtonClickListener() {

        @Override
        public void onClick(View view, Parcelable token) {
            SuperToast.create(view.getContext(), "OnClick!", Style.DURATION_SHORT)
                    .setPriorityLevel(Style.PRIORITY_HIGH).show();
        }
    };
}