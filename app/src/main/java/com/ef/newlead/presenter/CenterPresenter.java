package com.ef.newlead.presenter;


import android.content.Context;

import com.ef.newlead.data.model.DataBean.BaseResponse;
import com.ef.newlead.data.model.DataBean.BookInfoBean;
import com.ef.newlead.data.model.DataBean.CenterTimeBean;
import com.ef.newlead.data.model.DataBean.Response;
import com.ef.newlead.domain.usecase.CenterUseCase;
import com.ef.newlead.ui.view.CenterBookView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CenterPresenter extends Presenter<CenterBookView> {

    public CenterPresenter(Context context, CenterBookView view) {
        super(context, view, new CenterUseCase());
    }

    public void getCenterTime(String id) {
        useCase.new Builder<Response<CenterTimeBean>>()
                .useCaseArgs(id)
                .onSuccess(times -> getView().afterGetCenterTime(times.getData()))
                .build();
    }

    public void bookCenter(String id, String name, String age, String phone, String date, String time) {
        Map<String, String> bookInfo = new HashMap<>();
        bookInfo.put("center_id", id);
        bookInfo.put("name", name);
        bookInfo.put("age", age);
        bookInfo.put("phone", phone);
        bookInfo.put("booking_date", date);
        bookInfo.put("booking_time", time);

        useCase.new Builder<BaseResponse>()
                .useCaseArgs(bookInfo)
                .onSuccess(response -> getView().afterBook())
                .build();
    }

    public void getBookInfo() {
        useCase.new Builder<Response<List<BookInfoBean>>>()
                .useCaseName("GetBookInfo")
                .onSuccess(info -> getView().afterGetBookInfo(info.getData()))
                .build();
    }

}
