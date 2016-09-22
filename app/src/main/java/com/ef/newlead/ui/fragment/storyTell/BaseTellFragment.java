package com.ef.newlead.ui.fragment.storyTell;


import android.content.Intent;
import android.support.annotation.CallSuper;
import android.support.annotation.DrawableRes;

import com.ef.newlead.presenter.Presenter;
import com.ef.newlead.ui.activity.StoryTellActivity;
import com.ef.newlead.ui.activity.UserRecordActivity;
import com.ef.newlead.ui.fragment.BaseMVPFragment;

public abstract class BaseTellFragment<P extends Presenter> extends BaseMVPFragment<P> {

    protected StoryTellActivity mActivity;

    protected abstract String[] getHeaderText();

    @Override
    @CallSuper
    public void initView() {
        mActivity = (StoryTellActivity)getActivity();
        mActivity.initHeaderView(getHeaderText());
    }

    protected void startRecordActivity(String fullSentence, String input, String startHint,
                                       String endHint, @DrawableRes int drawable) {
        Intent i = new Intent(mActivity, UserRecordActivity.class);

        i.putExtra(UserRecordActivity.KEY_FULL_STRING, fullSentence);
        i.putExtra(UserRecordActivity.KEY_COLORFUL_STRING, input);
        i.putExtra(UserRecordActivity.KEY_START_HINT, startHint);
        i.putExtra(UserRecordActivity.KEY_END_HINT, endHint);
        i.putExtra(UserRecordActivity.KEY_IMAGE_URL, drawable);

        mActivity.startActivity(i);
    }
}
