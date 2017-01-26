package imartinez.com.spacematerial.peopleinspace;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import imartinez.com.spacematerial.App;
import imartinez.com.spacematerial.R;
import imartinez.com.spacematerial.base.BaseCleanFragment;
import imartinez.com.spacematerial.peopleinspace.PeopleInSpacePresenter.PeopleInSpaceRouter;
import imartinez.com.spacematerial.peopleinspace.PeopleInSpacePresenter.PeopleInSpaceView;
import imartinez.com.spacematerial.peopleinspace.PersonInSpaceRecyclerViewAdapter.OnPersonSelectedListener;
import imartinez.com.spacematerial.routing.DetailFragmentLoader;
import javax.inject.Inject;

import java.util.List;

public class PeopleInSpaceFragment extends BaseCleanFragment<PeopleInSpacePresenter>
        implements PeopleInSpaceView, PeopleInSpaceRouter {

    @Inject
    PeopleInSpacePresenter peopleInSpacePresenter;

    @BindView(R.id.people_in_space_recyclerview)
    RecyclerView recyclerView;

    private DetailFragmentLoader detailFragmentLoader;
    private PersonInSpaceRecyclerViewAdapter adapter;
    private Snackbar errorSnackbar;

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

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        adapter = new PersonInSpaceRecyclerViewAdapter(getContext());
        adapter.setOnPersonSelectedListener(new OnPersonSelectedListener() {
            @Override
            public void onPersonSelected(PersonInSpace personSelected) {
                getPresenter().onPersonSelected(personSelected);
            }
        });
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the container activity has implemented
        // DetailFragmentLoader. If not, it throws an exception
        try {
            detailFragmentLoader = (DetailFragmentLoader) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement DetailFragmentLoader");
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        // Safe check to avoid calling getPresenter before the view has been fully initialized
        if (peopleInSpacePresenter != null && peopleInSpacePresenter.isBoundToViewAndRouter()) {
            getPresenter().onViewVisibilityChanged(isVisibleToUser);
        }
    }

    @Override
    protected PeopleInSpacePresenter resolvePresenter() {
        peopleInSpacePresenter.bindViewAndRouter(this, this);
        return peopleInSpacePresenter;
    }

    @Override
    public void presentPeopleInSpace(List<PersonInSpace> peopleInSpace) {
        if (errorSnackbar != null && errorSnackbar.isShown()) {
            errorSnackbar.dismiss();
        }
        adapter.setPeopleInSpace(peopleInSpace);
    }

    @Override
    public void presentPeopleInSpaceRetrievalError() {
        errorSnackbar = Snackbar.make(getView(), R.string.error_fetching_people_in_space,
                Snackbar.LENGTH_INDEFINITE).setAction(R.string.retry, new OnClickListener() {
            @Override
            public void onClick(View view) {
                getPresenter().onRetrySelected();
            }
        });
        errorSnackbar.show();
    }

    @Override
    public void showPersonInSpaceDetail(final PersonInSpace personInSpace) {
        detailFragmentLoader.loadDetailFragment(
                        PersonInSpaceDetailFragment.newInstance(personInSpace));
    }
}
