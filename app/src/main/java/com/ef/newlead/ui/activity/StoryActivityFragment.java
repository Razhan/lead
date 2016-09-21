package com.ef.newlead.ui.activity;

import android.Manifest;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.ef.newlead.R;
import com.ef.newlead.ui.fragment.BaseFragment;
import com.ef.newlead.ui.widget.FontTextView;
import com.ef.newlead.ui.widget.MicrophoneVolumeView;
import com.ef.newlead.util.AudioRecordingHelper;

import java.io.File;

import butterknife.BindView;

/**
 * A fragment handles recording audio for a specified script.
 */
public class StoryActivityFragment extends BaseFragment implements AudioRecordingHelper.RecordListener {

    @BindView(R.id.script)
    FontTextView script;

    @BindView(R.id.recorder_button)
    Button recorderButton;

    @BindView(R.id.microphone_volume)
    MicrophoneVolumeView microphoneView;

    private File audioFile;
    private AudioRecordingHelper recordingHelper;

    private boolean hasAudioPermission = false;

    public StoryActivityFragment() {
    }

    @Override
    public int bindLayout() {
        return R.layout.fragment_story;
    }

    @Override
    public void initView() {
        recorderButton.setOnTouchListener(
                (View v, MotionEvent event) -> {
                    if (!hasAudioPermission)
                        return true;

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

        // require permission instantly for recording audio
        askForPermissions(new PermissionListener() {
            @Override
            public void permissionGranted() {
                hasAudioPermission = true;
            }

            @Override
            public void permissionDenied() {
                hasAudioPermission = false;

            }
        }, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE);

    }

    public void setScript(String text) {
        script.setText(text);
    }

    private void onRecordCancel() {

    }

    private void onRecordComplete() {
        if (recordingHelper != null)
            recordingHelper.stopRecording();

        microphoneView.setVisibility(View.INVISIBLE);
        microphoneView.setProportion(0);
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
}
