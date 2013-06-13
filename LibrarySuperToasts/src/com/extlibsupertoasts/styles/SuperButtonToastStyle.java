package com.extlibsupertoasts.styles;

public class SuperButtonToastStyle {

	public int undoButtonResource;
	public CharSequence buttonTextCharSequence;
	public int messageTextColor;
	public int buttonTextColor;
	public int backgroundResource;
	public int dividerResource;

	public SuperButtonToastStyle(int undoButtonResource, CharSequence buttonTextCharSequence, int messageTextColor, 
			int buttonTextColor, int backgroundResource, int dividerResource) {

		this.undoButtonResource = undoButtonResource;
		this.buttonTextCharSequence = buttonTextCharSequence;
		this.messageTextColor = messageTextColor;
		this.buttonTextColor = buttonTextColor;
		this.backgroundResource = backgroundResource;
		this.dividerResource = dividerResource;

	}

}
