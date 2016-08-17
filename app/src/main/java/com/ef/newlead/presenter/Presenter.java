package com.ef.newlead.presenter;

import android.content.Context;

import com.ef.newlead.ui.view.View;
import com.ef.newlead.usecase.UseCase;

public abstract class Presenter<V extends View> {

    protected V view;
    protected UseCase useCase;
    protected Context context;

    public Presenter(Context context, V view, UseCase useCase) {
        this.view = view;
        this.context = context;
        this.useCase = useCase;
    }

    public Presenter(V view, UseCase useCase) {
        this.view = view;
        this.useCase = useCase;
    }

    public Presenter(Context context, V view) {
        this.view = view;
        this.context = context;
    }

    public V getView() {
        return view;
    }

    public void onCreate() {

    }

    public void onStart() {

    }

    public void onResume() {

    }

    public void onPause() {

    }

    public void onDestroy(){
        view = null;
        context = null;

        if (useCase != null) {
            useCase.unsubscribe();
        }
    }
}