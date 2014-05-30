package com.github.johnpersano.supertoasts;

import android.app.Activity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.github.johnpersano.supertoasts.util.Style;

/**
 * Created by lgvalle on 30/05/14.
 */
public class SuperCardFloatingToast extends SuperCardToast {
	protected static LinearLayout linLayout;

	public SuperCardFloatingToast(Activity activity) {
		super(activity);
	}

	public SuperCardFloatingToast(Activity activity, Style style) {
		super(activity, style);
	}

	public SuperCardFloatingToast(Activity activity, SuperToast.Type type) {
		super(activity, type);
	}

	public SuperCardFloatingToast(Activity activity, SuperToast.Type type, Style style) {
		super(activity, type, style);
	}

	@Override
	protected void createContainer(Activity activity) {
		if (linLayout == null) {
			linLayout = (LinearLayout) mLayoutInflater.inflate(R.layout.supercardfloatingtoast_container, null, false);
		}
	}


	@Override
	protected void addToastToContainer() {
		// If container is not attached to main view -> attach. This should only happen once
		if (linLayout.getParent() == null) {
			ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			mActivity.addContentView(linLayout, params);
		}
		linLayout.addView(mToastView);
	}

	protected void toastAnimationDidEnd() {
		/* do nothing */
	}


	@Override
	protected void invalidateToast() {
		if (linLayout != null) {
			linLayout.removeView(mToastView);
			linLayout.postInvalidate();
		}
	}
}
