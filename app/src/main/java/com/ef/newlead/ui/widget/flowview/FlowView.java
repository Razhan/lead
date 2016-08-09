package com.ef.newlead.ui.widget.flowview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class FlowView extends RecyclerView {

    public static final int VERTICAL = 1;
    public static final int HORIZONTAL = 2;

    private Paint mPaint;
    private int currentPos, leftBorder, rightBorder, orientation;

    private boolean flag = false;
    private boolean isFling = true;

    private CoverFlowItemListener coverFlowListener;
    private LinearLayoutManager layoutManager;

    public FlowView(Context context) {
        this(context, null);
    }

    public FlowView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void setFling(boolean fling) {
        isFling = fling;
    }

    private void init() {
        mPaint = new Paint(Paint.FILTER_BITMAP_FLAG);
        mPaint.setAntiAlias(true);

        this.setChildrenDrawingOrderEnabled(true);
        this.addOnScrollListener(new CoverFlowScrollListener());
    }

    @Override
    public boolean drawChild(Canvas canvas, View child, long drawingTime) {
        final int childCenterX = child.getWidth() / 2;
        final int childCenterY = child.getHeight() / 2;

        final int distanceX = getWidth() / 2 - childCenterX - child.getLeft();
        final int distanceY = getHeight() / 2 - childCenterY - child.getTop();

        View firstChild = ((ViewGroup) child).getChildAt(0);

        if (orientation == HORIZONTAL) {
            setScale(firstChild, distanceX, child.getWidth() / 2);
            super.drawChild(canvas, child, drawingTime);
        } else {
            setScale(firstChild, distanceY, child.getHeight() / 2);
            setAlpha(firstChild, distanceY, getHeight() / 2);
            super.drawChild(canvas, child, drawingTime);
        }

        return false;
    }

    private void setAlpha(View view, int d, int r) {
        float alpha = 1 - (float) Math.abs(d) / (float) r;
        view.setAlpha(alpha);
    }

    private void setScale(View view, int d, int childSize) {
        float scale;

        if (orientation == HORIZONTAL) {
            scale = 1 - ((float) Math.abs(d) / (float) childSize) / 10;
        } else {
            if (Math.abs(d) <= childSize) {
                scale = 1f + (1 - (float) Math.abs(d) / (float) childSize);
            } else {
                scale = 1f;
            }
        }

        view.setScaleX(scale);
        view.setScaleY(scale);
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        int centerChild = childCount / 2;

        if (!flag) {
            ((FlowAdapter) getAdapter()).setBorderPosition(centerChild);
            leftBorder = centerChild;
            rightBorder = getAdapter().getItemCount() - centerChild - 1;
            flag = true;
        }

        currentPos = layoutManager.findFirstVisibleItemPosition() + centerChild;

        return super.getChildDrawingOrder(childCount, i);
    }

    public void setCoverFlowListener(CoverFlowItemListener coverFlowListener) {
        this.coverFlowListener = coverFlowListener;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
        DividerItemDecoration itemDecoration;

        if (orientation == VERTICAL) {
            layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            itemDecoration = new DividerItemDecoration(0, 0, 0, 0);
        } else {
            layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
            itemDecoration = new DividerItemDecoration(-50, 0, 0, 0);
        }

        this.setLayoutManager(layoutManager);
        this.addItemDecoration(itemDecoration);
    }

    public void scrollToCenter(int position) {
        int centerItem, size, center;

        int Pos = position - layoutManager.findFirstVisibleItemPosition();
        View targetChild = this.getChildAt(Pos);

        int[] childLocation = new int[2];
        int[] parentLocation = new int[2];
        targetChild.getLocationOnScreen(childLocation);
        FlowView.this.getLocationOnScreen(parentLocation);

        float scale = targetChild.getScaleX();

        if (orientation == HORIZONTAL) {
            centerItem = (int) ((float) childLocation[0] / scale) + targetChild.getWidth() / 2;
            size = FlowView.this.getWidth();
            center = size / 2 + parentLocation[0];
            FlowView.this.smoothScrollBy(centerItem - center, 0);
        } else {
            centerItem = childLocation[1] + targetChild.getHeight() / 2;
            size = FlowView.this.getHeight();
            center = size / 2 + parentLocation[1];
            FlowView.this.smoothScrollBy(0, centerItem - center);
        }
    }

    @Override
    public boolean fling(int velocityX, int velocityY) {
        if (!isFling) {
            int firstViewPos = layoutManager.findFirstVisibleItemPosition();
            int lastViewPos = layoutManager.findLastVisibleItemPosition();

            firstViewPos = Math.max(currentPos - 1, firstViewPos);
            lastViewPos = Math.min(currentPos + 1, lastViewPos);

            View firstView = layoutManager.findViewByPosition(firstViewPos);
            View lastView = layoutManager.findViewByPosition(lastViewPos);

            if (orientation == HORIZONTAL) {
                //if user swipes to the left
                if (velocityX > 0) {
                    smoothScrollBy(lastView.getLeft(), 0);
                } else {
                    smoothScrollBy(-1 * firstView.getRight(), 0);
                }
            }
            return true;
        } else {
            return super.fling(velocityX, velocityY);
        }
    }

    public interface CoverFlowItemListener {
        void onItemSelected(int position);
    }

    public class CoverFlowScrollListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                coverFlowListener.onItemSelected(currentPos);

                if (currentPos > rightBorder) {
                    scrollToCenter(rightBorder);
                } else if (currentPos < leftBorder) {
                    scrollToCenter(leftBorder);
                } else {
                    scrollToCenter(currentPos);
                }
            }
        }
    }

    private class DividerItemDecoration extends RecyclerView.ItemDecoration {

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
}