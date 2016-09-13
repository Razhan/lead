package com.ef.newlead.util;

import android.app.Activity;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import timber.log.Timber;

/**
 * Created by seanzhou on 11/10/15.
 * <p>
 * A <a href= "http://stackoverflow.com/questions/7417123/android-how-to-adjust-layout-in-full-screen-mode-when-softkeyboard-is-visible">workaround<a/>
 * for adjusting layout in full screen mode when softkeyboard is visible.
 */
public class KeyBoardVisibilityMonitor {

    // For more information, see https://code.google.com/p/android/issues/detail?id=5497
    // To use this class, simply invoke assistActivity() on an Activity that already has its content view set.

    private static KeyBoardStateListener keyBoardStateListener;
    private View mChildOfContent;
    private int usableHeightPrevious;
    private FrameLayout.LayoutParams frameLayoutParams;

    private int initialTopLayoutViewHeight;

    private KeyBoardVisibilityMonitor(Activity activity, KeyBoardStateListener keyBoardStateListener) {
        KeyBoardVisibilityMonitor.keyBoardStateListener = keyBoardStateListener;

        FrameLayout content = (FrameLayout) activity.findViewById(android.R.id.content);
        mChildOfContent = content.getChildAt(0);
        mChildOfContent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                possiblyResizeChildOfContent();
            }
        });
        frameLayoutParams = (FrameLayout.LayoutParams) mChildOfContent.getLayoutParams();
    }

    public static void assistActivity(Activity activity, KeyBoardStateListener keyBoardStateListener) {
        new KeyBoardVisibilityMonitor(activity, keyBoardStateListener);
    }

    public static void dispose() {
        keyBoardStateListener = null;
    }

    private void possiblyResizeChildOfContent() {
        int usableHeightNow = computeUsableHeight();
        if (usableHeightPrevious == 0) {
            initialTopLayoutViewHeight = usableHeightNow;
        }

        if (usableHeightNow != usableHeightPrevious) {
            int usableHeightSansKeyboard = mChildOfContent.getRootView().getHeight();
            int heightDifference = usableHeightSansKeyboard - usableHeightNow;
            if (heightDifference > (usableHeightSansKeyboard / 4)) {
                // keyboard probably just became visible
                frameLayoutParams.height = usableHeightSansKeyboard - heightDifference;
                Timber.d(">>> Keyboard visible , Layout root view height " + frameLayoutParams.height);

                if (keyBoardStateListener != null) {
                    keyBoardStateListener.onKeyboardVisible();
                }
            } else {
                // keyboard probably just became hidden
                // FIX: restore the initial height to resolve the issue about view covered by the
                // virtual keyboard (Nexus phone, etc).
                frameLayoutParams.height = initialTopLayoutViewHeight; //usableHeightSansKeyboard;
                Timber.d(">>> Keyboard NOT visible , Layout root view height " + frameLayoutParams.height);

                if (keyBoardStateListener != null) {
                    keyBoardStateListener.onKeyboardHidden();
                }
            }
            mChildOfContent.requestLayout();
            usableHeightPrevious = usableHeightNow;
        }
    }

    private int computeUsableHeight() {
        Rect r = new Rect();
        mChildOfContent.getWindowVisibleDisplayFrame(r);
        return (r.bottom - r.top);
    }

    public interface KeyBoardStateListener {
        void onKeyboardVisible();

        void onKeyboardHidden();
    }
}
