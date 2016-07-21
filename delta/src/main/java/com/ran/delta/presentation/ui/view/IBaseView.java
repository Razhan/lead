package com.ran.delta.presentation.ui.view;

import android.content.Context;

public interface IBaseView {

    void showProgress(boolean flag, String message);

    void showProgress(String message);

    void showProgress();

    void showProgress(boolean flag);

    void hideProgress();

    void showMessage(int resId);

    void showMessage(String msg);

    Context getContext();
}