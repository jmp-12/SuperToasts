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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.johnpersano.supertoasts.demo.R;

/**
 * {@link Fragment} that shows a Spinner with some text and a summary.
 */
public class AttributeSpinnerFragment extends Fragment {
    
    private static final String ARG_TITLE = "arg_title";
    private static final String ARG_SUBTITLE = "arg_subtitle";
    private static final String ARG_SUMMARY = "arg_summary";
    private static final String ARG_ARRAY_ID = "arg_array_id";

    /**
     * Returns a new instance of the {@link AttributeSpinnerFragment}.
     *
     * @param title The title to be shown in the TabStrip
     * @param subtitle The large subtitle
     * @param summary A summary of the Spinner
     * @param array The selection options of the Spinner
     *
     * @return A new AttributeSpinnerFragment
     */
    public static AttributeSpinnerFragment newInstance(String title, String subtitle,
                                                       String summary, String[] array) {
        final AttributeSpinnerFragment attributeSpinnerFragment = new AttributeSpinnerFragment();

        final Bundle bundle = new Bundle();
        bundle.putString(ARG_TITLE, title);
        bundle.putString(ARG_SUBTITLE, subtitle);
        bundle.putString(ARG_SUMMARY, summary);
        bundle.putStringArray(ARG_ARRAY_ID, array);

        attributeSpinnerFragment.setArguments(bundle);
        return attributeSpinnerFragment;
    }

    @Nullable
    @Override
    @SuppressWarnings("ConstantConditions")
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_attribute_spinner, container, false);
        
        if (this.getArguments() == null) {
            throw new IllegalArgumentException(getClass().getName().concat(" cannot be " +
                    "instantiated without arguments."));
        }
        
        final TextView titleTextView = (TextView) view.findViewById(R.id.title);
        titleTextView.setText(getArguments().getString(ARG_SUBTITLE));

        final TextView summaryTextView = (TextView) view.findViewById(R.id.summary);
        summaryTextView.setText(getArguments().getString(ARG_SUMMARY));

        final Spinner spinner = (Spinner) view.findViewById(R.id.spinner);
        spinner.setAdapter(new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, getArguments()
                .getStringArray(ARG_ARRAY_ID)));
        spinner.setSelection(PreferenceManager.getDefaultSharedPreferences(getActivity())
                .getInt(getArguments().getString(ARG_TITLE), 0));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                PreferenceManager.getDefaultSharedPreferences(getActivity())
                        .edit().putInt(getArguments().getString(ARG_TITLE), position).commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        return view;
    }
}