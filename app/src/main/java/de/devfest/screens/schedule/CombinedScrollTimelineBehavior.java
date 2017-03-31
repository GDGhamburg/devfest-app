package de.devfest.screens.schedule;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

import de.devfest.R;


public class CombinedScrollTimelineBehavior extends AppBarLayout.ScrollingViewBehavior {

    public CombinedScrollTimelineBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target,
                                       int nestedScrollAxes) {
        return (child.getId() == R.id.timeline && target.getId() == R.id.trackSessionList);
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dxConsumed, int dyConsumed,
                               int dxUnconsumed, int dyUnconsumed) {
        child.scrollBy(0, dyConsumed);
    }
}
