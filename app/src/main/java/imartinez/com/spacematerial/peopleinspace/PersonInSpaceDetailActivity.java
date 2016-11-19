package imartinez.com.spacematerial.peopleinspace;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.florent37.picassopalette.PicassoPalette;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import imartinez.com.spacematerial.R;

public class PersonInSpaceDetailActivity extends AppCompatActivity {

    static final String PERSON_IN_SPACE_EXTRA = "PERSON_IN_SPACE_EXTRA";
    private PersonInSpace personInSpace;

    @BindView(R.id.person_in_space_detail_collapsing_appbar)
    AppBarLayout appBarLayout;

    @BindView(R.id.person_in_space_detail_toolbar)
    Toolbar toolbar;

    @BindView(R.id.person_in_space_detail_photo_imageview)
    ImageView photoImageView;

    @BindView(R.id.person_in_space_detail_name_textview)
    TextView nameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_in_space_detail);

        // Postpone the shared element enter transition, in order to give time
        // to load images asynchronously.
        postponeEnterTransition();

        ButterKnife.bind(this);

        initViews();
    }

    private void initViews() {
        personInSpace = (PersonInSpace) getIntent().getSerializableExtra(PERSON_IN_SPACE_EXTRA);

        // Load photo using Picasso, and set text container background using Palette.
        Picasso.with(this)
                .load(personInSpace.bioPhotoImageUrl())
                .into(photoImageView, PicassoPalette.with(personInSpace.bioPhotoImageUrl(), photoImageView)
                        .intoCallBack(new PicassoPalette.CallBack() {
                            @Override
                            public void onPaletteLoaded(Palette palette) {
                                appBarLayout.setBackgroundColor(palette.getVibrantColor(getResources().getColor(R.color.colorPrimaryDark)));
                                getWindow().setStatusBarColor(palette.getDarkVibrantColor(getResources().getColor(R.color.colorPrimaryDark)));
                                // Now we are ready to start the transition
                                startPostponedEnterTransition();
                            }
                        }));

        toolbar.setTitle(personInSpace.name());

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nameTextView.setText(personInSpace.name());
    }

}
