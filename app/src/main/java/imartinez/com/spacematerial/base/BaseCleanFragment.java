package imartinez.com.spacematerial.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

public abstract class BaseCleanFragment<T extends BasePresenter<?, ?>> extends Fragment {

    private T presenter;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Retrieve presenter instance
        this.presenter = resolvePresenter();

        // Call onCreateView
        this.presenter.onCreateView();
    }

    protected abstract T resolvePresenter();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getPresenter().onDestroyView();
        getPresenter().unBindViewAndRouter();
    }

    protected T getPresenter() {
        if (presenter.getView() != this) throw new IllegalArgumentException("Not Binded!");
        return presenter;
    }
}
