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

import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.util.TypedValue;

import com.github.johnpersano.supertoasts.library.R;
import com.github.johnpersano.supertoasts.library.Style;

/**
 * Utility class that handles various {@link android.view.View} backgrounds.
 */
public class BackgroundUtils {

    /**
     * Returns a {@link GradientDrawable} with the
     * desired background color. If no {@link Style.Frame}
     * is set prior to calling this method, an appropriate {@link Style.Frame}
     * will be chosen based on the device's SDK level.
     *
     * @param style The current {@link Style}
     * @param color The desired color
     *
     * @return {@link GradientDrawable}
     */
    public static Drawable getBackground(Style style, int color) {

        // If a frame has been manually set, return the appropriate background
        if (style.frame > 0) {
            switch (style.frame) {
                case Style.FRAME_STANDARD: return BackgroundUtils.getStandardBackground(color);
                case Style.FRAME_KITKAT: return BackgroundUtils.getKitkatBackground(color);
                case Style.FRAME_LOLLIPOP: return BackgroundUtils.getLollipopBackground(color);
            }
        }

        // The frame has NOT been manually set so set the frame to correspond with SDK level
        final int sdkVersion = Build.VERSION.SDK_INT;

        // These statements should be ordered by highest SDK level to lowest
        if (sdkVersion >= Build.VERSION_CODES.LOLLIPOP) {
            style.frame = Style.FRAME_LOLLIPOP;
            return BackgroundUtils.getLollipopBackground(color);
        } else if (sdkVersion >= Build.VERSION_CODES.KITKAT) {
            style.frame = Style.FRAME_KITKAT;
            return BackgroundUtils.getKitkatBackground(color);
        } else {
            style.frame = Style.FRAME_STANDARD;
            return BackgroundUtils.getStandardBackground(color);
        }
    }

    /**
     * Returns a background resource for the Button in a
     * {@link com.github.johnpersano.supertoasts.library.SuperActivityToast}.
     * This Button resource will correspond to the corner radii of the
     * {@link Style.Frame}.
     *
     * @param frame The current {@link Style.Frame}
     * @return The corresponding Drawable reference
     */
    public static int getButtonBackgroundResource(@Style.Frame int frame) {
        switch (frame) {
            case Style.FRAME_LOLLIPOP: return R.drawable.selector_button_lollipop;
            case Style.FRAME_KITKAT: return R.drawable.selector_button_kitkat;
            case Style.FRAME_STANDARD: return R.drawable.selector_button_standard;
            default: return R.drawable.selector_button_standard;
        }
    }

    /**
     * Returns a density independent pixel value rounded to the nearest integer
     * for the desired dimension.
     *
     * @param pixels The pixel value to be converted
     * @return A rounded DIP value
     */
    public static int convertToDIP(int pixels) {
       return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, pixels,
                Resources.getSystem().getDisplayMetrics()));
    }

    /**
     * Private method.
     *
     * Returns a {@link GradientDrawable} with
     * no rounded corners.
     *
     * @param color The desired color of the GradientDrawable
     * @return A {@link GradientDrawable}
     */
    private static ColorDrawable getLollipopBackground(int color) {
        return new ColorDrawable(color);
    }

    /**
     * Private method.
     *
     * Returns a {@link GradientDrawable} with
     * rounded corners.
     *
     * @param color The desired color of the GradientDrawable
     * @return A {@link GradientDrawable}
     */
    private static GradientDrawable getKitkatBackground(int color) {
        final GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setCornerRadius(BackgroundUtils.convertToDIP(24));
        gradientDrawable.setColor(color);
        return gradientDrawable;
    }

    /**
     * Private method.
     *
     * Returns a {@link GradientDrawable} with
     * slightly rounded corners.
     *
     * @param color The desired color of the GradientDrawable
     * @return A {@link GradientDrawable}
     */
    private static GradientDrawable getStandardBackground(int color) {
        final GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setCornerRadius(BackgroundUtils.convertToDIP(4));
        gradientDrawable.setColor(color);
        return gradientDrawable;
    }
}