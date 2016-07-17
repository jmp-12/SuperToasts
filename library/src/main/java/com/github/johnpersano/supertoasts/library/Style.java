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

package com.github.johnpersano.supertoasts.library;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.IntDef;
import android.view.Gravity;
import android.widget.FrameLayout;

import com.github.johnpersano.supertoasts.library.utils.BackgroundUtils;
import com.github.johnpersano.supertoasts.library.utils.PaletteUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * This class will store references to the various attributes of a {@link com.github.johnpersano.supertoasts.library.SuperToast}
 * or {@link com.github.johnpersano.supertoasts.library.SuperActivityToast}.
 */
@SuppressWarnings({"UnusedDeclaration", "SpellCheckingInspection"})
public class Style implements Parcelable {

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({DURATION_VERY_SHORT, DURATION_SHORT, DURATION_MEDIUM, DURATION_LONG, DURATION_VERY_LONG})
    public @interface Duration {}

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({Typeface.BOLD, Typeface.BOLD_ITALIC, Typeface.ITALIC, Typeface.NORMAL})
    public @interface TypefaceStyle {}

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({TEXTSIZE_VERY_SMALL, TEXTSIZE_SMALL, TEXTSIZE_MEDIUM, TEXTSIZE_LARGE, TEXTSIZE_VERY_LARGE})
    public @interface TextSize {}

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({ICONPOSITION_LEFT, ICONPOSITION_RIGHT, ICONPOSITION_BOTTOM, ICONPOSITION_TOP})
    public @interface IconPosition {}

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({FRAME_STANDARD, FRAME_KITKAT, FRAME_LOLLIPOP})
    public @interface Frame {}

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({TYPE_STANDARD, TYPE_BUTTON, TYPE_PROGRESS_CIRCLE, TYPE_PROGRESS_BAR})
    public @interface Type {}

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({ANIMATIONS_FADE, ANIMATIONS_FLY, ANIMATIONS_SCALE, ANIMATIONS_POP})
    public @interface Animations {}

    @SuppressLint("RtlHardcoded")
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({Gravity.BOTTOM, Gravity.CENTER, Gravity.CENTER_HORIZONTAL,
            Gravity.CENTER_VERTICAL, Gravity.END, Gravity.LEFT, Gravity.NO_GRAVITY,
            Gravity.RIGHT, Gravity.START, Gravity.TOP})
    public @interface GravityStyle {}

    @Retention(RetentionPolicy.SOURCE)
    @IntDef(flag=true, value={PRIORITY_HIGH, PRIORITY_MEDIUM, PRIORITY_LOW})
    public @interface PriorityLevel {}

    // Use int values as flags to avoid using enums
    public static final int DURATION_VERY_SHORT = 1500;
    public static final int DURATION_SHORT = 2000;
    public static final int DURATION_MEDIUM = 2750;
    public static final int DURATION_LONG = 3500;
    public static final int DURATION_VERY_LONG = 4500;

    public static final int TEXTSIZE_VERY_SMALL = 12;
    public static final int TEXTSIZE_SMALL = 14;
    public static final int TEXTSIZE_MEDIUM = 16;
    public static final int TEXTSIZE_LARGE = 18;
    public static final int TEXTSIZE_VERY_LARGE = 20;

    public static final int ICONPOSITION_LEFT = 1;
    public static final int ICONPOSITION_RIGHT = 2;
    public static final int ICONPOSITION_BOTTOM = 3;
    public static final int ICONPOSITION_TOP = 4;

    public static final int FRAME_STANDARD = 1;
    public static final int FRAME_KITKAT = 2;
    public static final int FRAME_LOLLIPOP = 3;

    public static final int TYPE_STANDARD = 1;
    public static final int TYPE_BUTTON = 2;
    public static final int TYPE_PROGRESS_CIRCLE = 3;
    public static final int TYPE_PROGRESS_BAR = 4;

    public static final int ANIMATIONS_FADE = 1;
    public static final int ANIMATIONS_FLY = 2;
    public static final int ANIMATIONS_SCALE = 3;
    public static final int ANIMATIONS_POP = 4;

