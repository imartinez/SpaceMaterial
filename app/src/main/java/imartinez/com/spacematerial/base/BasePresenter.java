package imartinez.com.spacematerial.base;

import rx.Subscription;

public interface BasePresenter<V, R> {

    /**
     * Called when the bound view is created and ready.
     * getView() will return a valid, active view from this point, so
     * it is safe to start subscriptions or any other code depending on getView()
     */
    void onViewReady();

    /**
     * Called when the bound view is about to be destroyed and unbound.
     * getView() will return null from this point, so every subscription
     * or any other code depending on getView() should be unsubscribed/managed.
     */
    void onViewDestroyed();

    /**
     * Needed to create the relation between View-Presenter-Router.
     *
     * @param view      an active view of the needed type.
     * @param router an initialized router of the needed type.
     */
    void bindViewAndRouter(V view, R router);

    /**
     * Called after onViewDestroyed. Should perform clean-up of variables pointing to the view.
     */
    void unBindViewAndRouter();

    /**
     * @return the bound view. May be null if the view was unbound.
     */
    V getView();

    /**
     * @return the bound Router.
     */
    R getRouter();


    /**
     * Attach the subscription to the view lifecycle,
     * and unsubscribe when it goes out of scope
     */
    void track(Subscription subscription);

    /**
     * Unsubscribe all tracked subscriptions
     */
    void cancelSubscriptions();
}
