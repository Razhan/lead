package com.ef.newlead.asr;

import android.content.Context;

import com.ef.android.asr.util.jsgf.GenerateJSGF;
import com.ef.android.asr.util.jsgf.GroupGenerator;

import java.util.List;

/**
 * Created by seanzhou on 8/30/16.
 */
public class DroidASRComponent extends ASRRecognizer {
    private Context context;

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

    public interface AsrResultListener {
        void onSucceed();

        void onFailure();
    }

    private AsrResultListener resultListener;

    public DroidASRComponent setResultListener(AsrResultListener resultListener) {
        this.resultListener = resultListener;
        return this;
    }

    public DroidASRComponent() {

    }

    public DroidASRComponent setOptions(List<String> options) {
        this.options = options;
        setGrammar(GenerateJSGF.genJSGF(options, new GroupGenerator()));

        return this;
    }

    public DroidASRComponent setAsrWords(String asrWords) {
        this.asrWords = asrWords;
        return this;
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
        System.out.println(String.format(">>> Record File: %s", recordedFilePath));
    }

    @Override
    public void onError(String s) {
        System.out.println("Error: " + s);
        if (resultListener != null) {
            resultListener.onFailure();
        }
    }
}
