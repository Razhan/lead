package com.ef.newlead.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class VerificationView extends LinearLayout implements TextWatcher, View.OnKeyListener {

    private int maxTextLength = 1;
    private int currentIndex = 0;

    private boolean isDelete = false;

    private List<AppCompatEditText> viewList = new ArrayList<>();

    private FillFullListener listener;

    public VerificationView(Context context) {
        this(context, null);
    }

    public VerificationView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerificationView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setListener(FillFullListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        viewList.clear();
        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);

            if (childView instanceof AppCompatEditText) {
                ((AppCompatEditText) childView).addTextChangedListener(this);
                childView.setOnKeyListener(this);
                viewList.add((AppCompatEditText)childView);
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (!isDelete && s.length() >= maxTextLength && currentIndex < viewList.size() - 1) {
            viewList.get(++currentIndex).requestFocus();
        }

        isDelete = false;

        if (listener != null) {
            if (!viewList.get(currentIndex).getText().toString().equals("")) {
                listener.onFillFull(true);
            } else {
                listener.onFillFull(false);
            }
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == 0) {
            isDelete = true;

            if (v instanceof AppCompatEditText) {
                AppCompatEditText editText = (AppCompatEditText)v;
                String text = editText.getText().toString();

                if (text.length() >= maxTextLength) {
                    editText.setText(text.substring(0, text.length() - 1));
                } else if (currentIndex > 0) {
                    AppCompatEditText previousEditText = viewList.get(viewList.indexOf(v) - 1);
                    previousEditText.setText("");
                    previousEditText.requestFocus();
                    currentIndex--;
                }
            }
        } else {
            isDelete = false;
        }

        return false;
    }

    public void changeTextColor(int color) {
        for (AppCompatEditText view : viewList) {
            view.setTextColor(color);
        }
    }


    public interface FillFullListener {
        void onFillFull(boolean isFull);
    }

}