    public static final int PRIORITY_HIGH = 1;
    public static final int PRIORITY_MEDIUM = 2;
    public static final int PRIORITY_LOW = 3;

    // General SuperToast items
    public String message;
    public int duration;
    public int color;
    public int priorityColor;
    public int frame;
    public int animations;
    public int gravity;
    public int xOffset;
    public int yOffset;
    public int width;
    public int height;
    public String dismissTag;
    public Parcelable dismissToken;
    public int priorityLevel;
    protected long timestamp;
    protected boolean isSuperActivityToast;

    // Message TextView items
    public int messageTypefaceStyle;
    public int messageTextColor;
    public int messageTextSize;
    public int messageIconPosition;
    public int messageIconResource;

    // General SuperActivityToast items
    public int container;
    public int type;
    public boolean isIndeterminate;
    public boolean touchToDismiss;

    // SuperActivityToast Button items
    public String buttonText;
    public int buttonTypefaceStyle;
    public int buttonTextColor;
    public int buttonTextSize;
    public int buttonDividerColor;
    public int buttonIconResource;
    public String buttonTag;
    public Parcelable buttonToken;

    // SuperActivityToast Progress items
    public int progress;
    public int progressMax;
    public boolean progressIndeterminate;
    public int progressBarColor;

    /**
     * Public constructor for a new {@link com.github.johnpersano.supertoasts.library.Style}.
     * This constructor will assign a few default values.
     */
    public Style() {
        // General SuperToast items
        this.duration = DURATION_MEDIUM;
        this.color = PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_GREY);
        this.gravity = Gravity.BOTTOM | Gravity.CENTER;
        this.yOffset = BackgroundUtils.convertToDIP(64);
        this.width = FrameLayout.LayoutParams.WRAP_CONTENT;
        this.height = FrameLayout.LayoutParams.WRAP_CONTENT;
        this.priorityLevel = PRIORITY_MEDIUM;

        // Message TextView items
        this.messageTypefaceStyle = Typeface.NORMAL;
        this.messageTextColor = PaletteUtils.getSolidColor(PaletteUtils.WHITE);
        this.messageTextSize = TEXTSIZE_SMALL;
        this.messageIconPosition = ICONPOSITION_LEFT;

        // SuperActivityToast Button items
        this.buttonTypefaceStyle = Typeface.BOLD;
        this.buttonTextColor = PaletteUtils.getSolidColor(PaletteUtils.WHITE);
        this.buttonTextSize = TEXTSIZE_VERY_SMALL;
        this.buttonDividerColor = PaletteUtils.getSolidColor(PaletteUtils.WHITE);

        //SuperActivityToast Progress items
        this.progressBarColor = PaletteUtils.getSolidColor(PaletteUtils.WHITE);
        this.progressIndeterminate = true;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        // General SuperToast items
        parcel.writeString(message);
        parcel.writeInt(duration);
        parcel.writeInt(color);
        parcel.writeInt(priorityColor);
        parcel.writeInt(frame);
        parcel.writeInt(animations);
        parcel.writeInt(gravity);
        parcel.writeInt(xOffset);
        parcel.writeInt(yOffset);
        parcel.writeInt(width);
        parcel.writeInt(height);
        parcel.writeString(dismissTag);
        parcel.writeParcelable(dismissToken, 0);
        parcel.writeInt(priorityLevel);
        parcel.writeLong(timestamp);
        parcel.writeByte((byte) (isSuperActivityToast ? 1 : 0));

        // Message TextView items
        parcel.writeInt(messageTypefaceStyle);
        parcel.writeInt(messageTextColor);
        parcel.writeInt(messageTextSize);
        parcel.writeInt(messageIconPosition);
        parcel.writeInt(messageIconResource);

        // General SuperActivityToast items
        parcel.writeInt(container);
        parcel.writeInt(type);
        parcel.writeByte((byte) (isIndeterminate ? 1 : 0));
        parcel.writeByte((byte) (touchToDismiss ? 1 : 0));

