package com.ef.cat.ui.widget.coverFlow;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;

public class CoverFlowView extends RecyclerView {

    public static final int VERTICAL = 1;
    public static final int HORIZONTAL = 2;
    private final Camera mCamera = new Camera();
    private final Matrix mMatrix = new Matrix();
    private final Paint mPaint = new Paint(Paint.FILTER_BITMAP_FLAG);
    private int last_position = 0;
    private int current_position = 0;
    private int left_border_position = 0;
    private int right_border_position = 0;
    private int orientation = 0;
    private boolean flag = false;
    private CoverFlowItemListener coverFlowListener;
    private LinearLayoutManager layoutManager;


    public CoverFlowView(Context context) {
        super(context);
        init();
    }

    public CoverFlowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CoverFlowView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        mPaint.setAntiAlias(true);
        this.setChildrenDrawingOrderEnabled(true);
        this.addOnScrollListener(new CoverFlowScrollListener());
    }

    @Override
    public boolean drawChild(Canvas canvas, View child, long drawingTime) {
        Bitmap bitmap = getChildDrawingCache(child);
        final int top = child.getTop();
        final int left = child.getLeft();

        final int childCenterY = child.getHeight() / 2;
        final int childCenterX = child.getWidth() / 2;

        final int parentCenterY = getHeight() / 2;
        final int parentCenterX = getWidth() / 2;

        final int absChildCenterY = child.getTop() + childCenterY;
        final int absChildCenterX = child.getLeft() + childCenterX;

        final int distanceY = parentCenterY - absChildCenterY;

        final int distanceX = parentCenterX - absChildCenterX;

        if (orientation == HORIZONTAL) {
            prepareMatrix(mMatrix, distanceX, getWidth() / 2);
        } else {
            prepareMatrix(mMatrix, distanceY, getHeight() / 2);
        }

        mMatrix.preTranslate(-childCenterX, -childCenterY);
        mMatrix.postTranslate(childCenterX, childCenterY);
        mMatrix.postTranslate(left, top);

        canvas.drawBitmap(bitmap, mMatrix, mPaint);
        return false;
    }

    private void prepareMatrix(final Matrix outMatrix, int distanceY, int r) {
        final int d = Math.min(r, Math.abs(distanceY));

        final float translateZ = (float) Math.sqrt((r * r) - (d * d));
        mCamera.save();
        mCamera.translate(0, 0, (r - translateZ) * 2);
        mCamera.getMatrix(outMatrix);
        mCamera.restore();
    }

    private Bitmap getChildDrawingCache(final View child) {
        Bitmap bitmap = child.getDrawingCache();
        if (bitmap == null) {
            child.setDrawingCacheEnabled(true);
            child.buildDrawingCache();
            bitmap = child.getDrawingCache();
        }
        return bitmap;
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        int centerChild = childCount / 2;
        if (!flag) {
            ((CoverFlowAdapter) getAdapter()).setBorderPosition(centerChild);
            left_border_position = centerChild;
            right_border_position = getAdapter().getItemCount() - centerChild - 1;
            flag = true;
        }
        current_position = layoutManager.findFirstVisibleItemPosition() + centerChild;
        if (last_position != current_position) {
            last_position = current_position;
            coverFlowListener.onItemChanged(current_position);
        }

        int rez;
        if (i > centerChild) {
            rez = (childCount - 1) - i + centerChild;
        } else if (i == centerChild) {
            rez = childCount - 1;
        } else {
            rez = i;
        }
        return rez;

    }

    public void setCoverFlowListener(CoverFlowItemListener coverFlowListener) {
        this.coverFlowListener = coverFlowListener;
    }

    public void scrollToCenter(int position) {
        if (position <= right_border_position && position >= left_border_position) {
            int first_position = layoutManager.findFirstVisibleItemPosition();
            int current_position = position - first_position;
            View targetChild = this.getChildAt(current_position);
            int[] location = new int[2];
            targetChild.getLocationInWindow(location);
            final int targetItemX = location[0] + targetChild.getWidth() / 2;

            Display display = getDisplay();
            final Point size = new Point();
            display.getSize(size);
            int width = size.x;
            final int centerX = width / 2;

            new Handler().post(() -> CoverFlowView.this.smoothScrollBy(targetItemX - centerX, 0));
        }
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
        DividerItemDecoration itemDecoration;
        if (orientation == VERTICAL) {
            layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            itemDecoration = new DividerItemDecoration(0, -100);
        } else {
            layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
            itemDecoration = new DividerItemDecoration(-100, 0);
        }
        this.setLayoutManager(layoutManager);
        this.addItemDecoration(itemDecoration);
    }

    public interface CoverFlowItemListener {
        void onItemChanged(int position);

        void onItemSelected(int position);
    }

    public class CoverFlowScrollListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {

                coverFlowListener.onItemSelected(current_position);
                if (current_position > right_border_position) {
                    scrollToCenter(right_border_position);
                    return;
                }
                if (current_position < left_border_position) {
                    scrollToCenter(left_border_position);
                    return;
                }

                int first_position = layoutManager.findFirstVisibleItemPosition();
                View centerChild = CoverFlowView.this.getChildAt(current_position - first_position);
                int[] location = new int[2];
                centerChild.getLocationInWindow(location);
                int centerItemX = location[0] + centerChild.getWidth() / 2;

                Display display = getDisplay();
                final Point size = new Point();
                display.getSize(size);
                int width = size.x;
                int centerX = width / 2;
                CoverFlowView.this.smoothScrollBy(centerItemX - centerX, 0);
            }
        }
    }

    private class DividerItemDecoration extends RecyclerView.ItemDecoration {

        private int leftPadding, topPadding;

        public DividerItemDecoration(int leftPadding, int topPadding) {
            this.leftPadding = leftPadding;
            this.topPadding = topPadding;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            if (view.getId() == 0) {
                return;
            }
            outRect.left = leftPadding;
            outRect.top = topPadding;
        }
    }
}
