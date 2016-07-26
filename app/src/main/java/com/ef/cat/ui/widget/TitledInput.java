package com.ef.cat.ui.widget;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ef.cat.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TitledInput extends FrameLayout {

    @BindView(R.id.titled_input_title)
    TextView title;
    @BindView(R.id.titled_input_input)
    EditText input;
    private int cornerRadius = 15;

    public TitledInput(Context context) {
        this(context, null);
    }

    public TitledInput(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitledInput(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
        init();
    }

    private void initView() {
        View view = inflate(getContext(), R.layout.widget_titled_edittext, this);
        ButterKnife.bind(this, view);
    }

    private void init() {
        GradientDrawable titleBG = new GradientDrawable();
        GradientDrawable inputBG = new GradientDrawable();

        titleBG.setColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        inputBG.setColor(ContextCompat.getColor(getContext(), R.color.white));
        titleBG.setCornerRadii(new float[]{cornerRadius, cornerRadius, 0, 0, 0, 0, cornerRadius, cornerRadius});
        inputBG.setCornerRadii(new float[]{0, 0, cornerRadius, cornerRadius, cornerRadius, cornerRadius, 0, 0});

        title.setBackground(titleBG);
        input.setBackground(inputBG);
    }

    public void setCornerRadius(int cornerRadius) {
        this.cornerRadius = cornerRadius;
    }

    public TitledInput setHint(String hint) {
        input.setHint(hint);
        return this;
    }

    public TitledInput setTitle(String str) {
        title.setText(str);
        return this;
    }
}
