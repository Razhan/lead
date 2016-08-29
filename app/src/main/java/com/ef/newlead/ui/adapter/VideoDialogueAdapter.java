package com.ef.newlead.ui.adapter;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.View;

import com.ef.newlead.Constant;
import com.ef.newlead.R;
import com.ef.newlead.data.model.Dialogue;
import com.ef.newlead.ui.widget.recycleview.MultipleTypeRecyclerViewAdapter;
import com.ef.newlead.ui.widget.recycleview.ViewHolder;

import java.util.List;

public class VideoDialogueAdapter extends MultipleTypeRecyclerViewAdapter<Dialogue> {

    public VideoDialogueAdapter(Context context, List<Dialogue> list) {
        super(context, list);
    }

    @Override
    public int getItemLayout() {
        return R.layout.item_dialogue_video;
    }

    @Override
    protected void onBindItemViewHolder(ViewHolder holder, int position, Dialogue item) {
        if (position % 2 == 0) {
            holder.setVisible(R.id.dialogue_profileB, false);
            holder.setVisible(R.id.dialogue_profileA, true);

//            getSlideInLeftAnimation(holder.itemView)
//                    .setDuration(Constant.DEFAULT_ANIM_FULL_TIME).start();

        } else {
            holder.setVisible(R.id.dialogue_profileB, true);
            holder.setVisible(R.id.dialogue_profileA, false);

//            getSlideInRightAnimation(holder.itemView)
//                    .setDuration(Constant.DEFAULT_ANIM_FULL_TIME).start();
        }
    }

    public static ObjectAnimator getSlideInLeftAnimation(View view) {
        return ObjectAnimator.ofFloat(view, "translationX", -view.getRootView().getWidth(), 0);
    }

    public static ObjectAnimator getSlideInRightAnimation(View view) {
        return ObjectAnimator.ofFloat(view, "translationX", view.getRootView().getWidth(), 0);
    }
}
