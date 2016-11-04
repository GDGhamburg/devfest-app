package de.devfest.ui;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.LayerDrawable;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.TypedValue;
import android.widget.ImageButton;
import de.devfest.R;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.format.DateTimeFormatterBuilder;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;
import static android.util.TypedValue.COMPLEX_UNIT_DIP;
import static android.util.TypedValue.COMPLEX_UNIT_PX;
import static de.devfest.model.Speaker.TAG_ANALYTICS;
import static de.devfest.model.Speaker.TAG_ANDROID;
import static de.devfest.model.Speaker.TAG_CLOUD;
import static de.devfest.model.Speaker.TAG_FIREBASE;
import static de.devfest.model.Speaker.TAG_WEB;

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

    public static @ColorRes int getTagColor(String tag) {
        int colorResId = R.color.tag_default_overlay;
        if (!TextUtils.isEmpty(tag)) {
            switch (tag) {
                case TAG_ANDROID:
                    colorResId = R.color.tag_android;
                    break;
                case TAG_CLOUD:
                    colorResId = R.color.tag_cloud;
                    break;
                case TAG_WEB:
                    colorResId = R.color.tag_web;
                    break;
                case TAG_FIREBASE:
                    colorResId = R.color.tag_firebase;
                    break;
                case TAG_ANALYTICS:
                    colorResId = R.color.tag_analytics;
                    break;
                default:
                    colorResId = R.color.tag_default;
            }
        }
        return colorResId;
    }

    public static @ColorRes int getTagDarkColor(String tag) {
        int colorResId = R.color.tag_default_dark;
        if (!TextUtils.isEmpty(tag)) {
            switch (tag) {
                case TAG_ANDROID:
                    colorResId = R.color.tag_android_dark;
                    break;
                case TAG_CLOUD:
                    colorResId = R.color.tag_cloud_dark;
                    break;
                case TAG_WEB:
                    colorResId = R.color.tag_web_dark;
                    break;
                case TAG_FIREBASE:
                    colorResId = R.color.tag_firebase_dark;
                    break;
                case TAG_ANALYTICS:
                    colorResId = R.color.tag_analytics_dark;
                    break;
                default:
                    colorResId = R.color.tag_default_dark;
            }
        }
        return colorResId;
    }

    public static @ColorRes int getTagOverlayColor(String tag) {
        int colorResId = R.color.tag_default_overlay;
        if (!TextUtils.isEmpty(tag)) {
            switch (tag) {
                case TAG_ANDROID:
                    colorResId = R.color.tag_android_overlay;
                    break;
                case TAG_CLOUD:
                    colorResId = R.color.tag_cloud_overlay;
                    break;
                case TAG_WEB:
                    colorResId = R.color.tag_web_overlay;
                    break;
                case TAG_FIREBASE:
                    colorResId = R.color.tag_firebase_overlay;
                    break;
                case TAG_ANALYTICS:
                    colorResId = R.color.tag_analytics_overlay;
                    break;
                default:
                    colorResId = R.color.tag_default_overlay;
            }
        }
        return colorResId;
    }

    public static Drawable getTagIcon(Context context, @Nullable String tag) {
        int drawableResId;
        if (tag == null) {
            drawableResId = R.mipmap.ic_launcher;
        } else {
            switch (tag) {
                case TAG_ANDROID:
                    drawableResId = R.drawable.ic_android;
                    break;
                case TAG_CLOUD:
                    drawableResId = R.drawable.ic_cloud;
                    break;
                case TAG_WEB:
                    drawableResId = R.drawable.ic_web;
                    break;
                default:
                    drawableResId = R.mipmap.ic_launcher;
            }
        }
        return context.getDrawable(drawableResId);
    }

    public static Drawable getCircledTagIcon(Context context, String tag, boolean coloredCircle) {
        int drawableResId = 0;
        if (!TextUtils.isEmpty(tag)) {
            switch (tag) {
                case TAG_ANDROID:
                    drawableResId = coloredCircle ? R.drawable.ic_android : R.drawable.ic_android_colored;
                    break;
                case TAG_CLOUD:
                    drawableResId = coloredCircle ? R.drawable.ic_cloud : R.drawable.ic_cloud_colored;
                    break;
                case TAG_WEB:
                    drawableResId = coloredCircle ? R.drawable.ic_web : R.drawable.ic_web_colored;
                    break;
                default:
            }
        }
        if (drawableResId != 0) {
            Drawable[] layers = new Drawable[2];
            layers[0] = ContextCompat.getDrawable(context, R.drawable.shape_circle).mutate();
            if (coloredCircle) {
                int color = getTagColor(tag);
                layers[0].setColorFilter(ContextCompat.getColor(context, color), PorterDuff.Mode.SRC_OVER);
            }
            int bounds = (int) dipsToPxls(context, 8);
            layers[1] = new InsetDrawable(ContextCompat.getDrawable(context, drawableResId), bounds);
            return new LayerDrawable(layers);
        }
        return null;
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
