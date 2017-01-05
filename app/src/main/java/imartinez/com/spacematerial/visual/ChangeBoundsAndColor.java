package imartinez.com.spacematerial.visual;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.transition.ChangeBounds;
import android.transition.TransitionValues;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

/**
 * Transition that changes bounds and also transitions background color from a start color
 * to an end color.
 */
public class ChangeBoundsAndColor extends ChangeBounds {

    private static final String PROPERTY_COLOR = "property_color";
    private static final String[] TRANSITION_PROPERTIES = {
            PROPERTY_COLOR
    };
    private @ColorInt int startColor = Color.TRANSPARENT, endColor = Color.WHITE;

    public ChangeBoundsAndColor(@ColorInt int startColor, @ColorInt int endColor) {
        super();
        if (startColor != -1) this.startColor = startColor;
        if (endColor != -1) this.endColor = endColor;
    }

    @Override
    public String[] getTransitionProperties() {
        return TRANSITION_PROPERTIES;
    }

    @Override
    public void captureStartValues(TransitionValues transitionValues) {
        super.captureStartValues(transitionValues);
        final View view = transitionValues.view;
        if (view.getWidth() <= 0 || view.getHeight() <= 0) return;
        transitionValues.values.put(PROPERTY_COLOR, startColor);
    }

    @Override
    public void captureEndValues(TransitionValues transitionValues) {
        super.captureEndValues(transitionValues);
        final View view = transitionValues.view;
        if (view.getWidth() <= 0 || view.getHeight() <= 0) return;
        transitionValues.values.put(PROPERTY_COLOR, endColor);
    }

    @Override
    public Animator createAnimator(final ViewGroup sceneRoot,
            TransitionValues startValues,
            final TransitionValues endValues) {
        Animator changeBounds = super.createAnimator(sceneRoot, startValues, endValues);
        if (startValues == null || endValues == null || changeBounds == null) return null;

        Integer startColor = (Integer) startValues.values.get(PROPERTY_COLOR);
        Integer endColor = (Integer) endValues.values.get(PROPERTY_COLOR);

        if (startColor == null || endColor == null) return null;

        endValues.view.setBackgroundColor(startColor);

        ValueAnimator color = ObjectAnimator.ofArgb(startColor, endColor);
        color.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                endValues.view.setBackgroundColor((Integer)valueAnimator.getAnimatedValue());
            }
        });

        // ease in the dialog's child views (slide up & fade_fast in)
        if (endValues.view instanceof ViewGroup) {
            ViewGroup vg = (ViewGroup) endValues.view;
            float offset = vg.getHeight() / 3;
            for (int i = 0; i < vg.getChildCount(); i++) {
                View v = vg.getChildAt(i);
                v.setTranslationY(offset);
                v.setAlpha(0f);
                v.animate()
                        .alpha(1f)
                        .translationY(0f)
                        .setDuration(150)
                        .setStartDelay(150)
                        .setInterpolator(AnimationUtils.loadInterpolator(vg.getContext(),
                                android.R.interpolator.fast_out_slow_in));
                offset *= 1.8f;
            }
        }

        AnimatorSet transition = new AnimatorSet();
        transition.playTogether(changeBounds, color);
        transition.setInterpolator(AnimationUtils.loadInterpolator(sceneRoot.getContext(),
                android.R.interpolator.fast_out_slow_in));
        return transition;
    }
}
