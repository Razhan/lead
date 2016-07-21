package com.ef.cat.ui.adapter;

import android.content.Context;

import com.ef.cat.R;
import com.ef.cat.data.model.Interest;
import com.ran.delta.widget.recycleView.MultipleTypeRecyclerViewAdapter;
import com.ran.delta.widget.recycleView.ViewHolder;

import java.util.List;

public class InterestAdapter extends MultipleTypeRecyclerViewAdapter<Interest> {

    public InterestAdapter(Context context, List<Interest> list) {
        super(context, list);
    }

    @Override
    public int getItemLayout() {
        return R.layout.item_interest;
    }

    @Override
    protected void onBindItemViewHolder(ViewHolder holder, int position, Interest item) {
        holder.setText(R.id.interest_name, item.getValue());
    }
}
