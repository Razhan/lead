package com.ef.newlead.ui.adapter;

import android.content.Context;
import android.view.View;

import com.ef.newlead.R;
import com.ef.newlead.data.model.Dialogue;
import com.ef.newlead.ui.widget.recycleview.MultipleTypeRecyclerViewAdapter;
import com.ef.newlead.ui.widget.recycleview.ViewHolder;

import java.util.List;

public class VideoDialogueAdapter extends MultipleTypeRecyclerViewAdapter<Dialogue.DialogBean> {

    private boolean showTranslation = false;

    public VideoDialogueAdapter(Context context, List<Dialogue.DialogBean> list) {
        super(context, list);
    }

    @Override
    public int getItemLayout() {
        return R.layout.item_dialogue_video;
    }

    @Override
    protected void onBindItemViewHolder(ViewHolder holder, int position, Dialogue.DialogBean item) {
        holder.itemView.setTag(position);

        if (position % 2 == 0) {
            holder.setVisible(R.id.dialogue_profileB, false);
            holder.setVisible(R.id.dialogue_profileA, true);
        } else {
            holder.setVisible(R.id.dialogue_profileB, true);
            holder.setVisible(R.id.dialogue_profileA, false);
        }

        if (showTranslation) {
            holder.setVisible(R.id.dialogue_translation, View.VISIBLE);
        } else {
            holder.setVisible(R.id.dialogue_translation, View.GONE);
        }

        holder.setText(R.id.dialogue_sentence, item.getText());
        holder.setText(R.id.dialogue_translation, item.getTranslationText());
    }

    public void showTranslation(boolean showTranslation) {
        this.showTranslation = showTranslation;
        notifyDataSetChanged();
    }

}
