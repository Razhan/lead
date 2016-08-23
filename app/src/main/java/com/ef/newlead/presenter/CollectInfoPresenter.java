package com.ef.newlead.presenter;

import android.content.Context;

import com.ef.newlead.domain.usecase.UseCase;
import com.ef.newlead.ui.view.CollectInfoView;

public class CollectInfoPresenter extends Presenter<CollectInfoView> {

    public CollectInfoPresenter(Context context, CollectInfoView view, UseCase useCase) {
        super(context, view, useCase);
    }

    public void submitInfo() {
        
    }


}
