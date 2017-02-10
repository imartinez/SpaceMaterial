package imartinez.com.spacematerial.util;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;

/**
 * Empty implementation of AnimatorListener
 */
public class EmptyAnimatorListener implements AnimatorListener {
    @Override
    public void onAnimationStart(Animator animator) {}

    @Override
    public void onAnimationEnd(Animator animator) {}

    @Override
    public void onAnimationCancel(Animator animator) {}

    @Override
    public void onAnimationRepeat(Animator animator) {}
}
