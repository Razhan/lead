package com.ef.newlead.ui.fragment.storyTell;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;

import com.ef.newlead.R;
import com.ef.newlead.presenter.Presenter;
import com.ef.newlead.ui.activity.PermissionListener;
import com.ef.newlead.ui.activity.StoryTellActivity;
import com.ef.newlead.ui.activity.UserRecordActivity;
import com.ef.newlead.ui.fragment.BaseFragment;
import com.ef.newlead.ui.widget.DeletableEditText;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TellNameFragment extends BaseTellFragment {

    @BindView(R.id.tell_name_input)
    DeletableEditText input;

    public static Fragment newInstance() {
        return new TellNameFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String permissions[] = new String[] {Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        askForPermissions(new PermissionListener() {
            @Override
            public void permissionGranted() {

            }

            @Override
            public void permissionDenied() {
                ((StoryTellActivity)getActivity()).showPermissionDeniedView(permissions);
            }
        }, permissions);
    }

    @Override
    public int bindLayout() {
        return R.layout.fragment_tell_name;
    }

    @Override
    public void initView() {
        super.initView();

        input.setHint("Enter your name");

        input.setOnEditorActionListener((v, actionId, event) -> {
            if (KeyEvent.KEYCODE_ENDCALL == actionId) {
                startRecordActivity("Hello, My name is %s",
                        input.getText().toString(),
                        "Now record your introduction.",
                        "Awesome! Nice to meet you.",
                        R.drawable.bg_story_name);
            }
            return false;
        });
    }

    @Override
    protected Presenter createPresent() {
        return null;
    }

    @Override
    protected String[] getHeaderText() {
        return new String[] {
                "Hello, my name is…",
                "你好，我的名字是...",
                "First, Introduce yourself to your fellow travelers."};
    }
}
