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

    private int cornerRadius;

    public RoundedImageView(Context context) {
        super(context, null);
    }

    public RoundedImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundedImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundedImageView, defStyleAttr, 0);
        cornerRadius = a.getDimensionPixelSize(R.styleable.RoundedImageView_radius, 0);
        a.recycle();
        init();
    }

    private void init() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setOutlineProvider(new ViewOutlineProvider() {
                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                public void getOutline(View view, Outline outline) {
                    if (cornerRadius <= 0) {
                        cornerRadius = view.getMeasuredWidth() / 2;
                    }

                    outline.setRoundRect(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight(),
                            cornerRadius);
                }
            });
            setClipToOutline(true);
        }
    }
}