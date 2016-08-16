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

    }

    public void onStart() {

    }

    public void onResume() {

    }

    public void onPause() {

    }

    public void onStop() {

    }

    public void dispose(){
        view = null;

        if (useCase != null) {
            useCase.unsubscribe();
        }
    }

    public V getView() {
        return view;
    }
}