package com.ef.cat.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.ef.cat.R;
import com.ef.cat.data.model.Level;
import com.ef.cat.ui.widget.coverFlowView.CoverFlowAdapter;
import com.ran.delta.widget.recycleView.ViewHolder;

import java.util.LinkedList;
import java.util.List;

public class LevelAdapter extends CoverFlowAdapter<Level> {

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
        holder.setText(R.id.level_header, item.getValue());
        ImageView indicator = holder.getView(R.id.select_indicator);
        indicator.setOnClickListener(v -> {
            ((ImageView) v).setImageResource(R.drawable.ic_la);
            Log.d("IndicatedCircle", "IndicatedCircle");
        });
    }

    @Override
    public void setBorderPosition(int pos) {
        for (int i = 0; i < pos; i++){
            ((LinkedList) getItems()).addFirst(new Level(true));
            ((LinkedList) getItems()).addLast(new Level(true));
        }
        notifyDataSetChanged();
    }
}
