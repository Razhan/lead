package com.ef.newlead.ui.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;

public class RoundedEditText extends EditText {

    private GradientDrawable background;

    public RoundedEditText(Context context) {
        this(context, null);
    }

    public RoundedEditText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundedEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
//        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        background =  new GradientDrawable();
        background.setCornerRadius(20);
        background.setColor(Color.YELLOW);

//        setBackground(background);

        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                background.setColor(Color.GREEN);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }


}
