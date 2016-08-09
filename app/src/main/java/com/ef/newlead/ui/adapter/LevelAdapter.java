package com.ef.newlead.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.ef.newlead.R;
import com.ef.newlead.data.model.Level;
import com.ef.newlead.ui.widget.flowview.FlowAdapter;
import com.ef.newlead.ui.widget.recycleview.ViewHolder;

import java.util.LinkedList;
import java.util.List;

public class LevelAdapter extends FlowAdapter<Level> {

    public LevelAdapter(Context context, List<Level> list) {
        super(context, list);
    }

    @Override
    public int getItemLayout() {
        return R.layout.item_level;
    }

    @Override
    protected void onBindItemViewHolder(ViewHolder holder, int position, Level item) {
        if (item.isBorder()) {
            holder.itemView.setVisibility(View.INVISIBLE);
        } else {
            holder.itemView.setVisibility(View.VISIBLE);
        }
        holder.setText(R.id.level_title, item.getLevelTitle())
                .setText(R.id.level_detail, item.getLevelDetail())
                .setText(R.id.level_example, item.getLevelExample())
                .setOnClickListener(R.id.select_indicator, v -> ((ImageView) v).setImageResource(R.drawable.ic_la));
    }

    @Override
    public void setBorderPosition(int pos) {
        for (int i = 0; i < pos; i++) {
            ((LinkedList) getItems()).addFirst(new Level(true));
            ((LinkedList) getItems()).addLast(new Level(true));
        }
        notifyDataSetChanged();
    }
}
