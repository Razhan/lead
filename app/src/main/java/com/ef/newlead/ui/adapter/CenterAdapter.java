package com.ef.newlead.ui.adapter;

import android.content.Context;

import com.ef.newlead.R;
import com.ef.newlead.data.model.Center;
import com.ef.newlead.ui.widget.recycleview.BasicRecyclerViewAdapter;
import com.ef.newlead.ui.widget.recycleview.ViewHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CenterAdapter extends BasicRecyclerViewAdapter<Center> {

    private List<Center> allCenters;

    public CenterAdapter(Context context, List<Center> list) {
        super(context, null);
        this.allCenters = list;
    }

    @Override
    public int getItemLayout() {
        return R.layout.item_center;
    }

    @Override
    protected void onBindItemViewHolder(ViewHolder holder, int position, Center item) {
        holder.setText(R.id.center_name, item.getSchoolName())
                .setText(R.id.center_address, item.getAddress());
    }

    public List<Center> getCenterInCity(String code) {
        List<Center> res = new ArrayList<>();

        for (Center center : allCenters) {
            if (center.getCityCode().equals(code)) {
                res.add(center);
            }
        }

        Collections.sort(res);

        return res;
    }

}
