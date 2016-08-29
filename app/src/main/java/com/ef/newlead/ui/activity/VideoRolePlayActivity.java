package com.ef.newlead.ui.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.devbrackets.android.exomedia.core.video.scale.ScaleType;
import com.devbrackets.android.exomedia.listener.OnPreparedListener;
import com.ef.newlead.R;
import com.ef.newlead.ui.widget.AutoSizeVideoView;
import com.ef.newlead.ui.widget.ColorfulProgressBar;
import com.ef.newlead.ui.widget.VideoControlLayout;
import com.ef.newlead.util.ViewUtils;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/***
 * An Activity provides video interaction and ASR evaluation.
 */
public class VideoRolePlayActivity extends AppCompatActivity implements OnPreparedListener,
        VideoControlLayout.VisibilityAnimationListener, VideoControlLayout.PlayingProgressChangeListener, AutoSizeVideoView.VideoSizeMonitor {

    protected boolean pausedInOnStop = false;
    @BindView(R.id.script_parent)
    LinearLayout scriptLayout;
    private AutoSizeVideoView videoView;
    private boolean isRestarted = false;
    private VideoControlLayout controlLayout;
    private ActionBar actionBar;
    private ListView listView;
    private ColorfulProgressBar progressBar;
    // whether video size has been changed in order to keep its aspect ratio.
    private boolean videoResized = false;
    private int videoDisplayWidth = 0;
    private int videoDisplayHeight = 0;
    private boolean addedToFavorite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_role_play);

        ButterKnife.bind(this);

        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("At the cafe");
        }

        initVideoComponent();

        initList();

        adjustZOrderForChildView();
    }

    private void adjustZOrderForChildView() {
        scriptLayout.bringToFront();
        videoView.bringToFront();

        progressBar = (ColorfulProgressBar) findViewById(R.id.bar);
        progressBar.bringToFront();

        // set the cue points if any
        progressBar.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                float[] array = {1f, 2f, 3f, 5f, 8f, 9f};

                progressBar.setDotsPosition(10f, array);

                progressBar.getViewTreeObserver().removeOnPreDrawListener(this);
                return true;
            }
        });

        // The progressBar is located between the video and the script list, in order to achieve
        // this effect, progressBar's top margin will be updated dynamically. And all the Z-order of
        // the child views in layout activity_video_role_play get updated.
        progressBar.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (progressBar.getHeight() > 0 && videoView.getHeight() > 0 && videoResized) {
                    int h = (int) (videoView.getLayoutParams().height + videoView.getX());

                    int height = progressBar.getHeight();
                    FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) (progressBar.getLayoutParams());
                    layoutParams.topMargin = h - height / 2 - ViewUtils.dpToPx(VideoRolePlayActivity.this, 2);
                    Timber.i(">>> progressBar top margin changed to : " + layoutParams.topMargin);

                    progressBar.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                    if (controlLayout != null) {
                        // update child view layout after video view is ready
                        controlLayout.centralizeControlViewLayout();
                    }
                }
            }
        });

    }

    private void initList() {
        listView = (ListView) findViewById(R.id.listView);
        List<String> strings = new LinkedList<>();

        // testing data
        for (int i = 0; i < 15; i++) {
            strings.add("Item" + (i + 1));
        }
        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                strings));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.video_action_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;

            case R.id.menu_favorite:
                String message = "";

                if (!addedToFavorite) {
                    item.setIcon(R.drawable.favorite_pressed);
                    message = "Added to favorite list.";
                } else {
                    item.setIcon(R.drawable.favorite);
                    message = "Removed from favorite list.";
                }

                addedToFavorite = !addedToFavorite;
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPrepared() {
        videoView.start();

        if (isRestarted) {
            isRestarted = false;
            stopVideo();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopVideo();
    }

    private void stopVideo() {
        if (videoView.isPlaying()) {
            pausedInOnStop = true;
            videoView.pause();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (pausedInOnStop) {
            videoView.start();
            pausedInOnStop = false;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        videoView.release();
    }

    protected void initVideoComponent() {
        videoView = (AutoSizeVideoView) findViewById(R.id.video_play_activity_video_view);
        videoView.setVideoSizeMonitor(this);

        controlLayout = new VideoControlLayout(this);
        controlLayout.setVisibilityAnimationListener(this);
        controlLayout.setPlayingProgressChangeListener(this);

        videoView.setControls(controlLayout);
        videoView.setMeasureBasedOnAspectRatioEnabled(false);
        videoView.setOnPreparedListener(this);

        // https://github.com/brianwernick/ExoMedia/issues/1
        videoView.setScaleType(ScaleType.NONE); // works for width match_parent
        videoView.setOnCompletionListener(() -> isRestarted = videoView.restart());

        // for testing only
        Uri uri = Uri.parse("file:///android_asset/test.mp4");
        videoView.setVideoURI(uri);

        videoView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (videoView.getHeight() > 0 && !videoResized && videoDisplayWidth > 0) {
                    int width = videoView.getWidth();
                    int height = videoView.getHeight();
                    int scaledHeight = (int) (videoDisplayHeight * width * 1.0f / videoDisplayWidth);
                    Timber.i(">>> video height changed from %d to %d ", height, scaledHeight);

                    videoView.getLayoutParams().height = scaledHeight;
                    videoView.invalidate();

                    int totalHeight = (int) (videoView.getX() + videoView.getLayoutParams().height);
                    scriptLayout.setPadding(0, totalHeight, 0, 0);

                    videoResized = true;

                    videoView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });
    }

    @Override
    public void onAnimate(boolean visible) {
        if (actionBar == null)
            return;

        if (visible) {
            actionBar.show();
            progressBar.setThumb(true);
        } else {
            actionBar.hide();
            progressBar.setThumb(false);
        }
    }


    @Override
    public void onUpdate(float progress) {
        progressBar.setProgress(progress);
    }

    @Override
    public void onSizeChanged(int displayWidth, int displayHeight, float pixelWidthHeightRatio) {
        if (videoView.getLayoutParams() != null && displayHeight > 0) {
            Timber.i(">>> Real height for the video is %d , video origin size w:%d | h:%d",
                    videoView.getHeight(), displayWidth, displayHeight);

            videoDisplayWidth = displayWidth;
            videoDisplayHeight = displayHeight;
        }
    }
}
