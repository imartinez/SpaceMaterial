package imartinez.com.spacematerial.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

public abstract class BaseCleanFragment<T extends BasePresenter<?, ?>> extends Fragment {

    private T presenter;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Retrieve presenter instance
        this.presenter = resolvePresenter();

        // Call onViewReady
        this.presenter.onViewReady();
    }

    protected abstract T resolvePresenter();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getPresenter().onViewDestroyed();
        getPresenter().unBindViewAndRouter();
    }

    protected T getPresenter() {
        if (presenter.getView() != this) throw new IllegalArgumentException("Not Binded!");
        return presenter;
    }
}
