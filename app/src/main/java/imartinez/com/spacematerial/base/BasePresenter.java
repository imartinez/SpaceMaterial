package imartinez.com.spacematerial.base;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import org.jetbrains.annotations.NotNull;

public abstract class BasePresenter<V, R> {

    private V view;
    private R router;
    private CompositeDisposable compositeDisposable;

    /**
     * Called when the bound view is created and ready.
     * getView() will return a valid, active view from this point, so
     * it is safe to start subscriptions or any other code depending on getView()
     */
    public void onViewReady() {
        disposeTracked();
    }

    /**
     * Called when the bound view is about to be destroyed and unbound.
     * getView() will return null from this point, so every subscription
     * or any other code depending on getView() should be unsubscribed/managed.
     */
    public void onViewDestroyed() {
        disposeTracked();
    }

    /**
     * Needed to create the relation between View-Presenter-Router.
     *
     * @param view   an active view of the needed type.
     * @param router an initialized router of the needed type.
     */
    public void bindViewAndRouter(V view, R router) {
        this.view = view;
        this.router = router;
    }

    /**
     * Called after onViewDestroyed. Should perform clean-up of variables pointing to the view.
     */
    public void unBindViewAndRouter() {
        this.view = null;
        this.router = null;
    }

    /**
     * Check if this presenter is currently bound to a view and a router.
     * @return boolean true if is bound to a view and a router, false otherwise.
     */
    public boolean isBoundToViewAndRouter() {
        return view != null && router != null;
    }

    /**
     * @return the bound view. May be null if the view was unbound.
     */
    @NotNull
    public V getView() {
        if (view == null)
            throw new NullPointerException("View is not ready yet. " +
                    "REMEMBER TO CANCEL ALL SUBSCRIPTIONS WHEN VIEW IS DESTROYED");
        return view;
    }

    /**
     * @return the bound Router.
     */
    @NotNull
    public R getRouter() {
        if (router == null) throw new NullPointerException("Router is not ready yet");
        return router;
    }

    /**
     * Attach the disposable to the view lifecycle,
     * and dispose when it goes out of scope
     */
    public void track(Disposable disposable) {
        if (compositeDisposable == null)
            throw new NullPointerException("Can't track a disposable outside view lifecycle");

        compositeDisposable.add(disposable);
    }

    /**
     * Dispose all tracked disposables
     */
    public void disposeTracked() {
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
        }
        compositeDisposable = new CompositeDisposable();
    }
}
