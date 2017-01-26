package imartinez.com.spacematerial.peopleinspace;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.florent37.picassopalette.PicassoPalette;
import com.github.florent37.picassopalette.PicassoPalette.Profile;
import com.squareup.picasso.Picasso;
import imartinez.com.spacematerial.R;
import imartinez.com.spacematerial.visual.PicassoBitmapTransform;
import imartinez.com.spacematerial.visual.PicassoCircleTransform;

import java.util.ArrayList;
import java.util.List;

class PersonInSpaceRecyclerViewAdapter
        extends RecyclerView.Adapter<PersonInSpaceRecyclerViewAdapter.ViewHolder> {

    /**
     * Fixed values that limit detail image size to avoid out of memory exceptions and user
     * interface drop frames.
     */
    private static final int IMAGE_MAX_WIDTH = 512;
    private static final int IMAGE_MAX_HEIGHT = 384;

    private final Context context;
    private List<PersonInSpace> peopleInSpace = new ArrayList<>();
    private OnPersonSelectedListener onPersonSelectedListener;

    interface OnPersonSelectedListener {
        void onPersonSelected(PersonInSpace personSelected, ImageView selectedPhotoImageView,
                View selectedContentView, View selectedNameView, View selectedLocationView);
    }

    PersonInSpaceRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.person_in_space_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.item = peopleInSpace.get(position);
        // Load small circular flag with Picasso
        Picasso.with(context).load(holder.item.countryFlagImageUrl())
                // With a circular placeholder
                .placeholder(R.drawable.ic_placeholder) // TODO: 11/11/16 change placeholder image
                // Adjusting image size
                .fit()
                // Applying circle transformation to images
                .transform(new PicassoCircleTransform()).into(holder.flagImageView);
        // Load photo using Picasso, and set text container background using Palette.
        Picasso.with(context)
                .load(holder.item.bioPhotoImageUrl())
                // transformation to scale down image and reduce memory consumption
                .transform(new PicassoBitmapTransform(IMAGE_MAX_WIDTH, IMAGE_MAX_HEIGHT))
                .into(holder.photoImageView,
                        PicassoPalette.with(holder.item.bioPhotoImageUrl(), holder.photoImageView)
                                .use(Profile.VIBRANT_DARK)
                                .intoBackground(holder.textContainerView));

        // Set unique transition name to each photo imageView
        holder.photoImageView.setTransitionName("photoImageView" + position);

        holder.nameTextView.setText(holder.item.name());
        holder.locationTextView.setText(holder.item.location());

        // Add click listener to select an animal
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onPersonSelectedListener != null) {
                    onPersonSelectedListener.onPersonSelected(holder.item, holder.photoImageView,
                            holder.textContainerView, holder.nameTextView, holder.locationTextView);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return peopleInSpace.size();
    }

    void setOnPersonSelectedListener(OnPersonSelectedListener onPersonSelectedListener) {
        this.onPersonSelectedListener = onPersonSelectedListener;
    }

    /**
     * Compares the incoming list with the already painted list and updates the adapter.
     */
    void setPeopleInSpace(List<PersonInSpace> newPeopleInSpace) {
        DiffUtil.DiffResult diffResult =
                DiffUtil.calculateDiff(new DiffCallback(this.peopleInSpace, newPeopleInSpace));
        diffResult.dispatchUpdatesTo(this);
        this.peopleInSpace = newPeopleInSpace;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final View view;
        final View textContainerView;
        final ImageView photoImageView;
        final ImageView flagImageView;
        final TextView nameTextView;
        final TextView locationTextView;
        PersonInSpace item;

        ViewHolder(View view) {
            super(view);
            this.view = view;
            textContainerView = view.findViewById(R.id.text_container_view);
            photoImageView = (ImageView) view.findViewById(R.id.photo_imageview);
            flagImageView = (ImageView) view.findViewById(R.id.flag_imageview);
            nameTextView = (TextView) view.findViewById(R.id.name_textview);
            locationTextView = (TextView) view.findViewById(R.id.location_textview);
        }
    }

    /**
     * Implementation of DiffUtil to improved diffs within this adapter
     */
    private static class DiffCallback extends DiffUtil.Callback {

        List<PersonInSpace> oldPeopleInSpace;
        List<PersonInSpace> newPeopleInSpace;

        DiffCallback(List<PersonInSpace> newPeopleInSpace, List<PersonInSpace> oldPeopleInSpace) {
            this.newPeopleInSpace = newPeopleInSpace;
            this.oldPeopleInSpace = oldPeopleInSpace;
        }

        @Override
        public int getOldListSize() {
            return oldPeopleInSpace.size();
        }

        @Override
        public int getNewListSize() {
            return newPeopleInSpace.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return oldPeopleInSpace.get(oldItemPosition)
                    .name()
                    .equals(newPeopleInSpace.get(newItemPosition).name());
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return oldPeopleInSpace.get(oldItemPosition)
                    .equals(newPeopleInSpace.get(newItemPosition));
        }

        @Nullable
        @Override
        public Object getChangePayload(int oldItemPosition, int newItemPosition) {
            return super.getChangePayload(oldItemPosition, newItemPosition);
        }
    }
}
