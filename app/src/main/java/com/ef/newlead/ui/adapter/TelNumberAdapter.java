package com.ef.newlead.ui.adapter;

import android.content.Context;

import com.ef.newlead.R;
import com.ef.newlead.ui.widget.recycleview.BasicRecyclerViewAdapter;
import com.ef.newlead.ui.widget.recycleview.ViewHolder;

import java.util.List;

public class TelNumberAdapter extends BasicRecyclerViewAdapter<String> {

    public TelNumberAdapter(Context context, List<String> list) {
        super(context, list);
    }

    @Override
    public int getItemLayout() {
        return R.layout.item_tel;
    }

    @Override
    protected void onBindItemViewHolder(ViewHolder holder, int position, String item) {
        holder.setText(R.id.tel_number, item);
    }
}
