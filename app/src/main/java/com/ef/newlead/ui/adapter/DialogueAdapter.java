package com.ef.newlead.ui.adapter;

import android.content.Context;

import com.ef.newlead.R;
import com.ef.newlead.data.model.Dialogue;
import com.ef.newlead.ui.widget.recycleview.MultipleTypeRecyclerViewAdapter;
import com.ef.newlead.ui.widget.recycleview.ViewHolder;

import java.util.List;

public class DialogueAdapter extends MultipleTypeRecyclerViewAdapter<Dialogue> {

    public DialogueAdapter(Context context, List<Dialogue> list) {
        super(context, list);
    }

    @Override
    public int getItemLayout() {
        return R.layout.item_dialogue_sumary;
    }

    @Override
    protected void onBindItemViewHolder(ViewHolder holder, int position, Dialogue item) {
        if (position % 2 == 0) {
            holder.setVisible(R.id.dialogue_profileB, false);
            holder.setVisible(R.id.dialogue_profileA, true);
        } else {
            holder.setVisible(R.id.dialogue_profileB, true);
            holder.setVisible(R.id.dialogue_profileA, false);
        }

    }
}
