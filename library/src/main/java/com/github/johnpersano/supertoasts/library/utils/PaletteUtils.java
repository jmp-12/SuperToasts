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

package com.github.johnpersano.supertoasts.library.utils;

import android.graphics.Color;
import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Utility class that contains various colors that can be used in conjunction with
 * {@link com.github.johnpersano.supertoasts.library.utils.BackgroundUtils}.
 */
@SuppressWarnings("UnusedDeclaration")
public class PaletteUtils {

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({WHITE, LIGHT_GREY, GREY, DARK_GREY, BLACK,
            MATERIAL_RED, MATERIAL_PINK, MATERIAL_PURPLE, MATERIAL_DEEP_PURPLE,
            MATERIAL_INDIGO, MATERIAL_BLUE, MATERIAL_LIGHT_BLUE, MATERIAL_CYAN,
            MATERIAL_TEAL, MATERIAL_GREEN, MATERIAL_LIGHT_GREEN, MATERIAL_LIME,
            MATERIAL_YELLOW, MATERIAL_AMBER, MATERIAL_ORANGE, MATERIAL_DEEP_ORANGE,
            MATERIAL_BROWN, MATERIAL_GREY, MATERIAL_BLUE_GREY})
    public @interface PaletteColors {}

    // Use int values as flags to avoid using enums
    private static final String ALPHA_SOLID = "#FF";
    private static final String ALPHA_TRANSPARENT = "#E1";

    public static final String WHITE = "FFFFFF";
    public static final String LIGHT_GREY = "BDBDBD";
    public static final String GREY = "757575";
    public static final String DARK_GREY = "424242";
    public static final String BLACK = "000000";

    public static final String MATERIAL_RED = "F44336";
    public static final String MATERIAL_PINK = "E91E63";
    public static final String MATERIAL_PURPLE = "9C27B0";
    public static final String MATERIAL_DEEP_PURPLE = "673AB7";
    public static final String MATERIAL_INDIGO = "3F51B5";
    public static final String MATERIAL_BLUE = "2196F3";
    public static final String MATERIAL_LIGHT_BLUE = "03A9F4";
    public static final String MATERIAL_CYAN = "00BCD4";
    public static final String MATERIAL_TEAL = "009688";
    public static final String MATERIAL_GREEN = "4CAF50";
    public static final String MATERIAL_LIGHT_GREEN = "8BC34A";
    public static final String MATERIAL_LIME = "CDDC39";
    public static final String MATERIAL_YELLOW = "FFEB3B";
    public static final String MATERIAL_AMBER = "FFC107";
    public static final String MATERIAL_ORANGE = "FF9800";
    public static final String MATERIAL_DEEP_ORANGE = "FF5722";
    public static final String MATERIAL_BROWN = "795548";
    public static final String MATERIAL_GREY = "9E9E9E";
    public static final String MATERIAL_BLUE_GREY = "607D8B";

    /**
     * Returns a solid color.
     *
     * @param color {@link com.github.johnpersano.supertoasts.library.utils
     *              .PaletteUtils.PaletteColors}
     * @return The parsed {@link android.graphics.Color}
     */
    public static int getSolidColor(@PaletteColors String color) {
        return Color.parseColor(ALPHA_SOLID.concat(color));
    }

    /**
     * Returns a transparent color.
     *
     * @param color {@link com.github.johnpersano.supertoasts.library.utils
     *              .PaletteUtils.PaletteColors}
     * @return The parsed {@link android.graphics.Color}
     */
    public static int getTransparentColor(@PaletteColors String color) {
        return Color.parseColor(ALPHA_TRANSPARENT.concat(color));
    }
}