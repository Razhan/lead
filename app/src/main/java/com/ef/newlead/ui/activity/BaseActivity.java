package com.ef.newlead.ui.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ef.newlead.R;
import com.ef.newlead.data.model.GradientColor;
import com.ef.newlead.util.SystemText;
import com.tbruyelle.rxpermissions.RxPermissions;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {

    protected final String TAG = this.getClass().getSimpleName();

    protected boolean translucentStatusBar = false;
    protected boolean colorfulStatusBar = false;
    protected boolean fullScreen = false;
    protected boolean screenRotate = false;
    protected boolean doubleClickExit = false;

    @Nullable
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private ProgressDialog progressDialog;

    private boolean BackPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (fullScreen) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        setContentView(bindLayout());
        ButterKnife.bind(this);

        if (translucentStatusBar || colorfulStatusBar) {
            setTranslucentStatusBar();
        }

        if (!screenRotate) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        initView(savedInstanceState);
    }

    public abstract int bindLayout();

    public void setTranslucentStatusBar() {
        setTranslucentColor();
    }

    public void setFullScreen() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public void setScreenRotate(boolean screenRotate) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    public void setDoubleClickExit(boolean isBackExit) {
        doubleClickExit = isBackExit;
    }

    @Override
    public void onBackPressed() {
        if (!doubleClickExit) {
            super.onBackPressed();
            return;
        }

        if (BackPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.BackPressedOnce = true;
        Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(() -> BackPressedOnce = false, 2000);
    }

    @CallSuper
    public void initView(Bundle savedInstanceState) {
        if (toolbar == null) {
            return;
        }

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(showBackIcon());
            getSupportActionBar().setTitle(setToolBarText());

            if (getStatusGradientColor() != null) {
                toolbar.setBackground(getStatusGradientDrawable());
            }

            toolbar.setNavigationOnClickListener(v -> onBackPressed());
        }

        progressDialog = new ProgressDialog(this);
    }

    protected boolean showBackIcon() {
        return true;
    }

    protected String setToolBarText() {
        return null;
    }

    protected void showStatusBar(boolean show) {
        if (show) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        }
    }

    public void showProgress(boolean flag, String message) {
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(flag);
        progressDialog.setCanceledOnTouchOutside(flag);

        progressDialog.show();
    }

    public void hideProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    public void showMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    protected void showDialog(String message, String positive, String negative,
                              DialogInterface.OnClickListener positiveListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(message);
        builder.setPositiveButton(positive, positiveListener);
        builder.setNegativeButton(negative, (dialog, which) -> dialog.dismiss());

        builder.create().show();
    }

    public void setTranslucentColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            if (!colorfulStatusBar) {
                adjustToolbar();
                return;
            }

            ViewGroup decorView = (ViewGroup) this.getWindow().getDecorView();
            View statusView = createStatusView();

            if (getStatusGradientColor() != null) {
                statusView.setBackground(getStatusGradientDrawable());
            } else {
                statusView.setBackgroundColor(getStatusColor());
            }

            decorView.addView(statusView);

            ViewGroup rootView = (ViewGroup) ((ViewGroup) this.findViewById(android.R.id.content)).getChildAt(0);
            rootView.setFitsSystemWindows(true);
            rootView.setClipToPadding(true);
        }
    }

    private View createStatusView() {
        int resourceId = this.getResources().getIdentifier("status_bar_height", "dimen", "android");
        int statusBarHeight = this.getResources().getDimensionPixelSize(resourceId);

        View statusView = new View(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                statusBarHeight);
        statusView.setLayoutParams(params);

        return statusView;
    }

    private void adjustToolbar() {
        int resourceId = this.getResources().getIdentifier("status_bar_height", "dimen", "android");
        int statusBarHeight = this.getResources().getDimensionPixelSize(resourceId);

        if (toolbar != null) {
            ViewGroup.LayoutParams toolbarParams = toolbar.getLayoutParams();
            ((ViewGroup.MarginLayoutParams) toolbarParams).setMargins(0, statusBarHeight, 0, 0);
            toolbar.setLayoutParams(toolbarParams);
        }
    }

    @NonNull
    private GradientDrawable getStatusGradientDrawable() {
        GradientColor color = getStatusGradientColor();
        return new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,
                new int[]{color.getTopGradient().toHex(), color.getBottomGradient().toHex()});
    }

    protected GradientColor getStatusGradientColor() {
        return null;
    }

    protected int getStatusColor() {
        return Color.TRANSPARENT;
    }

    protected String getLocaleText(String key) {
        return SystemText.getSystemText(this, key);
    }

    protected void askForPermissions(PermissionListener listener, String... permissions) {
        RxPermissions.getInstance(this)
                .request(permissions)
                .subscribe(granted -> {
                    if (granted) {
                        listener.permissionGranted();
                    } else {
                        listener.permissionDenied();
                    }
                });
    }

    public interface PermissionListener {

        void permissionGranted();

        void permissionDenied();
    }
}
