package com.ef.newlead.ui.view;

import com.ef.newlead.data.model.DataBean.BookInfoBean;
import com.ef.newlead.data.model.DataBean.CenterTimeBean;
import com.ef.newlead.data.model.DataBean.Response;

import java.util.List;

public interface CenterBookView extends View {

    void afterGetCenterTime(CenterTimeBean times);

    void afterBook();

    void afterGetBookInfo(List<BookInfoBean> info);

}
