package com.ef.newlead.ui.view;

/**
 * Created by seanzhou on 8/30/16.
 */
public interface VideoRolePlayView extends View {
    void updateRecordScript(String script);

    void onRecognizeResult(boolean passed);

    void onStartPlayingSampleAudio();

    void onFinishPlayingSampleAudio();
}
