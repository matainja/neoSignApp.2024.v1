package com.matainja.Animation;

import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;


public class CustomAnimation extends TranslateAnimation {
    private Runnable onAnimationEnd;

    public CustomAnimation(float fromXDelta, float toXDelta, float fromYDelta, float toYDelta) {
        super(fromXDelta, toXDelta, fromYDelta, toYDelta);
    }

    public void setOnAnimationEnd(Runnable onAnimationEnd) {
        this.onAnimationEnd = onAnimationEnd;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);
        if (interpolatedTime == 1.0 && onAnimationEnd != null) {
            onAnimationEnd.run();
        }
    }
}
