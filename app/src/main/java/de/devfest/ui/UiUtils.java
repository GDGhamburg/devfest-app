package de.devfest.ui;

import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.util.TypedValue;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;
import static android.util.TypedValue.COMPLEX_UNIT_DIP;
import static android.util.TypedValue.COMPLEX_UNIT_PX;

public final class UiUtils {

    private UiUtils() {
        throw new RuntimeException("No instance allowed!");
    }

    public static int getStatusBarHeight(Context context) {
        return getDimensionSafely(context, context.getResources().getIdentifier("status_bar_height", "dimen", "android"));
    }

    private static int getDimensionSafely(Context context, int id) {
        if (id == 0) return 0;

        return context.getResources().getDimensionPixelSize(id);
    }

    public static float dipsToPxls(@NonNull Context context, int dips) {
        return TypedValue.applyDimension(COMPLEX_UNIT_DIP, dips, context.getResources().getDisplayMetrics());
    }

    public static float pxlsToDips(@NonNull Context context, int pixels) {
        return TypedValue.applyDimension(COMPLEX_UNIT_PX, pixels, context.getResources().getDisplayMetrics());
    }

    public static boolean isLargeScreen(@NonNull Context context) {
        int screenLayout = context.getResources().getConfiguration().screenLayout;
        screenLayout &= Configuration.SCREENLAYOUT_SIZE_MASK;
        return screenLayout == Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static boolean isXLargeScreen(@NonNull Context context) {
        int screenLayout = context.getResources().getConfiguration().screenLayout;
        screenLayout &= Configuration.SCREENLAYOUT_SIZE_MASK;
        return screenLayout == Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    public static boolean isLargeScreenAtLeast(@NonNull Context context) {
        return isLargeScreen(context) || isXLargeScreen(context);
    }

    public static boolean isLandscape(@NonNull Context context) {
        return context.getResources().getConfiguration().orientation == ORIENTATION_LANDSCAPE;
    }
}
