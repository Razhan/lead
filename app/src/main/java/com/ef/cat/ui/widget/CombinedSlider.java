package com.ef.cat.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ef.cat.R;

import app.minimize.com.seek_bar_compat.SeekBarCompat;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CombinedSlider extends FrameLayout {

    @BindView(R.id.slider_icon)         ImageView sliderIcon;
    @BindView(R.id.slider_title)        TextView sliderTitle;
    @BindView(R.id.slider_seekbar)      SeekBarCompat sliderSeekbar;

    private String title;
    private Drawable icon;

    public CombinedSlider(Context context) {
        this(context, null);
    }

    public CombinedSlider(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CombinedSlider(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
        init(context, attrs);
    }

    private void initView() {
        View view = inflate(getContext(), R.layout.widget_combined_slider, this);
        ButterKnife.bind(this, view);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CombinedSlider);
            try {
                title = a.getString(R.styleable.CombinedSlider_combined_title);
                icon = a.getDrawable(R.styleable.CombinedSlider_combined_icon);
            } finally {
                a.recycle();
            }
        }

        if (title != null) {
            sliderTitle.setText(title);
        }

        if (icon != null) {
            sliderIcon.setImageDrawable(icon);
        }
    }




}
