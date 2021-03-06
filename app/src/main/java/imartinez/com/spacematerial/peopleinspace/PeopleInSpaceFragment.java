package imartinez.com.spacematerial.peopleinspace;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import imartinez.com.spacematerial.App;
import imartinez.com.spacematerial.R;
import imartinez.com.spacematerial.base.BaseCleanFragment;
import imartinez.com.spacematerial.peopleinspace.PeopleInSpacePresenter.PeopleInSpaceRouter;
import imartinez.com.spacematerial.peopleinspace.PeopleInSpacePresenter.PeopleInSpaceView;
import imartinez.com.spacematerial.peopleinspace.PersonInSpaceRecyclerViewAdapter.OnPersonSelectedListener;
import javax.inject.Inject;

import java.util.List;

public class PeopleInSpaceFragment extends BaseCleanFragment<PeopleInSpacePresenter>
        implements PeopleInSpaceView, PeopleInSpaceRouter {

    @Inject
    PeopleInSpacePresenter peopleInSpacePresenter;

    @BindView(R.id.people_in_space_recyclerview)
    RecyclerView recyclerView;

    private PersonInSpaceRecyclerViewAdapter adapter;
    private Snackbar errorSnackbar;
    private ImageView selectedPhotoImageView;

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
            public void onPersonSelected(PersonInSpace personSelected,
                    ImageView selectedPhotoImageView) {
                PeopleInSpaceFragment.this.selectedPhotoImageView = selectedPhotoImageView;
                getPresenter().onPersonSelected(personSelected);
            }
        });

        recyclerView.setAdapter(adapter);

        return view;
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
        Intent intent = new Intent(getActivity(), PersonInSpaceDetailActivity.class);
        // Pass data object in the bundle to populate details activity
        intent.putExtra(PersonInSpaceDetailActivity.PERSON_IN_SPACE_DATA, personInSpace);
        intent.putExtra(PersonInSpaceDetailActivity.PERSON_IN_SPACE_IMAGE,
                ((BitmapDrawable) selectedPhotoImageView.getDrawable()).getBitmap());
        // Transition animation with shared elements
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(getActivity(), selectedPhotoImageView,
                        "person_in_space_detail_photo_imageview");

        startActivity(intent, options.toBundle());
    }
}
