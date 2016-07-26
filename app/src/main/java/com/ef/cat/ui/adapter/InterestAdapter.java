package com.ef.cat.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import com.ef.cat.R;
import com.ef.cat.data.model.Interest;
import com.ef.cat.ui.widget.IndicatedCircle;
import com.ran.delta.widget.recycleView.MultipleTypeRecyclerViewAdapter;
import com.ran.delta.widget.recycleView.ViewHolder;

import java.util.ArrayList;
import java.util.List;

public class InterestAdapter extends MultipleTypeRecyclerViewAdapter<Interest> {
    private List<Interest> likedInterests;
    private Interest currentInterest;

    public InterestAdapter(Context context, List<Interest> list) {
        super(context, list);
        likedInterests = new ArrayList<>();
    }

    @Override
    public int getItemLayout() {
        return R.layout.item_interest;
    }

    @Override
    protected void onBindItemViewHolder(ViewHolder holder, int position, Interest item) {
        holder.setText(R.id.interest_name, item.getValue());
        IndicatedCircle circle = holder.getView(R.id.interest_indicator);

        if (likedInterests.contains(item)) {
            holder.setBackgroundColor(R.id.interest_wrapper, Color.YELLOW);
        } else {
            holder.setBackgroundColor(R.id.interest_wrapper, Color.WHITE);
        }

        if (currentInterest != null && item.getValue().equals(currentInterest.getValue())) {
            circle.startAnim();
        }
    }

    public void selectInterest(int pos, Interest interest) {
        currentInterest = interest;

        if (likedInterests.contains(interest)) {
            likedInterests.remove(interest);
        } else {
            likedInterests.add(interest);
        }

        notifyDataSetChanged();
    }

}
