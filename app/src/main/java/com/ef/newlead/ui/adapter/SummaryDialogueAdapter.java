package com.ef.newlead.ui.adapter;

import android.content.Context;

import com.ef.newlead.R;
import com.ef.newlead.data.model.ActivityTemplate;
import com.ef.newlead.ui.widget.recycleview.MultipleTypeRecyclerViewAdapter;
import com.ef.newlead.ui.widget.recycleview.ViewHolder;

import java.util.List;

public class SummaryDialogueAdapter extends MultipleTypeRecyclerViewAdapter<ActivityTemplate.DialogBean> {

    public SummaryDialogueAdapter(Context context, List<ActivityTemplate.DialogBean> list) {
        super(context, list);
    }

    @Override
    public int getItemLayout() {
        return R.layout.item_dialogue_sumary;
    }

    @Override
    protected void onBindItemViewHolder(ViewHolder holder, int position, ActivityTemplate.DialogBean item) {
        if (position % 2 == 0) {
            holder.setVisible(R.id.dialogue_profileB, false);
            holder.setVisible(R.id.dialogue_profileA, true);
        } else {
            holder.setVisible(R.id.dialogue_profileB, true);
            holder.setVisible(R.id.dialogue_profileA, false);
        }

        holder.setText(R.id.dialogue_sentence, item.getText());
        holder.setText(R.id.dialogue_translation, item.getTranslationText());
    }
}
