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
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.johnpersano.supertoasts.demo.MainActivity;
import com.github.johnpersano.supertoasts.demo.R;
import com.github.johnpersano.supertoasts.demo.utils.AttributeUtils;
import com.github.johnpersano.supertoasts.library.SuperToast;


public class SuperToastFragment extends PagerFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final ActionBar actionBar = ((MainActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) actionBar.setSubtitle("SuperToast");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final FloatingActionButton button = (FloatingActionButton) getActivity()
                .findViewById(R.id.floating_action_button);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            @SuppressWarnings("ResourceType")
            public void onClick(View view) {
                new SuperToast(getActivity())
                        .setText("I am a SuperToast!")
                        .setDuration(AttributeUtils.getDuration(getActivity()))
                        .setFrame(AttributeUtils.getFrame(getActivity()))
                        .setColor(AttributeUtils.getColor(getActivity()))
                        .setAnimations(AttributeUtils.getAnimations(getActivity()))
                        .setColor(AttributeUtils.getColor(getActivity())).show();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void addFragments() {
        final Resources resources = getResources();
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
}