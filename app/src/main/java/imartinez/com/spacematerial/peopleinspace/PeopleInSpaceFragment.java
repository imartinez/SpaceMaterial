package imartinez.com.spacematerial.peopleinspace;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import imartinez.com.spacematerial.App;
import imartinez.com.spacematerial.R;
import imartinez.com.spacematerial.base.BaseCleanFragment;
import imartinez.com.spacematerial.peopleinspace.PeopleInSpacePresenter.PeopleInSpaceRouter;
import imartinez.com.spacematerial.peopleinspace.PeopleInSpacePresenter.PeopleInSpaceView;
import javax.inject.Inject;

import java.util.List;

/**
 * TODO: Add class description.
 *
 * Created on 8/11/16.
 */
public class PeopleInSpaceFragment extends BaseCleanFragment<PeopleInSpacePresenter>
        implements PeopleInSpaceView, PeopleInSpaceRouter {

    @Inject
    PeopleInSpacePresenter peopleInSpacePresenter;

    public static PeopleInSpaceFragment newInstance() {
        PeopleInSpaceFragment fragment = new PeopleInSpaceFragment();
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        DaggerPeopleInSpaceComponent.builder()
                .appComponent(((App) getActivity().getApplication()).getAppComponent())
                .build()
                .inject(this);

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_people_in_space, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    protected PeopleInSpacePresenter resolvePresenter() {
        peopleInSpacePresenter.bindViewAndRouter(this, this);
        return peopleInSpacePresenter;
    }

    @Override
    public void presentPeopleInSpace(List<PersonInSpace> peopleInSpace) {
        Snackbar.make(getView(), String.valueOf(peopleInSpace.size()), Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void presentPeopleInSpaceRetrievalError() {
        Snackbar.make(getView(), R.string.error_fetching_people_in_space, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.retry, new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getPresenter().onRetrySelected();
                    }
                })
                .show();
    }
}
