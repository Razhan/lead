package com.ef.newlead.ui.view;

public interface VideoView extends View {

    void updateLoadProgress(int progress);

    void afterLoaded();

    void afterScoreSubmitted();
}
