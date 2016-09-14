package com.ef.newlead.ui.fragment;

import android.graphics.drawable.GradientDrawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ef.newlead.R;
import com.ef.newlead.data.model.GradientColor;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by seanzhou on 9/13/16.
 */
public class NameFragment extends BaseFragment {

    @BindView(R.id.next)
    Button next;

    @BindView(R.id.your_name)
    TextView nameTitle;

    @BindView(R.id.editText)
    EditText nameEditor;

    @BindView(R.id.rootLayout)
    ViewGroup rootLayout;

    private String name;

    public interface NameInputListener {
        void onNameInput(String name);
    }

    private NameInputListener nameInputListener;

    public NameFragment setNameInputListener(NameInputListener nameInputListener) {
        this.nameInputListener = nameInputListener;
        return this;
    }

    @Override
    public int bindLayout() {
        return R.layout.fragment_name;
    }

    @Override
    public void initView() {
        GradientColor color = getDefaultGradientColor();
        GradientDrawable drawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{color.getTopGradient().toHex(), color.getBottomGradient().toHex()});
        rootLayout.setBackground(drawable);

        nameTitle.setText("你的名字是...?");
        nameEditor.setHint("真实姓名");
        next.setText("NEXT");

        nameEditor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                name = nameEditor.getText().toString().trim();
                boolean empty = TextUtils.isEmpty(name);

                next.setVisibility(empty ? View.INVISIBLE : View.VISIBLE);
            }
        });

        next.setVisibility(View.INVISIBLE);
    }

    @OnClick(R.id.next)
    public void onNext() {
        if (nameInputListener != null) {
            nameInputListener.onNameInput(name);
        }
    }

    @OnClick(R.id.close)
    public void onClose() {
        getActivity().finish();
    }
}
