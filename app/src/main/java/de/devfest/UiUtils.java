package de.devfest;

import android.content.res.Resources;

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
}
