package com.ran.delta.widget.bottomBar;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.MenuRes;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ran.delta.R;
import com.ran.delta.utils.MiscUtils;
import com.ran.delta.utils.ViewUtils;
import com.ran.delta.widget.fragmentManager.DeltaFragmentManager;

public class BottomBar extends FrameLayout implements View.OnClickListener {
    private static final long ANIMATION_DURATION = 150;

    private static final String STATE_CURRENT_SELECTED_TAB = "STATE_CURRENT_SELECTED_TAB";
    private static final String TAG_BOTTOM_BAR_VIEW_INACTIVE = "BOTTOM_BAR_VIEW_INACTIVE";
    private static final String TAG_BOTTOM_BAR_VIEW_ACTIVE = "BOTTOM_BAR_VIEW_ACTIVE";

    private Context mContext;

    private FrameLayout mUserContentContainer;
    private LinearLayout mItemContainer;

    private int mPrimaryColor;
    private int mInActiveColor;

    private int mTwoDp;
    private int mMaxFixedItemWidth;

    private OnTabClickListener mListener;

    private int mCurrentTabPosition;

    private int mFragmentContainer;
    private BottomBarTab[] mItems;

    private DeltaFragmentManager fragmentManager;


    public BottomBar(Context context) {
        super(context);
        init(context, null, 0, 0);
    }

