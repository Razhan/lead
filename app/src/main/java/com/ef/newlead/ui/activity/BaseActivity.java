package com.ef.newlead.ui.activity;

import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.ef.newlead.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {

    protected final String TAG = this.getClass().getSimpleName();
    protected boolean translucentStatusBar = false;
    protected boolean fullScreen = false;
    protected boolean screenRotate = false;
    protected boolean doubleClickExit = false;
    @Nullable
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @Nullable
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    private ProgressDialog progressDialog;

    private boolean doubleBackToExitPressedOnce = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (fullScreen) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        if (translucentStatusBar) {
            setTranslucentStatusBar();
        }

        setContentView(bindLayout());
        ButterKnife.bind(this);

        if (!screenRotate) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        initView();
    }

    public abstract int bindLayout();

    public void setTranslucentStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
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

        if (!doubleClickExit && doubleBackToExitPressedOnce) {
            finish();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
    }

    @CallSuper
    public void initView() {
        if (toolbar == null || toolbarTitle == null) {
            return;
        }

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            toolbarTitle.setText(getToolBarTitle());
            getSupportActionBar().setDisplayHomeAsUpEnabled(showBackIcon());
            toolbar.setNavigationOnClickListener(v -> onBackPressed());
        }

        progressDialog = new ProgressDialog(this);
    }

    protected boolean showBackIcon() {
        return true;
    }

    protected String getToolBarTitle() {
        return "";
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
        if (!isFinishing()) {
//            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), msg,
                    Snackbar.LENGTH_SHORT).show();
        }
    }

}
