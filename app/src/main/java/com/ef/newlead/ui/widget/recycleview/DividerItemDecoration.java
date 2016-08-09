package com.ef.newlead.ui.widget.recycleview;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class DividerItemDecoration extends RecyclerView.ItemDecoration {

    private int leftPadding, topPadding, rightPadding, bottomPadding;

    public DividerItemDecoration(int leftPadding, int topPadding, int rightPadding, int bottomPadding) {
        this.leftPadding = leftPadding;
        this.topPadding = topPadding;
        this.rightPadding = rightPadding;
        this.bottomPadding = bottomPadding;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (view.getId() == 0) {
            return;
        }
        outRect.left = leftPadding;
        outRect.top = topPadding;
        outRect.right = rightPadding;
        outRect.bottom = bottomPadding;
    }
}