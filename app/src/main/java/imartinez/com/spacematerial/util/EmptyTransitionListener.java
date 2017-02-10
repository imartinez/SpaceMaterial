package imartinez.com.spacematerial.util;

import android.transition.Transition;
import android.transition.Transition.TransitionListener;

/**
 * Empty implementation of TransitionListener
 */
public class EmptyTransitionListener implements TransitionListener {
    @Override
    public void onTransitionStart(Transition transition) {}

    @Override
    public void onTransitionEnd(Transition transition) {}

    @Override
    public void onTransitionCancel(Transition transition) {}

    @Override
    public void onTransitionPause(Transition transition) {}

    @Override
    public void onTransitionResume(Transition transition) {}
}
