package imartinez.com.spacematerial.base;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import org.jetbrains.annotations.NotNull;

public abstract class BasePresenterImpl<V, R> implements BasePresenter<V, R> {

    private V view;
    private R router;
    private CompositeDisposable compositeDisposable;

    @Override
    public void onViewReady() {
        disposeTracked();
    }

    @Override
    public void onViewDestroyed() {
        disposeTracked();
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
    public void track(Disposable disposable) {
        if (compositeDisposable == null)
            throw new NullPointerException("Can't track a disposable outside view lifecycle");

        compositeDisposable.add(disposable);
    }

    @Override
    public void disposeTracked() {
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
        }
        compositeDisposable = new CompositeDisposable();
    }
}
