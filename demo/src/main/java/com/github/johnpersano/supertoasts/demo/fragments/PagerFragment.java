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
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.johnpersano.supertoasts.demo.R;
import com.github.johnpersano.supertoasts.demo.views.TabStrip;

import java.util.ArrayList;

/**
 * A helper Fragment that will handle Fragments in a ViewPager.
 */
public abstract class PagerFragment extends Fragment {

    // This List will hold the titles of all of the Fragments attached to the PagerFragment
    private final ArrayList<String> mFragmentTitles = new ArrayList<>();
    // This List will hold all of the Fragments attached to the PagerFragment
    private final ArrayList<Fragment> mFragments = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addFragments();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_pager, container, false);

        final ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        viewPager.setAdapter(new CustomFragmentPagerAdapter(getChildFragmentManager()));

        ((TabStrip) getActivity().findViewById(R.id.tabstrip)).setViewPager(viewPager);
        
        return view;
    }

    /**
     * This method should be overridden in any class that extends PagerFragment.
     */
    @SuppressWarnings("WeakerAccess")
    public abstract void addFragments();

    /**
     * Add a Fragment to this PagerFragment.
     *
     * @param title    The title of the {@link android.app.Fragment} to be added
     * @param fragment The {@link android.app.Fragment} to be added
     * @return The current PagerFragmentInstance
     */
    @SuppressWarnings({"UnusedReturnValue", "WeakerAccess"})
    public PagerFragment addFragment(String title, Fragment fragment) {
        this.mFragmentTitles.add(title);
        this.mFragments.add(fragment);

        return this;
    }

    /**
     * A FragmentStatePagerAdapter designed for use with a PagerFragment.
     */
    private class CustomFragmentPagerAdapter extends FragmentStatePagerAdapter {

        /**
         * Empty public constructor..
         *
         * @param fragmentManager Should use a child FragmentManager
         */
        public CustomFragmentPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
            // Do nothing
        }

        @Override
        public int getCount() {
            return PagerFragment.this.mFragments.size();
        }

        @Override
        public Fragment getItem(int position) {
            return PagerFragment.this.mFragments.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return (PagerFragment.this.mFragmentTitles.get(position));
        }
    }
}