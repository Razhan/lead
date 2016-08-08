package com.ef.newlead.presenter;

import com.ef.newlead.ui.view.View;
import com.ef.newlead.usecase.UseCase;

public abstract class Presenter<V extends View> {

    protected V view;
    protected UseCase useCase;

    public Presenter(V view, UseCase useCase) {
        this.view = view;
        this.useCase = useCase;
    }

    public void onCreate() {
        return;
    }

    public void onStart() {
        return;
    }

    public void onResume() {
        return;
    }

    public void onPause() {
        return;
    }

    public void onStop() {
        view = null;

        if (useCase != null) {
            useCase.unsubscribe();
        }
    }

    public V getView() {
        return view;
    }
}