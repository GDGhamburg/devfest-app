package de.devfest.ui;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.ImageButton;

import org.jetbrains.annotations.NotNull;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.format.DateTimeFormatterBuilder;

import de.devfest.R;
import de.devfest.model.ScheduleSession;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;
import static android.util.TypedValue.COMPLEX_UNIT_DIP;
import static android.util.TypedValue.COMPLEX_UNIT_PX;

public final class UiUtils {

    public static final int CACHED_SPEAKER_IMAGE_SIZE = 500;

    private UiUtils() {
        throw new RuntimeException("No instance allowed!");
    }

    public static int getActionBarHeight(@NotNull Context context) {
        TypedValue tv = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.actionBarSize, tv, true);
        return TypedValue.complexToDimensionPixelSize(tv.data,
                context.getResources().getDisplayMetrics());
    }

    public static int getStatusBarHeight(Context context) {
        return getDimensionSafely(context, context.getResources().getIdentifier("status_bar_height", "dimen", "android"));
    }

    private static int getDimensionSafely(Context context, int id) {
        if (id == 0) return 0;

        return context.getResources().getDimensionPixelSize(id);
    }

    public static int dipsToPxls(@NonNull Context context, int dips) {
        float val = TypedValue.applyDimension(COMPLEX_UNIT_DIP, dips,
                context.getResources().getDisplayMetrics());
        int pixels = (int) (val + 0.5); // Round
        // Ensure at least 1 pixel if val was > 0
        return pixels == 0 && val > 0 ? 1 : pixels;
    }

    public static int pxlsToDips(@NotNull Context context, int pixels) {
        return (int) (pixels / context.getResources().getDisplayMetrics().density + 0.5f);
    }

    public static int getDisplayWidth(Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }

    public static int getDisplayHeight(Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.heightPixels;
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
        return addColumnsForContext(context, 2);
    }

    public static int getGridSmallColumnCount(Context context) {
        return addColumnsForContext(context, 3);
    }

    private static int addColumnsForContext(Context context, int defaultColumns) {
        int cols = defaultColumns;
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

    public static void onAddButtonClick(ImageButton addButton, ScheduleSession session,
                                        SessionAdapter.SessionInteractionListener listener, int addIconColor) {
        ((AnimatedVectorDrawable) addButton.getDrawable()).start();
        addButton.postDelayed(() -> {
            if (session.isScheduled) listener.onRemoveSessionClick(session.session);
            else listener.onAddSessionClick(session.session);
            session.isScheduled = !session.isScheduled;
            UiUtils.setAddDrawable(session.isScheduled, addButton, addIconColor);
        }, addButton.getContext().getResources().getInteger(R.integer.add_duration) + 100);
    }
}
