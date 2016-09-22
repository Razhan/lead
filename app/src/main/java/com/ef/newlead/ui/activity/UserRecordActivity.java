package com.ef.newlead.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ef.newlead.R;
import com.ef.newlead.ui.widget.ASRProgressView;
import com.ef.newlead.ui.widget.MicrophoneVolumeView;
import com.ef.newlead.util.AudioRecordingHelper;
import com.ef.newlead.util.MiscUtils;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

public class UserRecordActivity extends BaseActivity implements AudioRecordingHelper.RecordListener {

    public final static String KEY_COLORFUL_STRING = "colorfulString";
    public final static String KEY_FULL_STRING = "fullString";
    public final static String KEY_IMAGE_URL = "imageURL";
    public final static String KEY_START_HINT = "startHint";
    public final static String KEY_END_HINT = "endHint";

    @BindView(R.id.record_image)
    ImageView image;
    @BindView(R.id.record_indicator)
    ASRProgressView indicator;
    @BindView(R.id.record_sentence)
    TextView sentence;
    @BindView(R.id.record_replay)
    ImageView replay;
    @BindView(R.id.record_next)
    ImageView next;
    @BindView(R.id.recorder_button)
    Button recorderButton;
    @BindView(R.id.record_volume)
    MicrophoneVolumeView microphoneView;

    private File audioFile;
    private AudioRecordingHelper recordingHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        fullScreen = true;
        super.onCreate(savedInstanceState);
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_user_record;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);

        sentence.setText(MiscUtils.getSpannableText(getIntent().getStringExtra(KEY_FULL_STRING),
                getIntent().getStringExtra(KEY_COLORFUL_STRING),
                Color.parseColor("#0078ff")));

        indicator.setInitText(getIntent().getStringExtra(KEY_START_HINT));
        indicator.show();

        image.setImageResource(getIntent().getIntExtra(KEY_IMAGE_URL, 0));

        recorderButton.setOnTouchListener(
                (View v, MotionEvent event) -> {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        recorderButton.setBackgroundResource(R.drawable.ic_mic_tapping);
                        recorderButton.setPressed(true);
                        onRecordStart();

                        return true;
                    } else if (event.getAction() == MotionEvent.ACTION_UP) {
                        recorderButton.setBackgroundResource(R.drawable.ic_mic);
                        recorderButton.setPressed(false);
                        onRecordComplete();
                        return true;
                    } else if (event.getAction() == MotionEvent.ACTION_CANCEL) {
                        recorderButton.setPressed(false);
                        onRecordCancel();
                        return false;
                    }

                    return true;
                }
        );
    }

    private void onRecordCancel() {

    }

    private void onRecordComplete() {
        if (recordingHelper != null)
            recordingHelper.stopRecording();

        microphoneView.setVisibility(View.INVISIBLE);
        microphoneView.setProportion(0);

        indicator.setResult(true, getIntent().getStringExtra(KEY_END_HINT));
    }

    @Override
    public void onAudioVolumeChange(float value) {
        animateVoice(value);
    }

    @Override
    public File getOutputFile() {
        if (audioFile == null)
            audioFile = file();
        return audioFile;
    }

    private void onRecordStart() {
        setupRecorder();

        microphoneView.setVisibility(View.VISIBLE);

        if (recordingHelper != null)
            recordingHelper.startRecording();

        if (indicator.getTranslationY() != 0) {
            indicator.show();
        }
    }

    private void setupRecorder() {
        if (audioFile == null) {
            audioFile = file();
        }

        recordingHelper = new AudioRecordingHelper(this);
    }

    private void animateVoice(final float maxPeak) {
        microphoneView.setProportion(maxPeak);
    }

    @NonNull
    private File file() {
        return new File(Environment.getExternalStorageDirectory(), "test.wav");
    }

    @OnClick(R.id.record_next)
    public void onNextClick() {
        startActivity(new Intent(this, StoryTellActivity.class));
        finish();
    }


}
