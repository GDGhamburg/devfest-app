package de.devfest.ui;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.widget.ImageButton;
import de.devfest.R;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.format.DateTimeFormatterBuilder;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;
import static android.util.TypedValue.COMPLEX_UNIT_DIP;
import static android.util.TypedValue.COMPLEX_UNIT_PX;

public final class UiUtils {

    public static final int CACHED_SPEAKER_IMAGE_SIZE = 500;

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

    public static int getDefaultGridColumnCount(Context context) {
        int cols = 2;
        if (UiUtils.isLargeScreen(context)) cols += 1;
        if (UiUtils.isXLargeScreen(context)) cols += 2;
        if (UiUtils.isLandscape(context)) cols += 1;
        return cols;
    }

    public static DateTimeFormatter getSessionStartFormat() {
        return new DateTimeFormatterBuilder().appendPattern("E, HH:mm").toFormatter();
    }

    public static void setAddDrawable(boolean isScheduled, ImageButton buttonAdd, @ColorInt int color) {
        Drawable drawable = buttonAdd.getDrawable();
        if (drawable instanceof AnimatedVectorDrawable) {
            ((AnimatedVectorDrawable) drawable).stop();
        }
        int drawableRes = isScheduled ? R.drawable.avd_remove : R.drawable.avd_add;
        drawable = ContextCompat.getDrawable(buttonAdd.getContext(), drawableRes).mutate();
        drawable.setTint(color);
        buttonAdd.setImageDrawable(drawable);
    }
}
