package imartinez.com.spacematerial.visual;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.transition.Transition;
import android.transition.TransitionValues;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * TODO: Add class description.
 */
public class FadeTransition extends Transition {

    private static final String PROPNAME_BACKGROUND = "android:faderay:background";
    private static final String PROPNAME_TEXT_COLOR = "android:faderay:textColor";
    private static final String PROPNAME_ALPHA = "android:faderay:alpha";

    private float startAlpha;
    private float endAlpha;
    private TimeInterpolator timeInterpolator;

    public FadeTransition(final float startAlpha, final float endAlpha, final TimeInterpolator timeInterpolator) {
        this.startAlpha = startAlpha;
        this.endAlpha = endAlpha;
        this.timeInterpolator = timeInterpolator;
    }

    public FadeTransition(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    private void captureValues(final TransitionValues transitionValues) {
        transitionValues.values.put(PROPNAME_BACKGROUND, transitionValues.view.getBackground());
        transitionValues.values.put(PROPNAME_ALPHA, transitionValues.view.getAlpha());
        if (transitionValues.view instanceof TextView) {
            transitionValues.values.put(PROPNAME_TEXT_COLOR, ((TextView) transitionValues.view).getCurrentTextColor());
        }
    }

    @Override
    public void captureStartValues(final TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    @Override
    public void captureEndValues(final TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    @SuppressLint("NewApi")
    @Override
    public Animator createAnimator(final ViewGroup sceneRoot, final TransitionValues startValues,
            final TransitionValues endValues) {

        TextView textView = (TextView) endValues.view;

        if (startAlpha != endAlpha) {
            textView.setAlpha(endAlpha);
        }

        ObjectAnimator fade = ObjectAnimator.ofFloat(textView, View.ALPHA, startAlpha, endAlpha);
        fade.setInterpolator(timeInterpolator);
        return fade;
    }
}