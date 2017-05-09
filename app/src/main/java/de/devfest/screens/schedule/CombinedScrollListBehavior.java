package de.devfest.screens.schedule;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import de.devfest.R;

public class CombinedScrollListBehavior extends AppBarLayout.ScrollingViewBehavior {

    private static int scrollYOffset = 0;

    public CombinedScrollListBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target,
                                       int nestedScrollAxes) {
        return target.getId() == R.id.timeline
                || ((target.getId() == R.id.trackSessionList) && (target.hashCode() != child.hashCode()));
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dxConsumed, int dyConsumed,
                               int dxUnconsumed, int dyUnconsumed) {
        ViewPager pager = (ViewPager) child;
        for (int i = 0; i < pager.getChildCount(); i++) {
            View subChild = pager.getChildAt(i);
            if (subChild.hashCode() != target.hashCode()) {
                subChild.scrollBy(0, dyConsumed);
            } else {
                scrollYOffset += dyConsumed;
                pager.setTag(scrollYOffset);
            }

        }
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, View child, int layoutDirection) {
        return super.onLayoutChild(parent, child, layoutDirection);
    }

    @Override
    public boolean onNestedFling(CoordinatorLayout coordinatorLayout, View child, View target,
                                 float velocityX, float velocityY, boolean consumed) {
        ViewPager pager = (ViewPager) child;
        for (int i = 0; i < pager.getChildCount(); i++) {
            View grandChild = pager.getChildAt(i);
            if (grandChild.hashCode() != target.hashCode()) {
                ((RecyclerView) grandChild).fling((int) velocityX, (int) velocityY);
            }
        }
        return false;
    }
}
