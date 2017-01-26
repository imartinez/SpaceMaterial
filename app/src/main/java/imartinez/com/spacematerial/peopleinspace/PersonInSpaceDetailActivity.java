package imartinez.com.spacematerial.peopleinspace;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.transition.AutoTransition;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.Transition.TransitionListener;
import android.transition.TransitionSet;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.github.florent37.picassopalette.PicassoPalette;
import com.squareup.picasso.Picasso;
import imartinez.com.spacematerial.R;
import imartinez.com.spacematerial.visual.ChangeBoundsAndColorTransition;
import imartinez.com.spacematerial.visual.FadeTransition;

public class PersonInSpaceDetailActivity extends AppCompatActivity {

    static final String PERSON_IN_SPACE_EXTRA = "PERSON_IN_SPACE_EXTRA";
    private PersonInSpace personInSpace;

    @BindView(R.id.person_in_space_detail_collapsing_appbar)
    AppBarLayout appBarLayout;

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

    private final TransitionListener sharedElementEnterTransitionListener =
            new TransitionListener() {
                @Override
                public void onTransitionStart(Transition transition) {
                }

                @Override
                public void onTransitionEnd(Transition transition) {
                    Animator toolbarAnim = ObjectAnimator.ofFloat(toolbar, "alpha", 0f, 1f);
                    toolbarAnim.setInterpolator(new DecelerateInterpolator());
                    toolbarAnim.setDuration(200);
                    toolbar.setVisibility(View.VISIBLE);
                    toolbarAnim.start();

                    Animator fabAnim = ObjectAnimator.ofPropertyValuesHolder(fab,
                            PropertyValuesHolder.ofFloat(View.SCALE_X, 0f, 1f),
                            PropertyValuesHolder.ofFloat(View.SCALE_Y, 0f, 1f));
                    fabAnim.setInterpolator(new DecelerateInterpolator());
                    fabAnim.setDuration(200);
                    fab.setVisibility(View.VISIBLE);
                    fabAnim.start();

                    getWindow().getSharedElementEnterTransition().removeListener(this);
                }

                @Override
                public void onTransitionCancel(Transition transition) {
                }

                @Override
                public void onTransitionPause(Transition transition) {
                }

                @Override
                public void onTransitionResume(Transition transition) {
                }
            };

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

                                        appBarLayout.setBackgroundColor(darkVibrantColor);
                                        getWindow().setStatusBarColor(darkVibrantColor);

                                        ChangeBoundsAndColorTransition changeBoundsAndColorEnter =
                                                new ChangeBoundsAndColorTransition(
                                                        palette.getDarkVibrantColor(
                                                                getResources().getColor(
                                                                        R.color.colorPrimaryDark)),
                                                        -1);

                                        getWindow().setSharedElementEnterTransition(
                                                changeBoundsAndColorEnter);

                                        ChangeBoundsAndColorTransition changeBoundsAndColorReturn =
                                                new ChangeBoundsAndColorTransition(-1,
                                                        palette.getDarkVibrantColor(
                                                                getResources().getColor(
                                                                        R.color.colorPrimaryDark)));

                                        changeBoundsAndColorReturn.removeTarget("person_in_space_detail_name_textview");

                                        AutoTransition autoTransition = new AutoTransition();
                                        autoTransition.addTarget("person_in_space_detail_name_textview");

                                        Transition fadeIn = new FadeTransition(0f, 1f, new LinearInterpolator());
                                        fadeIn.addTarget("person_in_space_detail_name_textview");

                                        TransitionSet returnTransitionSet = new TransitionSet();
                                        returnTransitionSet.setOrdering(TransitionSet.ORDERING_SEQUENTIAL);
                                        //returnTransitionSet.addTransition(autoTransition);
                                        returnTransitionSet.addTransition(changeBoundsAndColorReturn);
                                        //returnTransitionSet.addTransition(fadeIn);

                                        getWindow().setSharedElementReturnTransition(
                                                returnTransitionSet);

                                        getWindow().getSharedElementEnterTransition()
                                                .addListener(sharedElementEnterTransitionListener);

                                        // Now we are ready to start the transition
                                        startPostponedEnterTransition();
                                    }
                                }));

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Set data
        nameTextView.setText(personInSpace.name());
        locationTextView.setText(personInSpace.location());

        // Set transition animations
        TransitionSet enterTransitionSet = new TransitionSet();
        Slide slideBottom = new Slide(Gravity.BOTTOM);
        slideBottom.addTarget(nameTextView);
        slideBottom.addTarget(locationTextView);

        Slide slideTop = new Slide(Gravity.TOP);
        slideTop.addTarget(appBarLayout);

        enterTransitionSet.addTransition(slideTop);
        enterTransitionSet.addTransition(slideBottom);

        enterTransitionSet.setInterpolator(
                AnimationUtils.loadInterpolator(this, android.R.interpolator.fast_out_slow_in));

        getWindow().setEnterTransition(enterTransitionSet);

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
        Animator toolbarAnim = ObjectAnimator.ofFloat(toolbar, "alpha", 1f, 0f);
        toolbarAnim.setInterpolator(new DecelerateInterpolator());
        toolbarAnim.setDuration(200);
        toolbarAnim.start();

        Animator fabAnim = ObjectAnimator.ofPropertyValuesHolder(fab,
                PropertyValuesHolder.ofFloat(View.SCALE_X, 1f, 0f),
                PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f, 0f));
        fabAnim.setInterpolator(new DecelerateInterpolator());
        fabAnim.setDuration(200);
        fabAnim.start();
        fabAnim.addListener(new AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

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

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }
}
