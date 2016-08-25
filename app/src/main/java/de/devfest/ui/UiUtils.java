package de.devfest.ui;

import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.util.TypedValue;

import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.format.DateTimeFormatterBuilder;

import de.devfest.R;
import de.devfest.model.Speaker;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;
import static android.util.TypedValue.COMPLEX_UNIT_DIP;
import static android.util.TypedValue.COMPLEX_UNIT_PX;
import static de.devfest.model.Speaker.TAG_ANDROID;
import static de.devfest.model.Speaker.TAG_CLOUD;
import static de.devfest.model.Speaker.TAG_WEB;

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

    public static @ColorRes int getTrackColor(Speaker speaker) {
        int colorResId = 0;
        if (speaker.tags.contains(TAG_ANDROID)) colorResId = R.color.tag_android;
        else if (speaker.tags.contains(TAG_WEB)) colorResId = R.color.tag_web;
        else if (speaker.tags.contains(TAG_CLOUD)) colorResId = R.color.tag_cloud;
        return colorResId;
    }

    public static @ColorRes int getTrackOverlayColor(Speaker speaker) {
        int colorResId = 0;
        if (speaker.tags.contains(TAG_ANDROID)) colorResId = R.color.tag_android_overlay;
        else if (speaker.tags.contains(TAG_WEB)) colorResId = R.color.tag_web_overlay;
        else if (speaker.tags.contains(TAG_CLOUD)) colorResId = R.color.tag_cloud_overlay;
        return colorResId;
    }

    public static @DrawableRes int getTrackIconRes(Speaker speaker) {
        int colorResId = 0;
        if (speaker.tags.contains(TAG_ANDROID)) colorResId = R.drawable.ic_mobile_circled;
        else if (speaker.tags.contains(TAG_WEB)) colorResId = R.drawable.ic_web_circled;
        else if (speaker.tags.contains(TAG_CLOUD)) colorResId = R.drawable.ic_cloud_circled;
        return colorResId;
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
}
