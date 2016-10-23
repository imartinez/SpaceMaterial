package imartinez.com.spacematerial;

import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import imartinez.com.spacematerial.isslocation.IssLocationFragment;

import static butterknife.ButterKnife.findById;

public class MainActivity extends AppCompatActivity {

    private static final int ISS_LOCATION_FRAGMENT_POSITION = 0;
    private static final int PEOPLE_IN_SPACE_FRAGMENT_POSITION = 1;
    private static final int ISS_PASS_TIMES_FRAGMENT_POSITION = 2;

    @BindView(R.id.contentViewPager)
    ViewPager mContentViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initViews();
    }

    private void initViews() {
        // Bottom bar
        BottomBar bottomBar = findById(this, R.id.bottomBar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                switch (tabId) {
                    case R.id.tab_iss_location:
                        mContentViewPager.setCurrentItem(ISS_LOCATION_FRAGMENT_POSITION, false);
                        break;
                    case R.id.tab_people_in_space:
                        mContentViewPager.setCurrentItem(PEOPLE_IN_SPACE_FRAGMENT_POSITION, false);
                        break;
                    case R.id.tab_iss_pass_times:
                        mContentViewPager.setCurrentItem(ISS_PASS_TIMES_FRAGMENT_POSITION, false);
                        break;
                }

            }
        });

        // ViewPager
        FragmentPagerAdapter mainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
        mContentViewPager.setAdapter(mainPagerAdapter);
    }

    private static class MainPagerAdapter extends FragmentPagerAdapter {

        private static final int NUM_ITEMS = 3;

        public MainPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position) {
                case ISS_LOCATION_FRAGMENT_POSITION:
                    fragment = IssLocationFragment.newInstance();
                    break;
                case PEOPLE_IN_SPACE_FRAGMENT_POSITION:
                    fragment = IssLocationFragment.newInstance();
                    break;
                case ISS_PASS_TIMES_FRAGMENT_POSITION:
                    fragment = IssLocationFragment.newInstance();
                    break;
                default:
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }
    }
}
