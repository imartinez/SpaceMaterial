package imartinez.com.spacematerial.base;

import org.jetbrains.annotations.NotNull;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public abstract class BasePresenterImpl<V, R> implements BasePresenter<V, R> {

    private V view;
    private R router;
    private CompositeSubscription compositeSubscription;

    @Override
    public void onCreateView() {
        cancelSubscriptions();
    }

    @Override
    public void onDestroyView() {
        cancelSubscriptions();
    }

    @Override
    public void bindViewAndRouter(V view, R router) {
        this.view = view;
        this.router = router;
    }

    @Override
    public void unBindViewAndRouter() {
        this.view = null;
        this.router = null;
    }

    @Override
    @NotNull
    public V getView() {
        if (view == null)
            throw new NullPointerException("View is not ready yet. " +
                    "REMEMBER TO CANCEL ALL SUBSCRIPTIONS WHEN VIEW IS DESTROYED");
        return view;
    }

    @Override
    @NotNull
    public R getRouter() {
        if (router == null) throw new NullPointerException("Router is not ready yet");
        return router;
    }

    @Override
    public void track(Subscription subscription) {
        if (compositeSubscription == null)
            throw new NullPointerException("Can't track a subscription outside view lifecycle");

        compositeSubscription.add(subscription);
    }

    @Override
    public void cancelSubscriptions() {
        if (compositeSubscription != null) {
            compositeSubscription.unsubscribe();
        }
        compositeSubscription = new CompositeSubscription();
    }
}
