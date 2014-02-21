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
import android.os.Build;
import com.github.johnpersano.supertoasts.R;
import com.github.johnpersano.supertoasts.SuperToast;

/** Creates a reference to basic style options so that all types of SuperToasts
 *  will be themed the same way in a particular class. */
@SuppressWarnings("UnusedDeclaration")
public class Style {

    public static final int BLACK = 0;
    public static final int BLUE = 1;
    public static final int GRAY = 2;
    public static final int GREEN = 3;
    public static final int ORANGE = 4;
    public static final int PURPLE = 5;
    public static final int RED = 6;
    public static final int WHITE = 7;

    public SuperToast.Animations animations = SuperToast.Animations.FADE;
    public int background = getBackground(GRAY);
    public int typefaceStyle = Typeface.NORMAL;
    public int textColor = Color.WHITE;
    public int dividerColor = Color.WHITE;
    public int buttonTextColor = Color.LTGRAY;

    /**
     * Returns a preset style.
     *
     * @param styleType {@link Style}
     *
     * @return {@link Style}
     */
    public static Style getStyle(int styleType) {

        final Style style = new Style();

        switch (styleType) {

            case BLACK:

                style.textColor = Color.WHITE;
                style.background = getBackground(BLACK);
                style.dividerColor = Color.WHITE;
                return style;

            case WHITE:

                style.textColor = Color.DKGRAY;
                style.background = getBackground(WHITE);
                style.dividerColor = Color.DKGRAY;
                style.buttonTextColor = Color.GRAY;
                return style;

            case GRAY:

                style.textColor = Color.WHITE;
                style.background = getBackground(GRAY);
                style.dividerColor = Color.WHITE;
                style.buttonTextColor = Color.GRAY;
                return style;

            case PURPLE:

                style.textColor = Color.WHITE;
                style.background = getBackground(PURPLE);
                style.dividerColor = Color.WHITE;
                return style;

            case RED:

                style.textColor = Color.WHITE;
                style.background = getBackground(RED);
                style.dividerColor = Color.WHITE;
                return style;

            case ORANGE:

                style.textColor = Color.WHITE;
                style.background = getBackground(ORANGE);
                style.dividerColor = Color.WHITE;
                return style;

            case BLUE:

                style.textColor = Color.WHITE;
                style.background = getBackground(BLUE);
                style.dividerColor = Color.WHITE;
                return style;

            case GREEN:

                style.textColor = Color.WHITE;
                style.background = getBackground(GREEN);
                style.dividerColor = Color.WHITE;
                return style;

            default:

                style.textColor = Color.WHITE;
                style.background = getBackground(GRAY);
                style.dividerColor = Color.WHITE;
                return style;

        }

    }

    /**
     * Returns a preset style with specified animations.
     *
     * @param styleType {@link Style}
     * @param animations {@link com.github.johnpersano.supertoasts.SuperToast.Animations}
     *
     * @return {@link Style}
     */
    public static Style getStyle(int styleType, SuperToast.Animations animations) {

        final Style style = new Style();
        style.animations = animations;

        switch (styleType) {

            case BLACK:

                style.textColor = Color.WHITE;
                style.background = getBackground(BLACK);
                style.dividerColor = Color.WHITE;
                return style;

            case WHITE:

                style.textColor = Color.DKGRAY;
                style.background = getBackground(WHITE);
                style.dividerColor = Color.DKGRAY;
                style.buttonTextColor = Color.GRAY;
                return style;

            case GRAY:

                style.textColor = Color.WHITE;
                style.background = getBackground(GRAY);
                style.dividerColor = Color.WHITE;
                style.buttonTextColor = Color.GRAY;
                return style;

            case PURPLE:

                style.textColor = Color.WHITE;
                style.background = getBackground(PURPLE);
                style.dividerColor = Color.WHITE;
                return style;

            case RED:

                style.textColor = Color.WHITE;
                style.background = getBackground(RED);
                style.dividerColor = Color.WHITE;
                return style;

            case ORANGE:

                style.textColor = Color.WHITE;
                style.background = getBackground(ORANGE);
                style.dividerColor = Color.WHITE;
                return style;

            case BLUE:

                style.textColor = Color.WHITE;
                style.background = getBackground(BLUE);
                style.dividerColor = Color.WHITE;
                return style;

            case GREEN:

                style.textColor = Color.WHITE;
                style.background = getBackground(GREEN);
                style.dividerColor = Color.WHITE;
                return style;

            default:

                style.textColor = Color.WHITE;
                style.background = getBackground(GRAY);
                style.dividerColor = Color.WHITE;
                return style;

        }

    }

    public static int getBackground(int style) {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            switch (style) {

                case BLACK:

                    return (R.drawable.background_kitkat_black);

                case WHITE:

                    return (R.drawable.background_kitkat_white);

                case GRAY:

                    return (R.drawable.background_kitkat_gray);

                case PURPLE:

                    return (R.drawable.background_kitkat_purple);

                case RED:

                    return (R.drawable.background_kitkat_red);

                case ORANGE:

                    return (R.drawable.background_kitkat_orange);

                case BLUE:

                    return (R.drawable.background_kitkat_blue);

                case GREEN:

                    return (R.drawable.background_kitkat_green);

                default:

                    return (R.drawable.background_kitkat_gray);

            }

        } else {

            switch (style) {

                case BLACK:

                    return (R.drawable.background_standard_black);

                case WHITE:

                    return (R.drawable.background_standard_white);

                case GRAY:

                    return (R.drawable.background_standard_gray);

                case PURPLE:

                    return (R.drawable.background_standard_purple);

                case RED:

                    return (R.drawable.background_standard_red);

                case ORANGE:

                    return (R.drawable.background_standard_orange);

                case BLUE:

                    return (R.drawable.background_standard_blue);

                case GREEN:

                    return (R.drawable.background_standard_green);

                default:

                    return (R.drawable.background_standard_gray);

            }

        }

    }

}