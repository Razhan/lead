package com.ef.newlead.presenter;

import android.content.Context;

import com.ef.newlead.asr.DroidASRComponent;
import com.ef.newlead.data.model.ActivityTemplate;
import com.ef.newlead.ui.view.VideoView;
import com.google.android.exoplayer.util.Assertions;
import com.google.common.base.Joiner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import timber.log.Timber;

/**
 * Created by seanzhou on 9/9/16.
 */
public class VideoRolePlayPresenter extends VideoPresenter {

    private static final VideoView EMPTY_VIEW = new VideoView() {
        @Override
        public void updateLoadProgress(int progress) {

        }

        @Override
        public void afterLoaded() {

        }

        @Override
        public void afterScoreSubmitted() {

        }

        @Override
        public void showMessage(String msg) {

        }
    };
    private final DroidASRComponent asrComponent;
    private List<ActivityTemplate.RolePlayElement> elements = new LinkedList<>();

    public VideoRolePlayPresenter(Context context) {
        super(context, EMPTY_VIEW, null);

        asrComponent = new DroidASRComponent();

        // init directly
        onCreate();

        initAsrElements();
    }

    public void setAsrListener(DroidASRComponent.AsrResultListener listener) {
        asrComponent.setResultListener(listener);
    }

    public void updateAsrData(int stepIndex) {
        ActivityTemplate.Asr asr = getAsr(stepIndex);
        String phones = asr.getCorrectPhones();
        String dict = Joiner.on("\n").join(asr.getDictionary());

        asrComponent.setAsrWords(phones);
        asrComponent.setOptions(Arrays.asList(phones));
        asrComponent.setDictionary(dict);
    }

    public void stopRecording() {
        asrComponent.stopRecording();
    }

    public void startRecording() {
        asrComponent.startRecording();
    }

    private void initAsrElements() {
        List<List<ActivityTemplate.RolePlayElement>> elementGroups = mActivityTemplate.getRolePlayItems();
        Assertions.checkNotNull(elementGroups);

        for (List<ActivityTemplate.RolePlayElement> group : elementGroups) {
            for (ActivityTemplate.RolePlayElement element : group) {

                elements.add(element);
            }
        }
    }

    @Override
    protected boolean isTarget(ActivityTemplate at) {
        Timber.d(">>> target template: " + at.getTypeNum());
        return ActivityTemplate.TemplateType.RolePlay.equals(at.getTypeNum());
    }

    @Override
    public List<ActivityTemplate.DialogBean> getAllDialogueBeans() {
        return Collections.EMPTY_LIST;
    }

    @Override
    public int getDialogSize() {
        return 0;
    }

    @Override
    public List<ActivityTemplate.DialogBean> getDialogsByIndex(int index) {
        return null;
    }

    public String getSentenceByIndex(int index) {
        return elements.get(index).getSentence();
    }

    public ActivityTemplate.Asr getAsr(int index) {
        return elements.get(index).getAsr();
    }

    @Override
    public List<Double> getTimeStamps() {
        List<Double> timestamps = new ArrayList<>();

        // add time stamps, respecting the configuration
        List<List<ActivityTemplate.RolePlayElement>> elementGroups = mActivityTemplate.getRolePlayItems();

        for (List<ActivityTemplate.RolePlayElement> group : elementGroups) {
            for (ActivityTemplate.RolePlayElement element : group) {

                timestamps.add(element.getStartTime());
            }
        }

        return timestamps;
    }
}
