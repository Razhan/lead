package com.ef.newlead.ui.view;

import com.ef.newlead.data.model.DataBean.CenterTimeBean;
import com.ef.newlead.data.model.DataBean.Response;

public interface CenterBookView extends View {

    void afterGetCenterTime(Response<CenterTimeBean> times);

    void afterBook();

}
