package com.ef.newlead.asr;

import com.ef.android.asr.util.jsgf.GenerateJSGF;
import com.ef.android.asr.util.jsgf.GroupGenerator;

import java.util.List;

import timber.log.Timber;

/**
 * Created by seanzhou on 8/30/16.
 * <p>
 * Concrete ASR component used to start/stop recording audio and recognize the input audio source.
 */
public class DroidASRComponent extends ASRRecognizer {

    /**
     * The file path for asr recording
     */
    private String recordedFilePath;

    /**
     * The options ( phones ) for each question
     */
    private List<String> options;

    /**
     * phones of the right answer
     */
    private String asrWords;
    private AsrResultListener resultListener;

    public DroidASRComponent setResultListener(AsrResultListener resultListener) {
        this.resultListener = resultListener;
        return this;
    }

    @Override
    public void onSampleLevel(short level) {
        if (resultListener != null) {
            resultListener.onSampleLevelChanged(level);
        }
    }

    /***
     * Sets the options ( phones ) for current question.
     *
     * @param options list of phones
     * @return
     */
    public DroidASRComponent setOptions(List<String> options) {
        this.options = options;
        setGrammar(GenerateJSGF.genJSGF(options, new GroupGenerator()));

        return this;
    }

    /***
     * Sets the correct phones as the right answer.
     *
     * @param asrWords correct phones
     * @return
     */
    public DroidASRComponent setAsrWords(String asrWords) {
        this.asrWords = asrWords;
        return this;
    }

    public String getRecordedFilePath() {
        return recordedFilePath;
    }

    @Override
    public void onResult(Result result) {

        String asrResult = result.bestHypothesis();

        boolean successful = false;
        if (options.contains(asrResult) && asrResult.equals(asrWords)) {
            successful = true;
        } else {
            successful = false;
        }

        if (resultListener != null) {
            if (successful)
                resultListener.onSucceed();
            else
                resultListener.onFailure();
        }

        recordedFilePath = result.audioFile().getAbsolutePath();
        Timber.i(">>> Record File: %s", recordedFilePath);
    }

    @Override
    public void onError(String s) {
        Timber.e("Error: " + s);
        if (resultListener != null) {
            resultListener.onFailure();
        }
    }

    /***
     * Audio recognition result listener
     */
    public interface AsrResultListener {
        void onSucceed();

        void onFailure();

        /***
         * Notify the max level of audio change during audio recording.
         *
         * @param level
         */
        void onSampleLevelChanged(short level);
    }
}
