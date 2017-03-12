package android.view.animation.interpolator;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.BaseInterpolator;

public class QuartEaseInOut extends BaseInterpolator {
    public QuartEaseInOut(Context context, AttributeSet attrs) {
    }

    public float getInterpolation(float t) {
        return inout(t);
    }

    private float inout(float t) {
        t *= 2.0f;
        if (t < 1.0f) {
            return (((0.5f * t) * t) * t) * t;
        }
        t -= 2.0f;
        return -0.5f * ((((t * t) * t) * t) - 2.0f);
    }
}