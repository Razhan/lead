package com.ef.newlead.presenter;


import android.content.Context;
import android.graphics.Color;

import com.ef.newlead.Constant;
import com.ef.newlead.R;
import com.ef.newlead.data.model.Center;
import com.ef.newlead.data.model.DataBean.BaseResponse;
import com.ef.newlead.data.model.DataBean.BookInfoBean;
import com.ef.newlead.data.model.DataBean.CenterTimeBean;
import com.ef.newlead.data.model.DataBean.Response;
import com.ef.newlead.domain.usecase.CenterUseCase;
import com.ef.newlead.ui.view.CenterBookView;
import com.ef.newlead.util.SharedPreUtils;
import com.google.gson.Gson;

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

    public void getBookInfo(String centerId) {
        useCase.new Builder<Response<List<BookInfoBean>>>()
                .useCaseName("GetBookInfo")
                .onSuccess(info -> handleBookInfo(centerId, info.getData()))
                .build();
    }

    private void handleBookInfo(String centerId, List<BookInfoBean> info) {
        Map<String, String> bookedMap = new HashMap<>();
        Gson gson = new Gson();

        for (BookInfoBean bean : info) {
            Center.BookInfo newInfo = new Center.BookInfo(bean.getBookingDate(), bean.getBookingTime());
            bookedMap.put(bean.getCenterId(), gson.toJson(newInfo));
        }

        SharedPreUtils.putNewStringMap(Constant.BOOKED_CENTER, bookedMap);

        checkCenterBooked(centerId);
    }

    public void checkCenterBooked(String id) {
        if (SharedPreUtils.containStringMap(Constant.BOOKED_CENTER, id)) {
            String bookInfoStr = SharedPreUtils.loadMap(Constant.BOOKED_CENTER).get(id);

            Center.BookInfo bookInfo = new Gson().fromJson(bookInfoStr, Center.BookInfo.class);

            getView().hasBooked(true, bookInfo);
        } else {
            getView().hasBooked(false, null);
        }
    }

    public void cancelBook(String id) {
//        SharedPreUtils.removeStringMap(Constant.BOOKED_CENTER, id);
//        getView().hasBooked(false, null);
    }

}