    public BottomBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0, 0);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public BottomBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BottomBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    public static BottomBar attach(Activity activity, Bundle savedInstanceState) {
        BottomBar bottomBar = new BottomBar(activity);
        bottomBar.onRestoreInstanceState(savedInstanceState);

        ViewGroup contentView = (ViewGroup) activity.findViewById(android.R.id.content);
        View oldLayout = contentView.getChildAt(0);
        contentView.removeView(oldLayout);

        bottomBar.getUserContainer()
                .addView(oldLayout, oldLayout.getLayoutParams());
        contentView.addView(bottomBar, 0);

        return bottomBar;
    }

    public static BottomBar attach(View view, Bundle savedInstanceState) {
        BottomBar bottomBar = new BottomBar(view.getContext());
        bottomBar.onRestoreInstanceState(savedInstanceState);

        ViewGroup contentView = (ViewGroup) view.getParent();

        if (contentView != null) {
            View oldLayout = contentView.getChildAt(0);
            contentView.removeView(oldLayout);

            bottomBar.getUserContainer()
                    .addView(oldLayout, oldLayout.getLayoutParams());
            contentView.addView(bottomBar, 0);
        } else {
            bottomBar.getUserContainer()
                    .addView(view, view.getLayoutParams());
        }

        return bottomBar;
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        mContext = context;

        mInActiveColor = Color.parseColor("#747474");

        mTwoDp = ViewUtils.dpToPx(2);
        mMaxFixedItemWidth = ViewUtils.dpToPx(168);

        initializeViews();
    }

    private void initializeViews() {
        ViewGroup.LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        setLayoutParams(params);

        RelativeLayout itemContainerRoot = (RelativeLayout) View.inflate(mContext,
                R.layout.view_bottom_bar, null);

        mUserContentContainer = (FrameLayout) itemContainerRoot.findViewById(R.id.bb_user_content_container);
        mItemContainer = (LinearLayout) itemContainerRoot.findViewById(R.id.bb_bottom_bar_item_container);

        addView(itemContainerRoot, params);
    }

    protected FrameLayout getUserContainer() {
        return mUserContentContainer;
    }

    public void setItems(BottomBarTab... bottomBarTabs) {
        clearItems();
        mItems = bottomBarTabs;
        updateItems(mItems);
    }

    public void setItemsFromMenu(@MenuRes int menuRes) {
        clearItems();
        mItems = MiscUtils.inflateMenuFromResource((Activity) mContext, menuRes);
        updateItems(mItems);
    }

    public void setPrimaryColor(int color) {
        this.mPrimaryColor = color;
    }

    public void setOnItemSelectedListener(OnTabClickListener listener) {
        mListener = listener;
    }

    public void setFragmentManager(DeltaFragmentManager manager) {
        fragmentManager = manager;
        fragmentManager.setDefaultPosition(mCurrentTabPosition);
        fragmentManager.showFragment(mCurrentTabPosition);
    }

    public void selectTabAtPosition(int position, boolean animate) {
        unselectTab((ViewGroup) mItemContainer.findViewWithTag(TAG_BOTTOM_BAR_VIEW_ACTIVE), animate);
        selectTab((ViewGroup) mItemContainer.getChildAt(position), animate);

        fragmentManager.showFragment(position);

        if (mListener != null) {
            mListener.onTabSelected(position);
        }
    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(STATE_CURRENT_SELECTED_TAB, mCurrentTabPosition);
    }

    @Override
    public void onClick(View v) {
        if (v.getTag().equals(TAG_BOTTOM_BAR_VIEW_INACTIVE)) {
            unselectTab((ViewGroup) findViewWithTag(TAG_BOTTOM_BAR_VIEW_ACTIVE), true);
            selectTab((ViewGroup) v, true);

            int newPosition = findItemPosition(v);

            mCurrentTabPosition = newPosition;

            if (mListener != null) {
                mListener.onTabSelected(mCurrentTabPosition);
            }

            fragmentManager.showFragment(newPosition);
        }
    }

    private void updateItems(BottomBarTab[] bottomBarItems) {
        int index = 0;
        int biggestWidth = 0;

        View[] viewsToAdd = new View[bottomBarItems.length];

        for (BottomBarTab bottomBarItemBase : bottomBarItems) {
            ViewGroup bottomBarView = (ViewGroup) View.inflate(mContext, R.layout.item_bottom_bar, null);

            ImageView icon = (ImageView) bottomBarView.findViewById(R.id.bb_bottom_bar_icon);
            TextView title = (TextView) bottomBarView.findViewById(R.id.bb_bottom_bar_title);

            icon.setImageDrawable(bottomBarItemBase.getIcon(mContext));
            title.setText(bottomBarItemBase.getTitle(mContext));

            bottomBarView.setId(bottomBarItemBase.id);

            if (index == mCurrentTabPosition) {
                selectTab(bottomBarView, false);
            } else {
                unselectTab(bottomBarView, false);
            }

            if (bottomBarView.getWidth() > biggestWidth) {
                biggestWidth = bottomBarView.getWidth();
            }

            bottomBarView.setOnClickListener(this);

            viewsToAdd[index] = bottomBarView;
            index++;
        }

        int screenWidth = ViewUtils.getScreenWidth(mContext);
        int proposedItemWidth = Math.min(ViewUtils.dpToPx(screenWidth / bottomBarItems.length), mMaxFixedItemWidth);

        LinearLayout.LayoutParams params = new LinearLayout
                .LayoutParams(proposedItemWidth, LinearLayout.LayoutParams.WRAP_CONTENT);

        for (View bottomBarView : viewsToAdd) {
            bottomBarView.setLayoutParams(params);
            mItemContainer.addView(bottomBarView);
        }
    }

    private void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mCurrentTabPosition = savedInstanceState.getInt(STATE_CURRENT_SELECTED_TAB, -1);

            if (mCurrentTabPosition == -1) {
                mCurrentTabPosition = 0;
            }
        }
    }

    private void selectTab(ViewGroup bottomBarView, boolean animate) {
        bottomBarView.setTag(TAG_BOTTOM_BAR_VIEW_ACTIVE);
        ImageView icon = (ImageView) bottomBarView.findViewById(R.id.bb_bottom_bar_icon);
        TextView title = (TextView) bottomBarView.findViewById(R.id.bb_bottom_bar_title);

        icon.setColorFilter(mPrimaryColor);
        title.setTextColor(mPrimaryColor);

        int translationY = mTwoDp;

        if (animate) {
            title.animate()
                    .setDuration(ANIMATION_DURATION)
                    .scaleX(1)
                    .scaleY(1)
                    .start();
            bottomBarView.animate()
                    .setDuration(ANIMATION_DURATION)
                    .translationY(-translationY)
                    .start();

        } else {
            title.setScaleX(1);
            title.setScaleY(1);
            bottomBarView.setTranslationY(-translationY);

        }
    }

    private void unselectTab(ViewGroup bottomBarView, boolean animate) {
        bottomBarView.setTag(TAG_BOTTOM_BAR_VIEW_INACTIVE);
        ImageView icon = (ImageView) bottomBarView.findViewById(R.id.bb_bottom_bar_icon);
        TextView title = (TextView) bottomBarView.findViewById(R.id.bb_bottom_bar_title);

        icon.setColorFilter(mInActiveColor);
        title.setTextColor(mInActiveColor);

        float scale = 0.86f;

        if (animate) {
            title.animate()
                    .setDuration(ANIMATION_DURATION)
                    .scaleX(scale)
                    .scaleY(scale)
                    .start();
            bottomBarView.animate()
                    .setDuration(ANIMATION_DURATION)
                    .translationY(0)
                    .start();
        } else {
            title.setScaleX(scale);
            title.setScaleY(scale);
            bottomBarView.setTranslationY(0);
        }
    }

    private int findItemPosition(View viewToFind) {
        int position = 0;

        for (int i = 0; i < mItemContainer.getChildCount(); i++) {
            View candidate = mItemContainer.getChildAt(i);

            if (candidate.equals(viewToFind)) {
                position = i;
                break;
            }
        }

        return position;
    }

    private void clearItems() {
        int childCount = mItemContainer.getChildCount();

        if (childCount > 0) {
            for (int i = 0; i < childCount; i++) {
                mItemContainer.removeView(mItemContainer.getChildAt(i));
            }
        }

        if (mFragmentContainer != 0) {
            mFragmentContainer = 0;
        }

        if (mItems != null) {
            mItems = null;
        }
    }
}
