package com.ef.newlead.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

import com.ef.newlead.R;
import com.ef.newlead.util.FontManager;

public class FontTextView extends TextView {

    public FontTextView(Context context) {
        this(context, null);
    }

    public FontTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FontTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (!isInEditMode()) {
            if (attrs != null) {
                TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FontTextView);
                String font = a.getString(R.styleable.FontTextView_font);
                a.recycle();

                if (!TextUtils.isEmpty(font)) {
                    FontManager.getInstance().setFont(this, "font/" + font + ".ttf");
                }
            }
        }
    }
}