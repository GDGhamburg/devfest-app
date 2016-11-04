package de.devfest.ui;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.LayerDrawable;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import de.devfest.R;

import static de.devfest.ui.UiUtils.dipsToPxls;

public class TagHelper {

    public final static String TAG_ANDROID = "android";
    public final static String TAG_WEB = "web";
    public final static String TAG_CLOUD = "cloud";

    public final static String TAG_FIREBASE = "firebase";
    public final static String TAG_ANALYTICS = "analytics";
    public final static String TAG_MACHINE_LEARNING = "machine learning";
    public final static String TAG_BOTS = "bots";
    public final static String TAG_IOS = "ios";

    public final static String TAG_GDE = "gde";
    public final static String TAG_GDG = "gdg";

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
                case TAG_IOS:
                    colorResId = R.color.tag_ios;
                    break;
                case TAG_BOTS:
                case TAG_MACHINE_LEARNING:
                    colorResId = R.color.tag_machine_learning;
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
                case TAG_IOS:
                    colorResId = R.color.tag_ios_dark;
                    break;
                case TAG_BOTS:
                case TAG_MACHINE_LEARNING:
                    colorResId = R.color.tag_machine_learning_dark;
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
                case TAG_IOS:
                    colorResId = R.color.tag_ios_overlay;
                    break;
                case TAG_BOTS:
                case TAG_MACHINE_LEARNING:
                    colorResId = R.color.tag_machine_learning_overlay;
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
                case TAG_ANALYTICS:
                    drawableResId = R.drawable.ic_chart;
                    break;
                case TAG_IOS:
                    drawableResId = R.drawable.ic_ios;
                    break;
                case TAG_MACHINE_LEARNING:
                case TAG_BOTS:
                    drawableResId = R.drawable.ic_memory;
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
}
