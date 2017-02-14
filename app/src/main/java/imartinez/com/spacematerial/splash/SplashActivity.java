package imartinez.com.spacematerial.splash;

import android.content.Intent;
import android.graphics.drawable.Animatable2.AnimationCallback;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import imartinez.com.spacematerial.MainActivity;
import imartinez.com.spacematerial.R;

public class SplashActivity extends AppCompatActivity {

    public static final int ANIM_DELAY_MILLIS = 500;
    public static final int FINISH_DELAY_MILLIS = 1500;

    @BindView(R.id.splash_imageview)
    ImageView splashImageView;

    @BindView(R.id.splash_title)
    View splashTitle;

    private AnimatedVectorDrawable animatedVectorDrawable;
    private Handler handlerTimer;

    private Runnable runAnimationRunnable = new Runnable() {
        @Override
        public void run() {
            animatedVectorDrawable.registerAnimationCallback(new AnimationCallback() {
                @Override
                public void onAnimationEnd(Drawable drawable) {
                    super.onAnimationEnd(drawable);
                    splashTitle.setVisibility(View.VISIBLE);
                    handlerTimer.postDelayed(loadMainActivityRunnable, FINISH_DELAY_MILLIS);
                }
            });
            animatedVectorDrawable.start();
        }
    };

    private Runnable loadMainActivityRunnable = new Runnable() {
        @Override
        public void run() {
            Intent mainActivityIntent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(mainActivityIntent);
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        initViews();
    }

    private void initViews() {
        // Init vector animation
        animatedVectorDrawable = (AnimatedVectorDrawable) splashImageView.getDrawable();
        handlerTimer = new Handler();
        handlerTimer.postDelayed(runAnimationRunnable, ANIM_DELAY_MILLIS);
    }
}
