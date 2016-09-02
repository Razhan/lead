package com.ef.newlead.ui.fragment;

import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ef.newlead.R;
import com.ef.newlead.asr.DroidASRComponent;
import com.ef.newlead.ui.view.VideoRolePlayView;
import com.ef.newlead.ui.widget.MicrophoneVolumeView;
import com.google.common.base.Joiner;

import java.util.Arrays;

import butterknife.BindView;

/**
 * Created by seanzhou on 8/30/16.
 */
public class VideoRolePlayFragment extends BaseFragment implements VideoRolePlayView {
    @BindView(R.id.recorder_button)
    Button recordBtn;

    @BindView(R.id.script)
    TextView script;

    @BindView(R.id.microphone_volume)
    MicrophoneVolumeView microphoneView;

    private final DroidASRComponent asrComponent;

    public VideoRolePlayFragment() {
        asrComponent = new DroidASRComponent();
    }

    @Override
    public int bindLayout() {
        return R.layout.fragment_video_role_play;
    }

    @Override
    public void initView() {
        recordBtn.setOnTouchListener((View v, MotionEvent event) ->
                {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        recordBtn.setPressed(true);
                        recordBtn.setBackgroundResource(R.drawable.mic_tapping);
                        onRecordStart();
                        return true;
                    } else if (event.getAction() == MotionEvent.ACTION_UP) {
                        recordBtn.setPressed(false);
                        recordBtn.setBackgroundResource(R.drawable.mic);
                        onRecordComplete();
                        return true;
                    } else {
                        recordBtn.setPressed(false);
                        onRecordCancel();
                        return false;
                    }
                }
        );

        // testing sentence for "I'm fine. And you?" with options: {"Yes, she's great!", "Nice to meet you, too."}
        asrComponent.setAsrWords(Joiner.on(" ").join(Arrays.asList("BVPRC", "UGNFA", "PIZNB", "JUHKF")));
        asrComponent.setOptions(Arrays.asList(
                Joiner.on(" ").join(Arrays.asList("BVPRC", "UGNFA", "PIZNB", "JUHKF")),
                Joiner.on(" ").join(Arrays.asList("BYJWB", "ICGQA", "UDMEP")),
                Joiner.on(" ").join(Arrays.asList("ZWGKN", "ZBPGW", "HXTZZ", "LRXBB", "YBCLD"))
        ));
        asrComponent.setDictionary("BVPRC  AY M\nJUHKF  Y UW\nPIZNB  AE N D\nPIZNB  AH N D\nUGNFA  F AY N\n"
                + "BYJWB  Y EH S\nICGQA  SH IY S\nICGQA  SH IY Z\nUDMEP  G R EY T\n" +
                "HXTZZ  M IY T\nLRXBB  Y UW\nYBCLD  T UW\nZBPGW  T AH\nZBPGW  T IH\nZBPGW  T UH\nZBPGW  T UW\nZWGKN  N AY S\n");

        asrComponent.setResultListener(new DroidASRComponent.AsrResultListener() {
            @Override
            public void onSucceed() {
                changeTextColor(Color.GREEN);
            }

            @Override
            public void onFailure() {
                changeTextColor(Color.RED);
            }

            @Override
            public void onSampleLevelChanged(short level) {
                //handler.post(runable);
                microphoneView.setProportion(level);
            }
        });
    }


    private void changeTextColor(int color) {
        getActivity().runOnUiThread(() -> script.setTextColor(color));

    }

    private void onRecordCancel() {
        //asrComponent.stopRecording();
    }

    private void onRecordComplete() {
        microphoneView.setVisibility(View.INVISIBLE);
        microphoneView.setProportion(0);
        asrComponent.stopRecording();
    }

    private void onRecordStart() {
        microphoneView.setVisibility(View.VISIBLE);
        asrComponent.startRecording();
    }

    @Override
    public void updateRecordScript(String script) {

    }

    @Override
    public void onRecognizeResult(boolean passed) {

    }

    @Override
    public void onStartPlayingSampleAudio() {

    }

    @Override
    public void onFinishPlayingSampleAudio() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        asrComponent.setResultListener(null);
    }
}
