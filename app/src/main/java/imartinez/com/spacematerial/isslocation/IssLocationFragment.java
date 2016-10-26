package imartinez.com.spacematerial.isslocation;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import imartinez.com.spacematerial.App;
import imartinez.com.spacematerial.R;
import imartinez.com.spacematerial.base.BaseCleanFragment;
import imartinez.com.spacematerial.isslocation.IssLocationPresenter.IssLocationRouter;
import imartinez.com.spacematerial.isslocation.IssLocationPresenter.IssLocationView;
import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass representing ISS location.
 * Activities that contain this fragment must implement the
 * {@link OnIssLocationUpdatedListener} interface
 * to handle interaction events.
 * Use the {@link IssLocationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IssLocationFragment extends BaseCleanFragment<IssLocationPresenter>
        implements IssLocationView, IssLocationRouter {

    @Inject
    IssLocationPresenter issLocationPresenter;

    @BindView(R.id.iss_location_textview)
    TextView issLocationTextView;

    private OnIssLocationUpdatedListener onIssLocationUpdateListener;

    public static IssLocationFragment newInstance() {
        IssLocationFragment fragment = new IssLocationFragment();
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        DaggerIssLocationComponent.builder()
                .appComponent(((App) getActivity().getApplication()).getAppComponent())
                .build()
                .inject(this);

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_iss_location, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnIssLocationUpdatedListener) {
            onIssLocationUpdateListener = (OnIssLocationUpdatedListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onIssLocationUpdateListener = null;
    }

    @Override
    protected IssLocationPresenter resolvePresenter() {
        issLocationPresenter.bindViewAndRouter(this, this);
        return issLocationPresenter;
    }

    @Override
    public void presentIssLocation(IssLocation issLocation) {
        issLocationTextView.setText(String.valueOf(issLocation.timestamp()));
    }

    @Override
    public void presentIssRetrievalError() {
        issLocationTextView.setText("ERROR!");
    }

    /**
     * Interface to be implemented by the host activity. Used by the fragment
     * to inform the activity that a new location has been represented.
     */
    public interface OnIssLocationUpdatedListener {
        void onIssLocationUpdated();
    }
}
