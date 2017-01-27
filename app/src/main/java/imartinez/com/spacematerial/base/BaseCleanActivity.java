package imartinez.com.spacematerial.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public abstract class BaseCleanActivity<T extends BasePresenter> extends AppCompatActivity {
    private T presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retrieve presenter instance
        this.presenter = resolvePresenter();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Call onViewReady
        this.presenter.onViewReady();
    }

    protected abstract T resolvePresenter();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getPresenter().onViewDestroyed();
        getPresenter().unBindViewAndRouter();
    }

    protected T getPresenter() {
        if (presenter.getView() != this) throw new IllegalArgumentException("Not bound!");
        return presenter;
    }
}
