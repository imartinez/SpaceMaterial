package imartinez.com.spacematerial.peopleinspace;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.github.florent37.picassopalette.PicassoPalette;
import com.github.florent37.picassopalette.PicassoPalette.Profile;
import com.squareup.picasso.Picasso;
import imartinez.com.spacematerial.App;
import imartinez.com.spacematerial.R;
import imartinez.com.spacematerial.base.BaseCleanFragment;
import imartinez.com.spacematerial.peopleinspace.PeopleInSpacePresenter.PeopleInSpaceRouter;
import imartinez.com.spacematerial.peopleinspace.PeopleInSpacePresenter.PeopleInSpaceView;
import imartinez.com.spacematerial.visual.PicassoBitmapTransform;
import imartinez.com.spacematerial.visual.PicassoCircleTransform;
import javax.inject.Inject;

import java.util.List;

public class PeopleInSpaceFragment extends BaseCleanFragment<PeopleInSpacePresenter>
        implements PeopleInSpaceView, PeopleInSpaceRouter {

    @Inject
    PeopleInSpacePresenter peopleInSpacePresenter;

    @BindView(R.id.people_in_space_recyclerview)
    RecyclerView recyclerView;

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
        // TODO: 11/11/16 check if has changed to avoid re-painting
        recyclerView.setAdapter(new PersonInSpaceRecyclerViewAdapter(peopleInSpace));
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

    class PersonInSpaceRecyclerViewAdapter
            extends RecyclerView.Adapter<PersonInSpaceRecyclerViewAdapter.ViewHolder> {

        /**
         * Fixed values that limit detail image size to avoid out of memory exceptions and user
         * interface drop frames.
         */
        private static final int IMAGE_MAX_WIDTH = 512;
        private static final int IMAGE_MAX_HEIGHT = 384;

        private final List<PersonInSpace> peopleInSpace;

        public PersonInSpaceRecyclerViewAdapter(List<PersonInSpace> peopleInSpace) {
            this.peopleInSpace = peopleInSpace;
        }

        @Override
        public PersonInSpaceRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.person_in_space_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final PersonInSpaceRecyclerViewAdapter.ViewHolder holder,
                int position) {
            holder.item = peopleInSpace.get(position);
            // Load small circular flag with Picasso
            Picasso.with(getContext()).load(holder.item.countryFlagImageUrl())
                    // With a circular placeholder
                    .placeholder(
                            R.drawable.ic_placeholder) // TODO: 11/11/16 change placeholder image
                    // Adjusting image size
                    .fit()
                    // Applying circle transformation to images
                    .transform(new PicassoCircleTransform()).into(holder.flagImageView);
            // Load photo using Picasso, and set text container background using Palette.
            Picasso.with(getContext())
                    .load(holder.item.bioPhotoImageUrl())
                    // transformation to scale down image and reduce memory consumption
                    .transform(new PicassoBitmapTransform(IMAGE_MAX_WIDTH, IMAGE_MAX_HEIGHT))
                    .into(holder.photoImageView, PicassoPalette.with(holder.item.bioPhotoImageUrl(),
                            holder.photoImageView)
                            .use(Profile.VIBRANT)
                            .intoBackground(holder.textContainerView));

            holder.nameTextView.setText(holder.item.name());
            holder.titleTextView.setText(holder.item.title());
            holder.locationTextView.setText(holder.item.location());

            // Add click listener to select an animal
            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getPresenter().onPersonSelected(holder.item);
                }
            });
        }

        @Override
        public int getItemCount() {
            return peopleInSpace.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View view;
            public final View textContainerView;
            public final ImageView photoImageView;
            public final ImageView flagImageView;
            public final TextView nameTextView;
            public final TextView titleTextView;
            public final TextView locationTextView;
            public PersonInSpace item;

            public ViewHolder(View view) {
                super(view);
                this.view = view;
                textContainerView = view.findViewById(R.id.text_container_view);
                photoImageView = (ImageView) view.findViewById(R.id.photo_imageview);
                flagImageView = (ImageView) view.findViewById(R.id.flag_imageview);
                nameTextView = (TextView) view.findViewById(R.id.name_textview);
                titleTextView = (TextView) view.findViewById(R.id.title_textview);
                locationTextView = (TextView) view.findViewById(R.id.location_textview);
            }
        }
    }
}
