package de.devfest.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Outline;
import android.os.Build;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;

import de.devfest.R;

/***
 * Applies rounded corners with the defined radius to itself.
 * Without applied radius the View will be drawn as a full circle
 *
 * Working on Lollipop and above, doesn't have any effect below.
 */
public class RoundedImageView extends AppCompatImageView {

    public RoundedImageView(Context context) {
        this(context, null);
    }

    public RoundedImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundedImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundedImageView, defStyleAttr, 0);
        int cornerRadius = a.getDimensionPixelSize(R.styleable.RoundedImageView_radius, 0);
        a.recycle();
        init(cornerRadius);
    }

    private void init(int radius) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setOutlineProvider(new Clipper(radius));
            setClipToOutline(true);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    static class Clipper extends ViewOutlineProvider {
        private final int radius;
        Clipper(int radius) {
            this.radius = radius;
        }
        @Override
        public void getOutline(View view, Outline outline) {
            int cornerRadius = radius;
            if (cornerRadius <= 0) {
                cornerRadius = view.getMeasuredWidth() / 2;
            }
            outline.setRoundRect(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight(), cornerRadius);
        }
    }
}