        // SuperActivityToast Button items
        parcel.writeString(buttonText);
        parcel.writeInt(buttonTypefaceStyle);
        parcel.writeInt(buttonTextColor);
        parcel.writeInt(buttonTextSize);
        parcel.writeInt(buttonDividerColor);
        parcel.writeInt(buttonIconResource);
        parcel.writeString(buttonTag);
        parcel.writeParcelable(buttonToken, 0);

        // SuperActivityToast Progress items
        parcel.writeInt(progress);
        parcel.writeInt(progressMax);
        parcel.writeByte((byte) (progressIndeterminate ? 1 : 0));
        parcel.writeInt(progressBarColor);
    }

    /**
     * Used for Parcelable functionality.
     */
    public static final Creator CREATOR = new Creator() {
        public Style createFromParcel(Parcel parcel) {
            return new Style(parcel);
        }

        public Style[] newArray(int size) {
            return new Style[size];
        }
    };

    private Style(Parcel parcel) {

        // General SuperToast items
        this.message = parcel.readString();
        this.duration = parcel.readInt();
        this.color = parcel.readInt();
        this.priorityColor = parcel.readInt();
        this.frame = parcel.readInt();
        this.animations = parcel.readInt();
        this.gravity = parcel.readInt();
        this.xOffset = parcel.readInt();
        this.yOffset = parcel.readInt();
        this.width = parcel.readInt();
        this.height = parcel.readInt();
        this.dismissTag = parcel.readString();
        this.dismissToken = parcel.readParcelable(((Object) this).getClass().getClassLoader());
        this.priorityLevel = parcel.readInt();
        this.timestamp = parcel.readLong();
        this.isSuperActivityToast = parcel.readByte() != 0;

        // Message TextView items
        this.messageTypefaceStyle = parcel.readInt();
        this.messageTextColor = parcel.readInt();
        this.messageTextSize = parcel.readInt();
        this.messageIconPosition = parcel.readInt();
        this.messageIconResource = parcel.readInt();

        // General SuperActivityToast items
        this.container = parcel.readInt();
        this.type = parcel.readInt();
        this.isIndeterminate = parcel.readByte() != 0;
        this.touchToDismiss = parcel.readByte() != 0;

        // SuperActivityToast Button items
        this.buttonText = parcel.readString();
        this.buttonTypefaceStyle = parcel.readInt();
        this.buttonTextColor = parcel.readInt();
        this.buttonTextSize = parcel.readInt();
        this.buttonDividerColor = parcel.readInt();
        this.buttonIconResource = parcel.readInt();
        this.buttonTag = parcel.readString();
        this.buttonToken = parcel.readParcelable(((Object) this).getClass().getClassLoader());

        // SuperActivityToast Progress items
        this.progress = parcel.readInt();
        this.progressMax = parcel.readInt();
        this.progressIndeterminate = parcel.readByte() != 0;
        this.progressBarColor = parcel.readInt();
    }

    /**
     * Default material red transparent style for SuperToasts.
     * @return A new Style
     */
    public static Style red() {
        final Style style = new Style();
        style.color = PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_RED);
        return style;
    }

    /**
     * Default material pink transparent style for SuperToasts.
     * @return A new Style
     */
    public static Style pink() {
        final Style style = new Style();
        style.color = PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_PINK);
        return style;
    }

    /**
     * Default material purple transparent style for SuperToasts.
     * @return A new Style
     */
    public static Style purple() {
        final Style style = new Style();
        style.color = PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_PURPLE);
        return style;
    }

    /**
     * Default material deep purple transparent style for SuperToasts.
     * @return A new Style
     */
    public static Style deepPurple() {
        final Style style = new Style();
        style.color = PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_DEEP_PURPLE);
        return style;
    }

    /**
     * Default material indigo transparent style for SuperToasts.
     * @return A new Style
     */
    public static Style indigo() {
        final Style style = new Style();
        style.color = PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_INDIGO);
        return style;
    }

    /**
     * Default material blue transparent style for SuperToasts.
     * @return A new Style
     */
    public static Style blue() {
        final Style style = new Style();
        style.color = PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_BLUE);
        return style;
    }

    /**
     * Default material light blue transparent style for SuperToasts.
     * @return A new Style
     */
    public static Style lightBlue() {
        final Style style = new Style();
        style.color = PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_LIGHT_BLUE);
        return style;
    }

    /**
     * Default material cyan transparent style for SuperToasts.
     * @return A new Style
     */
    public static Style cyan() {
        final Style style = new Style();
        style.color = PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_CYAN);
        return style;
    }

    /**
     * Default material teal transparent style for SuperToasts.
     * @return A new Style
     */
    public static Style teal() {
        final Style style = new Style();
        style.color = PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_TEAL);
        return style;
    }

    /**
     * Default material green transparent style for SuperToasts.
     * @return A new Style
     */
    public static Style green() {
        final Style style = new Style();
        style.color = PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_GREEN);
        return style;
    }

    /**
     * Default material light green transparent style for SuperToasts.
     * @return A new Style
     */
    public static Style lightGreen() {
        final Style style = new Style();
        style.color = PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_LIGHT_GREEN);
        return style;
    }

    /**
     * Default material lime transparent style for SuperToasts.
     * @return A new Style
     */
    public static Style lime() {
        final Style style = new Style();
        style.color = PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_LIME);
        style.messageTextColor = PaletteUtils.getSolidColor(PaletteUtils.DARK_GREY);
        style.buttonDividerColor = PaletteUtils.getSolidColor(PaletteUtils.DARK_GREY);
        style.buttonTextColor = PaletteUtils.getSolidColor(PaletteUtils.DARK_GREY);
        return style;
    }

    /**
     * Default material yellow transparent style for SuperToasts.
     * @return A new Style
     */
    public static Style yellow() {
        final Style style = new Style();
        style.color = PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_YELLOW);
        style.messageTextColor = PaletteUtils.getSolidColor(PaletteUtils.DARK_GREY);
        style.buttonDividerColor = PaletteUtils.getSolidColor(PaletteUtils.DARK_GREY);
        style.buttonTextColor = PaletteUtils.getSolidColor(PaletteUtils.DARK_GREY);
        return style;
    }

    /**
     * Default material amber transparent style for SuperToasts.
     * @return A new Style
     */
    public static Style amber() {
        final Style style = new Style();
        style.color = PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_AMBER);
        return style;
    }

    /**
     * Default material orange transparent style for SuperToasts.
     * @return A new Style
     */
    public static Style orange() {
        final Style style = new Style();
        style.color = PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_ORANGE);
        return style;
    }

    /**
     * Default material deep orange transparent style for SuperToasts.
     * @return A new Style
     */
    public static Style deepOrange() {
        final Style style = new Style();
        style.color = PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_DEEP_ORANGE);
        return style;
    }

    /**
     * Default material brown transparent style for SuperToasts.
     * @return A new Style
     */
    public static Style brown() {
        final Style style = new Style();
        style.color = PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_BROWN);
        return style;
    }

    /**
     * Default material grey transparent style for SuperToasts.
     * @return A new Style
     */
    public static Style grey() {
        final Style style = new Style();
        style.color = PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_GREY);
        return style;
    }

    /**
     * Default material blue-grey transparent style for SuperToasts.
     * @return A new Style
     */
    public static Style blueGrey() {
        final Style style = new Style();
        style.color = PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_BLUE_GREY);
        return style;
    }

    /**
     * Default material rotten banana transparent style for SuperToasts.
     * @return A new Style
     */
    public static Style rottenBanana() {
        final Style style = new Style();
        style.color = PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_YELLOW);
        style.frame = FRAME_LOLLIPOP;
        style.messageTextColor = PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_BROWN);
        style.buttonDividerColor = PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_BROWN);
        style.buttonTextColor = PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_BROWN);
        style.priorityColor = PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_BROWN);
        return style;
    }
}
