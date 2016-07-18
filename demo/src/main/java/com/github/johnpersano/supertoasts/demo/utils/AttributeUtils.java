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

package com.github.johnpersano.supertoasts.demo.utils;

import android.content.Context;
import android.preference.PreferenceManager;

import com.github.johnpersano.supertoasts.demo.R;
import com.github.johnpersano.supertoasts.library.Style;
import com.github.johnpersano.supertoasts.library.utils.PaletteUtils;

/**
 * Utils class to help with saved SuperToast attributes.
 */
public class AttributeUtils {
    
    public static int getDuration(Context context) {
        switch (PreferenceManager.getDefaultSharedPreferences(context).getInt(context
                .getResources().getString(R.string.duration_title), 0)) {
            case 0: return Style.DURATION_SHORT;
            case 1: return Style.DURATION_MEDIUM;
            case 2: return Style.DURATION_LONG;
            default: return Style.DURATION_SHORT;
        }
    }

    public static int getFrame(Context context) {
        switch (PreferenceManager.getDefaultSharedPreferences(context).getInt(context
                .getResources().getString(R.string.frame_title), 0)) {
            case 0: return Style.FRAME_STANDARD;
            case 1: return Style.FRAME_KITKAT;
            case 2: return Style.FRAME_LOLLIPOP;
            default: return Style.FRAME_STANDARD;
        }
    }

    public static int getAnimations(Context context) {
        switch (PreferenceManager.getDefaultSharedPreferences(context).getInt(context
                .getResources().getString(R.string.animation_title), 0)) {
            case 0: return Style.ANIMATIONS_FADE;
            case 1: return Style.ANIMATIONS_FLY;
            case 2: return Style.ANIMATIONS_SCALE;
            case 3: return Style.ANIMATIONS_POP;
            default: return Style.ANIMATIONS_FADE;
        }
    }

    public static int getColor(Context context) {
        switch (PreferenceManager.getDefaultSharedPreferences(context).getInt(context
                .getResources().getString(R.string.color_title), 0)) {
            case 0: return PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_RED);
            case 1: return PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_PINK);
            case 2: return PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_PURPLE);
            case 3: return PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_DEEP_PURPLE);
            case 4: return PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_INDIGO);
            case 5: return PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_BLUE);
            case 6: return PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_LIGHT_BLUE);
            case 7: return PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_CYAN);
            case 8: return PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_TEAL);
            case 9: return PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_GREEN);
            case 10: return PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_LIGHT_GREEN);
            case 11: return PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_LIME);
            case 12: return PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_YELLOW);
            case 13: return PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_AMBER);
            case 14: return PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_ORANGE);
            case 15: return PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_DEEP_ORANGE);
            case 16: return PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_BROWN);
            case 17: return PaletteUtils.getSolidColor(PaletteUtils.GREY);
            case 18: return PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_BLUE_GREY);
            default: return PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_BLUE_GREY);
        }
    }

    public static int getType(Context context) {
        switch (PreferenceManager.getDefaultSharedPreferences(context).getInt(context
                .getResources().getString(R.string.type_title), 0)) {
            case 0: return Style.TYPE_STANDARD;
            case 1: return Style.TYPE_BUTTON;
            case 2: return Style.TYPE_PROGRESS_BAR;
            case 3: return Style.TYPE_PROGRESS_CIRCLE;
            default: return Style.TYPE_STANDARD;
        }
    }
}