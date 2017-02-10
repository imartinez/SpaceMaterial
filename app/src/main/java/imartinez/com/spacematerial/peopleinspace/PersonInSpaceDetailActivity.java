package imartinez.com.spacematerial.peopleinspace;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionSet;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.github.florent37.picassopalette.PicassoPalette;
import com.squareup.picasso.Picasso;
import imartinez.com.spacematerial.R;
import imartinez.com.spacematerial.util.EmptyAnimatorListener;
import imartinez.com.spacematerial.util.EmptyTransitionListener;

public class PersonInSpaceDetailActivity extends AppCompatActivity {

    static final String PERSON_IN_SPACE_EXTRA = "PERSON_IN_SPACE_EXTRA";
    private PersonInSpace personInSpace;

    @BindView(R.id.person_in_space_detail_collapsing_appbar)
    AppBarLayout appBarLayout;

    @BindView(R.id.person_in_space_detail_collapsing)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @BindView(R.id.person_in_space_detail_toolbar)
    Toolbar toolbar;

    @BindView(R.id.person_in_space_detail_photo_imageview)
    ImageView photoImageView;

    @BindView(R.id.person_in_space_detail_content)
    View contentView;

    @BindView(R.id.person_in_space_detail_name_textview)
    TextView nameTextView;

    @BindView(R.id.person_in_space_detail_location_textview)
    TextView locationTextView;

    @BindView(R.id.person_in_space_detail_fab)
    View fab;

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
                .into(photoImageView,
                        PicassoPalette.with(personInSpace.bioPhotoImageUrl(), photoImageView)
                                .intoCallBack(new PicassoPalette.CallBack() {
                                    @Override
                                    public void onPaletteLoaded(Palette palette) {
                                        int darkVibrantColor = palette.getDarkVibrantColor(
                                                getResources().getColor(R.color.colorPrimaryDark));

                                        //appBarLayout.setBackgroundColor(darkVibrantColor);
                                        getWindow().setStatusBarColor(darkVibrantColor);

                                        // Now we are ready to start the transition
                                        startPostponedEnterTransition();
                                    }
                                }));

        // Toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        appBarLayout.addOnOffsetChangedListener(new AppBarOnOffsetChangedListener());

        // Data
        nameTextView.setText(personInSpace.name());
        locationTextView.setText(personInSpace.location());

        // Set transition animations
        TransitionSet enterTransitionSet = new TransitionSet();
        Fade fade = new Fade();

        Slide slideBottom = new Slide(Gravity.BOTTOM);
        slideBottom.addTarget(contentView);

        enterTransitionSet.addTransition(fade);
        enterTransitionSet.addTransition(slideBottom);

        getWindow().setEnterTransition(enterTransitionSet);

        getWindow().getSharedElementEnterTransition()
                .addListener(new ShowFabOnEndTransitionListener());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();    //Call the back button's method
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // Run hide animations before finishing
        buildHideToolbarAnimator().start();
        Animator hideFabAnimator = buildHideFabAnimator();
        hideFabAnimator.addListener(new EmptyAnimatorListener() {

            @Override
            public void onAnimationEnd(Animator animator) {
                fab.setVisibility(View.INVISIBLE);
                finishAfterTransition();
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                fab.setVisibility(View.INVISIBLE);
                finishAfterTransition();
            }
        });
        hideFabAnimator.start();
    }

    private Animator buildShowToolbarAnimator() {
        Animator toolbarAnim = ObjectAnimator.ofFloat(toolbar, "alpha", 0f, 1f);
        toolbarAnim.setInterpolator(new DecelerateInterpolator());
        toolbarAnim.setDuration(200);
        toolbar.setVisibility(View.VISIBLE);
        return toolbarAnim;
    }

    private Animator buildHideToolbarAnimator() {
        Animator toolbarAnim = ObjectAnimator.ofFloat(toolbar, "alpha", 1f, 0f);
        toolbarAnim.setInterpolator(new DecelerateInterpolator());
        toolbarAnim.setDuration(200);
        return toolbarAnim;
    }

    private Animator buildShowFabAnimator() {
        Animator fabAnim = ObjectAnimator.ofPropertyValuesHolder(fab,
                PropertyValuesHolder.ofFloat(View.SCALE_X, 0f, 1f),
                PropertyValuesHolder.ofFloat(View.SCALE_Y, 0f, 1f));
        fabAnim.setInterpolator(new DecelerateInterpolator());
        fabAnim.setDuration(200);
        fab.setVisibility(View.VISIBLE);
        return fabAnim;
    }

    private Animator buildHideFabAnimator() {
        Animator fabAnim = ObjectAnimator.ofPropertyValuesHolder(fab,
                PropertyValuesHolder.ofFloat(View.SCALE_X, 1f, 0f),
                PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f, 0f));
        fabAnim.setInterpolator(new DecelerateInterpolator());
        fabAnim.setDuration(200);
        return fabAnim;
    }

    /**
     * TransitionListener that shows the FAB and toolbar after transition ends
     */
    private class ShowFabOnEndTransitionListener extends EmptyTransitionListener {
        @Override
        public void onTransitionEnd(Transition transition) {
            buildShowToolbarAnimator().start();
            buildShowFabAnimator().start();
            getWindow().getSharedElementEnterTransition().removeListener(this);
        }
    }

    /**
     * Manages the visibility of the AppBar title. Only shows the title when the Collapsing
     * Toolbar is collapsed
     */
    private class AppBarOnOffsetChangedListener implements AppBarLayout.OnOffsetChangedListener {
        boolean isShow = false;
        int scrollRange = -1;

        @Override
        public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
            if (scrollRange == -1) {
                scrollRange = appBarLayout.getTotalScrollRange();
            }
            if (scrollRange + verticalOffset == 0) {
                collapsingToolbarLayout.setTitle(personInSpace.name());
                isShow = true;
            } else if (isShow) {
                collapsingToolbarLayout.setTitle(" ");
                isShow = false;
            }
        }
    }
}
