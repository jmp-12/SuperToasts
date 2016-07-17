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

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.github.johnpersano.supertoasts.demo.R;
import com.github.johnpersano.supertoasts.demo.utils.ViewUtils;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * {@link Fragment} that shows a RadioGroup with some text and a summary.
 */
public class AttributeRadioGroupFragment extends Fragment {
    
    private static final String ARG_TITLE = "arg_title";
    private static final String ARG_SUBTITLE = "arg_subtitle";
    private static final String ARG_SUMMARY = "arg_summary";
    private static final String ARG_ARRAY = "arg_array_id";

    /**
     * Returns a new instance of the {@link AttributeRadioGroupFragment}.
     *  
     * @param title The title to be shown in the TabStrip
     * @param subtitle The large subtitle
     * @param summary A summary of the RadioGroup
     * @param array The selection options of the RadioGroup 
     *                          
     * @return A new AttributeRadioGroupFragment
     */
    public static AttributeRadioGroupFragment newInstance(String title, String subtitle,
                                                          String summary, String[] array) {
        final AttributeRadioGroupFragment attributeRadioGroupFragment = new AttributeRadioGroupFragment();

        final Bundle bundle = new Bundle();
        bundle.putString(ARG_TITLE, title);
        bundle.putString(ARG_SUBTITLE, subtitle);
        bundle.putString(ARG_SUMMARY, summary);
        bundle.putStringArrayList(ARG_ARRAY, new ArrayList<>(Arrays.asList(array)));

        attributeRadioGroupFragment.setArguments(bundle);
        return attributeRadioGroupFragment;
    }

    @Nullable
    @Override
    @SuppressWarnings("ConstantConditions")
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_attribute_radiogroup, container, false);

        // Make sure the Fragment has found its arguments
        if (this.getArguments() == null) {
            throw new IllegalArgumentException(getClass().getName().concat(" cannot be " +
                    "instantiated without arguments."));
        }
        
        final TextView subtitleTextView = (TextView) view.findViewById(R.id.subtitle);
        subtitleTextView.setText(getArguments().getString(ARG_SUBTITLE));

        final TextView summaryTextView = (TextView) view.findViewById(R.id.summary);
        summaryTextView.setText(getArguments().getString(ARG_SUMMARY));

        final RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.radiogroup);
        for (String string : getArguments().getStringArrayList(ARG_ARRAY)) {
            final RadioButton radioButton = new RadioButton(getActivity());
            radioButton.setText(string);
            radioButton.setId(ViewUtils.generateViewId());
            radioGroup.addView(radioButton);
        }
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                PreferenceManager.getDefaultSharedPreferences(getActivity())
                        .edit().putInt(getArguments().getString(ARG_TITLE), group
                        .indexOfChild(group.findViewById(group
                        .getCheckedRadioButtonId()))).commit();
            }
        });

        // RadioGroup.check() is misleading, we must check the RadioButton manually
        ((RadioButton) radioGroup.getChildAt(PreferenceManager
                .getDefaultSharedPreferences(getActivity()).getInt(getArguments()
                        .getString(ARG_TITLE), 0))).setChecked(true);

        return view;
    }
}