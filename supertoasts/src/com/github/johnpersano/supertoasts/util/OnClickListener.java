package com.github.johnpersano.supertoasts.util;

import android.os.Parcelable;
import android.view.View;

/** Custom OnClickListener to be used with SuperActivityToasts/SuperCardToasts. Note that
 *  SuperActivityToasts/SuperCardToasts must use this with an {@link com.github.johnpersano.supertoasts.util.OnClickWrapper} */
public interface OnClickListener {

    public void onClick(View view, Parcelable token);

}
