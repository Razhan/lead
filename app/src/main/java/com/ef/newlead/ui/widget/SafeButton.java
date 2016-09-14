package com.ef.newlead.ui.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Class to work around the annoying queuing of multiple taps for Buttons. Could have a time attribute.
 * <p>
 * This is useful as sometimes (on some platform / device combos) you can quickly tap on a button and end up
 * starting an activity more than one. The reuests seem to be queued before the action has taken place so
 * solutions like disabling the buttons or setting singleTop do not seem to work :(
 * <p>
 * Alternativly a better solution maybe to put this in a OnClickListener subclass and then could use with any view
 *
 * @author dori
 */
public class SafeButton extends Button {
    public SafeButton(Context context) {
        super(context);
    }

    public SafeButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SafeButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @TargetApi(21)
    public SafeButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        super.setOnClickListener(new OnClickListenerWrapper(l));
    }

}
