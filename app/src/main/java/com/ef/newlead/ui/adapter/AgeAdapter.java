package com.ef.newlead.ui.adapter;

import android.content.Context;
import android.view.View;

import com.ef.newlead.R;
import com.ef.newlead.data.model.Age;
import com.ef.newlead.ui.widget.flowview.FlowAdapter;
import com.ef.newlead.ui.widget.recycleview.ViewHolder;

import java.util.LinkedList;
import java.util.List;

public class AgeAdapter extends FlowAdapter<Age> {

    public AgeAdapter(Context context, List<Age> list) {
        super(context, list);
    }

    @Override
    public int getItemLayout() {
        return R.layout.item_age;
    }

    @Override
    protected void onBindItemViewHolder(ViewHolder holder, int position, Age item) {
        if (item.isBorder()) {
            holder.itemView.setVisibility(View.INVISIBLE);
        } else {
            holder.itemView.setVisibility(View.VISIBLE);
        }

        holder.setText(R.id.age, item.getAge());
    }

    @Override
    public void setBorderPosition(int pos) {
        for (int i = 0; i < pos; i++) {
            ((LinkedList) getItems()).addFirst(new Age(true));
            ((LinkedList) getItems()).addLast(new Age(true));
        }
        notifyDataSetChanged();
    }


}
