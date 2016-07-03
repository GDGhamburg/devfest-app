package de.devfest;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.util.TypedValue;

import static android.util.TypedValue.COMPLEX_UNIT_DIP;
import static android.util.TypedValue.COMPLEX_UNIT_PX;

public final class UiUtils {

    private UiUtils() {
        throw new RuntimeException("No instance allowed!");
    }

    public static int getStatusBarHeight(Resources res) {
        return getDimensionSafely(res, res.getIdentifier("status_bar_height", "dimen", "android"));
    }

    private static int getDimensionSafely(Resources res, int id) {
        if (id == 0) return 0;

        return res.getDimensionPixelSize(id);
    }

    public static float dipsToPxls(@NonNull Resources res, int dips) {
        return TypedValue.applyDimension(COMPLEX_UNIT_DIP, dips, res.getDisplayMetrics());
    }

    public static float pxlsToDips(@NonNull Resources res, int pixels) {
        return TypedValue.applyDimension(COMPLEX_UNIT_PX, pixels, res.getDisplayMetrics());
    }
}
