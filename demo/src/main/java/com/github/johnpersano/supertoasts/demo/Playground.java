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

package com.github.johnpersano.supertoasts.demo;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.github.johnpersano.supertoasts.library.Style;
import com.github.johnpersano.supertoasts.library.SuperActivityToast;
import com.github.johnpersano.supertoasts.library.SuperToast;
import com.github.johnpersano.supertoasts.library.utils.ListenerUtils;

import java.util.Random;

@SuppressWarnings("ConstantConditions")
public class Playground extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playground);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Button button = (Button) findViewById(R.id.random);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                getRandomSuperToast().show();
            }
        });

        SuperActivityToast.onRestoreState(this, savedInstanceState,
                ListenerUtils.newInstance()
                        .putListener("good_tag_name", onButtonClickListener));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        SuperActivityToast.onSaveState(outState);
    }

    private SuperToast getRandomSuperToast() {
        final Random baseRandom = new Random();
        final int baseType = baseRandom.nextInt(1208);
        if (baseType % 2 == 0) return getSuperToast();
        return getSuperActivityToast();
    }

    private SuperToast getSuperToast() {
        final Random random = new Random();

        final SuperToast superToast = new SuperToast(Playground.this);
        superToast.setText("SuperToast");

        final int typefaceArbiter = random.nextInt(5);
        switch (typefaceArbiter) {
            case 1: superToast.setTypefaceStyle(Typeface.BOLD); break;
            case 2: superToast.setTypefaceStyle(Typeface.BOLD_ITALIC); break;
            case 3: superToast.setTypefaceStyle(Typeface.ITALIC); break;
            default: superToast.setTypefaceStyle(Typeface.NORMAL);
        }

        final int animationsArbiter = random.nextInt(4);
        switch (animationsArbiter) {
            case 1: superToast.setAnimations(Style.ANIMATIONS_FADE); break;
            case 2: superToast.setAnimations(Style.ANIMATIONS_FLY); break;
            case 3: superToast.setAnimations(Style.ANIMATIONS_POP); break;
            default: superToast.setAnimations(Style.ANIMATIONS_SCALE);
        }

        int red = random.nextInt(255);
        int blue = random.nextInt(255);
        int green = random.nextInt(255);
        superToast.setColor(Color.argb(255, red, blue, green));

        final int frameArbiter = random.nextInt(3);
        switch (frameArbiter) {
            case 1: superToast.setFrame(Style.FRAME_KITKAT); break;
            case 2: superToast.setFrame(Style.FRAME_LOLLIPOP); break;
            default: superToast.setFrame(Style.FRAME_STANDARD); break;
        }

        red = random.nextInt(255);
        blue = random.nextInt(255);
        green = random.nextInt(255);
        superToast.setPriorityColor(Color.argb(255, red, blue, green));

        final int gravityArbiter = random.nextInt(6);
        switch (gravityArbiter) {
            case 1: superToast.setGravity(Gravity.TOP); break;
            case 2: superToast.setGravity(Gravity.START); break;
            case 3: superToast.setGravity(Gravity.END); break;
            default: break;
        }

        final int durationArbiter = random.nextInt(3);
        switch (durationArbiter) {
            case 1: superToast.setDuration(Style.DURATION_SHORT); break;
            case 2: superToast.setDuration(Style.DURATION_LONG); break;
            default: superToast.setDuration(Style.DURATION_MEDIUM); break;
        }

        red = random.nextInt(255);
        blue = random.nextInt(255);
        green = random.nextInt(255);
        superToast.setTextColor(Color.argb(255, red, blue, green));

        final int textSizeArbiter = random.nextInt(3);
        switch (textSizeArbiter) {
            case 1: superToast.setTextSize(Style.TEXTSIZE_SMALL); break;
            case 2: superToast.setTextSize(Style.TEXTSIZE_LARGE); break;
            default: superToast.setTextSize(Style.TEXTSIZE_MEDIUM); break;
        }

        final int iconArbiter = random.nextInt(10);
        switch (iconArbiter) {
            case 1: superToast.setIconResource(Style.ICONPOSITION_BOTTOM, R.drawable.ic_about); break;
            case 2: superToast.setIconResource(Style.ICONPOSITION_LEFT, R.drawable.ic_about); break;
            case 3: superToast.setIconResource(Style.ICONPOSITION_RIGHT, R.drawable.ic_about); break;
            case 4: superToast.setIconResource(Style.ICONPOSITION_TOP, R.drawable.ic_about); break;
            default: break;
        }

        return superToast;
    }

    private SuperActivityToast getSuperActivityToast() {
        final Random random = new Random();

        SuperActivityToast superActivityToast;
        final int typeArbiter = random.nextInt(6);
        switch (typeArbiter) {
            case 1: superActivityToast = new SuperActivityToast(Playground.this, Style.TYPE_BUTTON); break;
            case 2: superActivityToast = new SuperActivityToast(Playground.this, Style.TYPE_PROGRESS_BAR); break;
            case 3: superActivityToast = new SuperActivityToast(Playground.this, Style.TYPE_PROGRESS_CIRCLE); break;
            case 4: superActivityToast = new SuperActivityToast(Playground.this, Style.TYPE_IMG); break;
            default: superActivityToast = new SuperActivityToast(Playground.this); break;
        }
        superActivityToast.setText("SuperActivityToast");
        superActivityToast.setTouchToDismiss(true);

        final int typefaceArbiter = random.nextInt(5);
        switch (typefaceArbiter) {
            case 1: superActivityToast.setTypefaceStyle(Typeface.BOLD); break;
            case 2: superActivityToast.setTypefaceStyle(Typeface.BOLD_ITALIC); break;
            case 3: superActivityToast.setTypefaceStyle(Typeface.ITALIC); break;
            default: superActivityToast.setTypefaceStyle(Typeface.NORMAL);
        }

        final int animationsArbiter = random.nextInt(4);
        switch (animationsArbiter) {
            case 1: superActivityToast.setAnimations(Style.ANIMATIONS_FADE); break;
            case 2: superActivityToast.setAnimations(Style.ANIMATIONS_FLY); break;
            case 3: superActivityToast.setAnimations(Style.ANIMATIONS_POP); break;
            default: superActivityToast.setAnimations(Style.ANIMATIONS_SCALE);
        }

        int red = random.nextInt(255);
        int blue = random.nextInt(255);
        int green = random.nextInt(255);
        superActivityToast.setColor(Color.argb(255, red, blue, green));

        final int frameArbiter = random.nextInt(3);
        switch (frameArbiter) {
            case 1: superActivityToast.setFrame(Style.FRAME_KITKAT); break;
            case 2: superActivityToast.setFrame(Style.FRAME_LOLLIPOP); break;
            default: superActivityToast.setFrame(Style.FRAME_STANDARD); break;
        }

        red = random.nextInt(255);
        blue = random.nextInt(255);
        green = random.nextInt(255);
        superActivityToast.setPriorityColor(Color.argb(255, red, blue, green));

        final int gravityArbiter = random.nextInt(6);
        switch (gravityArbiter) {
            case 1: superActivityToast.setGravity(Gravity.TOP); break;
            case 2: superActivityToast.setGravity(Gravity.START); break;
            case 3: superActivityToast.setGravity(Gravity.END); break;
            default: break;
        }

        final int durationArbiter = random.nextInt(3);
        switch (durationArbiter) {
            case 1: superActivityToast.setDuration(Style.DURATION_SHORT); break;
            case 2: superActivityToast.setDuration(Style.DURATION_LONG); break;
            default: superActivityToast.setDuration(Style.DURATION_MEDIUM); break;
        }

        red = random.nextInt(255);
        blue = random.nextInt(255);
        green = random.nextInt(255);
        superActivityToast.setTextColor(Color.argb(255, red, blue, green));

        final int textSizeArbiter = random.nextInt(3);
        switch (textSizeArbiter) {
            case 1: superActivityToast.setTextSize(Style.TEXTSIZE_SMALL); break;
            case 2: superActivityToast.setTextSize(Style.TEXTSIZE_LARGE); break;
            default: superActivityToast.setTextSize(Style.TEXTSIZE_MEDIUM); break;
        }

        final int iconArbiter = random.nextInt(10);
        switch (iconArbiter) {
            case 1: superActivityToast.setIconResource(Style.ICONPOSITION_BOTTOM, R.drawable.ic_about); break;
            case 2: superActivityToast.setIconResource(Style.ICONPOSITION_LEFT, R.drawable.ic_about); break;
            case 3: superActivityToast.setIconResource(Style.ICONPOSITION_RIGHT, R.drawable.ic_about); break;
            case 4: superActivityToast.setIconResource(Style.ICONPOSITION_TOP, R.drawable.ic_about); break;
            default: break;
        }

        if (superActivityToast.getType() == Style.TYPE_BUTTON) {
            superActivityToast.setButtonText("UNDO");
            superActivityToast.setButtonIconResource(R.drawable.ic_undo);
            superActivityToast.setOnButtonClickListener("good_tag_name", null, onButtonClickListener);

            red = random.nextInt(255);
            blue = random.nextInt(255);
            green = random.nextInt(255);
            superActivityToast.setButtonDividerColor(Color.argb(255, red, blue, green));

            red = random.nextInt(255);
            blue = random.nextInt(255);
            green = random.nextInt(255);
            superActivityToast.setButtonTextColor(Color.argb(255, red, blue, green));

            final int buttonTextSizeArbiter = random.nextInt(3);
            switch (buttonTextSizeArbiter) {
                case 1: superActivityToast.setButtonTextSize(Style.TEXTSIZE_SMALL); break;
                case 2: superActivityToast.setButtonTextSize(Style.TEXTSIZE_LARGE); break;
                default: superActivityToast.setButtonTextSize(Style.TEXTSIZE_MEDIUM); break;
            }
        } else if (superActivityToast.getType() == Style.TYPE_PROGRESS_BAR ||
                superActivityToast.getType() == Style.TYPE_PROGRESS_CIRCLE) {
            superActivityToast.setProgressIndeterminate(true);

            red = random.nextInt(255);
            blue = random.nextInt(255);
            green = random.nextInt(255);
            superActivityToast.setProgressBarColor(Color.argb(255, red, blue, green));
        }
        return superActivityToast;
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