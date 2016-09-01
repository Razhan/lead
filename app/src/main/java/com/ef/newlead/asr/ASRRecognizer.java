package com.ef.newlead.asr;

import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.ef.android.asr.DecodeTask;
import com.ef.android.asr.RawData;
import com.ef.android.asr.TaskListener;
import com.ef.android.asr.TaskProcessor;
import com.ef.android.asr.Util;
import com.ef.android.asr.afwriter.MediaMuxerAdapter;
import com.ef.android.asr.afwriter.RawAacAdapter;
import com.ef.android.asr.datasources.AudioInputSource;
import com.ef.android.asr.decoders.PocketSphinxDecoder;
import com.ef.android.asr.decoders.PocketSphinxDictionary;
import com.ef.android.asr.decoders.PocketSphinxResult;
import com.ef.android.asr.decoders.RawToFileDecoder;
import com.ef.android.asr.exceptions.DataSourceException;
import com.ef.newlead.NewLeadApplication;

import java.io.File;

import static com.ef.android.asr.Util.conjoin;

/**
 *
 * This class use the ASR wrapper to recognise audio input and save an audio file
 * of the recording.
 * Created by Michael on 2016-5-17.
 */
public abstract class ASRRecognizer implements TaskListener<ASRRecognizer.Result> {
    private static final String TAG = ASRRecognizer.class.toString();
    private static final int SAMPLE_RATE = 16000;

    private static final int MP4_BITRATE = 64000;
    private TaskProcessor<Result> taskProcessor;
    private PocketSphinxDecoder asrDecoder;
    private String grammar = "";
    private String dictionary = "";

    public class Result extends Util.ResultPair<File, PocketSphinxResult> {

        public String bestHypothesis() {
            return getSecond().getBestHypothesis();
        }

        public File audioFile() {
            return getFirst();
        }
    }

    public ASRRecognizer() {
        // Make sure the acoustic model data was created
        File hmmDir = new File(NewLeadApplication.getApp().getFilesDir().getAbsolutePath() + File.separator + NewLeadApplication.HMM_PACKAGE);
        if (!(hmmDir.exists() && hmmDir.isDirectory())) {
            onError("Failed to create acoustic model directory " + hmmDir);
        }else {
            // Configure the ASR engine with the acoustic model and sample rate
            asrDecoder = PocketSphinxDecoder.setup().hmmPath(hmmDir).sampleRate(SAMPLE_RATE).getEngine();
        }
    }

    public void setDictionary(String dictionary) {
        this.dictionary = dictionary;
    }

    /**
     * Override this to read the max level of the last read
     * @param level
     */
    public void onSampleLevel(short level) {
        // do nothing by default
    }


    public void setGrammar(String grammar) {
        this.grammar = grammar;
    }

    public void startRecording() {
        try {
            asrDecoder.setContext(PocketSphinxDictionary.parse(dictionary), grammar);

            RawToFileDecoder mp4encoder = createAudioFileEncoder();

            final AudioInputSource audioInputSource = new AudioInputSource(SAMPLE_RATE) {

                @Override
                public RawData read() throws DataSourceException {
                    RawData data = super.read();

                    // the following code computes a smoothed maximum volume level for the current frame.

                    final short[] shorts = data.getData();

                    // smoothing window
                    int windowSize = 20;

                    // initialise the total level (sum) of the initial window
                    int sum = 0;
                    for (int i = 0; i < windowSize; ++i) {
                        sum += shorts[i];
                    }

                    // this is the current maximum
                    int maxSum = sum;

                    int windowCount = shorts.length - windowSize;

                    for (int i = 1; i < windowCount; ++i) {

                        // remove first sample from the total
                        sum -= shorts[i - 1];

                        // add the next sample to total
                        sum += shorts[i + windowSize];

                        if (sum > maxSum) {
                            maxSum = sum;

                        }
                    }

                    onSampleLevel((short) (maxSum / (windowSize - 10)));

                    return data;
                }
            };

            // create a processor with a task that will join both decoders
            taskProcessor = new TaskProcessor<>(new DecodeTask<>(conjoin(mp4encoder, asrDecoder, new Result()), audioInputSource));

            taskProcessor.start(this);

        } catch (Exception e) {
            Log.e(TAG, "Error starting processor :\n"+ e.getMessage());
            onError(e.getMessage());
        }
    }

    public void stopRecording() {
        Log.i(TAG, "stop");
        if (taskProcessor != null) {
            taskProcessor.finish();
        }
    }

    private RawToFileDecoder createAudioFileEncoder() throws Exception {
        MediaFormat mediaFormat = new MediaFormat();
        mediaFormat.setString(MediaFormat.KEY_MIME, "audio/mp4a-latm");
        mediaFormat.setInteger(MediaFormat.KEY_AAC_PROFILE, MediaCodecInfo.CodecProfileLevel.AACObjectLC);
        mediaFormat.setInteger(MediaFormat.KEY_CHANNEL_COUNT, 1);
        mediaFormat.setInteger(MediaFormat.KEY_SAMPLE_RATE, SAMPLE_RATE);
        mediaFormat.setInteger(MediaFormat.KEY_BIT_RATE, MP4_BITRATE);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
            Log.w(TAG, "MP4 output is not supported on Android {}; using raw AAC :"+ Build.VERSION.SDK_INT);
            File outputFile = new File(Environment.getExternalStorageDirectory(), "asr_demo.aac");

            return new RawToFileDecoder(new RawAacAdapter(outputFile, mediaFormat), mediaFormat);

        } else {
            File outputFile = new File(Environment.getExternalStorageDirectory(), "asr_demo.m4a");

            return new RawToFileDecoder(new MediaMuxerAdapter(outputFile), mediaFormat);
        }
    }

}
