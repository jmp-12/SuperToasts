/**
 *  Copyright 2014 John Persano
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

package com.github.johnpersano.supertoasts.util;

import android.graphics.Color;
import android.graphics.Typeface;
import com.github.johnpersano.supertoasts.SuperToast;

/** Creates a reference to basic style options so that all types of SuperToasts
 *  will be themed the same way in a particular class. */
@SuppressWarnings("UnusedDeclaration")
public class Style {

    public static final int DARK = 0;
    public static final int LIGHT = 1;
    public static final int STANDARD = 2;
    public static final int PURPLE = 3;
    public static final int RED = 4;
    public static final int ORANGE = 5;
    public static final int BLUE = 6;
    public static final int GREEN = 7;

    public SuperToast.Animations animations;
    public int background = SuperToast.Background.TRANSLUCENT_GRAY;
    public int typefaceStyle = Typeface.NORMAL;
    public int textColor = Color.WHITE;
    public int dividerColor = Color.WHITE;
    public int buttonTextColor = Color.LTGRAY;
    public int buttonTypefaceStyle = Typeface.BOLD;


    /**
     * Returns a preset style.
     *
     * @param styleType {@link com.github.johnpersano.supertoasts.util.Style}
     *
     * @return {@link com.github.johnpersano.supertoasts.util.Style}
     */
    public static Style getStyle(int styleType) {

        final Style style = new Style();

        switch (styleType) {

            case DARK:

                style.textColor = Color.WHITE;
                style.background = SuperToast.Background.TRANSLUCENT_BLACK;
                style.dividerColor = Color.WHITE;
                style.buttonTextColor = Color.LTGRAY;
                return style;

            case LIGHT:

                style.textColor = Color.DKGRAY;
                style.background = SuperToast.Background.TRANSLUCENT_WHITE;
                style.dividerColor = Color.DKGRAY;
                return style;

            case STANDARD:

                style.textColor = Color.WHITE;
                style.background = SuperToast.Background.TRANSLUCENT_GRAY;
                style.dividerColor = Color.WHITE;
                return style;

            case PURPLE:

                style.textColor = Color.WHITE;
                style.background = SuperToast.Background.TRANSLUCENT_PURPLE;
                style.dividerColor = Color.WHITE;
                return style;

            case RED:

                style.textColor = Color.WHITE;
                style.background = SuperToast.Background.TRANSLUCENT_RED;
                style.dividerColor = Color.WHITE;
                return style;

            case ORANGE:

                style.textColor = Color.WHITE;
                style.background = SuperToast.Background.TRANSLUCENT_ORANGE;
                style.dividerColor = Color.WHITE;
                return style;

            case BLUE:

                style.textColor = Color.WHITE;
                style.background = SuperToast.Background.TRANSLUCENT_BLUE;
                style.dividerColor = Color.WHITE;
                return style;

            case GREEN:

                style.textColor = Color.WHITE;
                style.background = SuperToast.Background.TRANSLUCENT_GREEN;
                style.dividerColor = Color.WHITE;
                return style;

            default:

                style.textColor = Color.WHITE;
                style.background = SuperToast.Background.TRANSLUCENT_GRAY;
                style.dividerColor = Color.WHITE;
                return style;

        }

    }

    /**
     * Returns a preset style with specified animations.
     *
     * @param styleType {@link com.github.johnpersano.supertoasts.util.Style}
     * @param animations {@link com.github.johnpersano.supertoasts.SuperToast.Animations}
     *
     * @return {@link com.github.johnpersano.supertoasts.util.Style}
     */
    public static Style getStyle(int styleType, SuperToast.Animations animations) {

        final Style style = new Style();
        style.animations = animations;

        switch (styleType) {

            case DARK:

                style.textColor = Color.WHITE;
                style.background = SuperToast.Background.TRANSLUCENT_BLACK;
                style.dividerColor = Color.WHITE;
                style.buttonTextColor = Color.LTGRAY;
                return style;

            case LIGHT:

                style.textColor = Color.DKGRAY;
                style.background = SuperToast.Background.TRANSLUCENT_WHITE;
                style.dividerColor = Color.DKGRAY;
                return style;

            case STANDARD:

                style.textColor = Color.WHITE;
                style.background = SuperToast.Background.TRANSLUCENT_GRAY;
                style.dividerColor = Color.WHITE;
                return style;

            case PURPLE:

                style.textColor = Color.WHITE;
                style.background = SuperToast.Background.TRANSLUCENT_PURPLE;
                style.dividerColor = Color.WHITE;
                return style;

            case RED:

                style.textColor = Color.WHITE;
                style.background = SuperToast.Background.TRANSLUCENT_RED;
                style.dividerColor = Color.WHITE;
                return style;

            case ORANGE:

                style.textColor = Color.WHITE;
                style.background = SuperToast.Background.TRANSLUCENT_ORANGE;
                style.dividerColor = Color.WHITE;
                return style;

            case BLUE:

                style.textColor = Color.WHITE;
                style.background = SuperToast.Background.TRANSLUCENT_BLUE;
                style.dividerColor = Color.WHITE;
                return style;

            case GREEN:

                style.textColor = Color.WHITE;
                style.background = SuperToast.Background.TRANSLUCENT_GREEN;
                style.dividerColor = Color.WHITE;
                return style;

            default:

                style.textColor = Color.WHITE;
                style.background = SuperToast.Background.TRANSLUCENT_GRAY;
                style.dividerColor = Color.WHITE;
                return style;

        }

    }

}