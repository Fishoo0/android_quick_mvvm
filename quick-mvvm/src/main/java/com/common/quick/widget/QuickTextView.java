package com.common.quick.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RotateDrawable;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.widget.AppCompatTextView;

public class QuickTextView extends AppCompatTextView {

    public QuickTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    /**
     * Correspond to {@link TextView#getCompoundDrawables()}, used for animate {@link RotateDrawable}.
     */
    private Map<Object, ObjectAnimator> animators = new HashMap();


    public void enableAnimating(boolean value) {
        for (Drawable drawable : getCompoundDrawables()) {
            animationDrawable(drawable, value);
        }
    }

    private final void animationDrawable(Drawable drawable, boolean value) {
        if (drawable instanceof AnimationDrawable) {
            if (value) {
                ((AnimationDrawable) drawable).setOneShot(false);
                ((AnimationDrawable) drawable).start();
            } else {
                ((AnimationDrawable) drawable).stop();
            }
        } else if (drawable instanceof RotateDrawable) {
            if (animators.get(drawable) instanceof ObjectAnimator) {
                if (!value) {
                    animators.get(drawable).cancel();
                    animators.put(drawable, null);
                }
            } else if (value) {
                final ObjectAnimator objectAnimator = ObjectAnimator.ofInt(drawable, "level", 0, 10000).setDuration(1000);
                objectAnimator.setRepeatCount(ObjectAnimator.INFINITE);
                objectAnimator.setInterpolator(new LinearInterpolator());
                objectAnimator.start();
                animators.put(drawable, objectAnimator);
            }
        } else if (drawable instanceof LayerDrawable) {
            for (int i = 0; i < ((LayerDrawable) drawable).getNumberOfLayers(); i++) {
                Drawable dd = ((LayerDrawable) drawable).getDrawable(i);
                animationDrawable(dd, value);
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        for (ObjectAnimator animator : animators.values()) {
            if (animator != null) {
                animator.cancel();
            }
        }
    }

    @androidx.databinding.BindingAdapter(value = {"enableAnimate"})
    public static <T> void bindAnimate(QuickTextView view, boolean enable) {
        view.enableAnimating(enable);
    }


}
